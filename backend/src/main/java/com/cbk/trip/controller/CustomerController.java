package com.cbk.trip.controller;

import java.io.IOException;

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

import com.cbk.trip.dto.CustomerDTO;
import com.cbk.trip.dto.UserDTO;
import com.cbk.trip.enums.Status;
import com.cbk.trip.service.CustomerService;
import com.cbk.trip.utils.CommonUtil;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping
    public ResponseEntity<?> getCustomers(
            @Param("name") String name,
            @Param("email") String email,
            @Param("phoneNumber") String phoneNumber,
            @Param("status") Status status,
            @PageableDefault(size = 10) Pageable pageable) {
        
        return new ResponseEntity<>(customerService.getCustomers(name, email, phoneNumber, status, pageable), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> register(@Valid @RequestBody CustomerDTO customerDTO, Errors errors) throws IOException {
        if (errors.hasErrors()) {
            return CommonUtil.getFieldErrors(errors);
        }
        
        customerService.save(customerDTO, false);
        return new ResponseEntity<>(CommonUtil.responseSuccessMessage("Customer registered successfully"), HttpStatus.CREATED);
    }

    //@PreAuthorize("hasAuthority('SYSADMIN')")
    @PutMapping
    public ResponseEntity<?> update(@Valid @RequestBody CustomerDTO customerDTO, Errors errors) throws IOException {
        if (errors.hasErrors()) {
            return CommonUtil.getFieldErrors(errors);
        }
        
        customerService.save(customerDTO, true);
        return new ResponseEntity<>(CommonUtil.responseString("Customer updated successfully"), HttpStatus.OK);
    }

    
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable(required = true, name = "id") Long id) {
        
        CustomerDTO customerDTO = customerService.getById(id);
        
        return new ResponseEntity<>(customerDTO, HttpStatus.OK);
    }
    
    @GetMapping("/status")
    public ResponseEntity<?> getByStatus(@Param("status") Status status) {
        return new ResponseEntity<>(customerService.getByStatus(status), HttpStatus.OK);
    }
}