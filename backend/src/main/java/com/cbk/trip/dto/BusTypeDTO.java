package com.cbk.trip.dto;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.cbk.trip.entity.BusType;
import com.cbk.trip.enums.Status;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;

/**
 * @author HtetAungThan
 * @since 11/Jan/2025
 */
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class BusTypeDTO extends CommonDTO {

	public BusTypeDTO() {
		super();
	}

	private static final long serialVersionUID = 1L;

	@NotBlank(message = "Name is required")
	@Length(max = 100, message = "Name length must not be greater than 100")
	private String name;

	@NotNull(message = "Available seats is required")
	@Min(value = 1, message = "Available seats must be greater than zero")
	private Integer availableSeats;

	@Length(max = 255, message = "Description length must not be greater than 255")
	private String description;

	private Status status = Status.ACTIVE;
	private List<BusDTO> buses = new ArrayList<>(); 

	public BusTypeDTO(BusType entity) {
		super(entity);
		this.name = entity.getName();
		this.availableSeats = entity.getAvailableSeats();
		this.description = entity.getDescription();
		this.status = entity.getStatus();
		if (entity.getTransports() != null) {
			entity.getTransports().forEach(transport -> {
				if (transport.getBus() != null) {
					this.buses.add(new BusDTO(transport.getBus()));
				}
			});
		}
	}
}