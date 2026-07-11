package com.cbk.trip.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cbk.trip.dto.UserDTO;
import com.cbk.trip.enums.Status;
import com.cbk.trip.enums.UserRole;
import com.cbk.trip.service.UserService;
import com.cbk.trip.utils.CommonUtil;

@RestController
@RequestMapping("/api")
@PreAuthorize("hasAuthority('SYSADMIN')") 
public class UserController {

    @Autowired
    private UserService userService;
    
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
}