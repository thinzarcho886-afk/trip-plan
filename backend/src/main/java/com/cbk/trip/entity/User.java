package com.cbk.trip.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.cbk.trip.enums.Status;
import com.cbk.trip.enums.UserRole;
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

	@Column(name = "username", unique = true, nullable = false)
	private String username;

	@Getter
	@Setter
	@JsonIgnore
	@Column(name = "password", nullable = false)
	private String password;

	@Enumerated(EnumType.STRING)
	@Column(name = "role", nullable = false)
	private UserRole role;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
	private Status status = Status.INACTIVE;

}
