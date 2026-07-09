package com.cbk.devconstruction.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.cbk.devconstruction.enums.Status;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author HtetAungThan
 * @since January 01 , 2025
 *
 */

@Getter
@Setter
@Entity
@Table(name = "company")
public class Company extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "code", unique = true, nullable = false)
	private String code;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "company_letter_head_url")
	private String companyLetterHeadUrl;

	@Column(name = "company_type")
	private String companyType;

	@Column(name = "address")
	private String address;

	@Column(name = "phone_no")
	private String phoneNo;

	@Column(name = "email")
	private String email;

	@Column(name = "description")
	private String description;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
	private Status status;

}