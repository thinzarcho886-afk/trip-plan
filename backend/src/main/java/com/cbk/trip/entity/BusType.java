package com.cbk.trip.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany; // ✅ OneToMany mapping အတွက် import ထည့်ပါသည်
import javax.persistence.Table;

import com.cbk.trip.enums.Status;

import lombok.Getter;
import lombok.Setter;

/**
 * @author HtetAungThan
 * @since 11/Jan/2025
 */
@Getter
@Setter
@Entity
@Table(name = "bus_type")
public class BusType extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@Column(name = "name")
	private String name;

	@Column(name = "available_seats")
	private Integer availableSeats;

	@Column(name = "description")
	private String description;

	@Enumerated(EnumType.STRING)
	@Column(name = "status")
	private Status status = Status.ACTIVE;

	@OneToMany(mappedBy = "busType")
	private List<Transport> transports = new ArrayList<>();
}