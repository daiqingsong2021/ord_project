package com.wisdom.acm.szxm.common.officeUtils;

import com.wisdom.acm.szxm.common.StringHelper;
import com.wisdom.base.common.exception.BaseException;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.Map.Entry;

public class ExcelUtil
{

    /**
     * 获取execl的数据
     *
     * @param sheet
     * @param dataIndex 业务数据起始索引行数
     * @return // Map的key 为0,1,2，单元格索引，value为值，存在一个为rowIndex为行的索引（从0开始数）
     */
    public static List<Map<String, Object>> getSheetValue(Sheet sheet, int dataIndex)
    {
        return getSheetValue(sheet, dataIndex, null);
    }

    /**
     * 获取execl的数据
     *
     * @param sheet
     * @param dataIndex
     * @param maxRows 为最大行数
     * @return
     */
    public static List<Map<String, Object>> getSheetValue(Sheet sheet, int dataIndex, Integer maxRows)
    {
        List<Map<String, Object>> valueList = new ArrayList<Map<String, Object>>();
        Map<String, Object> valueMap;
        int sheetSize = sheet.getPhysicalNumberOfRows();
        if (!ObjectUtils.isEmpty(maxRows))
        {
            sheetSize = maxRows;
        }

        for (int j = dataIndex; j < sheetSize; j++)
        {
            Row hssfrow = sheet.getRow(j);
            // int cells = hssfrow.getPhysicalNumberOfCells();
            if (hssfrow != null)    //处理空行
            {
                int cells = hssfrow.getLastCellNum();
                valueMap = new HashMap<String, Object>();
                // 获取每一行的数据
                for (int c = 0; c < cells; c++)
                {
                    Cell cell = hssfrow.getCell((short) c);
                    if (!ObjectUtils.isEmpty(cell))
                    {
                        valueMap.put("" + c, getCellValue(cell));
                    }
                }
                // 判断是否存在空行，如果是空行不予保存
                Iterator<Entry<String, Object>> iter = valueMap.entrySet().iterator();
                boolean blg = false;
                while (iter.hasNext())
                {
                    Entry<String, Object> e = iter.next();

                    if (StringHelper.isNotNullAndEmpty(String.valueOf(e.getValue())))
                    {
                        blg = true;
                        break;
                    }
                }
                // 因为有可能中间会存在空行，所以这里记录行数值。
                valueMap.put("rowIndex", j);

                if (blg)
                    // 集中保存
                    valueList.add(valueMap);
            }
        }

        return valueList;
    }

    /**
     * 获取execl的数据
     *
     * @param sheet
     * @param dataIndex
     * @return
     */
    public static List<String[]> getSheetValueToArray(Sheet sheet, int dataIndex)
    {
        List<String[]> valueList = new ArrayList<String[]>();
        int sheetSize = sheet.getPhysicalNumberOfRows();
        for (int j = dataIndex; j < sheetSize; j++)
        {
            Row hssfrow = sheet.getRow(j);
            int cells = hssfrow.getLastCellNum();
            String[] values = new String[cells];
            for (int c = 0; c < cells; c++)
            {
                Cell cell = hssfrow.getCell((short) c);
                if (!ObjectUtils.isEmpty(cell))
                {
                    values[c] = String.valueOf(getCellValue(cell));
                }
            }
            valueList.add(values);
        }
        return valueList;
    }

    /**
     * 获取execl的数据,并且对number格式的数据进行保留scale位有效数字。（四舍五入）
     *
     * @param sheet
     * @param dataIndex
     * @param scale     number类型的数据保留scale位小数位。
     * @return
     */
    public static List<Map<String, Object>> getSheetValueScale(Sheet sheet, int dataIndex, int scale)
    {
        List<Map<String, Object>> valueList = new ArrayList<Map<String, Object>>();
        Map<String, Object> valueMap;
        int sheetSize = sheet.getPhysicalNumberOfRows();

        for (int j = dataIndex; j < sheetSize; j++)
        {
            Row hssfrow = sheet.getRow(j);
            // int cells = hssfrow.getPhysicalNumberOfCells();
            int cells = hssfrow.getLastCellNum();
            valueMap = new HashMap<String, Object>();
            // 获取每一行的数据
            for (int c = 0; c < cells; c++)
            {
                Cell cell = hssfrow.getCell((short) c);
                if (!ObjectUtils.isEmpty(cell))
                {
                    valueMap.put("" + c, getCellValue(cell, scale));
                }
            }
            // 集中保存
            valueList.add(valueMap);
        }

        return valueList;
    }

