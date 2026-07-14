package com.cbk.trip.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.cbk.trip.enums.Status;
import com.cbk.trip.enums.UserRole;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "user")
public class User extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank(message = "Username is required")
    @Column(name = "username", unique = true)
    private String username;

    @Getter(onMethod = @__(@JsonIgnore))
    @Setter
    @NotBlank(message = "Password is required")
    @Column(name = "password")
    private String password;

    @NotNull(message = "User role is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private UserRole role;

    @Column(name = "customer_id")
    private Long customerId;
    
   
    
    @Enumerated(EnumType.STRING)
	@Column(name = "status")
	private Status status = Status.ACTIVE;
}