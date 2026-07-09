package com.cbk.devconstruction.entity;

import java.io.Serializable;
import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;

import org.hibernate.annotations.CreationTimestamp;

import com.cbk.devconstruction.utils.CommonUtil;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@MappedSuperclass
public class ReviewBaseEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "created_by")
	private String createdBy;

	@CreationTimestamp
	@Column(name = "created_date")
	private Instant createdDate;

	@PrePersist
	private void prePersist() {
		createdBy = CommonUtil.getUsernameFromAuthentication();
	}

	
}
