package com.cbk.devconstruction.service;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.cbk.devconstruction.dto.BatchJobDTO;
import com.cbk.devconstruction.entity.BatchJob;
import com.cbk.devconstruction.entity.User;
import com.cbk.devconstruction.enums.ClassType;
import com.cbk.devconstruction.enums.JobStatus;
import com.cbk.devconstruction.exception.InternalServerException;
import com.cbk.devconstruction.exception.ResourceNotFoundException;
import com.cbk.devconstruction.repository.BatchJobRepository;
import com.cbk.devconstruction.utils.CommonUtil;
import com.cbk.devconstruction.utils.NginxUtil;

@Service
public class ExcelService {

	@Autowired
	private ApplicationContext context;

	@Autowired
	JobLauncher asyncJobLauncher;

	@Autowired
	BatchJobRepository batchJobRepository;

	@Autowired
	UserService userService;

	/**
	 * Fail all waiting and in_progress jobs before previous application stopped
	 */
	@EventListener(ApplicationReadyEvent.class)
	@Transactional
	public void runAfterStartup() {
		batchJobRepository.setPreviousJobsAsFailed();
	}

	/**
	 * Update batch job status when job is finished
	 * 
	 * @param jobId
	 * @param status
	 */
	public void updateBatchJob(String jobId, JobStatus status, String errorMsg) {
		Optional<BatchJob> batchJob = batchJobRepository.findByJobId(jobId);
		if (batchJob.isPresent()) {
			BatchJob job = batchJob.get();
			job.setStatus(status);

			// truncate error message if failed
			if (status.equals(JobStatus.Failed)) {
				job.setErrorMessage(truncateMessage(errorMsg, 220));
			}

			batchJobRepository.save(job);
		}
	}

	private String truncateMessage(String message, int length) {
		String msg = null;
		if (!StringUtils.isEmpty(message)) {
			msg = message.length() > length ? message.substring(0, length) + "..." : message;
		}
		return msg;
	}

	public List<BatchJobDTO> getProcessingJobs() {
		String currentUser = CommonUtil.getUsernameFromAuthentication();
		User user = userService.getUserByName(currentUser);

		List<BatchJob> batchJobList = batchJobRepository.findByDownloadedAndCreatedUserIdOrderByCreatedDateDesc(false,
				user.getId());
		List<BatchJobDTO> batchJobDTOList = CommonUtil.getDTOList(batchJobList, BatchJobDTO::new);

		return batchJobDTOList;
	}

	@Transactional(rollbackFor = Exception.class)
	public void finishJob(Long id, boolean isFileDelete) {
		Optional<BatchJob> batchJobOpt = batchJobRepository.findById(id);
		if (batchJobOpt.isPresent()) {

			String currentUser = CommonUtil.getUsernameFromAuthentication();
			User user = userService.getUserByName(currentUser);

			BatchJob batchJob = batchJobOpt.get();

			if (user.getId().equals(batchJob.getCreatedUserId())) {
				batchJob.setDownloaded(true);

				if (isFileDelete)
					// delete file
					NginxUtil.deleteFile(NginxUtil.getExcelFilePath(batchJob.getFileName(), false));
			}
		}

	}

	public Resource downloadExcel(Long id) throws MalformedURLException {
		String currentUser = CommonUtil.getUsernameFromAuthentication();
		User user = userService.getUserByName(currentUser);

		BatchJob batchJob = CommonUtil.checkValidById(id, batchJobRepository);
		batchJobRepository.findById(id);

		if (!user.getId().equals(batchJob.getCreatedUserId())) {
			throw new AccessDeniedException("Access Denied");
		}

		String fileUri = NginxUtil.getExcelFilePath(batchJob.getFileName(), false);
		Path path = Paths.get(fileUri);

		if (path == null) {
			throw new ResourceNotFoundException("File not found.");
		}

		return new UrlResource(path.toUri());
	}

	/**
	 * Check if class type is present or is it valid
	 * 
	 * @param type
	 * @return
	 */
	public Object[] validateClassType(String type) {
		if (StringUtils.isEmpty(type)) {
			Object[] response = { true, "Class Type is required." };
			return response;
		} else {
			try {
				ClassType.valueOf(type);
			} catch (IllegalArgumentException e) {
				Object[] response = { true, "Invalid Class Type" };
				return response;
			}
		}

		// no error
		Object[] response = { false };
		return response;
	}

