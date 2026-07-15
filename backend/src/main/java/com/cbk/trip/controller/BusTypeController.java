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

import com.cbk.trip.dto.BusTypeDTO;
import com.cbk.trip.dto.PageableDTO;
import com.cbk.trip.enums.Status;
import com.cbk.trip.service.BusTypeService;
import com.cbk.trip.utils.CommonUtil;

/**
 * @author HtetAungThan
 * @since 11/Jan/2025
 */
@RestController
@RequestMapping("/api/auth/bus-type")
public class BusTypeController {

	@Autowired
	BusTypeService busTypeService;

	@PreAuthorize("hasAnyAuthority('SYSADMIN','CUSTOMER')")
	@GetMapping
	public ResponseEntity<?> getBusTypes(@Param("name") String name, @Param("availableSeats") Integer availableSeats,
			@Param("status") Status status,
			@PageableDefault(size = Integer.MAX_VALUE, sort = "updatedDate") Pageable pageable) {

		PageableDTO busTypeDTO = busTypeService.getBusTypes(name, availableSeats, status, pageable);
		return new ResponseEntity<>(busTypeDTO, HttpStatus.OK);
	}

	@PreAuthorize("hasAnyAuthority('SYSADMIN','CUSTOMER')")
	@PostMapping
	public ResponseEntity<?> register(@Valid @RequestBody BusTypeDTO busTypeDTO, Errors errors) {

		if (busTypeService.isNameDuplicate(busTypeDTO.getName(), null)) {
			errors.rejectValue("name", "error.name", "Name already duplicated");
		}

		if (errors.hasErrors()) {
			return CommonUtil.getFieldErrors(errors);
		}

		BusTypeDTO respondDTO = busTypeService.save(busTypeDTO, false);
		return new ResponseEntity<>(respondDTO, HttpStatus.CREATED);
	}

	@PreAuthorize("hasAnyAuthority('SYSADMIN','CUSTOMER')")
	@PutMapping
	public ResponseEntity<?> update(@Valid @RequestBody BusTypeDTO busTypeDTO, Errors errors) {

		if (busTypeService.isNameDuplicate(busTypeDTO.getName(), busTypeDTO.getId())) {
			errors.rejectValue("name", "error.name", "Name already duplicated");
		}

		if (errors.hasErrors()) {
			return CommonUtil.getFieldErrors(errors);
		}

		busTypeService.save(busTypeDTO, true);
		return new ResponseEntity<>(CommonUtil.responseSuccessMessage("BusType updated"), HttpStatus.OK);
	}

	@PreAuthorize("hasAnyAuthority('SYSADMIN','CUSTOMER')")
	@GetMapping("/{id}")
	public ResponseEntity<?> getById(@PathVariable(required = true, name = "id") Long id) {
		BusTypeDTO busTypeDTO = busTypeService.getById(id);
		return new ResponseEntity<>(busTypeDTO, HttpStatus.OK);
	}
	
	
	
	
	@PreAuthorize("hasAnyAuthority('SYSADMIN','CUSTOMER')")
	@PostMapping("/{busTypeId}/add-bus/{busId}")
	public ResponseEntity<?> addBusToTransport(@PathVariable Long busTypeId, @PathVariable Long busId) {
		busTypeService.addBusToTransport(busTypeId, busId);
		return new ResponseEntity<>(CommonUtil.responseSuccessMessage("Bus added to BusType successfully"), HttpStatus.OK);
	}

	@PreAuthorize("hasAnyAuthority('SYSADMIN','CUSTOMER')")
	@PostMapping("/{busTypeId}/remove-bus/{busId}")
	public ResponseEntity<?> removeBusFromTransport(@PathVariable Long busTypeId, @PathVariable Long busId) {
		busTypeService.removeBusFromTransport(busTypeId, busId);
		return new ResponseEntity<>(CommonUtil.responseSuccessMessage("Bus removed from BusType successfully"), HttpStatus.OK);
	}
}