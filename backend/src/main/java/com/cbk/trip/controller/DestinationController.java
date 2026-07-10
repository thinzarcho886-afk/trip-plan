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
import org.springframework.web.bind.annotation.*;

import com.cbk.trip.dto.DestinationDTO;
import com.cbk.trip.dto.FloorDTO;
import com.cbk.trip.dto.PageableDTO;
import com.cbk.trip.enums.Status;
import com.cbk.trip.service.DestinationService;
import com.cbk.trip.utils.CommonUtil;

@RestController
@RequestMapping("/api/auth/destination")
public class DestinationController {

    @Autowired
    DestinationService destinationService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN','CUSTOMER')")
    public ResponseEntity<?> getDestinations(
            @Param("name") String name, 
            @Param("status") Status status,
            @PageableDefault(size = 10, sort = "updatedDate") Pageable pageable) {

        PageableDTO result = destinationService.getDestinations(name, status, pageable);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> register(@Valid @RequestBody DestinationDTO dto, Errors errors) {

        // Duplicate Check
        if (destinationService.isNameDuplicate(dto.getName(), null)) {
            errors.rejectValue("name", "error.name", "Destination name is already duplicated.");
        }

        if (errors.hasErrors()) {
            return CommonUtil.getFieldErrors(errors);
        }

        DestinationDTO response = destinationService.save(dto, false);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> update(@Valid @RequestBody DestinationDTO dto, Errors errors) {

        if (destinationService.isNameDuplicate(dto.getName(), dto.getId())) {
            errors.rejectValue("name", "error.name", "Destination name is already duplicated.");
        }

        if (errors.hasErrors()) {
            return CommonUtil.getFieldErrors(errors);
        }

        destinationService.save(dto, true);
        return new ResponseEntity<>(CommonUtil.responseSuccessMessage("Destination updated successfully"), HttpStatus.OK);
    }

    @GetMapping("/by-status/{status}")
    public ResponseEntity<?> getByStatus(@PathVariable(required = true) Status status) {
        return new ResponseEntity<>(destinationService.getByStatus(status), HttpStatus.OK);
    }
    
    @GetMapping("/{id}")
	public ResponseEntity<?> getById(@PathVariable(required = true, name = "id") Long id) {
    	DestinationDTO currencyDTO =destinationService.getById(id);
		return new ResponseEntity<>(currencyDTO, HttpStatus.OK);
	}
}