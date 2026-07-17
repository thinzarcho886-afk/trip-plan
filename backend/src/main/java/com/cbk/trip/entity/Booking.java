package com.cbk.trip.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.cbk.trip.enums.BookingStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "booking")
public class Booking extends BaseEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name = "package_id")
	private Package pkg;

	@ManyToOne
	@JoinColumn(name = "customer_id")
	private Customer customer;

	@ManyToOne
	@JoinColumn(name = "payment_method_id")
	private PaymentMethod paymentMethod;

	@Column(name = "travelers_qty")
	private Integer travelersQty = 1;

	@Enumerated(EnumType.STRING)
	@Column(name = "status")
	private BookingStatus status = BookingStatus.DRAFT;

	@Column(name = "payment_receive_image")
	private String paymentReceiveImage;

	@Column(name = "note")
	private String note;
}