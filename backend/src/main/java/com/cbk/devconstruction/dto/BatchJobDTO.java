package com.cbk.devconstruction.dto;

import com.cbk.devconstruction.entity.BatchJob;
import com.cbk.devconstruction.enums.ClassType;
import com.cbk.devconstruction.enums.JobStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author sansintkyaw
 * 
 */
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class BatchJobDTO extends CommonDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ClassType jobType;
	private String fileName;
	private Boolean export;
	private JobStatus status;
	private String errorMessage;
	private Long createdUserId;

	public BatchJobDTO(BatchJob entity) {
		super(entity);
		this.jobType = entity.getJobType();
		this.fileName = entity.getFileName();
		this.export = entity.getExport();
		this.status = entity.getStatus();
		this.errorMessage = entity.getErrorMessage();
		this.createdUserId = entity.getCreatedUserId();
	}

}
