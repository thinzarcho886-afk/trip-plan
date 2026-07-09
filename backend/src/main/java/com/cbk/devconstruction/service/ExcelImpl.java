package com.cbk.devconstruction.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import com.cbk.devconstruction.dto.ExcelData;
import com.cbk.devconstruction.utils.ExcelUtils;

/**
 * Default implementation for excel import and export
 * 
 * @author User
 *
 * @param <D> dto class of register method
 * 
 */
public abstract class ExcelImpl<D> {

	/**
	 * get meta data for excel ,for example headers, sheet name, etc.
	 */
	protected abstract ExcelData getExcelData(Map<String, String> params);

	/**
	 * create rows for excel sheet
	 */
	protected abstract void createRows(SXSSFWorkbook workbook, List<Sheet> sheets, Map<String, String> params)
			throws Exception;

	/**
	 * abstract method to extract list of specified object class from sheet
	 * 
	 * @param rowIterator header skipped row iterator
	 */
	protected abstract List<D> getImportData(XSSFWorkbook workbook, Iterator<Row> rowIterator, List<String> errors,
			Map<String, String> params) throws Exception;

	/**
	 * abstract method to add additional validation for resulted dto list class from
	 * import
	 */
	protected abstract void additionalValidation(XSSFWorkbook workbook, List<D> dtoList, List<String> errors);

	/**
	 * to provide register method to add to entity
	 */
	protected abstract void importRegister(D dto) throws Exception;

	/**
	 * set other settings (eg. comment) to sheet header cells in import template
	 */
	protected abstract void setAdditionalSettingsForHeader(Sheet sheet, CreationHelper creationHelper, Row row);

	/**
	 * set additional rows before headers in export. For examples - Summary rows or
	 * filtered fields rows
	 */
	protected abstract void addAdditionalRowsBeforeHeader(SXSSFWorkbook workbook, Sheet sheet, int sheetNo,
			Map<String, String> params);

	/**
	 * set maximum column length to use in auto sizing column for each sheet
	 */
	protected abstract List<Integer> getMaximumColumnLength();

	/**
	 * Default implementation for excel export. Implementation is only for simple
	 * header-details excel template with multiple sheets. May need to override the
	 * entire method for complex excel template
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	SXSSFWorkbook excelExport(Map<String, String> params) throws Exception {
		SXSSFWorkbook workbook = new SXSSFWorkbook(100);

		ExcelData excelData = getExcelData(params);
		ExcelData exportData = excelData.getExportData();

		List<Sheet> sheets = new ArrayList<>();

		for (int i = 0; i < exportData.getSheetNo(); i++) {
			// create sheets
			Sheet sheet = workbook.createSheet(exportData.getSheetNames()[i]);

			// create cellStyle with the font
			CellStyle headerCellStyle = ExcelUtils.createHeaderCellStyle(workbook);

			// create array for header
			String[] headers = exportData.getHeadersList().get(i);
			String[] localizeHeaders = ExcelUtils.getLocalizeHeaders(headers, null);

			// set additional rows
			addAdditionalRowsBeforeHeader(workbook, sheet, i, params);

			int rowIndex = sheet.getPhysicalNumberOfRows();
			if (rowIndex > 0) {
				rowIndex += 1;
			}

			// create row for header
			Row headerRow = sheet.createRow(rowIndex);

			// create cells for header
			for (int j = 0; j < localizeHeaders.length; j++)
				ExcelUtils.createCell(headerRow, j, headerCellStyle).setCellValue(localizeHeaders[j]);

			sheets.add(sheet);
		}

		// create rows
		createRows(workbook, sheets, params);

		// Resize all columns to fit the content size
		List<Integer> columnList = getMaximumColumnLength();
		for (Sheet sheet : sheets) {
			for (int i = 0; i < columnList.get(sheets.indexOf(sheet)); i++) {
				sheet.autoSizeColumn(i);
			}
		}

		return workbook;
	}

	/**
	 * Default implementation for excel import template.
	 * 
	 * @param params
	 * @return
	 */
	SXSSFWorkbook exportTemplate(Map<String, String> params) {
		SXSSFWorkbook workbook = new SXSSFWorkbook();
		CreationHelper creationFactory = workbook.getCreationHelper();

		ExcelData excelData = getExcelData(params);
		ExcelData importData = excelData.getImportData();

		List<Sheet> sheets = new ArrayList<>();

		for (int i = 0; i < importData.getSheetNo(); i++) {
			// create sheets
			Sheet sheet = workbook.createSheet(importData.getSheetNames()[i]);

			// create cellStyle with the font
			CellStyle headerCellStyle = ExcelUtils.createHeaderCellStyle(workbook);

			// create array for header
			String[] headers = importData.getHeadersList().get(i);
			String[] localizeHeaders = ExcelUtils.getLocalizeHeaders(headers, null);

			// create row for header
			Row headerRow = sheet.createRow(0);

			// create cells for header
			for (int j = 0; j < localizeHeaders.length; j++)
				ExcelUtils.createCell(headerRow, j, headerCellStyle).setCellValue(localizeHeaders[j]);

			// headers cell additional settings
			setAdditionalSettingsForHeader(sheet, creationFactory, headerRow);

			sheets.add(sheet);
		}

		// Resize all columns to fit the content size
		for (Sheet sheet : sheets) {
			for (int i = 0; i < sheet.getRow(0).getLastCellNum(); i++) {
				sheet.autoSizeColumn(i);
			}
		}

		return workbook;
	}

	/**
	 * Default implementation for excel import.
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	Object[] excelImport(MultipartFile file, Map<String, String> params) throws Exception {
		List<String> errors = new ArrayList<>();

		InputStream f = file.getInputStream();

		// create workbook instance from input stream
		XSSFWorkbook workbook = new XSSFWorkbook(f);
		workbook.setMissingCellPolicy(Row.CREATE_NULL_AS_BLANK);

		// get first sheet
		Sheet sheet = workbook.getSheetAt(0);

		Iterator<Row> rowIterator = sheet.iterator();

		// Skip header row
		if (rowIterator.hasNext())
			rowIterator.next();

		// Process row by row and get list
		List<D> dtoList = getImportData(workbook, rowIterator, errors, params);

		// additional additional validation
		additionalValidation(workbook, dtoList, errors);

		if (errors.size() > 0) {
			Object[] response = { true, errors };
			return response;
		}

		// register
		for (D d : dtoList) {
			importRegister(d);
		}

		Object[] response = { false };
		return response;
	}

}
