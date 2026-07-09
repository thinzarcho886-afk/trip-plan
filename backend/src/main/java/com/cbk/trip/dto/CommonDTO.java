package com.cbk.trip.dto;

import java.io.Serializable;

import com.cbk.trip.entity.BaseEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 
 * @author sansintkyaw
 *
 */
@Getter
@Setter
@NoArgsConstructor
public class CommonDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;
	private String createdBy;
	private String updatedBy;
	private Long createdDateInMilliSeconds;
	private Long updatedDateInMilliSeconds;

	public <T extends BaseEntity> CommonDTO(T entity) {
		this.id = entity.getId();
		this.createdBy = entity.getCreatedBy();
		this.updatedBy = entity.getUpdatedBy();
		this.createdDateInMilliSeconds = entity.getCreatedDate() == null ? null
				: entity.getCreatedDate().toEpochMilli();
		this.updatedDateInMilliSeconds = entity.getUpdatedDate() == null ? null
				: entity.getUpdatedDate().toEpochMilli();
	}

}
