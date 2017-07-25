package canghailongyin.blog.main;

/**
 * Created by mingl on 2017-4-26.
 */

import au.com.bytecode.opencsv.CSVReader;
import canghailongyin.blog.utils.MysqlBasic;
import com.monitorjbl.xlsx.StreamingReader;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;

import static com.sun.xml.internal.fastinfoset.alphabet.BuiltInRestrictedAlphabets.table;

/**
 * 多线程（文件分块），批量插入Mysql，性能如何优化
 * txt,csv怎么处理，xls怎么处理，xlsx怎么处理
 */
public class FileToDBThread implements Runnable {
    /**
     * 线程名称
     */
    private String threadName;
    /**
     * 准备入库的文件路径
     */
    private String filePath;
    /**
     * 线程读取文件的起始位置
     */
    private long startIndex;
    /**
     * 线程读取文件的结束位置
     */
    private long endIndex;
    /**
     * 要插入的数据表名称
     */
    private String tableName;
    /**
     * 数据表的表头部分，即insert into Table(?)问号部分
     */
    private String tableHeader;
    /**
     * 批量入库的长度，例如1000行数据一插入
     */
    private int batchLength;
    /**
     * 文件每一行的分隔符
     */
    private String split;
    /**
     * 文件的编码格式
     */
    private String encode;
    /**
     * 数据库连接
     */
    private Connection con;

    public FileToDBThread() {

    }

    public FileToDBThread(String threadName, String filePath, long startIndex, long endIndex, String tableName,
                          String tableHeader, int batchLength, String split, String encode, Connection con) {
        this.threadName = threadName;
        this.filePath = filePath;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.tableName = tableName;
        this.tableHeader = tableHeader;
        this.batchLength = batchLength;
        this.split = split;
        this.encode = encode;
        this.con = con;
    }

    @Override
    public void run() {
        if (filePath.endsWith(".csv")) {
            csvFileToDB(threadName, filePath, startIndex, endIndex, tableName, tableHeader, batchLength, split, encode, con);
        } else if (filePath.endsWith(".xlsx")) {//xls最长65536行，所以不考虑
            xlsxFileToDB(threadName, filePath, startIndex, endIndex, tableName, tableHeader, batchLength, split, encode, con);
        } else {
            normalFileToDB(threadName, filePath, startIndex, endIndex, tableName, tableHeader, batchLength, split, encode, con);
        }
    }