	public SXSSFWorkbook excelExport(Map<String, String> params) throws Exception {

		ClassType classType = ClassType.valueOf(params.get("classType"));
		String className = classType.getExportValue();

		Class<?> serviceClass = Class.forName(className);
		Method method = serviceClass.getSuperclass().getDeclaredMethod("excelExport", Map.class);

		SXSSFWorkbook workbook = null;
		try {
			workbook = (SXSSFWorkbook) method.invoke(context.getBean(serviceClass), params);
		} catch (InvocationTargetException e) {
			Throwable cause = e.getCause();
			if (cause != null) {
				cause.printStackTrace();
				throw new InternalServerException(cause.getLocalizedMessage());
			} else
				throw new InternalServerException(e.getLocalizedMessage());
		}

		return workbook;
	}

	public void excelBatchExport(Map<String, String> params) throws Exception {
		String currentUser = CommonUtil.getUsernameFromAuthentication();
		User user = userService.getUserByName(currentUser);

		// get file_path and job_id
		String type = params.get("classType");
		String excelFileName = "JUDO_" + type + "_" + Instant.now().toEpochMilli() + ".xlsx";
		String filePath = NginxUtil.getExcelFilePath(excelFileName, false);
		String jobId = type.toLowerCase() + "_" + UUID.randomUUID().toString().substring(0, 8) + "_"
				+ Instant.now().toEpochMilli();

		// create folder if there's no folder yet
		File folder = new File(filePath);
		folder.getParentFile().mkdirs();

		// save progress to entity
		BatchJob batchJob = new BatchJob();
		batchJob.setJobId(jobId);
		batchJob.setJobType(ClassType.valueOf(type));
		batchJob.setExport(true);
		batchJob.setFileName(excelFileName);
		batchJob.setStatus(JobStatus.Waiting);
		batchJob.setDownloaded(false);
		batchJob.setCreatedUserId(user.getId());
		batchJobRepository.save(batchJob);

		/**
		 * method name must be in the format of `lower case name of ClassType`_export
		 */
		// invoke export method of provided type
		try {
			Method method = this.getClass().getDeclaredMethod(type.toLowerCase() + "_export", Map.class, String.class,
					String.class);
			method.invoke(context.getBean(this.getClass()), params, filePath, jobId);
			method.setAccessible(true);

		} catch (InvocationTargetException e) {
			Throwable cause = e.getCause();
			if (cause != null) {
				cause.printStackTrace();

				// failed job when error occurs before starting the job
				failedJob(batchJob, cause.getLocalizedMessage());

				throw new InternalServerException(cause.getLocalizedMessage());

			} else {
				// failed job when error occurs before starting the job
				failedJob(batchJob, e.getLocalizedMessage());

				throw new InternalServerException(e.getLocalizedMessage());
			}
		} catch (Exception e) {
			// failed job when error occurs before starting the job
			failedJob(batchJob, e.getLocalizedMessage());

			throw new InternalServerException(e.getLocalizedMessage());
		}

	}

	private void failedJob(BatchJob batchJob, String errorMessage) {
		batchJob.setStatus(JobStatus.Failed);
		batchJob.setErrorMessage(truncateMessage(errorMessage, 220));
		batchJobRepository.save(batchJob);
	}

	public SXSSFWorkbook exportTemplate(Map<String, String> params) throws Exception {
		ClassType classType = ClassType.valueOf(params.get("classType"));
		String className = classType.getExportValue();

		Class<?> serviceClass = Class.forName(className);
		Method method = serviceClass.getSuperclass().getDeclaredMethod("exportTemplate", Map.class);

		SXSSFWorkbook workbook = null;
		try {
			workbook = (SXSSFWorkbook) method.invoke(context.getBean(serviceClass), params);
		} catch (InvocationTargetException e) {
			Throwable cause = e.getCause();
			if (cause != null) {
				cause.printStackTrace();
				throw new InternalServerException(cause.getLocalizedMessage());
			} else
				throw new InternalServerException(e.getLocalizedMessage());
		}

		return workbook;
	}

	@Transactional(rollbackFor = Exception.class)
	public Object[] excelImport(MultipartFile file, ClassType type, Map<String, String> params) throws Exception {
		String className = type.getExportValue();

		Class<?> serviceClass = Class.forName(className);
		Method method = serviceClass.getSuperclass().getDeclaredMethod("excelImport", MultipartFile.class, Map.class);

		Object[] response;
		try {
			response = (Object[]) method.invoke(context.getBean(serviceClass), file, params);
		} catch (InvocationTargetException e) {
			Throwable cause = e.getCause();
			if (cause != null) {
				cause.printStackTrace();
				throw new InternalServerException(cause.getLocalizedMessage());
			} else
				throw new InternalServerException(e.getLocalizedMessage());
		}

		return response;
	}

}
