package com.cbk.trip.controller;

import javax.validation.Valid;

import lombok.extern.slf4j.Slf4j;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cbk.trip.common.MessageConst;
import com.cbk.trip.dto.UserDTO;
import com.cbk.trip.entity.User;
import com.cbk.trip.enums.Status;
import com.cbk.trip.enums.UserRole;
import com.cbk.trip.exception.BadRequestException;
import com.cbk.trip.service.UserService;
import com.cbk.trip.utils.CommonUtil;

@Slf4j 
@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;
    
    @Autowired
	PasswordEncoder passwordEncoder;
    
    @PostMapping("/login")
    @PreAuthorize("permitAll()") 
    public ResponseEntity<?> login(@RequestBody UserDTO userDTO) {
        return new ResponseEntity<>(userService.login(userDTO), HttpStatus.OK);
    }

    @GetMapping("/auth/user")
    public ResponseEntity<?> getUsers(
            @Param("username") String username,
            @Param("role") UserRole role,
            @Param("status") Status status,
            @PageableDefault(size = 10) Pageable pageable) {
        
        return new ResponseEntity<>(userService.getUsers(username, role, status, pageable), HttpStatus.OK);
    }

    @PostMapping("/auth/user")
    public ResponseEntity<?> register(@Valid @RequestBody UserDTO userDTO, Errors errors) {
        
        if (userService.isUsernameDuplicate(null, userDTO.getUsername())) {
            errors.rejectValue("username", "error.username", "Username already exists");
        }

        if (errors.hasErrors()) {
            return CommonUtil.getFieldErrors(errors);
        }

        userService.save(userDTO, false);
        return new ResponseEntity<>(CommonUtil.responseSuccessMessage("User created successfully"), HttpStatus.CREATED);
    }
    
    @GetMapping("/auth/user/{id}")
    public ResponseEntity<?> getById(@PathVariable(required = true, name = "id") Long id) {
        
        UserDTO userDTO = userService.getById(id);
        
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @PutMapping("/auth/user")
    public ResponseEntity<?> update(@Valid @RequestBody UserDTO userDTO, Errors errors) {
        
        if (errors.hasErrors()) {
            return CommonUtil.getFieldErrors(errors);
        }

        userService.save(userDTO, true);
        return new ResponseEntity<>(CommonUtil.responseString("User status updated successfully"), HttpStatus.OK);
    }
    
    
    @PutMapping("/auth/change-password-by-current-user")
	public ResponseEntity<?> changePasswordByCurrentUser(@RequestBody String payload) {
		JSONObject obj = new JSONObject(payload);
		String oldPassword = obj.getString("oldPassword");
		String newPassword = obj.getString("newPassword");
		String username = CommonUtil.getUsernameFromAuthentication();
		User user = userService.getByName(username);

		if (oldPassword.isEmpty()) {
			throw new BadRequestException(CommonUtil.getLocalizeMessage("error.auth.requiredConfirmPassword", null));
		}

		if (newPassword.isEmpty()) {
			throw new BadRequestException(CommonUtil.getLocalizeMessage("error.auth.requiredPassword", null));
		}

		if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
			log.info(MessageConst.INCORRECT_PASSWORD);
			throw new BadRequestException(CommonUtil.getLocalizeMessage("error.auth.incorrectPassword", null));
		}

		userService.changePassword(user.getId(), newPassword);

		return new ResponseEntity<>(CommonUtil.responseSuccessMessage("Password changed."), HttpStatus.OK);
	}

	@PreAuthorize("hasAnyAuthority('SYSADMIN','OWNER','CUSTOMER')")
	@PutMapping("/auth/change-password-by-id")
	public ResponseEntity<?> changePasswordById(@RequestBody String payload) {

		JSONObject obj = new JSONObject(payload);
		Long id = obj.getLong("id");
		String newPassword = obj.getString("newPassword");

		if (newPassword.isEmpty()) {
			throw new BadRequestException(CommonUtil.getLocalizeMessage("error.auth.requiredPassword", null));
		}

		userService.changePassword(id, newPassword);

		return new ResponseEntity<>(CommonUtil.responseString("Password changed"), HttpStatus.OK);
	}

    
    
    
    
}