package com.wisdom.base.common.util;

import com.wisdom.base.common.exception.BaseException;
import com.wisdom.base.common.util.calc.calendar.Tools;
import com.wisdom.base.common.vo.plan.project.ProjectInfoVo;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.*;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

public class ExcelUtil {

    /**
     * 获取工作簿
     * @param wb
     * @param sheetNum
     * @return
     * @throws IOException
     */
    public static Sheet getSheet(Workbook wb,int sheetNum) throws IOException
    {
        return wb.getSheetAt(sheetNum);
    }

	/**
	 * 获取指定工作表
	 *
	 * @param file  文件
	 * @param cells 列
	 * @return 表格数据
	 * @throws Exception 文件异常
	 */
	public static List<Map<String, Object>> getSheetValueMap(final File file, final ExcelCell[] cells) throws Exception {
		return getSheetValueMap(file, cells, 0);
	}

	/**
	 * 获取指定工作表
	 *
	 * @param file          文件
	 * @param cells         列
	 * @param rowStartIndex 取值开始行
	 * @return 表格数据
	 * @throws Exception 文件异常
	 */
	public static List<Map<String, Object>> getSheetValueMap(final File file, final ExcelCell[] cells, final int rowStartIndex) throws Exception {
		Workbook workbook = getWorkbook(file);
		Sheet sheet = workbook.getSheetAt(0);
		return getSheetValueMap(sheet, cells, rowStartIndex, -1);
	}

	/**
	 * 获取指定工作表
	 *
	 * @param is            文件
	 * @param cells         列名称
	 * @param rowStartIndex 取值开始行
	 * @return 表格数据
	 * @throws Exception 文件异常
	 */
	public static List<Map<String, Object>> getSheetValueMap(final InputStream is, final ExcelCell[] cells, final int rowStartIndex) throws Exception {
		Workbook workbook = getWorkbook(is);
		Sheet sheet = workbook.getSheetAt(0);
		return getSheetValueMap(sheet, cells, rowStartIndex, -1);
	}

	/**
	 * 获取的工作表
	 *
	 * @param sheet         工作表
	 * @param cells         列名称
	 * @param rowStartIndex 取值开始行
	 * @param rowEndIndex   取值结束行
	 * @return 表格数据
	 */
	public static List<Map<String, Object>> getSheetValueMap(final Sheet sheet, final ExcelCell[] cells, final int rowStartIndex, final Integer rowEndIndex) {
		List<Map<String, Object>> valueList = new ArrayList<>();
		Map<String, Object> valueMap;
		if (!Tools.isEmpty(cells)) {

			int sheetSize = rowEndIndex < 0 || rowEndIndex > sheet.getPhysicalNumberOfRows() ? sheet.getPhysicalNumberOfRows() : rowEndIndex;

			for (int j = rowStartIndex; j < sheetSize; j++) {

				Row row = sheet.getRow(j); // 获取每一行的数据

				if (row == null) {
					continue;
				}
				int lastCellNum = row.getLastCellNum();
				valueMap = new HashMap<>();
				boolean emptyRow = true; // 判断空行, 只要有一列值不为空就不是空行

				for (int c = 0; c < lastCellNum; c++) {

					Cell cell = row.getCell((short) c);
					if (!Tools.isEmpty(cell) && cells.length > c) {

						Object value = getCellValue(cell, cells[c]);
						valueMap.put(cells[c].getName(), value);

						if(!Tools.isEmpty(value)){
							emptyRow = false;
						}
					}
				}
				if (!emptyRow) {
                    valueMap.put("rowIndex",j);
					valueList.add(valueMap); // 集中保存
				}
			}
		}
		return valueList;
	}

    /**
     * 获取的工作表
     *
     * @param sheet         工作表
     * @param cellIndex     列结束行
     * @param rowStartIndex 取值开始行
     * @param rowEndIndex   取值结束行
     * @return 表格数据
     */
    public static List<Map<String, Object>> getSheetValueMap(final Sheet sheet, final int cellIndex, final int rowStartIndex, final Integer rowEndIndex) {

        char[] strs = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
        ExcelCell[] cells = new ExcelCell[cellIndex+1];
        if(cellIndex > 0){
            for(int i = 0; i <= cellIndex; i++){
                cells[i] = new ExcelCell(""+strs[i]);
            }
        }

        return getSheetValueMap(sheet,cells,rowStartIndex,rowEndIndex);
    }


	/**
	 * 获取指定的工作表
	 *
	 * @param file 文件
	 * @return 表格数据
	 * @throws Exception 文件异常
	 */
	public static List<Object[]> getSheetValues(final File file) throws Exception {
		return getSheetValues(file, 0);
	}

