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
import com.cbk.devconstruction.dto.TownshipDTO;
import com.cbk.devconstruction.enums.Status;
import com.cbk.devconstruction.service.TownshipService;


@RestController
@RequestMapping("/api/township")
public class TownshipPublicController {
	private static final long seralVersionUID=1L;
	
	@Autowired
	TownshipService townshipService;

	@GetMapping
	public ResponseEntity<?> getTownships(@Param("cityId") Long cityId, @Param("townshipName") String townshipName,@Param("status") Status status,@Param("has") Boolean has, @PageableDefault(size = Integer.MAX_VALUE) Pageable pageable) {

		PageableDTO townshipPageDTO =townshipService.getTownships(cityId, townshipName, status, has, pageable);

		return new ResponseEntity<>(townshipPageDTO, HttpStatus.OK);
	}
	
	
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getById(@PathVariable(required = true, name = "id") Long id) {

		TownshipDTO township=townshipService.getById(id);
		return new ResponseEntity<>(township, HttpStatus.OK);
	}

	
	
	
	

}
