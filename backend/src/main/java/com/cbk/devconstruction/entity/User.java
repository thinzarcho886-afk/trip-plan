package com.cbk.devconstruction.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.cbk.devconstruction.enums.Status;
import com.cbk.devconstruction.enums.UserRole;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author sansintkyaw
 *
 */
@Getter
@Setter
@Entity
@Table(name = "user")
public class User extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


//	@Getter(onMethod = @__(@JsonIgnore))
//	@Setter
//	@Column(name = "password", nullable = false)
//	private String password;
	
	

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "owner_id", nullable=true)
	private Owner owner;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "student_id", nullable=true)
	private Student student;
	
	



	@Column(name = "username", unique = true, nullable = false)
	private String username;
	
	
	@Getter
	@Setter
	@JsonIgnore
	@Column(name = "password", nullable = false)
	private String password;
	
	
	@Enumerated(EnumType.STRING)
	@Column(name="role", nullable=false)
	private UserRole role;
	
	
	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
	private Status status = Status.INACTIVE;

}
