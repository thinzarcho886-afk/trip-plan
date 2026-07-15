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

import com.cbk.trip.dto.BusDTO;
import com.cbk.trip.dto.PageableDTO;
import com.cbk.trip.enums.Status;
import com.cbk.trip.service.BusService;
import com.cbk.trip.utils.CommonUtil;

/**
 * @author HtetAungThan
 * @since 11/Jan/2025
 */
@RestController
@RequestMapping("/api/auth/bus")
public class BusController {

	@Autowired
	BusService busService;

	@PreAuthorize("hasAnyAuthority('SYSADMIN','CUSTOMER')")
	@GetMapping
	public ResponseEntity<?> getBuses(@Param("name") String name, @Param("status") Status status,@Param("busTypeId")Long busTypeId,
			@PageableDefault(size = Integer.MAX_VALUE, sort = "updatedDate") Pageable pageable) {

		PageableDTO busDTO = busService.getBuses(name, status,busTypeId, pageable);
		return new ResponseEntity<>(busDTO, HttpStatus.OK);
	}

	@PreAuthorize("hasAuthority('SYSADMIN')")
	@PostMapping
	public ResponseEntity<?> register(@Valid @RequestBody BusDTO busDTO, Errors errors) {

		if (busService.isNameDuplicate(busDTO.getName(), null)) {
			errors.rejectValue("name", "error.name", "Name already duplicated");
		}

		if (errors.hasErrors()) {
			return CommonUtil.getFieldErrors(errors);
		}

		BusDTO respondDTO = busService.save(busDTO, false);
		return new ResponseEntity<>(respondDTO, HttpStatus.CREATED);
	}

	@PreAuthorize("hasAuthority('SYSADMIN')")
	@PutMapping
	public ResponseEntity<?> update(@Valid @RequestBody BusDTO busDTO, Errors errors) {

		if (busService.isNameDuplicate(busDTO.getName(), busDTO.getId())) {
			errors.rejectValue("name", "error.name", "Name already duplicated");
		}

		if (errors.hasErrors()) {
			return CommonUtil.getFieldErrors(errors);
		}

		busService.save(busDTO, true);
		return new ResponseEntity<>(CommonUtil.responseSuccessMessage("Bus updated"), HttpStatus.OK);
	}
	@PreAuthorize("hasAnyAuthority('SYSADMIN','CUSTOMER')")
	@GetMapping("/{id}")
	public ResponseEntity<?> getById(@PathVariable(required = true, name = "id") Long id) {

		BusDTO busDTO = busService.getById(id);

		return new ResponseEntity<>(busDTO, HttpStatus.OK);
	}
}