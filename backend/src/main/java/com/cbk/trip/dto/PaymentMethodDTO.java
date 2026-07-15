package com.cbk.trip.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

import com.cbk.trip.entity.PaymentMethod;
import com.cbk.trip.enums.Status;
import com.cbk.trip.utils.NginxUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class PaymentMethodDTO extends CommonDTO {

	private static final long serialVersionUID = 1L;

	@NotBlank(message = "Name is required")
	@Length(max = 100)
	private String name;

	@NotNull(message = "Account number is required")
	private String accountNumber; 

	@NotBlank(message = "Account name is required")
	@Length(max = 100)
	private String accountName;

	@Length(max = 300)
	private String description;

	private String imageUrl;
	private String image;

	private Status status = Status.ACTIVE;

	public PaymentMethodDTO() {
	}

	public PaymentMethodDTO(PaymentMethod entity) {
		super(entity);
		this.name = entity.getName();
		this.accountNumber = entity.getAccountNumber();
		this.accountName = entity.getAccountName();
		this.description = entity.getDescription();
		this.imageUrl = NginxUtil.getFileUrl(entity.getImageUrl(), true);

		this.status = entity.getStatus();
	}
}