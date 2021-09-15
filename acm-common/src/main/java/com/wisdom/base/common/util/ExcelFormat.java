package com.wisdom.base.common.util;

import org.apache.poi.ss.usermodel.Sheet;

public abstract class ExcelFormat {
	
	public ExcelFormat() {

	}
	
	public abstract void formatSheet(final Sheet sheet, final ExcelCell[] excelCells);
}