package canghailongyin.blog.main;


import com.monitorjbl.xlsx.StreamingReader;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;

/**
 * Created by mingl on 2017-5-2.
 */
public class BigXLSXAnalysis {
    public static final String FILE_PATH = "E:\\程序生成\\100WX10.xlsx";

    public static void main(String[] args) {
        //注意，如果文件过大，normal方法会报错，请注释掉
        long normalStart = System.currentTimeMillis();
        normalAnalysis();
        long normalEnd = System.currentTimeMillis();
        caculateTime("normalAnalysis", normalStart, normalEnd);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long streamingStart = System.currentTimeMillis();
        streamingAnalysis();
        long streamingEnd = System.currentTimeMillis();
        caculateTime("streamingAnalysis", streamingStart, streamingEnd);
    }

    /**
     * 显示方法计算所用的时间
     *
     * @param function
     * @param timeStart
     * @param timeEnd
     */
    private static void caculateTime(String function, long timeStart, long timeEnd) {
        System.out.println("方法" + function + "耗时:" + (timeEnd - timeStart) / 1000 + "s");
    }

    public static void streamingAnalysis() {
        InputStream input = null;
        Workbook workbook = null;
        long start = System.currentTimeMillis();
        try {
            input = new FileInputStream(FILE_PATH);
            workbook = StreamingReader.builder()
                    .rowCacheSize(1000)    // number of rows to keep in memory (defaults to 10)
                    .bufferSize(4096)     // buffer size to use when reading InputStream to file (defaults to 1024)
                    .open(input);
            long end = System.currentTimeMillis();
            caculateTime("将input封装为workbook对象", start, end);
            int sheetNum = workbook.getNumberOfSheets();
            for (int i = 0; i < sheetNum; i++) {
                Sheet sheet = workbook.getSheetAt(i);
                System.out.println("sheetName:" + sheet.getSheetName());
                int rowIndex = 0;
                for (Row row : sheet) {
//                    System.out.println("第"+rowIndex+"行，cellNum:"+(row.getLastCellNum()-1));
                    for (Cell cell : row) {
//                        System.out.println(cell.getStringCellValue());
                    }
                    rowIndex++;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
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

    public static void normalAnalysis() {
        InputStream input = null;
        XSSFWorkbook xssfWorkbook = null;
        long start = System.currentTimeMillis();
        try {
            input = new FileInputStream(FILE_PATH);
            xssfWorkbook = new XSSFWorkbook(input);
            long end = System.currentTimeMillis();
            caculateTime("将input封装为XSSFWorkbook对象", start, end);
            int sheetNum = xssfWorkbook.getNumberOfSheets();
            for (int numSheet = 0; numSheet < sheetNum; numSheet++) {
                //获取当前sheet
                XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(numSheet);
                if (xssfSheet == null) {
                    continue;
                }
                // 获取当前工作薄的每一行，第一行是标题
                for (int rowNum = 0; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
                    XSSFRow xssfRow = xssfSheet.getRow(rowNum);
                    //一行共有多少列
                    int cellNum = xssfRow.getLastCellNum();
//                    System.out.println("第"+rowNum+"行，cellNum:"+cellNum);
                    if (xssfRow != null) {
                        for (int i = 0; i < cellNum; i++) {
                            //一行的每个列的值
                            XSSFCell cell = xssfRow.getCell(i);
//                            System.out.println(getValue(cell));
                        }
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                input.close();
                xssfWorkbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private static String getValue(XSSFCell xssfRow) {

        if (xssfRow.getCellType() == xssfRow.CELL_TYPE_BOOLEAN) {
            return String.valueOf(xssfRow.getBooleanCellValue());
        } else if (xssfRow.getCellType() == xssfRow.CELL_TYPE_NUMERIC) {
            return String.valueOf(xssfRow.getNumericCellValue());
        } else {
            return String.valueOf(xssfRow.getStringCellValue());
        }
    }
}
