package com.wisdom.base.common.util;

import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;

import java.io.Serializable;

/**
 * 列的设置
 * @author TZ <br>
 * @version 1.0.0 2017-12-7<br>
 * @see
 * @since JDK 1.5.0
 */
public class ExcelCell implements Serializable{

	private static final long serialVersionUID = 1L;

	public ExcelCell() {

	}

	public ExcelCell(String name) {
		this.name = name;
	}

	public ExcelCell(String name, String title) {
		this.name = name;
		this.title = title;
	}

	public ExcelCell(String name, String title, int width) {
		this.name = name;
		this.title = title;
		this.width = width;
	}

	public ExcelCell(String name, String title, int width, HorizontalAlignment align, VerticalAlignment vertical) {
		this.name = name;
		this.title = title;
		this.width = width;
		this.align = align;
		this.vertical = vertical;
	}

	public ExcelCell(String name, String title, int width, HorizontalAlignment align, VerticalAlignment vertical, ExcelCellFormat format) {
		this.name = name;
		this.title = title;
		this.width = width;
		this.align = align;
		this.vertical = vertical;
		this.format = format;
	}

	private String name; // 列名称
	private String title; // 列显示名称
	private int width; // 列宽度
	private HorizontalAlignment align = HorizontalAlignment.LEFT; // 列水平显示位置
	private VerticalAlignment vertical = VerticalAlignment.TOP; // 列垂直的显示位置
	private ExcelCellFormat format; //列格式化

	public String getName() {
		return name;
	}

	public void setName(final String newName) {
		name = newName;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(final String newTitle) {
		title = newTitle;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(final int newWidth) {
		width = newWidth;
	}

	public HorizontalAlignment getAlign() {
		return align;
	}

	public void setAlign(final HorizontalAlignment newAlign) {
		align = newAlign;
	}

	public VerticalAlignment getVertical() {
		return vertical;
	}

	public void setVertical(final VerticalAlignment newVertical) {
		vertical = newVertical;
	}

	public ExcelCellFormat getFormat() {
		return format;
	}

	public void setFormat(ExcelCellFormat format) {
		this.format = format;
	}
}