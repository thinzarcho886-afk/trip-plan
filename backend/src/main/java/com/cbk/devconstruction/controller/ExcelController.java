package com.cbk.devconstruction.controller;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cbk.devconstruction.dto.BatchJobDTO;
import com.cbk.devconstruction.enums.ClassType;
import com.cbk.devconstruction.service.ExcelService;
import com.cbk.devconstruction.utils.CommonUtil;
import com.cbk.devconstruction.utils.ExcelUtils;

/**
 * 
 * @author sansintkyaw
 *
 */
@RestController
@RequestMapping(value = "/api/auth")
public class ExcelController {

	@Autowired
	ExcelService excelService;

	@GetMapping("/excel-export")
	public ResponseEntity<?> excelExport(@RequestParam Map<String, String> params) throws Exception {

		// validate class type
		String type = params.get("classType");
		Object[] typeError = excelService.validateClassType(type);
		if (typeError[0] == Boolean.TRUE) {
			return new ResponseEntity<>(typeError[1], HttpStatus.BAD_REQUEST);
		}

		SXSSFWorkbook workbook = excelService.excelExport(params);

		byte[] bytes = ExcelUtils.writeToByteArray(workbook);

		String excelFileName = "JUDO_" + type + "_" + Instant.now().toEpochMilli() + ".xlsx";

		HttpHeaders headers = ExcelUtils.createHttpHeaders(bytes, excelFileName);

		return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
	}

	@GetMapping("/excel-batch-export")
	public ResponseEntity<?> excelBatchExport(@RequestParam Map<String, String> params) throws Exception {

		// validate class type
		String type = params.get("classType");
		Object[] typeError = excelService.validateClassType(type);
		if (typeError[0] == Boolean.TRUE) {
			return new ResponseEntity<>(typeError[1], HttpStatus.BAD_REQUEST);
		}

		excelService.excelBatchExport(params);

		return new ResponseEntity<>(CommonUtil.responseSuccessMessage("Export Started."), HttpStatus.OK);
	}

	@GetMapping("/export-template")
	public ResponseEntity<?> exportTemplate(@RequestParam Map<String, String> params) throws Exception {

		// validate class type
		String type = params.get("classType");
		Object[] typeError = excelService.validateClassType(type);
		if (typeError[0] == Boolean.TRUE) {
			return new ResponseEntity<>(typeError[1], HttpStatus.BAD_REQUEST);
		}

		SXSSFWorkbook workbook = excelService.exportTemplate(params);

		byte[] bytes = ExcelUtils.writeToByteArray(workbook);

		String excelFileName = "JUDO_" + type + "_TEMPLATE_" + Instant.now().toEpochMilli() + ".xlsx";

		HttpHeaders headers = ExcelUtils.createHttpHeaders(bytes, excelFileName);

		return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
	}

	@PostMapping("/excel-import")
	public ResponseEntity<?> excelImport(@RequestParam("file") MultipartFile file,
			@RequestParam("classType") String classType, @RequestParam Map<String, String> params) throws Exception {

		ClassType type = ClassType.valueOf(classType);

		Object[] response = excelService.excelImport(file, type, params);

		// check error
		if (response[0] == Boolean.TRUE) {
			Map<String, Object> errorResponse = new HashMap<>();
			errorResponse.put("error", true);
			errorResponse.put("msgList", response[1]);
			return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<>(CommonUtil.responseSuccessMessage("Imported"), HttpStatus.CREATED);
	}

	/**
	 * Get all jobs for current user
	 */
	@GetMapping("/processing-jobs")
	public ResponseEntity<?> getProcessingJobs() {

		List<BatchJobDTO> batchJobList = excelService.getProcessingJobs();

		return new ResponseEntity<>(batchJobList, HttpStatus.OK);
	}

	/**
	 * To remove failed job from user job list
	 */
	@PutMapping("/remove-job/{id}")
	public ResponseEntity<?> removeFailedJob(@PathVariable("id") Long id) {

		excelService.finishJob(id, false);

		return new ResponseEntity<>(CommonUtil.responseSuccessMessage("Job removed."), HttpStatus.OK);
	}

	@GetMapping("/download-excel")
	public ResponseEntity<?> downloadExcel(@Param("id") Long id) throws Exception {

		Resource resource = excelService.downloadExcel(id);

		String contentType = "application/octet-stream";
		String fileName = "attachment; filename=\"" + resource.getFilename() + "\"";

		return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
				.header(HttpHeaders.CONTENT_DISPOSITION, fileName)
				.header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION).body(resource);
	}

	@PutMapping("/remove-excel/{id}")
	public ResponseEntity<?> deleteExcelFile(@PathVariable("id") Long id) {
		excelService.finishJob(id, true);
		return new ResponseEntity<>(CommonUtil.responseSuccessMessage("File removed."), HttpStatus.OK);
	}

}