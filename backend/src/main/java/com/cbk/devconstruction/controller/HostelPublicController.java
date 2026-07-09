package com.cbk.devconstruction.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cbk.devconstruction.dto.HostelDTO;
import com.cbk.devconstruction.dto.PageableDTO;
import com.cbk.devconstruction.enums.Gender;
import com.cbk.devconstruction.enums.HostelStatus;
import com.cbk.devconstruction.enums.Status;
import com.cbk.devconstruction.service.HostelService;

@RestController
@RequestMapping("/api/hostel")
public class HostelPublicController {
	
	@Autowired
	HostelService hostelService;
	

	@GetMapping
	public ResponseEntity<?> getHostels(@Param("cityName") String cityName, @Param("townshipName") String townshipName, @Param("streetName") String streetName, @Param("ownerName") String ownerName, @Param("hostelName") String hostelName, @Param("gender") Gender gender, @Param("hostelStatus") HostelStatus hostelStatus,@Param("status") Status status,@Param("currUserId") String currUserId, @PageableDefault(size = Integer.MAX_VALUE) Pageable pageable) {

		PageableDTO  hostelPageDTO =hostelService.getHostels(cityName, townshipName, streetName, ownerName,hostelName, gender, hostelStatus,currUserId, status, pageable);
		
		
		
		return new ResponseEntity<>(hostelPageDTO, HttpStatus.OK);
	}

	
	@GetMapping("/{id}")
	public ResponseEntity<?> getById(@PathVariable(required = true, name = "id") Long id) {

		HostelDTO hostel=hostelService.getById(id);
		return new ResponseEntity<>(hostel, HttpStatus.OK);
	}

	
	
	

	

	
	

}
