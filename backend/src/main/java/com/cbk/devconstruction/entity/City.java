package com.cbk.devconstruction.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.cbk.devconstruction.enums.Status;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="city")
public class City extends BaseEntity{
	
	public static final long serialVersionUID=1L;
	
	@Column(name="city_name")
	private String cityName;
	
	@Enumerated(EnumType.STRING)
	@Column(name="status")
	private Status status;
	
	@OneToMany(mappedBy="city",cascade= {CascadeType.PERSIST,CascadeType.MERGE})
	private List<Township> townshipDetailList = new ArrayList<>();
	

}
