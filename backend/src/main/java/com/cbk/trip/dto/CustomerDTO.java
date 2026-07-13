package com.cbk.trip.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.cbk.trip.entity.Customer;
import com.cbk.trip.enums.Status;
import com.cbk.trip.utils.NginxUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
@NoArgsConstructor
public class CustomerDTO extends CommonDTO {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Phone number is required")
    private String phoneNumber;

    private String profileImage;
    private String profileImageUrl;

    @NotNull(message = "Status is required")
    private Status status;
    
    private String password;
    

    public CustomerDTO(Customer entity) {
        super(entity);
        this.name = entity.getName();
        this.email = entity.getEmail();
        this.phoneNumber = entity.getPhoneNumber();
        this.profileImage = entity.getProfileImage();
        this.profileImageUrl = NginxUtil.getFileUrl(entity.getProfileImage(), true);
        this.status = entity.getStatus();
        this.password = password;
    }
}