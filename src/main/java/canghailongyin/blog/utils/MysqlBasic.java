package canghailongyin.blog.utils;

import java.io.Closeable;
import java.sql.*;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by mingl on 2017-4-26.
 */
public class MysqlBasic {

    public static String database = "";
    private static final String enCoding = "?useUnicode=true&characterEncoding=utf8&autoReconnect=true&maxReconnects=3&useSSL=false";
    public Connection conn = null;
    public PreparedStatement pst = null;

    public MysqlBasic(String database) {
        this.database = database;
    }

    public MysqlBasic() {

    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public Connection open() {
        try {
            String dbHost = PropertiesUtil.getValue("mysqlHost", "localhost");
            String dbPort = PropertiesUtil.getValue("mysqlPort", "3306");
            String dbUser = PropertiesUtil.getValue("mysqlUser", "root");
            String dbPassword = PropertiesUtil.getValue("mysqlPassword", "123456");
            String dbDriver = PropertiesUtil.getValue("mysqlDriver", "com.mysql.jdbc.Driver");
            String url = "";
            if (database.equals("")) {
                url = "jdbc:mysql://" + dbHost + ":" + dbPort + enCoding;
            } else {
                url = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + database + enCoding;
            }
            Class.forName(dbDriver);
            conn = DriverManager.getConnection(url, dbUser, dbPassword);
            System.out.println(conn);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }


    public ResultSet get(String sql) {
        ResultSet ret = null;
        try {
            pst = conn.prepareStatement(sql);
            ret = pst.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ret;
    }

    public int create(String sql) {
        int ret = 0;
        try {
            pst = conn.prepareStatement(sql);
            ret = pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ret;
    }

    public int update(String sql) {
        int ret = 0;
        try {
            pst = conn.prepareStatement(sql);
            ret = pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ret;
    }

    public int put(String sql) {
        int ret = 0;
        try {
            pst = conn.prepareStatement(sql);
            ret = pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ret;
    }

    public int delete(String sql) {
        int ret = 0;
        try {
            pst = conn.prepareStatement(sql);
            ret = pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ret;
    }

    public void close() {
        try {
            if (conn != null)
                this.conn.close();
            if (pst != null)
                this.pst.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
