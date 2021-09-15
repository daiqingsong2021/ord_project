package com.wisdom.base.common.util;

import org.apache.poi.ss.usermodel.Cell;

public abstract class ExcelCellFormat {

	public ExcelCellFormat() {

	}

	public Object getValue(Cell cell, ExcelCell excelCell, Object value){
		return value;
	}

	public void formatCell(Cell cell, ExcelCell excelCell, Object value){
		//cell.setCellValue();
	}
}