    /**
     * 获取单元格数据
     *
     * @param cell
     * @return
     */
    private static Object getCellValue(Cell cell)
    {
        Object object = null;
        if (!ObjectUtils.isEmpty(cell))
        {
            switch (cell.getCellType())
            {
            // 布尔类型
            case Cell.CELL_TYPE_BOOLEAN:
                object = cell.getBooleanCellValue();
                break;
            // 数字
            case Cell.CELL_TYPE_NUMERIC:
                // 判断是否是日期
                if (DateUtil.isCellDateFormatted(cell))
                {
                    Date date = cell.getDateCellValue();
                    object = date;
                    break;
                }
                // 数字
                object = cell.getNumericCellValue();
                //
                String numstr = "";
                if (numstr != null)
                    numstr = object.toString();
                // 处理科学计数法问题。
                numstr = StringHelper.BigDecToPlainString(numstr);
                object = numstr;
                // 一下方法主要是处理int类型的数据，如果小数点后面为0,则将小数点后面去掉。（例如，2.00会转化为2；2.01还是2.01）
                if (StringHelper.isNotNullAndEmpty(numstr))
                {
                    String numStrArr[] = numstr.split("\\.");

                    if (numStrArr.length > 1 && 0 == Integer.valueOf(numStrArr[1]))
                    {
                        object = numStrArr[0];
                    }
                }

                break;
            case Cell.CELL_TYPE_STRING:
                // 字符
                object = cell.getStringCellValue();
                break;
            case Cell.CELL_TYPE_BLANK:
                // 为空
                break;
            case Cell.CELL_TYPE_ERROR:
                // 错误
                break;
            // CELL_TYPE_FORMULA will never happen
            case Cell.CELL_TYPE_FORMULA:
                // 公式
                //object = cell.getRawValue();
                break;
            }
        }

        return object;
    }

    /**
     * 获取单元格数据
     *
     * @param cell
     * @return
     */
    public static Object getCellValue(Cell cell, int scale)
    {
        Object object = null;
        if (!ObjectUtils.isEmpty(cell))
        {
            switch (cell.getCellType())
            {
            // 布尔类型
            case Cell.CELL_TYPE_BOOLEAN:
                object = cell.getBooleanCellValue();
                break;
            // 数字
            case Cell.CELL_TYPE_NUMERIC:
                // 判断是否是日期
                if (DateUtil.isCellDateFormatted(cell))
                {
                    Date date = cell.getDateCellValue();
                    object = date;
                    break;
                }
                // 数字
                object = cell.getNumericCellValue();
                String numstr = String.valueOf(object);
                // 处理科学计数法问题。
                numstr = StringHelper.BigDecToPlainString(numstr, scale);
                object = numstr;

                // 一下方法主要是处理int类型的数据，如果小数点后面为0,则将小数点后面去掉。（例如，2.00会转化为2；2.01还是2.01）
                if (StringHelper.isNotNullAndEmpty(numstr))
                {
                    String numStrArr[] = numstr.split("\\.");

                    if (numStrArr.length > 1 && 0 == Integer.valueOf(numStrArr[1]))
                    {
                        object = numStrArr[0];
                    }
                }

                break;
            case Cell.CELL_TYPE_STRING:
                // 字符
                object = cell.getStringCellValue();
                break;
            case Cell.CELL_TYPE_BLANK:
                // 为空
                break;
            case Cell.CELL_TYPE_ERROR:
                // 错误
                break;
            // CELL_TYPE_FORMULA will never happen
            case Cell.CELL_TYPE_FORMULA:
                // 公式
                //object = cell.getRawValue();
                break;
            }
        }

        return object;
    }


    /**
     * 获取工作簿
     *
     * @param wb
     * @param sheetName
     * @return
     * @throws IOException
     * @throws BaseException
     * @throws InvalidFormatException
     */
    public static Sheet getSheet(Workbook wb, String sheetName) throws
            IOException,
            BaseException,
            InvalidFormatException
    {
        int sheetNum = StringHelper.isNullAndEmpty(sheetName) ? 0 : wb.getSheetIndex(sheetName);// 检索sheet的位置。

        if (sheetNum != -1)
        {
            return wb.getSheetAt(sheetNum);
        }
        else
        {
            throw new BaseException("找不到\"" + sheetName + "\"工作簿!");
        }
    }

