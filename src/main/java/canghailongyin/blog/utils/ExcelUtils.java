package canghailongyin.blog.utils;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

/**
 * Created by mingl on 2017-8-28.
 */
public class ExcelUtils {

    public static String getCellStringValue(Cell cell) {
        SimpleDateFormat sFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Short[] yyyyMMdd = {14, 31, 57, 58, 176, 177, 178, 181, 184, 185, 186, 187, 188};
        Short[] HHmmss = {20, 32, 21, 179, 190, 191, 192};
        ArrayList<Short> yyyyMMddList = new ArrayList<Short>(Arrays.asList(yyyyMMdd));
        ArrayList<Short> hhMMssList = new ArrayList<Short>(Arrays.asList(HHmmss));

        String cellValue = "default_1";
        if (cell == null) {
            return "default_2";
        }
        switch (cell.getCellType()) {
            case HSSFCell.CELL_TYPE_STRING:    //字符串类型
                cellValue = cell.getStringCellValue();
                if (cellValue.trim().equals("") || cellValue.trim().length() <= 0)
                    cellValue = "default_3";

                break;
            case HSSFCell.CELL_TYPE_NUMERIC:    //数值类型
                //数值型包括纯数字格式和日期格式
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    short format = cell.getCellStyle().getDataFormat();
                    if (format == 178) {
                        sFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    } else if (yyyyMMddList.contains((Short) format)) {
                        sFormat = new SimpleDateFormat("yyyy-MM-dd");
                    } else if (hhMMssList.contains((Short) format)) {
                        sFormat = new SimpleDateFormat("HH:mm:ss");
                    }
                    Date date = cell.getDateCellValue();
                    cellValue = sFormat.format(date);
                } else {
                    //2017年1月15日---->180
                    if (cell.getCellStyle().getDataFormat() == 180) {
                        sFormat = new SimpleDateFormat("yyyy-MM-dd");
                        Date date = cell.getDateCellValue();
                        cellValue = sFormat.format(date);
                    } else {
                        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                        cellValue = cell.getStringCellValue();
                        if (cellValue == null || cellValue.equals("")) {
                            cellValue = "default_4";
                        }
                    }
                }
                break;
            case HSSFCell.CELL_TYPE_FORMULA:    //公式类型
                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                cellValue = cell.getStringCellValue();
                if (cellValue == null || cellValue.equals("")) {
                    cellValue = "default_5";
                }
                break;
            case HSSFCell.CELL_TYPE_BLANK:
                break;
            case HSSFCell.CELL_TYPE_BOOLEAN:
                break;
            case HSSFCell.CELL_TYPE_ERROR:
                break;
            default:
                break;
        }
        return cellValue;
    }

}
