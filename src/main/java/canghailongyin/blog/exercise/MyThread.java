package canghailongyin.blog.exercise;

import canghailongyin.blog.utils.DateUtils;
import canghailongyin.blog.utils.MysqlBasic;
import canghailongyin.blog.utils.RandomUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by mingl on 2018-1-3.
 * 自定义的线程类，为具体业务生成run方法
 */
public class MyThread implements Runnable {
    private String threadName;
    private int insertNum;
    private String tableName;

    public MyThread(String threadName, int insertNum, String tableName) {
        this.threadName = threadName;
        this.insertNum = insertNum;
        this.tableName = tableName;
    }

    @Override
    public void run() {
        try {
            insertData(threadName,insertNum,tableName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 线程执行插入的主体
     * @param threadName
     * @param insertNum
     * @param tableName
     */
    private void insertData(String threadName, int insertNum, String tableName) throws SQLException {
        System.out.println("线程"+threadName+"开始向"+tableName+"中插入"+insertNum+"记录..."+new Date());
        //打开数据库连接
        MysqlBasic basic = new MysqlBasic("data2");
        Connection connection = basic.open();
        //插入sql
        String header = "(user_id,date,time,action)";
        String sql = "insert into `"+tableName+"` "+header+" values ";
        PreparedStatement statement = connection.prepareStatement(sql);
        //多少行插入一次
        int valueLength = 1000;
        //检测目前插入的个数
        int index = 0;
        //确定有多少人
        int realUserNum = (int)(insertNum * 0.1);
        List<String> userIdList = new ArrayList<String>();
        for(int i=0;i<realUserNum;i++){
            userIdList.add(UUID.randomUUID().toString());
        }
        //开始插入数据
        StringBuilder valueString = new StringBuilder();
        String id = "",date = "",time = "",action = "";
        String startDate = "2017-01-01 00:00:00";
        String endDate = "2017-12-31 23:59:59";
        for(int i=0;i<insertNum;i++){
            index++;
            id = userIdList.get(RandomUtils.getNum(0,userIdList.size()-1));
            date = DateUtils.getRandomDate(startDate,endDate).substring(0,10);
            time = DateUtils.getRandomDate(startDate,endDate).substring(11,19);
            action = i%2==0?"上线":"下线";
            valueString.append("('"+id+"','"+date+"','"+time+"','"+action+"'),");
            if(index%valueLength == 0 || index == insertNum){
                statement.addBatch(sql + valueString.toString().substring(0,valueString.length()-1));
                statement.executeBatch();
                valueString = new StringBuilder();
            }
        }
        basic.close();
        System.out.println("线程"+threadName+"向"+tableName+"中插入"+insertNum+"记录结束..."+new Date());
    }
}
