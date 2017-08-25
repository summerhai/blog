package canghailongyin.blog.exercise;

import com.alibaba.fastjson.JSONArray;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by mingl on 2017-8-22.
 */
public class ExcelToJSONTest {

    @Test
    public void testXlsToJson() throws Exception{
        String correctPath = "C:\\Users\\mingl\\Desktop\\laugh_data.xlsx";
        String errorPath = "abc";
        String nullPtah = null;
        String emptyPath = "";
//        Assert.assertSame(null,ExcelToJSON.excelToJSON(nullPtah));
//        Assert.assertSame(null,ExcelToJSON.excelToJSON(errorPath));
//        Assert.assertSame(null,ExcelToJSON.excelToJSON(emptyPath));
//        JSONArray result = ExcelToJSON.excelToJSON(correctPath);
//        System.out.println(result.toJSONString());
//        List<String> result = new ArrayList<>();
//        result.add("111");
//        result.add("333");
//        System.out.println(result.toString().replace("[","").replace("]","").replace(", ",","));
    }

    @Test
    public void testXlsxToJson(){
        String correctPath = "C:\\Users\\mingl\\Desktop\\laugh_data.xlsx";
        String errorPath = "abc";
        String nullPtah = null;
        String emptyPath = "";

    }



}