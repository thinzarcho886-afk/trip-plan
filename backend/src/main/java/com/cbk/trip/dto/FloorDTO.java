package com.cbk.trip.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.cbk.trip.entity.Floor;
import com.cbk.trip.enums.Status;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author HtetAungThan
 * @since 11/Jan/2025
 *
 */
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class FloorDTO extends CommonDTO {

	public FloorDTO() {

	}

	/**
	* 
	*/
	private static final long serialVersionUID = 1L;

	@NotBlank
	@Length(max = 100)
	private String name;

	@NotNull
	private Long companyId;
	private String companyName;

	@Length(max = 300)
	private String description;

	private Boolean isDefault = false;

	private Status status = Status.ACTIVE;

	public FloorDTO(Floor entity) {
		super(entity);
		this.name = entity.getName();
		this.description = entity.getDescription();
		this.status = entity.getStatus();
	}
}
