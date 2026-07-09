package com.cbk.devconstruction.entity;

import java.util.ArrayList;
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

import com.cbk.devconstruction.enums.Gender;
import com.cbk.devconstruction.enums.HostelStatus;
import com.cbk.devconstruction.enums.Status;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="hostel")
public class Hostel extends BaseEntity{
	
	private static final long serialVersionUID=1L;
	
	@ManyToOne
	@JoinColumn(name="city_id",referencedColumnName="id")
	private City city;
	
	@ManyToOne
	@JoinColumn(name="township_id",referencedColumnName="id")
	private Township township;
	
	@ManyToOne
	@JoinColumn(name="street_id",referencedColumnName="id")
	private Street street;
	
	@ManyToOne
	@JoinColumn(name="user_id",referencedColumnName="id")
	private User user;
	
	@Column(name = "hostel_image")
	private String hostelImage;
	
	@Column(name="hostel_name")
	private String hostelName;
	
	@Enumerated(EnumType.STRING)
	@Column(name="gender")
	private Gender gender;
	
	
	@Column(name="description")
	private String description;
	
	@Enumerated(EnumType.STRING)
	@Column(name="hostel_status")
	private HostelStatus hostelStatus;
	
	@Enumerated(EnumType.STRING)
	@Column(name="status")
	private Status status;
	
	@Column(name="latitude")
	private Double latitude;
	
	@Column(name="longitude")
	private Double longitude;
	
	@OneToMany(mappedBy="hostel",cascade= {CascadeType.PERSIST,CascadeType.MERGE})
	private List<Room> roomList = new ArrayList<>();
	
	

}
