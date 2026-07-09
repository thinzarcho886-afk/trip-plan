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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cbk.devconstruction.dto.CityDTO;
import com.cbk.devconstruction.dto.PageableDTO;
import com.cbk.devconstruction.enums.Status;
import com.cbk.devconstruction.service.CityService;

@RestController
@RequestMapping("/api/city")
public class CityPublicController {
	
	@Autowired
	CityService cityService;

	@GetMapping
	public ResponseEntity<?> getCities(@Param("cityName") String cityName,@Param("status") Status status, @PageableDefault(size = Integer.MAX_VALUE) Pageable pageable) {

		PageableDTO townshipPageDTO =cityService.getCities(cityName, status, pageable);

		return new ResponseEntity<>(townshipPageDTO, HttpStatus.OK);
	}
	
	
	
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getCityId(@PathVariable(required = true, name = "id")Long id){
		CityDTO cityDTO=cityService.getById(id);
		return new ResponseEntity<>(cityDTO, HttpStatus.OK);
	}
	
	
	

}
