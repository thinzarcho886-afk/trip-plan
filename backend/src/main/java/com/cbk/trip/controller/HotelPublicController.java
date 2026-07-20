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

import com.cbk.trip.dto.HotelDTO;
import com.cbk.trip.dto.PageableDTO;
import com.cbk.trip.enums.Status;
import com.cbk.trip.service.HotelService;
import com.cbk.trip.utils.CommonUtil;

@RestController
@RequestMapping("/api/hotel")
public class HotelPublicController {

	@Autowired
	HotelService hotelService;
	@GetMapping
	public ResponseEntity<?> getHotels(@Param("name") String name, @Param("destinationId") Long destinationId,
			@Param("status") Status status, @PageableDefault(size = 10) Pageable pageable) {

		PageableDTO result = hotelService.getHotels(name, destinationId, status, pageable);
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	
	@GetMapping("/{id}")
	public ResponseEntity<?> getById(@PathVariable(required = true, name = "id") Long id) {
		HotelDTO currencyDTO = hotelService.getById(id);
		return new ResponseEntity<>(currencyDTO, HttpStatus.OK);
	}

}