package com.cbk.devconstruction.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cbk.devconstruction.dto.UserDTO;
import com.cbk.devconstruction.enums.UserRole;
import com.cbk.devconstruction.service.UserService;

@RestController
@RequestMapping("/api/auth/owner")
public class OwnerController {

	@Autowired
	private UserService userService;

	@PreAuthorize("hasAnyAuthority('SYSADMIN','OWNER')")
	@PostMapping
	public ResponseEntity<?> createOwner(@RequestBody UserDTO userDTO) throws IOException {
		userService.save(userDTO, false);
		return new ResponseEntity<>("Owner registered successfully", HttpStatus.CREATED);
	}
	
	@PreAuthorize("hasAnyAuthority('SYSADMIN','OWNER')")
	@PutMapping
	public ResponseEntity<?> updateOwner(@RequestBody UserDTO userDTO) throws IOException {
		userService.save(userDTO, true);
		return new ResponseEntity<>("Owner updated successfully", HttpStatus.OK);
	}
	
	@PreAuthorize("hasAnyAuthority('SYSADMIN','OWNER')")
	@GetMapping("/by-list")
	public ResponseEntity<List<UserDTO>> getAllOwners() {
		List<UserDTO> owners = userService.getUserByRole(UserRole.OWNER);
		return ResponseEntity.ok(owners);
	}
	
	@PreAuthorize("hasAnyAuthority('SYSADMIN','OWNER')")
	@GetMapping
	public ResponseEntity<?> getOwnerPageable(Pageable pageable) {
		return ResponseEntity.ok(userService.getAllUser(null, null, null, UserRole.OWNER, null, pageable));
	}

	@PreAuthorize("hasAnyAuthority('SYSADMIN','OWNER')")
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<UserDTO> getOwnerById(@PathVariable(required = true, name = "id")Long id) {
		return ResponseEntity.ok(userService.getById(id));
	}

	@PreAuthorize("hasAnyAuthority('SYSADMIN','OWNER')")
	@GetMapping("/search-by-name")
	public ResponseEntity<List<UserDTO>> getOwnerByName(@RequestParam String name) {
		return ResponseEntity.ok(userService.getOwnerByName(name));
	}

}