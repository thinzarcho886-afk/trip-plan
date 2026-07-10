package com.cbk.trip.dto;

import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;

import com.cbk.trip.entity.PaymentMethod;
import com.cbk.trip.enums.Status;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;

/**
 * * @author HtetAungThan
 * @since 11/Jan/2025
 *
 */
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class PaymentMethodDTO extends CommonDTO {

	private static final long serialVersionUID = 1L;

	@NotBlank(message = "Name is required")
	@Length(max = 100)
	private String name;

	@NotBlank(message = "Account number is required")
	@Length(max = 50)
	private String accountNumber;

	@NotBlank(message = "Account name is required")
	@Length(max = 100)
	private String accountName;

	@Length(max = 300)
	private String description;

	private String imageUrl;

	private Status status = Status.ACTIVE;

	public PaymentMethodDTO() {
	}

	public PaymentMethodDTO(PaymentMethod entity) {
		super(entity);
		this.name = entity.getName();
		this.accountNumber = entity.getAccountNumber();
		this.accountName = entity.getAccountName();
		this.description = entity.getDescription();
		this.imageUrl = entity.getImageUrl();
		this.status = entity.getStatus();
	}
}