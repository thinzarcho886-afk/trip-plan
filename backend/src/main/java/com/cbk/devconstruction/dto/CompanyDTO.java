package com.cbk.devconstruction.dto;

import javax.validation.constraints.NotBlank;

import com.cbk.devconstruction.entity.Company;
import com.cbk.devconstruction.enums.Status;
import com.cbk.devconstruction.utils.NginxUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 
 * @author HtetAungThan
 * @since January 01 , 2025
 *
 */

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
@NoArgsConstructor
public class CompanyDTO extends CommonDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String code;

	@NotBlank(message = "{field.name} {error.required}")
	private String name;

	private String companyLetterHeadImage;
	private String companyLetterHeadUrl;

	private String companyType;

	private String address;

	private String phoneNo;

	private String email;

	private String description;

	private Status status = Status.ACTIVE;

	public CompanyDTO(Company entity) {
		super(entity);
		this.code = entity.getCode();
		this.name = entity.getName();
		this.companyType = entity.getCompanyType();
		this.address = entity.getAddress();
		this.phoneNo = entity.getPhoneNo();
		this.email = entity.getEmail();
		this.description = entity.getDescription();
		this.status = entity.getStatus();
		this.companyLetterHeadUrl = NginxUtil.getFileUrl(entity.getCompanyLetterHeadUrl(), true);
	}

}
