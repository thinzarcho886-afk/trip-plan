package com.cbk.trip.entity;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author sansintkyaw
 *
 */
@Getter
@Setter
@MappedSuperclass
public class AttachmentBaseEntity extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "file_name")
	private String fileName;

	@Column(name = "file_url")
	private String fileUrl;

}
