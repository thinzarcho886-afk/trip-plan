package com.cbk.devconstruction.dto;

import java.io.Serializable;

import com.cbk.devconstruction.entity.ReviewBaseEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class ReviewCommonDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;
	private String createdBy;
	private Long createdDateInMilliSeconds;

	public <T extends ReviewBaseEntity> ReviewCommonDTO(T entity) {
		this.id = entity.getId();
		this.createdBy = entity.getCreatedBy();
		this.createdDateInMilliSeconds = entity.getCreatedDate() == null ? null
				: entity.getCreatedDate().toEpochMilli();
		
	}

}
