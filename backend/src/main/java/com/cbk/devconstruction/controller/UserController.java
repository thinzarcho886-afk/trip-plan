package com.cbk.devconstruction.controller;

import java.io.IOException;

import javax.validation.Valid;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cbk.devconstruction.common.MessageConst;
import com.cbk.devconstruction.dto.PageableDTO;
import com.cbk.devconstruction.dto.UserDTO;
import com.cbk.devconstruction.entity.User;
import com.cbk.devconstruction.enums.Status;
import com.cbk.devconstruction.enums.UserRole;
import com.cbk.devconstruction.exception.BadRequestException;
import com.cbk.devconstruction.security.JwtAuthenticationResponse;
import com.cbk.devconstruction.security.JwtTokenProvider;
import com.cbk.devconstruction.service.UserService;
import com.cbk.devconstruction.utils.CommonUtil;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = "/api")
@Slf4j
public class UserController {

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	JwtTokenProvider tokenProvider;

	@Autowired
	UserService userService;

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody String data) {
		JSONObject obj = new JSONObject(data);
		String username = obj.getString("username").trim();
		String password = obj.getString("password");

		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(username, password));

		SecurityContextHolder.getContext().setAuthentication(authentication);

		String jwt = tokenProvider.generateToken(authentication);

		User user = userService.getUserByName(username);
		UserDTO userDTO = new UserDTO(user);

		JwtAuthenticationResponse jwtResponse = new JwtAuthenticationResponse(jwt, userDTO);
		return new ResponseEntity<JwtAuthenticationResponse>(jwtResponse, HttpStatus.OK);
	}

	@PostMapping("/register")
	public ResponseEntity<?> register(@Valid @RequestBody UserDTO userDTO, Errors errors) throws IOException {

		// check name duplicate
		if (userService.isUsernameDuplicate(userDTO.getUsername().trim(), null)) {
			errors.rejectValue("username", "error.name", CommonUtil.getLocalizeMessage("user.username", null) + " "
					+ CommonUtil.getLocalizeMessage("error.duplicated", null));
		}

		// If error, just return a 400 bad request, along with the error message
		if (errors.hasErrors()) {
			return CommonUtil.getFieldErrors(errors);
		}

		userService.save(userDTO, false);

		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(userDTO.getUsername(), userDTO.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);

		String jwt = tokenProvider.generateToken(authentication);

		User user = userService.getUserByName(userDTO.getUsername());
		UserDTO response = new UserDTO(user);

		JwtAuthenticationResponse jwtResponse = new JwtAuthenticationResponse(jwt, response);
		return new ResponseEntity<JwtAuthenticationResponse>(jwtResponse, HttpStatus.OK);
	}

	@PutMapping("/auth/change-password-by-current-user")
	public ResponseEntity<?> changePasswordByCurrentUser(@RequestBody String payload) {
		JSONObject obj = new JSONObject(payload);
		String oldPassword = obj.getString("oldPassword");
		String newPassword = obj.getString("newPassword");
		String username = CommonUtil.getUsernameFromAuthentication();
		User user = userService.getUserByName(username);

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

	@PreAuthorize("hasAnyAuthority('SYSADMIN','OWNER')")
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

	@PreAuthorize("hasAnyAuthority('SYSADMIN','OWNER')")
	@GetMapping("/auth/users")
	public ResponseEntity<?> getAllUsers(@Param("studentName") String studentName, @Param("ownerName") String ownerName,
			@Param("username") String username, @Param("role") UserRole role, @Param("status") Status status,
			@PageableDefault(size = Integer.MAX_VALUE) Pageable pageable) {

		PageableDTO pageableDTO = userService.getAllUser(studentName, ownerName, username, role, status, pageable);

		return new ResponseEntity<>(pageableDTO, HttpStatus.OK);
	}

	@GetMapping("/auth/user/{id}")
	public ResponseEntity<?> getById(@PathVariable("id") Long id) {

		UserDTO userDTO = userService.getById(id);

		return new ResponseEntity<>(userDTO, HttpStatus.OK);
	}

//  @PreAuthorize("hasAuthority('SYSADMIN')")
//  @PostMapping("/auth/user")
//  public ResponseEntity<?> register(@Valid @RequestBody UserDTO userDTO, Errors errors) throws IOException {
//
//    // check name duplicate
//    if (userService.isUsernameDuplicate(userDTO.getUsername().trim(), null)) {
//      errors.rejectValue("username", "error.name", CommonUtil.getLocalizeMessage("user.username", null) + " "
//          + CommonUtil.getLocalizeMessage("error.duplicated", null));
//    }
//
//    // If error, just return a 400 bad request, along with the error message
//    if (errors.hasErrors()) {
//      return CommonUtil.getFieldErrors(errors);
//    }
//
//    userService.save(userDTO, false);
//
//    return new ResponseEntity<>(CommonUtil.responseSuccessMessage("User registered."), HttpStatus.CREATED);
//  }

//  @PreAuthorize("hasAuthority('SYSADMIN')")
	@PutMapping("/auth/user")
	public ResponseEntity<?> update(@Valid @RequestBody UserDTO userDTO, Errors errors) throws IOException {

		// check name duplicate
		if (userService.isUsernameDuplicate(userDTO.getUsername(), userDTO.getId())) {
			errors.rejectValue("username", "error.name", CommonUtil.getLocalizeMessage("user.username", null) + " "
					+ CommonUtil.getLocalizeMessage("error.duplicated", null));
		}

		// If error, just return a 400 bad request, along with the error message
		if (errors.hasErrors()) {
			return CommonUtil.getFieldErrors(errors);
		}

		userService.save(userDTO, true);

		return new ResponseEntity<>(CommonUtil.responseSuccessMessage("User updated."), HttpStatus.OK);
	}

}