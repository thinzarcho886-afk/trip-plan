package com.cbk.devconstruction.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.cbk.devconstruction.enums.RoomStatus;
import com.cbk.devconstruction.enums.Status;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="room")
public class Room extends BaseEntity{
	
	private static final long serialVersionUID=1L;
	
	
	@ManyToOne
	@JoinColumn(name="hostel_id",referencedColumnName="id",nullable=true)
	private Hostel hostel;
	
	@ManyToOne
	@JoinColumn(name="room_type_id",referencedColumnName="id",nullable=true)
	private RoomType roomType;
	
	@Lob
	@Column(name = "room_image" , columnDefinition = "LONGTEXT")
	private String roomImage;
	
	@Column(name="room_no")
	private String roomNo;
	
	@Column(name="capacity_per_rom")
	private String capacityPerRoom;
	 
	@Column(name="price")
	private Long price;
	
	
	@Enumerated(EnumType.STRING)
	@Column(name="room_status")
	private RoomStatus roomStatus;
	
	
	@Column(name="available_unit")
	private Integer availableUnit;
	

	@Enumerated(EnumType.STRING)
	@Column(name="status")
	private Status status;
	
	
	

}
