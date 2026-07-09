package com.cbk.devconstruction.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="reviews")
public class Reviews extends ReviewBaseEntity{
	public static final long serialVersionUID=1L;
	
	@ManyToOne
	@JoinColumn(name="student_id", referencedColumnName="id")
	private Student student;

	
	@Column(name="review")
	private String review;
}
