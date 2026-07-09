package com.cbk.devconstruction.utils;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.cbk.devconstruction.dto.ExcelData;

/**
 * 
 * @author sansintkyaw
 *
 */
@Component
public class ExcelUtils {

	private static MessageSource messageSource;

	@Autowired
	public ExcelUtils(MessageSource messageSource) {
		ExcelUtils.messageSource = messageSource;
	}

	/**
	 * Add frame with sheet and row headers to workbook
	 * 
	 * @param data
	 * @return
	 */
	public static SXSSFWorkbook createWorkbook(SXSSFWorkbook workbook, ExcelData data) {
		for (int i = 0; i < data.getSheetNo(); i++) {

			// create sheets
			Sheet sheet = workbook.createSheet(data.getSheetNames()[i]);

			// create cellStyle with the font
			CellStyle headerCellStyle = ExcelUtils.createHeaderCellStyle(workbook);

			// create array for header
			String[] headers = data.getHeadersList().get(i);

			// create row for header
			Row headerRow = sheet.createRow(data.getStartIndexes()[i]);

			// create cells for header
			for (int j = 0; j < headers.length; j++) {
				ExcelUtils.createCell(headerRow, j, headerCellStyle).setCellValue(headers[j]);
			}

		}

		return workbook;
	}

	/**
	 * Get localize message from code string array. separate with "{@code |}" if
	 * want to use two localization in one header
	 * 
	 * @param headers
	 * @param locale
	 * @return
	 */
	public static String[] getLocalizeHeaders(String[] headers, Locale locale) {
		if (locale == null)
			locale = LocaleContextHolder.getLocale();
		String[] localizeHeaders = new String[headers.length];
		for (int i = 0; i < headers.length; i++) {
			String header = headers[i];
			if (!StringUtils.isEmpty(header)) {
				String[] splitedHeader = header.split("\\|");
				String localizeHeader = messageSource.getMessage(splitedHeader[0], null, locale);
				for (int j = 1; j < splitedHeader.length; j++) {
					localizeHeader += " " + messageSource.getMessage(splitedHeader[j], null, locale);
				}
				localizeHeaders[i] = localizeHeader;
			} else
				localizeHeaders[i] = "";
		}
		return localizeHeaders;
	}

	/**
	 * 
	 * @param row
	 * @param index
	 * @param style {@code null} if style is not required
	 * @return
	 */
	public static Cell createCell(Row row, int index, CellStyle style) {
		Cell cell = row.createCell(index);
		if (!Objects.isNull(style)) {
			cell.setCellStyle(style);
		}
		return cell;
	}

	/**
	 * Get error message for excel import with cell reference
	 * 
	 * @param sheet
	 * @param row
	 * @param index
	 * @param error
	 * @return
	 */
	public static String getErrorMsg(Sheet sheet, Row row, int index, String error) {
		String cellreferenceStr = "";
		if (row != null) {
			CellReference cell = new CellReference(row.getCell(index));
			cellreferenceStr = " | " + cell.formatAsString();
		}
		String errorMsg = error + " [" + sheet.getSheetName() + cellreferenceStr + "].";
		return errorMsg;
	}

	/**
	 * Check if at least one of cells in a given cell range is not empty
	 * 
	 * @param row
	 * @param startIndex inclusive
	 * @param endIndex   inclusive
	 * @return {@code true} if at least one of the cells is not blank
	 */
	public static boolean checkNonEmptyCell(Row row, int startIndex, int endIndex) {
		boolean flag = false;
		for (int i = startIndex; i <= endIndex; i++) {
			if (row.getCell(i).getCellType() != Cell.CELL_TYPE_BLANK) {
				flag = true;
				break;
			}
		}
		return flag;
	}

	/**
	 * Resize workbook's columns
	 * 
	 * @param workbook
	 * @param lengths
	 */
	public static void resizeWorkbookColumn(Workbook workbook, List<Integer> lengths) {

		// get sheet list
		List<Sheet> sheets = new ArrayList<>();
		for (int i = 0; i < lengths.size(); i++) {
			sheets.add(workbook.getSheetAt(i));
		}

		// Resize all columns to fit the content size
		for (Sheet sheet : sheets) {
			for (int i = 0; i < lengths.get(sheets.indexOf(sheet)); i++) {
				sheet.autoSizeColumn(i);
			}
		}

	}

	/**
	 * Write excel workbook to byte array
	 * 
	 * @param workbook
	 * @return
	 * @throws IOException
	 */
	public static byte[] writeToByteArray(SXSSFWorkbook workbook) throws IOException {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		workbook.write(byteArrayOutputStream);
		workbook.dispose();
		byte[] bytes = byteArrayOutputStream.toByteArray();
		return bytes;
	}

	/**
	 * Write workbook to excel file from provided file path
	 * 
	 * @param workbook
	 * @param filePath
	 * @throws IOException
	 */
	public static void writeWorkbook(SXSSFWorkbook workbook, String filePath) throws IOException {
		FileOutputStream outputStream = new FileOutputStream(filePath);
		workbook.write(outputStream);
		workbook.dispose();
		outputStream.close();
	}