    /**
     * 获取工作簿
     *
     * @param wb
     * @param sheetIndex
     * @return
     * @throws IOException
     * @throws BaseException
     * @throws InvalidFormatException
     */
    public static Sheet getSheet(Workbook wb, int sheetIndex) throws IOException, BaseException, InvalidFormatException
    {
        return wb.getSheetAt(sheetIndex);
    }

    /**
     * 获取Excel Workbook
     *
     * @return
     * @throws IOException
     * @throws InvalidFormatException
     */
    public static Workbook getWorkbook(MultipartFile file) throws
            IOException,
            InvalidFormatException
    {
        Workbook wb = null;
        InputStream is = file.getInputStream();
        wb = getWorkbook(is);
        return wb;
    }

    /**
     * 获取excel 工作
     *
     * @param is
     * @return
     * @throws IOException
     * @throws InvalidFormatException
     * @author LCS
     */
    public static Workbook getWorkbook(InputStream is) throws IOException, InvalidFormatException
    {

        Workbook wb = WorkbookFactory.create(is);

        return wb;
    }


    /**
     * 打开指定execl
     *
     * @param filepath
     * @return
     * @throws BaseException
     * @throws FileNotFoundException
     * @throws IOException
     * @throws InvalidFormatException
     */
    public static Workbook openWorkBook(String filepath) throws
            BaseException,
            FileNotFoundException,
            IOException,
            InvalidFormatException
    {
        File file = new File(filepath);

        if (!file.exists())
        {
            throw new BaseException("找不到指定文件!");
        }

        Workbook wb = WorkbookFactory.create(file);

        return wb;
    }

    /**
     * 打开指定execl文件的指定工作簿
     *
     * @param filepath
     * @param sheetNum
     * @return
     * @throws BaseException
     * @throws FileNotFoundException
     * @throws IOException
     * @throws InvalidFormatException
     */
    public static Workbook writeSheet(String filepath, int sheetNum, int rowIndex, String[][] cellArr,
            List<Map<String, Object>> dataList) throws
            BaseException,
            FileNotFoundException,
            IOException,
            InvalidFormatException
    {
        Workbook wb = openWorkBook(filepath);

        if (wb instanceof XSSFWorkbook)
        {
            //读取指定工作簿
            XSSFSheet sheet = (XSSFSheet) wb.getSheetAt(sheetNum);
            writeXSSFSheet(sheet, rowIndex, cellArr, dataList);

        }
        else if (wb instanceof HSSFWorkbook)
        {
            //读取指定工作簿
            HSSFSheet sheet = (HSSFSheet) wb.getSheetAt(sheetNum);
            writeHSSFSheet(sheet, rowIndex, cellArr, dataList);
        }

        return wb;
    }

