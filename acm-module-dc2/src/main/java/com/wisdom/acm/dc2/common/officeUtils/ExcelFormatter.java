package com.wisdom.acm.dc2.common.officeUtils;

import com.wisdom.acm.dc2.common.DateUtil;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.util.ObjectUtils;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 综合导出工具类
 *
 * @author PC
 */
public class ExcelFormatter
{
    /**
     * 字体居中
     */
    public static enum FontAlign
    {
        Left, Right, Center;
    }

    /**
     * 处理字体 -- 暂时只有宋体和微软雅黑
     *
     * @author FAN
     */
    public static enum FontType
    {
        ST("宋体"), WRYH("微软雅黑");

        // 定义属性
        private String title;

        // 定义一个有参构造,不能使用public声明
        private FontType(String title)
        {
            this.title = title;
        }

        // 定义一个方法
        public String toString()
        {
            return this.title;
        }
    }

    /**
     * 垂直居中
     */
    public static enum FontVertical
    {
        Top/*上*/, Bottom/*下*/, Center/*居中*/;
    }

    /**
     * 字体加粗
     */
    public static enum FontBold
    {
        TRUE, FALSE;
    }

    /**
     * 字体颜色
     */
    public static enum FontColor
    {
        WHITE, BLACK, red, RED, yellow, YELLOW, green, GREEN, BLUE, GREENYELLOW, YELLOWRED;
    }

    /**
     * 是否是时间格式
     */
    public static enum FontDate
    {
        TRUE, FALSE;
    }

    /**
     * 边框是否加线
     */
    public static enum CellBorder
    {
        Left/* 左 */, Bottom/* 下 */, Right/* 右 */, Top/* 上 */, All/*所有*/;
    }

    /**
     * 合并单元格 四个参数分别是：起始行，结束行，起始列，结束列
     *
     * @param sheet
     * @param cells
     */
    public static void mergeCells(XSSFSheet sheet, int cells[][])
    {
        if (cells.length > 0)
        {
            for (int cell[] : cells)
            {
                if (cell.length == 4)    // 只能是四个参数  不然不考虑
                {
                    sheet.addMergedRegion(new CellRangeAddress(cell[0], cell[1], cell[2], cell[3]));// 合并供应商
                }
            }
        }
    }

    /**
     * 设置列宽
     *
     * @param sheet
     * @param columnWidth
     */
    public static void setColumnWidth(XSSFSheet sheet, int columnWidth[])
    {
        if (columnWidth.length > 0)
        {
            for (int index = 0; index < columnWidth.length; index++)
            {
                sheet.setColumnWidth(index, columnWidth[index]); // 设置列的宽度
            }
        }
    }

    /**
     * 设置列宽
     *
     * @param sheet
     */
    public static void setColumnWidth(XSSFSheet sheet, List<Integer> columnWidthList)
    {
        if (columnWidthList.size() > 0)
        {
            for (int index = 0; index < columnWidthList.size(); index++)
            {
                sheet.setColumnWidth(index, columnWidthList.get(0)); // 设置列的宽度
            }
        }
    }

    /**
     * 给单元格赋值
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
     * 给单元格赋空值
     *
     * @param cell
     * @param style
     */
    public static void setXSSFCellInfo(XSSFCell cell, CellStyle style)
    {
        setXSSFCellInfo(cell, style, "");
    }

    /**
     * 设置样式
     *
     * @param wb
     * @return
     */
    public static XSSFCellStyle getXSSFCellStyle(XSSFWorkbook wb, Object... param)
    {
        Object[] paramArr = param;

        XSSFCellStyle cellStyle = (XSSFCellStyle) wb.createCellStyle(); // 创建单元格样式
        // 默认是垂直居中
        cellStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER); // 设置单元格垂直居中对齐
        cellStyle.setWrapText(true); // 创建单元格内容显示不下时自动换行

        XSSFFont font = (XSSFFont) wb.createFont(); // 设置单元格字体样式
        font.setFontName("宋体"); // 字体 默认是宋体
        font.setFontHeightInPoints((short) 10); // 字体大小,默认为10
        cellStyle.setFont(font);

