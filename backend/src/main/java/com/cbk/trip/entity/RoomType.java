package com.cbk.trip.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.cbk.trip.enums.Status;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "room_type")
public class RoomType extends BaseEntity {
	private static final long serialVersionUID = 1L;

	@Column(name = "type_name")
	private String name;
	@Enumerated(EnumType.STRING)
	@Column(name = "status")
	private Status status = Status.ACTIVE;
}