    /**
     * 将数据写入指定路径的execl的指定工作簿
     *
     * @param wb
     * @param sheetNum 工作簿下标
     * @param rowIndex 第几行开始写入的下标
     * @param cellArr  {{'列编号','对应集合中字段','数据类型',"格式化","水平向左向右","border粗细级别(borderThin,..)","是否换行(1是0否)","","","","",""},{.....},.......}
     *                 例如String[][] cellArr = {{"0","name","string","","left","","","","","",""},{"1","age","number","0.00","right","","","","","",""},{"2","sore","number","0.00%","right","","","","","",""}}
     * @param dataList 数据集合
     * @return
     * @throws BaseException
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static void writeSheet(Workbook wb, int sheetNum, int rowIndex, String[][] cellArr,
            List<Map<String, Object>> dataList) throws BaseException, FileNotFoundException, IOException
    {

        if (wb instanceof XSSFWorkbook)
        {
            //读取指定工作簿
            XSSFSheet sheet = (XSSFSheet) wb.getSheetAt(sheetNum);
            writeXSSFSheet(sheet, rowIndex, cellArr, dataList);

        }
        else if (wb instanceof HSSFWorkbook)
        {
            //读取指定工作簿
            HSSFSheet sheet = (HSSFSheet) wb.getSheetAt(sheetNum);
            writeHSSFSheet(sheet, rowIndex, cellArr, dataList);
        }
    }

    /**
     * @param sheet
     * @param rowIndex
     * @param dataList
     * @throws BaseException
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static void writeXSSFSheet(XSSFSheet sheet, int rowIndex, String[][] cellArr,
            List<Map<String, Object>> dataList)
    {
        XSSFRow dataRow;
        XSSFCell cell;
        int col, len;
        String v;
        if (!ObjectUtils.isEmpty(dataList) && !ObjectUtils.isEmpty(cellArr))
        {
            Map<String, XSSFCellStyle> styleMap = getXSSFCellStyle(sheet, cellArr);
            XSSFCellStyle style;

            for (Map<String, Object> map : dataList)
            {
                dataRow = sheet.createRow(rowIndex++);
                for (col = 0, len = cellArr.length; col < len; col++)
                {
                    v = String.valueOf(map.get(cellArr[col][1]));
                    cell = dataRow.createCell(Integer.valueOf(cellArr[col][0]));

                    style = styleMap.get(cellArr[col][0]);
                    if (!ObjectUtils.isEmpty(style))
                        cell.setCellStyle(style);

                    setCellValue(cell, v, cellArr[col][2]);
                }
            }
        }
    }

    /**
     * @param sheet
     * @param rowIndex
     * @param dataList
     * @throws BaseException
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static void writeHSSFSheet(HSSFSheet sheet, int rowIndex, String[][] cellArr,
            List<Map<String, Object>> dataList)
    {
        HSSFRow dataRow;
        HSSFCell cell;
        int col, len;
        String v;
        if (!ObjectUtils.isEmpty(dataList) && !ObjectUtils.isEmpty(cellArr))
        {
            Map<String, HSSFCellStyle> styleMap = getHSSFCellStyle(sheet, cellArr);
            HSSFCellStyle style;
            for (Map<String, Object> map : dataList)
            {
                dataRow = sheet.createRow(rowIndex++);
                for (col = 0, len = cellArr.length; col < len; col++)
                {
                    v = String.valueOf(map.get(cellArr[col][1]));
                    cell = dataRow.createCell(Integer.valueOf(cellArr[col][0]));
                    style = styleMap.get(cellArr[col][0]);
                    if (!ObjectUtils.isEmpty(style))
                        cell.setCellStyle(style);

                    setCellValue(cell, v, cellArr[col][2]);
                    //cell.setCellValue(100);
                }
            }
        }
    }

    /**
     * @param cell
     * @param v
     * @param type
     */
    private static void setCellValue(HSSFCell cell, String v, String type)
    {

        if ("date".equalsIgnoreCase(type))
        {
            cell.setCellValue(com.wisdom.acm.szxm.common.DateUtil
                    .formatDate(v, com.wisdom.acm.szxm.common.DateUtil.DATE_DEFAULT_FORMAT));
        }
        else if ("number".equalsIgnoreCase(type))
        {
            cell.setCellValue(Double.valueOf(v));
        }
        else
        {
            cell.setCellValue(v);
        }
    }

    /**
     * @param cell
     * @param v
     * @param type
     */
    private static void setCellValue(XSSFCell cell, String v, String type)
    {

        if ("date".equalsIgnoreCase(type))
        {
            cell.setCellValue(com.wisdom.acm.szxm.common.DateUtil
                    .formatDate(v, com.wisdom.acm.szxm.common.DateUtil.DATE_DEFAULT_FORMAT));
        }
        else if ("number".equalsIgnoreCase(type))
        {
            cell.setCellValue(Double.valueOf(v));
        }
        else
        {
            cell.setCellValue(v);
        }
    }

