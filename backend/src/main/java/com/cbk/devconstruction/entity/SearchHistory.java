package com.cbk.devconstruction.entity;

import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="search_history")
public class SearchHistory {
	
	public static final long serialVersionUID=1L;
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="user_id" ,referencedColumnName="id")
	private User user;
	
	@ManyToOne
	@JoinColumn(name="hostel_id", referencedColumnName="id")
	private Hostel hostel;
	
	@Column(name="datetime")
	private LocalDate datetime;
	
	

}
