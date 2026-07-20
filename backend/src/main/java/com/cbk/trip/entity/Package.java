package com.cbk.trip.entity;

import java.time.Instant;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.cbk.trip.enums.Status;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "package")
public class Package extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@Column(name = "package_image_url")
	private String packageImageUrl;
	
	@Column(name = "name", nullable = false, unique = true)
	private String name;

	@ManyToOne
	@JoinColumn(name = "destination_id")
	private Destination destination;

	@ManyToOne
	@JoinColumn(name = "hotel_id")
	private Hotel hotel;

	@ManyToOne
	@JoinColumn(name = "transport_id")
	private Transport transport;

	@ManyToOne
	@JoinColumn(name = "duration_id")
	private Duration duration;

	@Column(name = "departure_date")
	private Instant departureDate;

	@Column(name = "transport_fee")
	private Double transportFee = 0d;

	@Column(name = "hotel_fee")
	private Double hotelFee = 0d;

	@Column(name = "service_fee")
	private Double serviceFee = 0d;

	@Column(name = "budget_amount")
	private Double budgetAmount = 0d;

	@Column(name = "description")
	private String description;

	@Column(name = "extra_service")
	private String extraService;

	@Enumerated(EnumType.STRING)
	@Column(name = "status")
	private Status status = Status.ACTIVE;

	@OneToMany(mappedBy = "packageEntity", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<PackageDetail> packageDetails;
}