    /**
     * 获取单元格类型
     *
     * @param cellarr
     * @return
     */
    private static Map<String, HSSFCellStyle> getHSSFCellStyle(HSSFSheet sheet, String[][] cellarr)
    {

        Map<String, HSSFCellStyle> hcsMap = new HashMap<String, HSSFCellStyle>();

        if (!ObjectUtils.isEmpty(cellarr))
        {
            HSSFWorkbook wb = sheet.getWorkbook();
            HSSFCellStyle style;
            HSSFDataFormat format = wb.createDataFormat();

            for (String[] carr : cellarr)
            {
                if (!ObjectUtils.isEmpty(carr))
                {
                    style = wb.createCellStyle();

                    style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直

                    // 单元格格式
                    if ("yyyy/MM/dd".equals(carr[3]) || "yyyy-MM-dd".equals(carr[3]) || "yyyy年MM月dd日".equals(carr[3])
                            || "¥#,##0".equals(carr[3]))
                    {
                        style.setDataFormat(format.getFormat(carr[3]));
                    }
                    else if ("0.00%".equals(carr[3]) || "0.00".equals(carr[3]))
                    {
                        style.setDataFormat(HSSFDataFormat.getBuiltinFormat(carr[3]));
                    }
                    else
                    {
                        style.setDataFormat(format.getFormat("@"));
                    }

                    // 水平
                    if ("center".equals(carr[4]))
                    {
                        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);//
                    }
                    else if ("right".equals(carr[4]))
                    {
                        style.setAlignment(HSSFCellStyle.ALIGN_RIGHT);//
                    }
                    else
                    {
                        style.setAlignment(HSSFCellStyle.ALIGN_LEFT);//
                    }

                    if ("borderThin".equals(carr[5]))
                    {//是否加粗 borderThin
                        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
                        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
                        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
                    }

                    if ("1".equals(carr[6]))
                    {//是否换行
                        style.setWrapText(true);// 指定当单元格内容显示不下时自动换行
                    }
                    else
                    {
                        style.setWrapText(false);// 指定当单元格内容显示不下时自动换行
                    }
                    hcsMap.put(carr[0], style);
                    style = null;
                }
            }
        }
        return hcsMap;
    }

    /**
     * 获取单元格类型
     *
     * @param cellarr
     * @return
     */
    private static Map<String, XSSFCellStyle> getXSSFCellStyle(XSSFSheet sheet, String[][] cellarr)
    {

        Map<String, XSSFCellStyle> hcsMap = new HashMap<String, XSSFCellStyle>();

        if (!ObjectUtils.isEmpty(cellarr))
        {
            XSSFWorkbook wb = sheet.getWorkbook();
            XSSFCellStyle style;
            XSSFDataFormat format = wb.createDataFormat();
            for (String[] carr : cellarr)
            {
                if (!ObjectUtils.isEmpty(carr))
                {
                    style = wb.createCellStyle();
                    style = (XSSFCellStyle) style.clone();
                    style.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);// 垂直

                    // 单元格格式
                    if ("yyyy/MM/dd".equals(carr[3]) || "yyyy-MM-dd".equals(carr[3]) || "yyyy年MM月dd日".equals(carr[3])
                            || "¥#,##0".equals(carr[3]))
                    {
                        style.setDataFormat(format.getFormat(carr[3]));

                    }
                    else if ("0.00%".equals(carr[3]) || "0.00".equals(carr[3]))
                    {
                        style.setDataFormat(HSSFDataFormat.getBuiltinFormat(carr[3]));
                    }
                    else
                    {
                        style.setDataFormat(format.getFormat("@"));
                    }

                    // 水平
                    if ("center".equals(carr[4]))
                    {
                        style.setAlignment(XSSFCellStyle.ALIGN_CENTER);//
                    }
                    else if ("right".equals(carr[4]))
                    {
                        style.setAlignment(XSSFCellStyle.ALIGN_RIGHT);//
                    }
                    else
                    {
                        style.setAlignment(XSSFCellStyle.ALIGN_LEFT);//
                    }

                    if ("borderThin".equals(carr[5]))
                    {//是否加粗 borderThin
                        style.setBorderLeft(XSSFCellStyle.BORDER_THIN);
                        style.setBorderRight(XSSFCellStyle.BORDER_THIN);
                        style.setBorderTop(XSSFCellStyle.BORDER_THIN);
                        style.setBorderBottom(XSSFCellStyle.BORDER_THIN);
                    }

                    if ("1".equals(carr[6]))
                    {//是否换行
                        style.setWrapText(true);// 指定当单元格内容显示不下时自动换行
                    }
                    else
                    {
                        style.setWrapText(false);// 指定当单元格内容显示不下时自动换行
                    }
                    hcsMap.put(carr[0], style);
                }
            }
        }
        return hcsMap;
    }

    public static void writeCell(Row row, int cellnum, String text, CellStyle cellStyle)
    {

        Cell cell = row.getCell(cellnum);
        if (cell == null)
            cell = row.createCell(cellnum);

        cell.setCellValue(text);
        cell.setCellStyle(cellStyle);

    }

    /**
     * 合并单元格
     *
     * @param sheetNum 工作薄下标
     * @return
     */
    public static void mergeCells(Workbook workbook, int sheetNum, CellRangeAddress callRangeAddress) {
        if (workbook instanceof XSSFWorkbook)
        {
            //读取指定工作簿
            XSSFSheet sheet = (XSSFSheet) workbook.getSheetAt(sheetNum);
            sheet.addMergedRegion(callRangeAddress);
        }
        else if (workbook instanceof HSSFWorkbook)
        {
            //读取指定工作簿
            HSSFSheet sheet = (HSSFSheet) workbook.getSheetAt(sheetNum);
            sheet.addMergedRegion(callRangeAddress);
        }
    }

}