	/**
	 * 获取指定的工作表
	 *
	 * @param file          文件
	 * @param rowStartIndex 取值开始行
	 * @return 表格数据
	 * @throws Exception 文件异常
	 */
	public static List<Object[]> getSheetValues(final File file, final int rowStartIndex) throws Exception {
		Workbook workbook = getWorkbook(file);
		Sheet sheet = workbook.getSheetAt(0);
		return getSheetValues(sheet, rowStartIndex, -1);
	}

	/**
	 * 获取指定的工作表
	 *
	 * @param is            文件
	 * @param rowStartIndex 取值开始行
	 * @return 表格数据
	 * @throws Exception 文件异常
	 */
	public static List<Object[]> getSheetValues(final InputStream is, final int rowStartIndex) throws Exception {
		Workbook workbook = getWorkbook(is);
		Sheet sheet = workbook.getSheetAt(0);
		return getSheetValues(sheet, rowStartIndex, -1);
	}

	/**
	 * 获取工作表的数据
	 *
	 * @param sheet         工作表
	 * @param rowStartIndex 取值开始行
	 * @param rowEndIndex   取值结束行
	 * @return 表格数据
	 */
	public static List<Object[]> getSheetValues(final Sheet sheet, final int rowStartIndex, final Integer rowEndIndex) {
		List<Object[]> valueList = new ArrayList<>();
		int sheetSize = rowEndIndex < 0 || rowEndIndex > sheet.getPhysicalNumberOfRows() ? sheet.getPhysicalNumberOfRows() : rowEndIndex;
		for (int j = rowStartIndex; j < sheetSize; j++) {
			Row row = sheet.getRow(j);
			int cells = row.getLastCellNum();
			Object[] values = new Object[cells];
			boolean emptyRow = true; // 判断空行, 只要有一列值不为空就不是空行
			for (int c = 0; c < cells; c++) {
				Cell cell = row.getCell((short) c);
				if (!Tools.isEmpty(cell)) {
					values[c] = getCellValue(cell, null);
					if (!Tools.isEmpty(values[c])) {
						emptyRow = false; //只要有一列值不为空就不是空行
					}
				}
			}
			if (!emptyRow) {
				valueList.add(values);
			}
		}
		return valueList;
	}

	/**
	 * 获取指定的工作表
	 *
	 * @param file   文件
	 * @param cells  列配置
	 * @param entity 实体
	 * @param <T>    实体
	 * @return 实体
	 * @throws Exception 文件异常
	 */
	public static <T> List<T> getSheetValues(final File file, final ExcelCell[] cells, final Class<T> entity) throws Exception {
		return getSheetValues(file, cells, 0, entity);
	}

	/**
	 * 获取指定的工作表
	 *
	 * @param file          文件
	 * @param cells         列
	 * @param rowStartIndex 取值开始行
	 * @param entity        实体
	 * @param <T>           实体
	 * @return 实体
	 * @throws Exception 文件异常
	 */
	public static <T> List<T> getSheetValues(final File file, final ExcelCell[] cells, final int rowStartIndex, final Class<T> entity) throws Exception {
		Workbook workbook = getWorkbook(file);
		Sheet sheet = workbook.getSheetAt(0);
		return getSheetValues(sheet, cells, rowStartIndex, -1, entity);
	}

	/**
	 * 获取指定的工作表
	 *
	 * @param is            文件
	 * @param cells         列配置
	 * @param rowStartIndex 取值开始行
	 * @param entity        实体
	 * @param <T>           实体
	 * @return 实体
	 * @throws Exception 文件异常
	 */
	public static <T> List<T> getSheetValues(final InputStream is, final ExcelCell[] cells, final int rowStartIndex, final Class<T> entity) throws Exception {
		Workbook workbook = getWorkbook(is);
		Sheet sheet = workbook.getSheetAt(0);
		return getSheetValues(sheet, cells, rowStartIndex, -1, entity);
	}

	/**
	 * 获取工作表的数据
	 *
	 * @param sheet         工作表\
	 * @param cells         列名
	 * @param rowStartIndex 取值开始行
	 * @param rowEndIndex   取值结束行
	 * @return 表格数据
	 */
	public static <T> List<T> getSheetValues(final Sheet sheet, final ExcelCell[] cells, final int rowStartIndex, final Integer rowEndIndex, final Class<T> entity) {
		List<T> valueList;
		try {
			List<Object[]> values = getSheetValues(sheet, rowStartIndex, rowEndIndex);
			valueList = getValues(cells, values, entity);
		} catch (Exception e) {
			throw new BaseException(e.getMessage());
		}
		return valueList;
	}

