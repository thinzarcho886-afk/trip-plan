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

import com.cbk.trip.dto.HostelDTO;
import com.cbk.trip.dto.PageableDTO;
import com.cbk.trip.enums.HostelStatus;
import com.cbk.trip.enums.Status;
import com.cbk.trip.repository.UserRepository;
import com.cbk.trip.service.HostelService;
import com.cbk.trip.utils.CommonUtil;

@RestController
@RequestMapping("/api/auth/hostel")
public class HostelController {

	@Autowired
	HostelService hostelService;

	@Autowired
	UserRepository userRepository;

	@PreAuthorize("hasAnyAuthority('SYSADMIN','OWNER','STUDENT')")
	@GetMapping
	public ResponseEntity<?> getHostels(@Param("cityName") String cityName, @Param("townshipName") String townshipName,
			@Param("streetName") String streetName, @Param("ownerName") String ownerName,
			@Param("hostelName") String hostelName, @Param("hostelStatus") HostelStatus hostelStatus,
			@Param("status") Status status, @Param("currUserId") String currUserId,
			@PageableDefault(size = Integer.MAX_VALUE) Pageable pageable) {

		PageableDTO hostelPageDTO = hostelService.getHostels(cityName, townshipName, streetName, ownerName, hostelName,
				hostelStatus, currUserId, status, pageable);

		return new ResponseEntity<>(hostelPageDTO, HttpStatus.OK);
	}

	@PreAuthorize("hasAnyAuthority('SYSADMIN', 'OWNER')")
	@PostMapping
	public ResponseEntity<?> register(@Valid @RequestBody HostelDTO hostelDTO, Errors errors) throws IOException {

		// name duplicate
		if (hostelService.isNameDuplicate(null, hostelDTO.getHostelName())) {
			errors.rejectValue("hostelName", "error.name", CommonUtil.getLocalizeMessage("field.name", null) + " "
					+ CommonUtil.getLocalizeMessage("error.duplicated", null));
		}

		// If error, just return a 400 bad request, along with the error message
		if (errors.hasErrors()) {
			return CommonUtil.getFieldErrors(errors);
		}

		hostelService.save(hostelDTO, false);

		return new ResponseEntity<>(CommonUtil.responseSuccessMessage("Hostel registered"), HttpStatus.CREATED);
	}

	@PreAuthorize("hasAnyAuthority('SYSADMIN', 'OWNER')")
	@PutMapping
	public ResponseEntity<?> update(@Valid @RequestBody HostelDTO hostelDTO, Errors errors) throws IOException {

		// name duplicate
		if (hostelService.isNameDuplicate(hostelDTO.getId(), hostelDTO.getHostelName())) {
			errors.rejectValue("hostelName", "error.name", CommonUtil.getLocalizeMessage("field.name", null) + " "
					+ CommonUtil.getLocalizeMessage("error.duplicated", null));
		}

		// If error, just return a 400 bad request, along with the error message
		if (errors.hasErrors()) {
			return CommonUtil.getFieldErrors(errors);
		}

		hostelService.save(hostelDTO, true);

		return new ResponseEntity<>(CommonUtil.responseString("Hostel updated"), HttpStatus.OK);
	}

	@PreAuthorize("hasAnyAuthority('SYSADMIN','OWNER','STUDENT')")
	@GetMapping("/{id}")
	public ResponseEntity<?> getById(@PathVariable(required = true, name = "id") Long id) {

		HostelDTO hostel = hostelService.getById(id);
		return new ResponseEntity<>(hostel, HttpStatus.OK);
	}

}
