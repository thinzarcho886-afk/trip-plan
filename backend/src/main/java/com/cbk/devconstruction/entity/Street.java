package com.cbk.devconstruction.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.cbk.devconstruction.enums.Status;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="street")
public class Street extends BaseEntity{
	
	private static final long serialVersionUID=1L;
	
	
	@ManyToOne
	@JoinColumn(name="township_id",referencedColumnName="id",nullable=true)
	private Township township;
	
	@Column(name="street_name")
	private String streetName;
	
	@Enumerated(EnumType.STRING)
	@Column(name="status")
	private Status status;
	
	
	
	
	

}
