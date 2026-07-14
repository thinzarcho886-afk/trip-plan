package com.cbk.trip.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "transport")
public class Transport extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name = "bus_type_id")
	private BusType busType;

	@ManyToOne
	@JoinColumn(name = "bus_id")
	private Bus bus;
}