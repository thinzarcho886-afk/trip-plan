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

import com.cbk.devconstruction.dto.PageableDTO;

import com.cbk.devconstruction.dto.StreetDTO;

import com.cbk.devconstruction.enums.Status;
import com.cbk.devconstruction.service.StreetService;


@RestController
@RequestMapping("/api/street")
public class StreetPublicController {
	
	@Autowired
	StreetService streetService;

	@GetMapping
	public ResponseEntity<?> getStreets(@Param("townshipId") Long townshipId, @Param("streetName") String streetName,@Param("status") Status status, Boolean has,@PageableDefault(size = Integer.MAX_VALUE) Pageable pageable) {

		PageableDTO streetPageDTO =streetService.getStreets(townshipId, streetName, status, has, pageable);

		return new ResponseEntity<>(streetPageDTO, HttpStatus.OK);
	}
	
	
	
	
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getById(@PathVariable(required = true, name = "id") Long id) {

		StreetDTO streetDTO = streetService.getById(id);

		return new ResponseEntity<>(streetDTO, HttpStatus.OK);
	}

	
	
	
	

}