	/**
	 * 赋值
	 *
	 * @param fields 列名
	 * @param values 取值
	 * @param entity 对象
	 * @param <T>    对象
	 * @return 对象
	 * @throws Exception 错误信息
	 */
	private static <T> List<T> getValues(final ExcelCell[] fields, final List<Object[]> values, final Class<T> entity) throws Exception {
		Map<String, Method> methodMap = new HashMap<>();
		Map<String, Class> parameterTypeMap = new HashMap<>();
		Method[] methods = entity.getMethods();
		for (Method method : methods) {
			if (method.getName().indexOf("set") == 0 && method.getParameterTypes().length == 1) {
				method.setAccessible(true);
				methodMap.put(method.getName().substring(3).toUpperCase(), method);
				parameterTypeMap.put(method.getName().substring(3).toUpperCase(), method.getParameterTypes()[0]);
			}
		}
		return getValues(methodMap, parameterTypeMap, fields, values, entity);
	}

	/**
	 * 赋值
	 *
	 * @param methodMap        方法
	 * @param parameterTypeMap 类型
	 * @param fields           字段
	 * @param values           值
	 * @param entity           对象
	 * @param <T>              对象
	 * @return 对象
	 * @throws Exception 文件异常
	 */
	private static <T> List<T> getValues(final Map<String, Method> methodMap, final Map<String, Class> parameterTypeMap, final ExcelCell[] fields,
										 final List<Object[]> values, final Class<T> entity) throws Exception {
		List<T> valueList = new ArrayList<>();
		if (!Tools.isEmpty(values)) {
			for (Object[] value : values) {
				T vo = entity.newInstance();
				for (int i = 0; i < fields.length; i++) {
					String field = fields[i].getName().toUpperCase();
					Method method = methodMap.get(field);
					setFieldValue(method, parameterTypeMap.get(field), value[i], vo);
				}
				valueList.add(vo);
			}
		}
		return valueList;
	}

	/**
	 * 给对象设值
	 *
	 * @param method        方法
	 * @param parameterType 参数类型
	 * @param value         值
	 * @param entity        对象
	 * @param <T>           对象
	 */
	private static <T> void setFieldValue(final Method method, final Class parameterType, final Object value, final T entity) throws Exception {
		if (!Tools.isEmpty(method) && !Tools.isEmpty(value)) {
			if (parameterType != value.getClass()) {
				Object _value = null;
				if (parameterType == Integer.class) {
					_value = Tools.parseInt(value);
				} else if (parameterType == Double.class) {
					_value = Tools.parseDouble(value);
				} else if (parameterType == Float.class) {
					_value = Tools.parseFloat(value);
				} else if (parameterType == Long.class) {
					_value = Tools.parseLong(value);
				} else if (parameterType == Boolean.class) {
					_value = Tools.parseBoolean(value);
				} else if (parameterType == Date.class) {
					_value = Tools.toDate(value);
				}
				method.invoke(entity, _value);
			} else {
				method.invoke(entity, value);
			}
		}
	}

	/**
	 * 获取工作簿
	 *
	 * @param file 文件
	 * @return 工作薄
	 * @throws Exception 文件异常
	 */
	public static Workbook getWorkbook(final File file) throws Exception {
		InputStream is = null;
		try {
			is = new FileInputStream(file);
			return getWorkbook(is);
		} finally {
			if (is != null) {
				is.close();
			}
		}
	}

	/**
	 * 获取工作簿
	 *
	 * @param url 文件流
	 * @return 工作薄
	 * @throws Exception 文件异常
	 */
	public static Workbook getWorkbookFromUrl(final String url) throws Exception {
		InputStream in = readUrl(url);
		return getWorkbook(in);
	}

	/**
	 * 获取工作簿
	 *
	 * @param is 文件流
	 * @return 工作薄
	 * @throws Exception 文件异常
	 */
	public static Workbook getWorkbook(final InputStream is) throws Exception {
		Workbook wb = WorkbookFactory.create(is);
		wb.getCreationHelper().createFormulaEvaluator().evaluateAll();// 预处理workbook中的公式
		return wb;
	}

