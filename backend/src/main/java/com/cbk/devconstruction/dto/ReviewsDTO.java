package com.cbk.devconstruction.dto;

import com.cbk.devconstruction.entity.Reviews;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
@NoArgsConstructor
public class ReviewsDTO extends ReviewCommonDTO {
	
	public static final long serialVersionUID=1L;
	
	
	private Long studentId;
	private String studentName;
	
	private String review;
	
	public ReviewsDTO(Reviews entity) {
		super(entity);
		if(entity.getStudent()!=null) {
			this.studentId=entity.getStudent().getId();
			this.studentName=entity.getStudent().getName();
		}
		this.review=entity.getReview();
	}

}
