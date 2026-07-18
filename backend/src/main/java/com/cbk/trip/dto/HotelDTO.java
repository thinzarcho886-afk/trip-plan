package com.cbk.trip.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

import com.cbk.trip.entity.Hotel;
import com.cbk.trip.enums.Status;
import com.cbk.trip.utils.NginxUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
@NoArgsConstructor
public class HotelDTO extends CommonDTO {

	private static final long serialVersionUID = 1L;

	@NotBlank(message = "Name is required")
	private String name;

	@NotNull(message = "Destination is required")
	private Long destinationId;
	private String destinationName;

	@NotBlank(message = "Address is required")
	private String address;

	private String imageUrl;
	private String image;

	@NotBlank(message = "Description is required")
	private String description;

	@Range(min = 1, message = "Price per night must be greater than zero.")
	private Double pricePerNight;

	private Status status = Status.ACTIVE;

	public HotelDTO(Hotel entity) {
		super(entity);
		this.name = entity.getName();
		this.address = entity.getAddress();
		this.imageUrl = NginxUtil.getFileUrl(entity.getImageUrl(), true);
		this.description = entity.getDescription();
		this.pricePerNight = entity.getPricePerNight();
		this.status = entity.getStatus();

		if (entity.getDestination() != null) {
			this.destinationId = entity.getDestination().getId();
			this.destinationName = entity.getDestination().getName();
		}
	}
}