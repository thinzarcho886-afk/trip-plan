package com.cbk.devconstruction.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.cbk.devconstruction.enums.ClassType;
import com.cbk.devconstruction.enums.JobStatus;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author sansintkyaw
 * 
 */
@Getter
@Setter
@Entity
@Table(name = "batch_job")
public class BatchJob extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "job_id")
	private String jobId;

	@Enumerated(EnumType.STRING)
	@Column(name = "job_type")
	private ClassType jobType;

	@Column(name = "is_export")
	private Boolean export;

	@Column(name = "file_name")
	private String fileName;

	@Enumerated(EnumType.STRING)
	@Column(name = "status")
	private JobStatus status;

	@Column(name = "error_message")
	private String errorMessage;

	@Column(name = "is_downloaded")
	private Boolean downloaded;

	@Column(name = "created_user_id")
	private Long createdUserId;

}