        for (Object paramObject : paramArr)
        {
            if (paramObject instanceof FontVertical)    // 是否加粗
            {
                FontVertical fontVertical = (FontVertical) paramObject;
                if (fontVertical == FontVertical.Top)
                    cellStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_TOP); // 垂直往下
                if (fontVertical == FontVertical.Bottom)
                    cellStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_BOTTOM); // 垂直往下
                if (fontVertical == FontVertical.Center)
                    cellStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER); // 垂直往下
            }
            if (paramObject instanceof FontBold)    // 是否加粗
            {
                FontBold fontBold = (FontBold) paramObject;
                if (fontBold == FontBold.TRUE)
                    font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
            }
            else if (paramObject instanceof FontAlign)    // 是否居中
            {
                FontAlign fontAlign = (FontAlign) paramObject;
                if (fontAlign == FontAlign.Left)
                    cellStyle.setAlignment(XSSFCellStyle.ALIGN_LEFT); // 设置单元格水平居中对齐
                if (fontAlign == FontAlign.Right)
                    cellStyle.setAlignment(XSSFCellStyle.ALIGN_RIGHT); // 设置单元格水平居中对齐
                if (fontAlign == FontAlign.Center)
                    cellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER); // 设置单元格水平居中对齐
            }
            else if (paramObject instanceof FontDate)    // 是否是时间格式
            {
                FontDate fontDate = (FontDate) paramObject;
                if (fontDate == FontDate.TRUE)    // 设置为时间格式
                {
                    CreationHelper createHelper = wb.getCreationHelper();
                    cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("yyyy/MM/dd"));
                }
            }
            else if (paramObject instanceof FontColor)    // 字体颜色
            {
                FontColor color = (FontColor) paramObject;
                if (FontColor.red == color)
                    font.setColor(new XSSFColor(new Color(255, 0, 0)));
                if (FontColor.YELLOWRED == color)
                    font.setColor(new XSSFColor(new Color(235, 148, 55)));
                else if (FontColor.yellow == color)
                    font.setColor(new XSSFColor(new Color(249, 249, 0)));
                else if (FontColor.GREENYELLOW == color)
                    font.setColor(new XSSFColor(new Color(47, 163, 78)));
                else if (FontColor.green == color)
                    font.setColor(new XSSFColor(new Color(40, 255, 40)));
                else if (FontColor.RED == color)
                    font.setColor(new XSSFColor(Color.red));
                else if (FontColor.YELLOW == color)
                    font.setColor(new XSSFColor(Color.yellow));
                else if (FontColor.GREEN == color)
                    font.setColor(new XSSFColor(Color.green));
            }
            else if (paramObject instanceof Color)    // 背景颜色
            {
                Color color = (Color) paramObject;
                if (color != Color.WHITE)
                {
                    cellStyle.setFillForegroundColor(new XSSFColor(color));
                    cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
                }
            }
            else if (paramObject instanceof Integer)    // 字体大小.默认为"10"号
            {
                int fontSize = Integer.valueOf(String.valueOf(paramObject));
                font.setFontHeightInPoints((short) fontSize);
            }
            else if (paramObject instanceof FontType)    // 字体样式,默认是"宋体"
            {
                String fontName = paramObject.toString();
                font.setFontName(fontName); // 字体
            }
            else if (paramObject instanceof CellBorder)    // 设置单元格边框为细线条
            {
                CellBorder border = (CellBorder) paramObject;
                if (CellBorder.Left == border || CellBorder.All == border)
                    cellStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);
                if (CellBorder.Bottom == border || CellBorder.All == border)
                    cellStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN);
                if (CellBorder.Right == border || CellBorder.All == border)
                    cellStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);
                if (CellBorder.Top == border || CellBorder.All == border)
                    cellStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);
            }
        }
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
        if (ExcelFormatter.isNumberic(doubleStr)) // 是小数
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
            else // 转换为double
            {
                drtn = Double.valueOf(doubleStr);
            }
            ExcelFormatter.setXSSFCellInfo(c, commonStyle, drtn);
        }
        else // 是字符串
        {
            ExcelFormatter.setXSSFCellInfo(c, commonStyle, doubleStr);
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
        if (ExcelFormatter.isDate(dateStr)) // 是小数
        {
            ExcelFormatter
                    .setXSSFCellInfo(c, dateStyle, DateUtil.formatDate(dateStr, DateUtil.DATETIME_DEFAULT_FORMAT));
        }
        else // 是字符串
        {
            ExcelFormatter.setXSSFCellInfo(c, commonStyle, dateStr);
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
    public static Double getNumType(Cell cell)
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
    public static Object getDateType(XSSFCell cell)
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
    public static String getImportErrorInfo(int index, String errorInfo)
    {
        return "(" + index + ")" + errorInfo;
    }

    /**
     * 返回对应的颜色--字体颜色
     *
     * @param color
     * @return
     */
    public static FontColor getExcelFontColorByColor(String color)
    {
        if ("red".equalsIgnoreCase(color) || "#ff0000".equalsIgnoreCase(color))
            return FontColor.red;
        else if ("yellow".equalsIgnoreCase(color) || "#f9f900".equalsIgnoreCase(color))
            return FontColor.yellow;
        else if ("green".equalsIgnoreCase(color) || "#28ff28".equalsIgnoreCase(color))
            return FontColor.green;
        else if ("greenyellow".equalsIgnoreCase(color) || "#2FA34E".equalsIgnoreCase(color))
            return FontColor.GREENYELLOW;
        else if ("yellowred".equalsIgnoreCase(color) || "#EDA537".equalsIgnoreCase(color))
            return FontColor.YELLOWRED;
        else
            return FontColor.BLACK;
    }

    /**
     * 返回对应的颜色--背景颜色
     *
     * @param color
     * @return
     */
    public static Color getExcelBgColorByColor(String color)
    {
        if ("red".equalsIgnoreCase(color) || "#ff0000".equalsIgnoreCase(color))
            return Color.RED;
        else if ("yellow".equalsIgnoreCase(color) || "#f9f900".equalsIgnoreCase(color))
            return Color.YELLOW;
        else if ("green".equalsIgnoreCase(color) || "#28ff28".equalsIgnoreCase(color))
            return Color.GREEN;
        else
            return Color.WHITE;    // 默认为白色
    }

    /**
     * 判断行中单元格中内容是否为空
     *
     * @param hssfrow
     * @param index
     * @return
     */
    public static boolean isHssfrowCellInfoEmpty(XSSFRow hssfrow, int index)
    {
        if ("".equals(String.valueOf(hssfrow.getCell((short) index))))
        {
            return true;
        }
        return false;
    }

    /**
     * 获取单元格中的内容  是字符串类型
     *
     * @param hssfrow
     * @param index
     * @return
     */
    public static String getHssfrowCellInfo(XSSFRow hssfrow, int index)
    {
        if (!"".equals(String.valueOf(hssfrow.getCell((short) index))))
        {
            if (XSSFCell.CELL_TYPE_NUMERIC == hssfrow.getCell(index).getCellType())
            {
                if (HSSFDateUtil.isCellDateFormatted(hssfrow.getCell(index)))    // 如果是日期
                {
                    return DateUtil.getDateFormat(hssfrow.getCell(index).getDateCellValue());
                }
                else    // 数字
                {
                    String value = String.valueOf(hssfrow.getCell(index).getNumericCellValue()); // 如果是double型并且小数后只有0
                    if (value.contains(".0"))
                    {
                        value = value.substring(0, value.lastIndexOf("."));
                    }
                    return value;
                }
            }
            hssfrow.getCell((short) index).setCellType(XSSFCell.CELL_TYPE_STRING);
            return String.valueOf(hssfrow.getCell((short) index)).trim();
        }
        return "";
    }

    /**
     * 判断是否是数字
     *
     * @param hssfrow
     * @param index
     * @return
     */
    public static boolean isHssfrowCellInfoIsNumber(XSSFRow hssfrow, int index, boolean type/*true为必填 false为选填*/)
    {
        if (!"".equals(String.valueOf(hssfrow.getCell((short) index))))
        {
            hssfrow.getCell((short) index).setCellType(XSSFCell.CELL_TYPE_STRING);
            String number = ExcelFormatter.getHssfrowCellInfo(hssfrow, index);    // 合同经费
            if (!NumberUtils.isNumber(number))
            {
                return false;
            }
            else
            {
                return true;
            }
        }
        else
        {
            return !type;
        }
    }

}