	/**
	 * Create http headers for excel response
	 * 
	 * @param bytes
	 * @param excelFileName
	 * @return
	 */
	public static HttpHeaders createHttpHeaders(byte[] bytes, String excelFileName) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheeet");
		headers.add(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
		headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + excelFileName);
		headers.setContentLength(bytes.length);
		return headers;
	}

	/**
	 * Create Summary Rows in format of Label - Value. leave blank in the label to
	 * skip row
	 * 
	 * @param sheet
	 * @param values
	 * @param rowNumHeader
	 * @return
	 */
	public static int createSummaryRows(Sheet sheet, List<ExcelData> values, int rowNumHeader,
			CellStyle headerCellStyle) {
		for (int i = 0; i < values.size(); i++) {
			ExcelData data = values.get(i);
			String label = data.getLabel();
			Object value = data.getValue();

			if (StringUtils.isEmpty(data.getLabel())) {
				rowNumHeader++;
				continue;
			}

			Row summaryRow = sheet.createRow(rowNumHeader++);
			ExcelUtils.createCell(summaryRow, 0, headerCellStyle).setCellValue(label);
			if (value instanceof String)
				ExcelUtils.createCell(summaryRow, 1, data.getStyle()).setCellValue((String) value);
			else if (value instanceof Double)
				ExcelUtils.createCell(summaryRow, 1, data.getStyle()).setCellValue((Double) value);
			else if (value instanceof Integer)
				ExcelUtils.createCell(summaryRow, 1, data.getStyle()).setCellValue((Integer) value);

		}
		return rowNumHeader;
	}

	/**
	 * Create cell comment
	 * 
	 * @param creationHelper
	 * @param drawing
	 * @param text
	 * @param anchorParam    array in the order of
	 *                       {@code firstColumnIndex, firstRowIndex, secondColumnIndex, secondRowIndex},
	 *                       index are 0 based
	 * @return
	 */
	public static Comment createComment(CreationHelper creationHelper, Drawing drawing, String text,
			int[] anchorParam) {
		ClientAnchor anchor = drawing.createAnchor(100, 100, 100, 100, anchorParam[0], anchorParam[1], anchorParam[2],
				anchorParam[3]);
		Comment comment = drawing.createCellComment(anchor);
		RichTextString richText = creationHelper.createRichTextString(text);
		comment.setString(richText);
		return comment;
	}

	/**
	 * Create cell style with red color, bold and fond height with 11 point
	 * 
	 * @param workbook
	 * @return
	 */
	public static CellStyle createHeaderCellStyle(SXSSFWorkbook workbook) {
		Font headerFont = workbook.createFont();
		headerFont.setBoldweight((short) 3);
		headerFont.setFontHeightInPoints((short) 11);
		headerFont.setColor(IndexedColors.RED.getIndex());

		CellStyle headerCellStyle = workbook.createCellStyle();
		headerCellStyle.setFont(headerFont);

		return headerCellStyle;
	}

	/**
	 * Create date cell style with provided date format and left alignment
	 * 
	 * @param workbook
	 * @param creationHelper
	 * @param dateFormat
	 * @return
	 */
	public static CellStyle createDateCellStyle(SXSSFWorkbook workbook, CreationHelper creationHelper,
			String dateFormat) {
		CellStyle dateCellStyle = workbook.createCellStyle();
		dateCellStyle.setDataFormat(creationHelper.createDataFormat().getFormat(dateFormat));
		dateCellStyle.setAlignment(CellStyle.ALIGN_LEFT);

		return dateCellStyle;
	}

	/**
	 * Create number format cell style for amount
	 * 
	 * @param workbook
	 * @param dataFormat
	 * @param numberFormat example- {@code #,###,##0.00}
	 * @return
	 */
	public static CellStyle createNumberCellStyle(SXSSFWorkbook workbook, DataFormat dataFormat, String numberFormat,
			boolean isBold) {
		CellStyle numberCellStyle = workbook.createCellStyle();
		numberCellStyle.setDataFormat(dataFormat.getFormat(numberFormat));

		if (isBold) {
			Font boldFont = workbook.createFont();
			boldFont.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
			numberCellStyle.setFont(boldFont);
		}

		return numberCellStyle;
	}

	/**
	 * Create cell style with bold
	 * 
	 * @param workbook
	 * @return
	 */
	public static CellStyle createBoldCellStyle(SXSSFWorkbook workbook) {
		Font boldFont = workbook.createFont();
		boldFont.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		boldFont.setFontHeightInPoints((short) 11);

		CellStyle boldCellStyle = workbook.createCellStyle();
		boldCellStyle.setFont(boldFont);

		return boldCellStyle;
	}

	/**
	 * Create cell style with provided color
	 * 
	 * @param workbook
	 * @param colorIndex can be found on {@link IndexedColors}
	 * @return
	 */
	public static CellStyle createColorCellStyle(SXSSFWorkbook workbook, short colorIndex) {
		CellStyle colorCellStyle = workbook.createCellStyle();
		colorCellStyle.setFillForegroundColor(colorIndex);
		colorCellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
		return colorCellStyle;
	}

}
