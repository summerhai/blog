package canghailongyin.blog.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by mingl on 2018-1-5.
 */
public class DateUtils {

    /**
     * 获取随机日期
     *
     * @param beginDate 起始日期，格式为：yyyy-MM-dd HH:mm:ss
     * @param endDate   结束日期，格式为：yyyy-MM-dd HH:mm:ss
     * @return
     */

    public static String getRandomDate(String beginDate, String endDate) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date start = format.parse(beginDate);// 构造开始日期
            Date end = format.parse(endDate);// 构造结束日期
            // getTime()表示返回自 1970 年 1 月 1 日 00:00:00 GMT 以来此 Date 对象表示的毫秒数。
            if (start.getTime() >= end.getTime()) {
                return null;
            }
            long date = random(start.getTime(), end.getTime());
            return format.format(new Date(date));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static long random(long begin, long end) {
        long rtn = begin + (long) (Math.random() * (end - begin));
//        // 如果返回的是开始时间和结束时间，则递归调用本函数查找随机值
//        if (rtn == begin || rtn == end) {
//            return random(begin, end);
//        }
        return rtn;
    }
}
