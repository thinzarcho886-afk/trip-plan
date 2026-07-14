package com.cbk.trip.dto;

import javax.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

import com.cbk.trip.entity.Bus;
import com.cbk.trip.enums.Status;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;

/**
 * * @author HtetAungThan
 * @since 11/Jan/2025
 *
 */
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class BusDTO extends CommonDTO {

	public BusDTO() {
		super();
	}

	private static final long serialVersionUID = 1L;

	@NotBlank(message = "Name is required")
	@Length(max = 100, message = "Name length must not be greater than 100")
	private String name;

	private Status status = Status.ACTIVE;

	public BusDTO(Bus entity) {
		super(entity);
		this.name = entity.getName();
		this.status = entity.getStatus();
	}
}