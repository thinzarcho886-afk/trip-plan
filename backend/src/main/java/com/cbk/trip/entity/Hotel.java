package com.cbk.trip.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.cbk.trip.enums.Status;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "hotel")
public class Hotel extends BaseEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "name")
	private String name;

	@Column(name = "address")
	private String address;

	@Column(name = "image_url")
	private String imageUrl;

	@Column(name = "price_per_night")
	private Double pricePerNight = 0d;

	@Column(name = "description")
	private String description;

	@Enumerated(EnumType.STRING)
	@Column(name = "status")
	private Status status = Status.ACTIVE;

	@ManyToOne
	@JoinColumn(name = "destination_id")
	private Destination destination;
}