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

import com.cbk.devconstruction.enums.Status;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="township")
public class Township extends BaseEntity {
	
	public static final long serialVersionUID=1L;
	
	@ManyToOne
	@JoinColumn(name="city_id",referencedColumnName="id",nullable=true)
	private City city;
	
	@Column(name="township_name")
	private String townshipName;
	
	@Enumerated(EnumType.STRING)
	@Column(name="status")
	private Status status;
	
	
	@OneToMany(mappedBy="township",cascade= {CascadeType.PERSIST,CascadeType.MERGE})
	private List<Street> streetDetailList = new ArrayList<>();
	
	
	
	
}
