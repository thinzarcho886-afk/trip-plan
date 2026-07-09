package com.cbk.trip.entity;

import java.io.Serializable;
import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.cbk.trip.utils.CommonUtil;

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
public class BaseEntity implements Serializable {
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

	@Column(name = "updated_by")
	private String updatedBy;

	@UpdateTimestamp
	@Column(name = "updated_date")
	private Instant updatedDate;

	@PrePersist
	private void prePersist() {
		createdBy = CommonUtil.getUsernameFromAuthentication();
		updatedBy = CommonUtil.getUsernameFromAuthentication();
	}

	@PreUpdate
	private void preUpdate() {
		updatedBy = CommonUtil.getUsernameFromAuthentication();
	}
		 
}
