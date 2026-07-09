package com.cbk.trip.dto;

import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;

import com.cbk.trip.entity.User;
import com.cbk.trip.enums.Status;
import com.cbk.trip.enums.UserRole;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
@NoArgsConstructor
public class UserDTO extends CommonDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String studentImage;
	private String studentImageUrl;
	private String studentName;
	private String studentEmail;
	private String studentPhone;
	private String studentAddress;
	private Status studentStatus;

	private String ownerImage;
	private String ownerImageUrl;
	private String ownerName;
	private String ownerEmail;
	private String ownerPhone;
	private String ownerAddress;
	private Status ownerStatus;

	@NotBlank(message = "{user.username} {error.required}")
	@Length(max = 100, message = "{user.username} {error.maxLength}")
	private String username;

	@Length(max = 50, message = "{user.username} {error.maxLength}")
	private String password;

	private UserRole role;

//	private MultipartFile image;

	private Status status = Status.ACTIVE;

	public UserDTO(User entity) {
		super(entity);

		this.username = entity.getUsername();
		this.password = entity.getPassword();
		this.role = entity.getRole();
		this.status = entity.getStatus();

	}

}
