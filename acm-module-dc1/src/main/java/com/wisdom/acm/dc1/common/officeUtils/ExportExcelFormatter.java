package com.wisdom.acm.dc1.common.officeUtils;

import com.wisdom.acm.dc1.common.DateUtil;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.util.ObjectUtils;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 综合导出工具类
 *
 * @author PC
 */
public class ExportExcelFormatter
{
    /**
     * 给单元格设置格式---可以設置單元格格式
     *
     * @param cell
     * @param style
     * @param value
     */
    public static void setXSSFCellInfo(XSSFCell cell, CellStyle style, Object value)
    {
        cell.setCellStyle(style); // 设置样式
        if (value instanceof Integer)
        {
            cell.setCellValue(Integer.valueOf(String.valueOf(value))); // 表头标题
            cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC); // 定义单元格为数字类型
        }
        else if (value instanceof String)
        {
            cell.setCellValue(String.valueOf(value)); // 表头标题
            cell.setCellType(HSSFCell.CELL_TYPE_STRING); // 定义单元格为数字类型
        }
        else if (value instanceof Double)
        {
            cell.setCellValue(Double.valueOf(String.valueOf(value))); // 表头标题
        }
        else if (value instanceof Date)
        {
            cell.setCellValue((Date) value); // 表头标题
        }
    }

    /**
     * 设置一般单元格样式
     *
     * @param wb
     * @param fontSize 字体大小
     * @param fontName 字体类型
     * @return
     */
    public static XSSFCellStyle getXSSFCommonCellStyle(XSSFWorkbook wb, int fontSize, String fontName)
    {
        return getXSSFCellStyle(wb, fontSize, fontName, false, false, false, null, null);
    }

    public static XSSFCellStyle getXSSFCommonCellStyle(XSSFWorkbook wb, int fontSize, String fontName, Color color)
    {
        return getXSSFCellStyle(wb, fontSize, fontName, false, false, false, null, color);
    }

    /**
     * 设置有北京颜色的一般单元格样式
     *
     * @param wb
     * @param fontSize 字体大小
     * @param fontName 字体类型
     * @return
     */
    public static XSSFCellStyle getXSSFColorCommonCellStyle(XSSFWorkbook wb, int fontSize, String fontName, Color color)
    {
        return getXSSFCellStyle(wb, fontSize, fontName, false, false, false, color, null);
    }

    /**
     * 设置时间单元格样式
     *
     * @param wb
     * @param fontSize 字体大小
     * @param fontName 字体类型
     * @return
     */
    public static XSSFCellStyle getXSSFDateCellStyle(XSSFWorkbook wb, int fontSize, String fontName)
    {
        return getXSSFCellStyle(wb, fontSize, fontName, false, false, true, null, null);
    }

    /**
     * 设置默认单元格样式
     *
     * @param wb
     * @return
     */
    public static XSSFCellStyle getXSSFDefaultCellStyle(XSSFWorkbook wb)
    {
        return getXSSFCellStyle(wb, 11, "仿宋", false, false, false, null, null);
    }

    /**
     * 設置分數的單元格樣式
     *
     * @param wb
     * @param fontSize
     * @param fontName
     * @param color
     * @return
     */
    public static XSSFCellStyle getXSSFScoreCellStyle(XSSFWorkbook wb, int fontSize, String fontName, Color color)
    {
        return getXSSFCellStyle(wb, fontSize, fontName, true, false, false, color, null);
    }

    /**
     * 设置表頭单元格样式
     *
     * @param wb
     * @return
     */
    public static XSSFCellStyle getXSSFHeadCellStyle(XSSFWorkbook wb, int fontSize, String fontName)
    {
        return getXSSFCellStyle(wb, fontSize, fontName, true, true, false, null, null);
    }

    /**
     * 设置带颜色的表頭单元格样式
     *
     * @param wb
     * @return
     */
    public static XSSFCellStyle getXSSFHeadCellStyle(XSSFWorkbook wb, int fontSize, String fontName, Color color)
    {
        return getXSSFCellStyle(wb, fontSize, fontName, true, true, false, color, null);
    }

    /**
     * 設置樣式
     *
     * @param wb
     * @param fontSize
     * @param fontName
     * @param bColor
     * @return
     */
    public static XSSFCellStyle getXSSFCellStyle(XSSFWorkbook wb, int fontSize, String fontName, boolean isAlign,
            boolean isBold, boolean isDate, Color bColor, Color fontColor)
    {
        XSSFCellStyle cellStyle = wb.createCellStyle(); // 创建单元格样式
        if (isAlign) // 是否水平居中
            cellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER); // 设置单元格水平居中对齐
        cellStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER); // 设置单元格垂直居中对齐
        cellStyle.setWrapText(true); // 创建单元格内容显示不下时自动换行

        cellStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN); // 设置单元格边框为细线条
        cellStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);

        if (isDate) // 时间格式
        {
            CreationHelper createHelper = wb.getCreationHelper();
            cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("yyyy/MM/dd"));
        }
        if (bColor != null) // 是否设置背景颜色
        {
            cellStyle.setFillForegroundColor(new XSSFColor(bColor));
            cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        }

        XSSFFont font = wb.createFont(); // 设置单元格字体样式
        if (isBold) // 字體加粗
            font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
        if (!ObjectUtils.isEmpty(fontColor))
            font.setColor(new XSSFColor(fontColor));
        font.setFontName(fontName); // 字体
        font.setFontHeightInPoints((short) fontSize); // 字体大小
        cellStyle.setFont(font);

        return cellStyle;
    }

    /**
     * 验证小数
     *
     * @param xssfrow   当前行
     * @param cellIndex 验证单元格
     * @return
     */
    public static Map<String, Object> validateDoubleType(XSSFRow xssfrow, int cellIndex)
    {
        Map<String, Object> returnMap = new HashMap<String, Object>();
        if (xssfrow.getCell(cellIndex) != null && !xssfrow.getCell(cellIndex).toString().trim().equals(""))
        {
            if (xssfrow.getCell(cellIndex).getCellType() != XSSFCell.CELL_TYPE_NUMERIC)
            {
                String drtn = xssfrow.getCell(cellIndex).toString().trim();
                if (isNumberic(drtn))
                    returnMap.put("successInfo", Double.valueOf(drtn)); // 验证成功
                else
                    returnMap.put("errorInfo", "验证失败"); // 验证成功
            }
            else
                returnMap.put("successInfo", xssfrow.getCell(cellIndex).getNumericCellValue()); // 验证成功
        }
        else // 为空的话报错
        {
            returnMap.put("errorInfo", "验证失败"); // 验证成功
        }
        return returnMap;
    }

    /**
     * 验证整数
     *
     * @param xssfrow   当前行
     * @param cellIndex 验证单元格
     * @return
     */
    public static Map<String, Object> validateIntegerType(XSSFRow xssfrow, int cellIndex)
    {
        Map<String, Object> returnMap = new HashMap<String, Object>();
        if (xssfrow.getCell(cellIndex) != null && !xssfrow.getCell(cellIndex).toString().trim().equals(""))
        {
            if (xssfrow.getCell(cellIndex).getCellType() != XSSFCell.CELL_TYPE_NUMERIC)
            {
                try
                {
                    String doubleStr = xssfrow.getCell(cellIndex).toString().trim();
                    String intStr = doubleStr.substring(0, doubleStr.indexOf("."));
                    Integer drtn = Integer.valueOf(intStr);
                    returnMap.put("successInfo", drtn); // 验证成功
                }
                catch (Exception e)
                {
                    returnMap.put("errorInfo", "验证失败"); // 验证成功
                }
            }
            else
            {
                try
                {
                    String doubleStr = xssfrow.getCell(cellIndex).toString().trim();
                    String intStr = doubleStr.substring(0, doubleStr.indexOf("."));
                    Integer drtn = Integer.valueOf(intStr);
                    returnMap.put("successInfo", drtn); // 验证成功
                }
                catch (Exception e)
                {
                    returnMap.put("errorInfo", "验证失败"); // 验证成功
                }
            }
        }
        else
        {
            returnMap.put("errorInfo", "验证失败"); // 验证成功
        }
        return returnMap;
    }

    /**
     * 验证时间
     *
     * @param xssfrow   当前行
     * @param cellIndex 验证单元格
     * @return
     */
    public static Map<String, Object> validateDateType(XSSFRow xssfrow, int cellIndex)
    {
        Map<String, Object> returnMap = new HashMap<String, Object>();
        if (xssfrow.getCell((short) cellIndex).getCellType() == XSSFCell.CELL_TYPE_NUMERIC)
        {
            if (HSSFDateUtil.isCellDateFormatted(xssfrow.getCell((short) cellIndex)))
            {
                returnMap.put("successInfo",
                        HSSFDateUtil.getJavaDate(xssfrow.getCell((short) cellIndex).getNumericCellValue())); // 验证成功
            }
            else
            {
                returnMap.put("errorInfo", "验证失败"); // 验证失败
            }
        }
        else
        {
            returnMap.put("errorInfo", "验证失败"); // 验证失败
        }
        return returnMap;
    }

    /**
     * 验证导入数据是否有问题
     *
     * @param xssfrow
     * @return
     */
    public static Map<String, Object> getRowErrorInfoMap(XSSFRow xssfrow, int index)
    {
        Map<String, Object> errorMap = new HashMap<String, Object>();
        for (int i = 0; i < index; i++)
        {
            if ("".equals(String.valueOf(xssfrow.getCell((short) i)).trim()))
                errorMap.put("errorInfo", "带'*'数据为必填,请输入!");
        }
        return errorMap;
    }

    /**
     * 根据不同的类型转换字符串,字符串异常的话就直接输出字符串
     *
     * @param c
     * @param doubleStr
     * @param type
     */
    public static void insertXSSFCellByType(XSSFCell c, XSSFCellStyle commonStyle, String doubleStr, String type)
    {
        if (ExportExcelFormatter.isNumberic(doubleStr)) // 是小数
        {
            Object drtn = null;
            if ("int".equalsIgnoreCase(type)) // 转换为小数
            {
                String intStr = "";
                if (doubleStr.indexOf(".") >= 0)
                    intStr = doubleStr.substring(0, doubleStr.indexOf("."));
                else
                    intStr = doubleStr;
                drtn = Integer.valueOf(intStr);
            }
            else
            // 转换为double
            {
                drtn = Double.valueOf(doubleStr);
            }
            ExportExcelFormatter.setXSSFCellInfo(c, commonStyle, drtn);
        }
        else
        // 是字符串
        {
            ExportExcelFormatter.setXSSFCellInfo(c, commonStyle, doubleStr);
        }
    }

    /**
     * 输出日期,有异常的话直接输出字符串
     *
     * @param c
     */
    public static void insertXSSFCellByDateType(XSSFCell c, XSSFCellStyle dateStyle, XSSFCellStyle commonStyle,
            String dateStr)
    {
        if (ExportExcelFormatter.isDate(dateStr)) // 是小数
        {
            ExportExcelFormatter
                    .setXSSFCellInfo(c, dateStyle, DateUtil.formatDate(dateStr, DateUtil.DATE_CHINA_FORMAT));
        }
        else // 是字符串
        {
            ExportExcelFormatter.setXSSFCellInfo(c, commonStyle, dateStr);
        }
    }

    /**
     * 验证字符串是否是时间
     *
     * @param time
     * @return
     */
    public static boolean isDate(String time)
    {
        if (ObjectUtils.isEmpty(time))
        {
            return false;
        }
        time = time.trim();
        String format = "";
        if (time.indexOf("-") > -1)
        {
            format = "yyyy-MM-dd";
        }
        else if (time.indexOf("/") > -1)
        {
            format = "yyyy/MM/dd";
        }
        try
        {
            new SimpleDateFormat(format).parse(time);
        }
        catch (Exception e)
        {
            return false;
        }
        return true;
    }

    /**
     * 验证是否是小数
     */
    public static boolean isNumberic(String str)
    {
        if (str == null)
        {
            return false;
        }
        try
        {
            Double.parseDouble(str);
        }
        catch (Exception e)
        {
            return false;
        }
        return true;
    }

    /**
     * 获取行里面的小数
     *
     * @param cell
     * @return
     */
    public static Double isNumType(Cell cell)
    {
        Double value = null;
        if (cell != null)
        {
            if (XSSFCell.CELL_TYPE_NUMERIC == cell.getCellType() && !HSSFDateUtil.isCellDateFormatted(cell))
            {
                value = cell.getNumericCellValue();
            }
        }
        return value;
    }

    /**
     * 获取行里面的时间
     *
     * @param cell
     * @return
     */
    public static Object isDateType(XSSFCell cell)
    {
        Object value = cell;
        if (cell != null)
        {
            if (XSSFCell.CELL_TYPE_NUMERIC == cell.getCellType())
            {
                if (HSSFDateUtil.isCellDateFormatted(cell))
                {
                    value = DateUtil.getDateFormat(cell.getDateCellValue());
                }
                else
                {
                    value = cell.getStringCellValue();
                }
            }
            else
            {
                value = cell.getStringCellValue();
            }
        }
        return value;
    }

    /**
     * 获取一行一面的错误信息
     *
     * @param index
     * @param errorInfo
     * @return
     */
    public static String getImportQuestionErrorInfo(int index, String errorInfo)
    {
        return "(" + index + ")" + errorInfo;
    }
}
