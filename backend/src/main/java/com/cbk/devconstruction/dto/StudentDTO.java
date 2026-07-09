package com.cbk.devconstruction.dto;


import com.cbk.devconstruction.entity.Student;
import com.cbk.devconstruction.enums.Status;
import com.cbk.devconstruction.utils.NginxUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
@NoArgsConstructor
public class StudentDTO extends CommonDTO {

	
	private static final long serialVersionUID = 1L;

	private String image;
	private String imageUrl;

	private String name;

	private String email;

	private String phone;

	private String address;

	private Status status=Status.INACTIVE;

	

	public StudentDTO(Student entity) {
		super(entity);
		this.imageUrl = NginxUtil.getFileUrl(entity.getImage(), true);
		this.name = entity.getName();
		this.email = entity.getEmail();
		this.phone = entity.getPhone();
		this.address = entity.getAddress();
		this.status=entity.getStatus();
			}
}