    public static void xlsxFileToDB(String threadName, String filePath, long startIndex, long endIndex, String tableName,
                                    String tableHeader, int batchLength, String split, String encode, Connection con) {
        System.out.println("线程" + threadName + "启动.......................");
        System.out.println("split=" + split + ".encode=" + encode
                + ",fileName=" + filePath + ", start=" + startIndex + ", end=" + endIndex);
        InputStream input = null;
        Workbook workbook = null;
        String curSql = "";//当前插入数据库的sql语句
        try {
            input = new FileInputStream(new File(filePath));
            workbook = StreamingReader.builder()
                    .rowCacheSize(1000)    // number of rows to keep in memory (defaults to 10)
                    .bufferSize(4096)     // buffer size to use when reading InputStream to file (defaults to 1024)
                    .open(input);
            String preSql = "insert into `" + tableName + "` " + tableHeader + " values ";
            PreparedStatement statement = con.prepareStatement(preSql);
            StringBuilder value = new StringBuilder();
            Sheet sheet = workbook.getSheetAt(0);//默认取第一个sheet
            System.out.println(sheet.getSheetName());
            int count = 0;
            boolean isFirstLine = true;
            for (Row r : sheet) {
                count++;
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                int columnNum = r.getLastCellNum() - 1;
                value.append("(");
                for (int j = 0; j < columnNum; j++) {
                    Cell c = r.getCell(j);
                    if (j == columnNum - 1) {
                        value.append("'" + c.getStringCellValue() + "'),");
                    } else {
                        value.append("'" + c.getStringCellValue() + "',");
                    }
                }
                if (count % batchLength == 0) {
                    statement.addBatch(preSql + value.toString().substring(0, value.length() - 1));
                    statement.executeBatch();
                    value = new StringBuilder();
                }
            }
            statement.addBatch(preSql + value.toString().substring(0, value.length() - 1));
            statement.executeBatch();
            value = null;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                input.close();
                workbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void csvFileToDB(String threadName, String filePath, long startIndex, long endIndex, String tableName,
                            String tableHeader, int batchLength, String split, String encode, Connection con) {
        System.out.println("线程" + threadName + "启动.......................");
        System.out.println("split=" + split + ".encode=" + encode
                + ",fileName=" + filePath + ", start=" + startIndex + ", end=" + endIndex);
        long count = 0;//文件索引块位置
        String curSql = "";//当前插入数据库的sql语句
        if (encode == null)
            encode = "UTF-8";//默认编码
        if (split == null)
            split = ",";
        InputStream input = null;
        CSVReader csvReader = null;
        int recordNum = getRecordNumFromFile(filePath);
        try {
            //读取标题行
            input = new FileInputStream(filePath);
            csvReader = new CSVReader(new InputStreamReader(input,
                    encode), split.charAt(0));
            String[] lines = csvReader.readNext();
//            count++;
            String preSql = "insert into `" + tableName + "` " + tableHeader
                    + " values ";
            PreparedStatement statement = con.prepareStatement(preSql);

            String titleLine = Arrays.asList(lines).toString().replace("[", "").replace("]", "");
            String[] titleLineData = processLine(titleLine, split);
            if (titleLineData == null) {
                return;
            }
            StringBuilder value = new StringBuilder();//拼接的sql语句
            while ((lines = csvReader.readNext()) != null) {
                count++;
                if (count < startIndex) {
                    continue;
                }
                if (count >= endIndex)
                    break;
                //如果是线程所在区域的行，进行封装sql处理
                if (count >= startIndex && count < endIndex) {
                    value.append("(");
                    for (int i = 0; i < lines.length; i++) {
                        if (i == lines.length - 1) {
                            value.append("'" + lines[i] + "'),");
                        } else {
                            value.append("'" + lines[i] + "',");
                        }
                    }
                }
                //达到如下情况执行sql操作，1达到batchLength，2到达结尾处
                if ((count % batchLength == 0 || count == endIndex - 1) && count != startIndex) {
                    curSql = preSql + value.toString().substring(0, value.length() - 1);
                    statement.addBatch(curSql);
                    statement.executeBatch();
                    value = new StringBuilder();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("errorsql:" + curSql);
        } finally {
            try {
                input.close();
                csvReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public static void normalFileToDB(String threadName, String filePath, long startIndex, long endIndex, String tableName,
                                      String tableHeader, int batchLength, String split, String encode, Connection con) {
        System.out.println("线程" + threadName + "启动.......................");
        System.out.println("split=" + split + ".encode=" + encode
                + ",fileName=" + filePath + ", start=" + startIndex + ", end=" + endIndex);
        long count = 0;//文件索引块位置
        String curSql = "";//当前插入数据库的sql语句
        if (encode == null)
            encode = "UTF-8";//默认编码
        try {
            //读取标题行
            RandomAccessFile raf = new RandomAccessFile(filePath, "r");
            String line = raf.readLine();
            count += line.length() + split.length();
            line = new String(line.getBytes("ISO-8859-1"), encode);
            System.out.println("从原文中取出的标题是:" + line + ", skip byte count: " + count);
            System.out.println("current index: " + count + ", start is: " + startIndex + ", end is: " + endIndex);
            String preSql = "insert into `" + tableName + "` " + tableHeader + " values ";//入库语句的前缀
            System.out.println(preSql);
            PreparedStatement statement = con.prepareStatement(preSql);
            String[] titleLineData = processLine(line, split);
            if (titleLineData == null) {
                return;
            }
            //定位到当前线程需要读取的位置
            if (startIndex >= count) {
                System.out.println("start index is: " + startIndex);
                raf.seek(startIndex);
                count = startIndex;
            }
            int lineLength = 0;//单行按分隔符分割后的长度
            int lineCount = 0;//统计行数，到达batchLength就入库
            StringBuilder value = new StringBuilder();//拼接的sql语句
            while ((line = raf.readLine()) != null) {
                lineLength = line.length();
                line = new String(line.getBytes("ISO-8859-1"), encode);
                String[] lines = line.split(split);
                //如果是线程所在区域的行，进行封装sql处理
                if (count >= startIndex && count <= endIndex) {
                    value.append("(");
                    lineCount++;
                    for (int i = 0; i < lines.length; i++) {
                        if (i == lines.length - 1) {
                            value.append("'" + lines[i] + "'),");
                        } else {
                            value.append("'" + lines[i] + "',");
                        }
                    }
                    count += lineLength + split.length();
                }
                //达到如下情况执行sql操作，1达到batchLength，2到达结尾处
                if (((lineCount % batchLength == 0) && (lineCount / batchLength > 0)) || count >= endIndex) {
                    curSql = preSql + value.toString().substring(0, value.length() - 1);
                    statement.addBatch(curSql);
                    statement.executeBatch();
                    value = new StringBuilder();
                }
                if (count >= endIndex) {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("errorsql:" + curSql);
        }
    }

    /**
     * Computing record columns according to file record line with column splitter
     *
     * @param line: file record line
     * @param split
     * @return
     */
    private static String[] processLine(String line, String split) {
        String[] data = null;
        if (line == null || line.trim().equals("")) {
            return data;
        }
        if (line.contains("\t")) {
            line = line.replace("\t", "制表符");
        }
        line = line.replaceAll("\\s{1,}", " ");
        if (line.contains("制表符")) {
            data = line.split("制表符");
            split = "\t";
        } else if (line.contains(" ")) {
            data = line.split(" ");
            split = " ";
        } else if (line.contains(",")) {// 默认用逗号
            data = line.split(",");
            split = ",";
        } else {
            data = line.split("\\" + split);
        }
        return data;
    }

    /**
     * get file length: measured by bytes
     *
     * @param filePath
     * @return
     */
    public static long getFileLength(String filePath) {
        long fileLength = 0;
        if (filePath == null || filePath.trim().isEmpty()) {
            fileLength = 0;
        } else {
            try {
                RandomAccessFile raf = new RandomAccessFile(filePath, "r");
                fileLength = raf.length();
                raf.close();
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return fileLength;
    }

    /**
     * Adjust file read position according to initial position
     *
     * @param filePath
     * @param index
     * @return
     */
    public static long adjustFileReadPosition(String filePath, long index) {
        long modifiedIndex = index;
        if (filePath != null && !filePath.trim().isEmpty()) {
            try {
                RandomAccessFile raf = new RandomAccessFile(filePath, "r");
                raf.seek(index);
                int value = raf.read();
                if (value == 10 || value == -1) { // 当前位置即为换行符或文件结束符
                    modifiedIndex = index;
                } else {
                    raf.seek(index - 1); // 当前位置为一行开始的第一个字符
                    value = raf.read();
                    if (value == 10) {
                        modifiedIndex = index - 1;
                    } else {
                        // 处于一行的中间
                        modifiedIndex++;
                        raf.seek(modifiedIndex);
                        while (true) {
                            value = raf.read();
                            if (value == 10 || value == -1) { // 当前位置即为换行符或文件结束符
                                break;
                            } else {
                                modifiedIndex++; // 判断下一个位置
                            }
                        }
                    }
                }
                raf.close();
                return modifiedIndex;
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return modifiedIndex;
    }

    private static void testBatch() {
        MysqlBasic mysqlBasic = new MysqlBasic("wztest");
        final Connection con = mysqlBasic.open();
        final long startTime = System.currentTimeMillis();
        final String filePath = "E:\\程序生成\\1W行.xlsx";
        String tableName = "testW";
        String header = "(";
        for (int i = 1; i <= 8; i++) {
            header += "column" + i + ",";
        }
        header = header.substring(0, header.length() - 1) + ")";
        if (filePath.endsWith(".xlsx")) {
            xlsxFileToDB("thread", filePath, 0, 0, tableName, header, 1000, ",", "utf-8", con);
        } else if (filePath.endsWith(".csv")) {
            csvFileThread(con, filePath, tableName, header);
        } else {
            normalFileThread(con, filePath, tableName, header);
        }
    }

    private static void csvFileThread(final Connection con, final String filePath, String tableName, String header) {
        final long startTime = System.currentTimeMillis();
        int recordNum = getRecordNumFromFile(filePath);
        if (recordNum < 1000) { // 1 * 1024 = 1KB, 1024 * 1024 = 1MB
            FileToDBThread fileToDBThread = new FileToDBThread("thread1", filePath, 0, recordNum, tableName, header, 1000, ",", "UTF-8", con);
            Thread thread = new Thread(fileToDBThread);
            thread.start();
        } else {
            recordNum = recordNum - 1;
            int threadProcessNum = recordNum / 10;
            final Thread[] threads = new Thread[11];
            for (int i = 0; i < 11; i++) {
                int start = i * threadProcessNum;
                int end = (i + 1) * threadProcessNum;
                if (start >= recordNum) {
                    break;
                }
                if (end >= recordNum) {
                    end = recordNum + 1;
                }
                FileToDBThread fileToDBThread = new FileToDBThread("thread" + i, filePath, start, end, tableName, header, 1000, ",", "UTF-8", con);
                Thread thread = new Thread(fileToDBThread);
                thread.start();
                threads[i] = thread;
            }
            /**
             * 开启一个线程去检验所有线程是否跑完
             */
            Thread thread = new Thread("checkThreadAlive") {
                public void run() {
                    for (Thread thread : threads) {
                        while (true) {
                            if (!thread.isAlive()) {
                                System.out.println("线程" + thread.getName() + "完成......");
                                thread = null;
                                break;
                            }
                        }
                    }
                    long endTime = System.currentTimeMillis();
                    try {
                        con.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    System.out.println("文件" + filePath + "入库完成，用时" + (endTime - startTime) / 1000);
                }
            };
            thread.start();
        }
    }

    private static void normalFileThread(final Connection con, final String filePath, String tableName, String header) {
        final long startTime = System.currentTimeMillis();
        long fileLength = getFileLength(filePath);
        if (fileLength < 10 * 1024) { // 1 * 1024 = 1KB, 1024 * 1024 = 1MB
            FileToDBThread fileToDBThread = new FileToDBThread("thread1", filePath, 0, fileLength, tableName, header, 1000, ",", "UTF-8", con);
            Thread thread = new Thread(fileToDBThread);
            thread.start();
        } else {
            // get available processor number
            int threadProcessNum = Runtime.getRuntime()
                    .availableProcessors();
            System.out.println("txt avaliable thread number: " + threadProcessNum);
            long fileBlockLength = (fileLength + threadProcessNum - 1)
                    / threadProcessNum;
            long lastEndIndex = -1;
            final Thread[] threads = new Thread[threadProcessNum];
            for (int i = 0; i < threadProcessNum; i++) {
                long currentStartIndex = lastEndIndex + 1;
                long currentEndIndex = currentStartIndex + fileBlockLength;
                if (currentEndIndex < fileLength) {
                    currentEndIndex = adjustFileReadPosition(filePath,
                            currentEndIndex);
                } else {
                    currentEndIndex = fileLength;
                }
                FileToDBThread fileToDBThread = new FileToDBThread("thread" + i, filePath, currentStartIndex, currentEndIndex, tableName, header, 1000, ",", "UTF-8", con);
                Thread thread = new Thread(fileToDBThread);
                thread.start();
                threads[i] = thread;
                lastEndIndex = currentEndIndex;
                System.out.println("参数是:" + filePath + ",tableName=" + tableName + "," + header + ",");
                System.out.println("线程" + i + "开始,start=" + currentStartIndex + ",end=" + currentEndIndex);
            }
            /**
             * 开启一个线程去检验所有线程是否跑完
             */
            Thread thread = new Thread("checkThreadAlive") {
                public void run() {
                    for (Thread thread : threads) {
                        while (true) {
                            if (!thread.isAlive()) {
                                System.out.println("线程" + thread.getName() + "完成......");
                                thread = null;
                                break;
                            }
                        }
                    }
                    long endTime = System.currentTimeMillis();
                    try {
                        con.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    System.out.println("文件" + filePath + "入库完成，用时" + (endTime - startTime) / 1000);
                }
            };
            thread.start();
        }
    }

    public static int getRecordNumFromFile(String filePath) {
        int count = 0;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File(filePath)));
            String line = reader.readLine();
            while (line != null) {
                count++;
                line = reader.readLine();
            }
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return count;
    }

    public static void main(String[] args) {
        testBatch();
    }
}


