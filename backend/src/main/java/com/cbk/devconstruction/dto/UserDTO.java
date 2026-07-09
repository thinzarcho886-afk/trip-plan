package com.cbk.devconstruction.dto;

import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import com.cbk.devconstruction.entity.User;
import com.cbk.devconstruction.enums.Status;
import com.cbk.devconstruction.enums.UserRole;
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
		
			

		if (entity.getStudent() != null) {
			
			this.studentImage = NginxUtil.getFileUrl(entity.getStudent().getImage(), true);
			this.studentImageUrl=entity.getStudent().getImage();
			this.studentName = entity.getStudent().getName();
			this.studentEmail=entity.getStudent().getEmail();
			this.studentPhone=entity.getStudent().getPhone();
			this.studentAddress=entity.getStudent().getAddress();
			this.studentStatus=entity.getStudent().getStatus();
			
			
		}
		
		if(entity.getOwner()!=null) {
			this.ownerImage = NginxUtil.getFileUrl(entity.getOwner().getImage(), true);
			this.ownerImageUrl=entity.getOwner().getImage();
			this.ownerName=entity.getOwner().getName();
			this.ownerEmail=entity.getOwner().getEmail();
			this.ownerPhone=entity.getOwner().getPhone();
			this.ownerAddress=entity.getOwner().getAddress();
			this.ownerStatus=entity.getOwner().getStatus();
		}
		this.username = entity.getUsername();
		this.password=entity.getPassword();
		this.role = entity.getRole();
		this.status = entity.getStatus();	
		
	}

}
