package com.cbk.devconstruction.service;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Service;

/**
 * To manage workbook object through out batch job because workbook doesn't
 * implements serializable to put in execution context
 * 
 * @author sansintkyaw
 * 
 */
@Service
public class WorkbookService {

	private SXSSFWorkbook workbook;

	/**
	 * Get Workbook from singleton service
	 */
	public SXSSFWorkbook getWorkbook() {
		if (workbook == null) {
			// Initialize the workbook
			workbook = new SXSSFWorkbook(100);
		}
		return workbook;
	}

	/**
	 * Update Workbook of service class to main state for next chuck
	 */
	public void setWorkbook(SXSSFWorkbook workbook) {
		this.workbook = workbook;
	}

}
