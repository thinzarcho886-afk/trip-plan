package com.cbk.devconstruction.dto;

import java.util.List;

import org.apache.poi.ss.usermodel.CellStyle;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 
 * @author sansintkyaw
 *
 */
@Getter
@Setter
@NoArgsConstructor
public class ExcelData {

	private ExcelData exportData;
	private ExcelData importData;

	private int sheetNo;

	// sheet name array for multiple sheets
	private String[] sheetNames;

	// sheets headers list for export
	private List<String[]> headersList;

	// start index to create row for each sheet
	private int[] startIndexes;

	// used in creating summary
	private String label;
	private Object value;
	private CellStyle style;

	public ExcelData(int sheetNo, String[] sheetNames, List<String[]> headersList, int[] startIndexes) {
		this.sheetNo = sheetNo;
		this.sheetNames = sheetNames;
		this.headersList = headersList;
		this.startIndexes = startIndexes;
	}

	public ExcelData(String label, Object value, CellStyle style) {
		this.label = label;
		this.value = value;
		this.style = style;
	}

}
