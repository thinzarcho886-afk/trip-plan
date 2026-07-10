package com.cbk.trip.dto;

import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;
import com.cbk.trip.entity.User;
import com.cbk.trip.enums.Status;
import com.cbk.trip.enums.UserRole;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class UserDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    @NotBlank(message = "Username is required.")
    @Length(max = 100, message = "Username length must not be greater than 100.")
    private String username;

    @NotBlank(message = "Password is required.")
    @Length(max = 50, message = "Password length must not be greater than 50.")
    private String password;
    
    private String confirmPassword;

    private UserRole role;
    
    private Status status = Status.ACTIVE;

    private Long customerId;
    private String customerName; 

    private String createdBy;
    private Long createdDateInMilliSeconds;
    private String updatedBy;
    private Long updatedDateInMilliSeconds;

    public UserDTO(User entity) {
        this.id = entity.getId();
        this.username = entity.getUsername();
        this.role = entity.getRole();
        this.status = entity.getStatus();
        this.customerId = entity.getCustomerId();
        this.createdBy = entity.getCreatedBy();
        this.createdDateInMilliSeconds = entity.getCreatedDate() == null ? null : entity.getCreatedDate().toEpochMilli();
		this.updatedBy = entity.getUpdatedBy();
		this.updatedDateInMilliSeconds = entity.getUpdatedDate() == null ? null : entity.getUpdatedDate().toEpochMilli();
	}
}