	/**
	 * 得读取URL的
	 *
	 * @param url url
	 * @return 流
	 */
	private static InputStream readUrl(final String url) {
		try {
			URL u = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) u.openConnection();
			return conn.getInputStream();
		} catch (Exception e) {
			throw new BaseException(e.getMessage());
		}
	}

	/**
	 * 获取单元格数据
	 *
	 * @param cell 单元格
	 * @return 值
	 */
	public static Object getCellValue(final Cell cell, final ExcelCell excelCell) {
		Object obj = null;
		if (!Tools.isEmpty(cell)) {
			if (cell.getCellType() == CellType.NUMERIC) { // 数字
				if (DateUtil.isCellDateFormatted(cell)) { // 判断是否是日期
					obj = cell.getDateCellValue();
				} else {
					double value = cell.getNumericCellValue(); // 数字
					String strVal = FormatUtil.doubleFormat(value, "#");
					if (value == FormatUtil.parseDouble(strVal))
						obj = strVal;
					else
						obj = value;
				}
			} else if (cell.getCellType() == CellType.STRING) { // 字符
				obj = cell.getStringCellValue();
			}/* else if (cell.getCellType() == CellType.FORMULA) {  // 公式
				// object = cell.getRawValue();
				obj = null;
			}*//* else if (cell.getCellType() == CellType.BLANK) {  // 为空
				obj = null;
			}*/ else if (cell.getCellType() == CellType.BOOLEAN) {  // 布尔类型
				obj = cell.getBooleanCellValue();
			}/* else if (cell.getCellType() == CellType.ERROR) {  // 错误
				obj = null;
			}*/
			if (!Tools.isEmpty(excelCell) && !Tools.isEmpty(excelCell.getFormat())) {
				obj = excelCell.getFormat().getValue(cell, excelCell, obj);
			}
		}
		return obj;
	}

	/**
	 * 写入excel数据
	 *
	 * @param cells    列
	 * @param dataList 数据
	 * @return 工作薄
	 * @throws Exception 文件异常
	 */
	public static Workbook writeSheet(final ExcelCell[] cells, final List<?> dataList) throws Exception {
		return writeSheet(cells, dataList, true);
	}

	/**
	 * 写入excel数据
	 *
	 * @param cells      列
	 * @param dataList   数据
	 * @param createHead 是否创建头
	 * @return 工作薄
	 * @throws Exception 文件异常
	 */
	public static Workbook writeSheet(final ExcelCell[] cells, final List<?> dataList, final boolean createHead) throws Exception {
		return writeSheet(cells, dataList, createHead, 0);
	}

	/**
	 * 写入excel数据
	 *
	 * @param cells    列
	 * @param dataList 数据
	 * @return 工作薄
	 * @throws Exception 文件异常
	 */
	public static Workbook writeSheet(final ExcelCell[] cells, final List<?> dataList, final int rowStartIndex) throws Exception {
		return writeSheet(cells, dataList, false, rowStartIndex);
	}

	/**
	 * 写入excel数据
	 *
	 * @param cells         列信息
	 * @param dataList      数据
	 * @param rowStartIndex 是否创建头
	 * @return 工作薄
	 * @throws Exception 文件异常
	 */
	public static Workbook writeSheet(final ExcelCell[] cells, final List<?> dataList, final boolean createHead, final int rowStartIndex) throws Exception {
		return writeSheet(cells, dataList, createHead, rowStartIndex, null);
	}

	/**
	 * 写入excel数据
	 *
	 * @param cells         列信息
	 * @param dataList      数据
	 * @param rowStartIndex 开始索引
	 * @param format        格式化
	 * @return 工作薄
	 */
	public static Workbook writeSheet(final ExcelCell[] cells, final List<?> dataList, final boolean createHead, final int rowStartIndex, final ExcelFormat format) throws Exception {
		Workbook workbook = new SXSSFWorkbook(5000);
		writeSheet(workbook, cells, dataList, createHead, rowStartIndex, format);
		return workbook;
	}

	/**
	 * 写入excel数据
	 *
	 * @param file     文件
	 * @param cells    列信息
	 * @param dataList 数据集
	 * @return 工作薄
	 * @throws Exception 文件异常
	 */
	public static Workbook writeSheet(final File file, final ExcelCell[] cells, final List<?> dataList, final boolean createHead) throws Exception {
		return writeSheet(file, cells, dataList, createHead, 0);
	}

	/**
	 * 写入excel数据
	 *
	 * @param is       文件
	 * @param cells    列信息
	 * @param dataList 数据集
	 * @return 工作薄
	 * @throws Exception 文件异常
	 */
	public static Workbook writeSheet(final InputStream is, final ExcelCell[] cells, final List<?> dataList, final boolean createHead) throws Exception {
		return writeSheet(is, cells, dataList, createHead, 0);
	}

	/**
	 * 写入excel数据
	 *
	 * @param file          文件
	 * @param cells         列信息
	 * @param dataList      数据集
	 * @param rowStartIndex 开始行索引
	 * @return 工作薄
	 * @throws Exception 文件异常
	 */
	public static Workbook writeSheet(final File file, final ExcelCell[] cells, final List<?> dataList, final int rowStartIndex) throws Exception {
		return writeSheet(file, cells, dataList, false, rowStartIndex);
	}

	/**
	 * 写入excel数据
	 *
	 * @param file          文件
	 * @param cells         列信息
	 * @param dataList      数据集
	 * @param rowStartIndex 开始行索引
	 * @return 工作薄
	 * @throws Exception 文件异常
	 */
	public static Workbook writeSheet(final File file, final ExcelCell[] cells, final List<?> dataList, final boolean createHead, final int rowStartIndex) throws Exception {
		return writeSheet(file, cells, dataList, createHead, rowStartIndex, null);
	}

	/**
	 * 写入excel数据
	 *
	 * @param is            文件
	 * @param cells         列信息
	 * @param dataList      数据集
	 * @param rowStartIndex 开始行索引
	 * @return 工作薄
	 * @throws Exception 文件异常
	 */
	public static Workbook writeSheet(final InputStream is, final ExcelCell[] cells, final List<?> dataList, final int rowStartIndex) throws Exception {
		return writeSheet(is, cells, dataList, false, rowStartIndex);
	}

	/**
	 * 写入excel数据
	 *
	 * @param is            文件
	 * @param cells         列信息
	 * @param dataList      数据集
	 * @param rowStartIndex 开始行索引
	 * @return 工作薄
	 * @throws Exception 文件异常
	 */
	public static Workbook writeSheet(final InputStream is, final ExcelCell[] cells, final List<?> dataList, final boolean createHead, final int rowStartIndex) throws Exception {
		return writeSheet(is, cells, dataList, createHead, rowStartIndex, null);
	}

	/**
	 * 写入excel数据
	 *
	 * @param file          文件
	 * @param cells         列信息
	 * @param dataList      数据集
	 * @param rowStartIndex 开始行索引
	 * @param format        格式化
	 * @return 工作薄
	 * @throws Exception 文件异常
	 */
	public static Workbook writeSheet(final File file, final ExcelCell[] cells, final List<?> dataList, final boolean createHead, final int rowStartIndex,
									  final ExcelFormat format) throws Exception {
		Workbook workbook = getWorkbook(file);
		writeSheet(workbook, cells, dataList, createHead, rowStartIndex, format);
		return workbook;
	}

	/**
	 * 写入excel数据
	 *
	 * @param is            文件
	 * @param cells         列信息
	 * @param dataList      数据集
	 * @param rowStartIndex 开始行索引
	 * @param format        格式化
	 * @return 工作薄
	 * @throws Exception 文件异常
	 */
	public static Workbook writeSheet(final InputStream is, final ExcelCell[] cells, final List<?> dataList, final boolean createHead, final int rowStartIndex,
									  final ExcelFormat format) throws Exception {
		Workbook workbook = getWorkbook(is);
		writeSheet(workbook, cells, dataList, createHead, rowStartIndex, format);
		return workbook;
	}

	/**
	 * 写入excel数据
	 *
	 * @param workbook 工作薄
	 * @param cells    列信息
	 * @param dataList 数据集
	 * @return 工作薄
	 * @throws Exception 文件异常
	 */
	public static Workbook writeSheet(final Workbook workbook, final ExcelCell[] cells, final List<?> dataList) throws Exception {
		return writeSheet(workbook, cells, dataList, true);
	}

	/**
	 * 写入excel数据
	 *
	 * @param workbook   工作薄
	 * @param cells      列信息
	 * @param dataList   数据集
	 * @param createHead 是否创建表头
	 * @return 工作薄
	 * @throws Exception 文件异常
	 */
	public static Workbook writeSheet(final Workbook workbook, final ExcelCell[] cells, final List<?> dataList, final boolean createHead) throws Exception {
		return writeSheet(workbook, cells, dataList, createHead, 0);
	}

	/**
	 * 写入excel数据
	 *
	 * @param workbook      工作薄
	 * @param cells         列
	 * @param dataList      数据
	 * @param rowStartIndex 开始行
	 * @return 工作薄
	 * @throws Exception 文件异常
	 */
	public static Workbook writeSheet(final Workbook workbook, final ExcelCell[] cells, final List<?> dataList, final boolean createHead, final int rowStartIndex)
		throws Exception {
		return writeSheet(workbook, cells, dataList, createHead, rowStartIndex, null);
	}

	/**
	 * 写入excel数据
	 *
	 * @param workbook      工作薄
	 * @param cells         列
	 * @param dataList      数据
	 * @param rowStartIndex 开始行
	 * @param format        格式化
	 * @return 工作薄
	 */
	public static Workbook writeSheet(final Workbook workbook, final ExcelCell[] cells, final List<?> dataList, final boolean createHead, final int rowStartIndex,
									  final ExcelFormat format) throws Exception {
		Sheet sheet;
		try {
			sheet = workbook.getSheetAt(0);
		} catch (Exception e) {
			sheet = workbook.createSheet();
		}
		writeSheet(sheet, cells, dataList, createHead, rowStartIndex, format);
		return workbook;
	}

	/**
	 * 写入excel数据
	 *
	 * @param sheet         工作表
	 * @param cells         列
	 * @param dataList      数据
	 * @param rowStartIndex 开始行索引
	 * @param format        格式化
	 */
	public static void writeSheet(final Sheet sheet, final ExcelCell[] cells, final List<?> dataList, final boolean createHead, final int rowStartIndex,
								  final ExcelFormat format) throws Exception {
		if (sheet != null && !Tools.isEmpty(cells)) {
			int rowIndex = rowStartIndex;
			if (createHead) { // 创建表头
				Row row = sheet.createRow(rowIndex++);
				createHead(sheet, cells, row, format);
			}
			setSheet(sheet, cells);
			if (format != null) {
				format.formatSheet(sheet, cells);
			}
			writeSheet(sheet, cells, dataList, rowIndex, format);
		}
	}

	/**
	 * 行复制功能
	 *
	 * @param wb            工作薄
	 * @param sheet         页签
	 * @param fromRow       开始行
	 * @param toRow         结事长
	 * @param copyValueFlag 是否复制值
	 * @param cellList      列
	 */
	public static void copyRow(final Workbook wb, final Sheet sheet, final Row fromRow, final Row toRow, final boolean copyValueFlag,
							   final List<Cell> cellList) {
		boolean b = true;
		Cell cell = null;
		List<Integer> list = new ArrayList<>();
		for (int y = 0; y < cellList.size(); y++) {
			Cell tmpCell = cellList.get(y);
			Cell newCell;
			if (!Tools.isEmpty(tmpCell)) {
				String v = Tools.toString(getCellValue(tmpCell, null));
				if (!Tools.isEmpty(v)) {
					if (!b && !Tools.isEmpty(cell)) {
						CellRangeAddress cra = new CellRangeAddress(toRow.getRowNum(), toRow.getRowNum(), list.get(0), list.get(list.size() - 1));
						sheet.addMergedRegion(cra);
					}
					list = new ArrayList<>();
					b = true;
				} else {
					b = false;
				}
				cell = tmpCell;
				newCell = toRow.createCell(y);
				list.add(y);
				copyCell(wb, tmpCell, newCell, copyValueFlag);
				if (y == cellList.size() - 1) {
					if (!b && !Tools.isEmpty(cell)) {
						CellRangeAddress cra = new CellRangeAddress(toRow.getRowNum(), toRow.getRowNum(), list.get(0), list.get(list.size() - 1));
						sheet.addMergedRegion(cra);
					}
				}
			}
		}
	}

	/**
	 * 行复制功能
	 *
	 * @param wb            工作书签
	 * @param sheet         书签
	 * @param fromRow       开始行
	 * @param rows          总数
	 * @param copyValueFlag 是否复制
	 */
	public static void copyRow(final Workbook wb, final Sheet sheet, final Row fromRow, final int rows, final boolean copyValueFlag) {
		int rowNum = fromRow.getRowNum();
		if (rows > 0) {
			List<Cell> cellList = new ArrayList<>();
			for (int i = 0; i < fromRow.getLastCellNum(); i++) {
				Cell cell = fromRow.getCell(i);
				cellList.add(cell);
			}
			sheet.shiftRows(fromRow.getRowNum(), sheet.getLastRowNum(), rows);
			int i = 0;
			for (; i < rows; i++) {
				Row row = sheet.createRow(rowNum + i);
				copyRow(wb, sheet, fromRow, row, copyValueFlag, cellList);
			}
			Row r = sheet.getRow(rowNum + i);
			boolean b = true;
			Cell cell = null;
			List<Integer> list = new ArrayList<>();
			for (int y = 0; y < r.getLastCellNum(); y++) {
				Cell tmpCell = r.getCell(y);
				if (!Tools.isEmpty(tmpCell)) {
					String v = Tools.toString(getCellValue(tmpCell, null));
					if (!Tools.isEmpty(v)) {
						if (!b && !Tools.isEmpty(cell)) {
							CellRangeAddress cra = new CellRangeAddress(r.getRowNum(), r.getRowNum(), list.get(0), list.get(list.size() - 1));
							sheet.addMergedRegion(cra);
						}
						b = true;
						list = new ArrayList<>();
					} else {
						b = false;
					}
					cell = tmpCell;
					list.add(y);
				}
				if (y == r.getLastCellNum() - 1) {
					if (!b && !Tools.isEmpty(cell)) {
						CellRangeAddress cra = new CellRangeAddress(r.getRowNum(), r.getRowNum(), list.get(0), list.get(list.size() - 1));
						sheet.addMergedRegion(cra);
					}
				}
			}
		}
	}

	/**
	 * 复制单元格
	 *
	 * @param wb            工作薄
	 * @param srcCell       开始行
	 * @param distCell      目标行
	 * @param copyValueFlag true则连同cell的内容一起复制
	 */
	public static void copyCell(final Workbook wb, final Cell srcCell, final Cell distCell, final boolean copyValueFlag) {
		CellStyle fromStyle = wb.createCellStyle();
		copyCellStyle(srcCell.getCellStyle(), fromStyle);
		//distCell.setEncoding(srcCell.getEncoding());
		//样式
		distCell.setCellStyle(fromStyle);
		//评论
		if (srcCell.getCellComment() != null) {
			distCell.setCellComment(srcCell.getCellComment());
		}
		// 不同数据类型处理
		distCell.setCellType(srcCell.getCellType());
	}

	/**
	 * 复制一个单元格样式到目的单元格样式
	 *
	 * @param fromStyle 开始样式
	 * @param toStyle   到什么的样式
	 */
	public static void copyCellStyle(final CellStyle fromStyle, final CellStyle toStyle) {
		toStyle.setAlignment(fromStyle.getAlignment());
		//边框和边框颜色
		toStyle.setBorderBottom(fromStyle.getBorderBottom());
		toStyle.setBorderLeft(fromStyle.getBorderLeft());
		toStyle.setBorderRight(fromStyle.getBorderRight());
		toStyle.setBorderTop(fromStyle.getBorderTop());
		toStyle.setTopBorderColor(fromStyle.getTopBorderColor());
		toStyle.setBottomBorderColor(fromStyle.getBottomBorderColor());
		toStyle.setRightBorderColor(fromStyle.getRightBorderColor());
		toStyle.setLeftBorderColor(fromStyle.getLeftBorderColor());

		//背景和前景
		toStyle.setFillBackgroundColor(fromStyle.getFillBackgroundColor());
		toStyle.setFillForegroundColor(fromStyle.getFillForegroundColor());
		toStyle.setDataFormat(fromStyle.getDataFormat());
		toStyle.setFillPattern(fromStyle.getFillPattern());
		// toStyle.setFont(fromStyle.getFont(null));
		toStyle.setHidden(fromStyle.getHidden());
		toStyle.setIndention(fromStyle.getIndention());//首行缩进
		toStyle.setLocked(fromStyle.getLocked());
		toStyle.setRotation(fromStyle.getRotation());//旋转
		toStyle.setVerticalAlignment(fromStyle.getVerticalAlignment());
		toStyle.setWrapText(fromStyle.getWrapText());
	}


	/**
	 * 设置头信息
	 *
	 * @param sheet 页签
	 * @param cells 列信息
	 */
	private static void setSheet(final Sheet sheet, final ExcelCell[] cells) {
		for (int i = 0; i < cells.length; i++) {
			ExcelCell cell = cells[i];
			if (cell.getWidth() > 0) {
				sheet.setColumnWidth(i, cell.getWidth() * 256);
			}
			Workbook workbook = sheet.getWorkbook();
			CellStyle style = workbook.createCellStyle();
			style.setAlignment(cell.getAlign()); // 居中
			style.setVerticalAlignment(cell.getVertical()); // 垂直排列
			sheet.setDefaultColumnStyle(i, style);
		}
	}

	/**
	 * 设置头信息
	 *
	 * @param sheet  页签
	 * @param cells  列信息
	 * @param row    行
	 * @param format 格化式
	 */
	private static void createHead(final Sheet sheet, final ExcelCell[] cells, final Row row, final ExcelFormat format) {
		for (int i = 0; i < cells.length; i++) {
			ExcelCell cell = cells[i];
			Cell ec = writeCell(row, i, cell, cell.getTitle(), format);
			Workbook workbook = sheet.getWorkbook();
			CellStyle style = workbook.createCellStyle();
			style.setAlignment(HorizontalAlignment.CENTER); // 居中
			style.setVerticalAlignment(VerticalAlignment.CENTER);// 垂直居中
			Font cellFont = workbook.createFont();
			cellFont.setBold(true); // 字体加粗
			style.setFont(cellFont);
			ec.setCellStyle(style);
		}
	}

	/**
	 * 写表格信息
	 *
	 * @param sheet         页签
	 * @param cells         列信息
	 * @param dataList      数据
	 * @param rowStartIndex 开始行索引
	 * @param format        格式化
	 */
	private static void writeSheet(final Sheet sheet, final ExcelCell[] cells, final List<?> dataList, final int rowStartIndex, final ExcelFormat format) throws Exception {
		int rowIndex = rowStartIndex;
		if (!Tools.isEmpty(dataList)) {
			Map<String, Method> methodMap = getGetMethodMap(dataList.get(0));
			for (Object dataRow : dataList) {
				Row row = sheet.createRow(rowIndex++);
				writeSheet(row, dataRow, cells, format, methodMap);
			}
		}
	}

	/**
	 * 写入单元格
	 *
	 * @param row     行
	 * @param dataRow 数据行
	 * @param cells   列配置
	 * @param format  格式化
	 */
	private static void writeSheet(final Row row, Object dataRow, final ExcelCell[] cells, final ExcelFormat format, final Map<String, Method> methodMap) throws Exception {
		Object value;
		if (dataRow != null) {
			if (dataRow instanceof Map) {
				Map<?, ?> map = (Map<?, ?>) dataRow;
				for (int i = 0; i < cells.length; i++) {
					value = map.get(cells[i].getName());
					writeCell(row, i, cells[i], value, format);
				}
			} else if (dataRow instanceof Iterable) {
				Iterable<?> objects = (Iterable<?>) dataRow;
				Iterator<?> data = objects.iterator();
				for (int i = 0; i < cells.length; i++) {
					value = data.hasNext() ? data.next() : null;
					writeCell(row, i, cells[i], value, format);
				}
			} else if (dataRow instanceof Object[]) {
				Object[] objects = (Object[]) dataRow;
				for (int i = 0; i < cells.length && i < objects.length; i++) {
					value = objects[i];
					writeCell(row, i, cells[i], value, format);
				}
			} else {
				for (int i = 0; i < cells.length; i++) {
					value = getGetMethodValue(methodMap.get(cells[i].getName().toUpperCase()), dataRow);
					writeCell(row, i, cells[i], value, format);
				}
			}
		}
	}

	/**
	 * 写入单元格
	 *
	 * @param row       行
	 * @param cellIndex 列索引
	 * @param excelCell 列
	 * @param value     值
	 * @param format    格式代
	 * @return 列
	 */
	private static Cell writeCell(final Row row, final int cellIndex, final ExcelCell excelCell, final Object value, final ExcelFormat format) {
		Cell cell = row.getCell(cellIndex);
		if (cell == null) {
			cell = row.createCell(cellIndex);
		}
		setCellvalue(cell, excelCell, value);
		if (excelCell.getFormat() != null) {
			excelCell.getFormat().formatCell(cell, excelCell, value);
		}
		return cell;
	}

	/**
	 * 设置单元格值
	 *
	 * @param cell      列
	 * @param excelCell 列
	 * @param value     值
	 */
	private static void setCellvalue(final Cell cell, final ExcelCell excelCell, final Object value) {
		cell.setCellValue(Tools.toString(value));
	}

	/**
	 * 得到get方法
	 *
	 * @param obj 对象
	 * @return 对象方法
	 */
	private static Map<String, Method> getGetMethodMap(final Object obj) {
		Map<String, Method> methodMap = new HashMap<>();
		Method[] methods = obj.getClass().getMethods();
		for (Method method : methods) {
			if (method.getName().indexOf("get") == 0) {
				method.setAccessible(true);
				methodMap.put(method.getName().substring(3).toUpperCase(), method);
			}
		}
		return methodMap;
	}

	/**
	 * 得到get方法
	 *
	 * @param method 方法
	 * @param object 对象
	 * @return 对象
	 */
	private static Object getGetMethodValue(final Method method, final Object object) throws Exception {
		Object value = null;
		if (!Tools.isEmpty(method)) {
			value = method.invoke(object);
		}
		return value;
	}

	/**
	 * 测试
	 * @param args 参数
	 */
	public static void main(String[] args) {
		ExcelCell[] cells = new ExcelCell[]{
			new ExcelCell("code", "常规"),
			new ExcelCell("totalDrtn", "数值"),
			new ExcelCell("totalDrtn", "货币"),
			new ExcelCell("totalDrtn", "会计专用"),
			new ExcelCell("planStartTime", "日期"),
			new ExcelCell("planStartTime", "时间"),
			new ExcelCell("totalDrtn", "百比分"),
			new ExcelCell("totalDrtn", "分数"),
			new ExcelCell("totalDrtn", "科学记数"),
			new ExcelCell("totalDrtn", "文本"),
			new ExcelCell("totalDrtn", "特殊"),
			new ExcelCell("planStartTime", "自定义"),
		};
		ProjectInfoVo vo = new ProjectInfoVo();
		vo.setCode("常规");
		vo.setTotalDrtn(1234.111111);
		vo.setPlanStartTime(new Date());
		List<ProjectInfoVo> vos = new ArrayList<>();
		vos.add(vo);
		File file = new File("C:\\Users\\91872\\Desktop\\123.xlsx");
		try {
			Workbook wb = ExcelUtil.writeSheet(cells, vos);
			FileOutputStream fos = new FileOutputStream(file);
			wb.write(fos);
			wb.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
