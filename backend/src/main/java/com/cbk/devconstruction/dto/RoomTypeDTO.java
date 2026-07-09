package com.cbk.devconstruction.dto;

import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;

import com.cbk.devconstruction.entity.RoomType;
import com.cbk.devconstruction.enums.Status;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class RoomTypeDTO extends CommonDTO {
	public RoomTypeDTO() {

	}
	private static final long serialVersionUID = 1L;

	@NotBlank
	@Length(max = 100)
	private String name;
	
	private Status status = Status.ACTIVE;
	
	
	public RoomTypeDTO(RoomType entity) {
		super(entity);
		this.name=entity.getName();
		this.status = entity.getStatus();
	}
	
}
