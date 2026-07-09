package com.cbk.devconstruction.controller;

import java.io.IOException;
import java.util.List;

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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cbk.devconstruction.dto.CityDTO;
import com.cbk.devconstruction.dto.PageableDTO;
import com.cbk.devconstruction.enums.Status;
import com.cbk.devconstruction.service.CityService;
import com.cbk.devconstruction.utils.CommonUtil;

@RestController
@RequestMapping("/api/auth/city")
public class CityController {
	
	@Autowired
	CityService cityService;

	@PreAuthorize("hasAnyAuthority('SYSADMIN','OWNER')")
	@GetMapping
	public ResponseEntity<?> getCities(@Param("cityName") String cityName,@Param("status") Status status, @PageableDefault(size = Integer.MAX_VALUE) Pageable pageable) {

		PageableDTO townshipPageDTO =cityService.getCities(cityName, status, pageable);

		return new ResponseEntity<>(townshipPageDTO, HttpStatus.OK);
	}
	
	
	
	@PreAuthorize("hasAnyAuthority('SYSADMIN', 'OWNER')")
	@PostMapping
	public ResponseEntity<?> register(@Valid @RequestBody CityDTO cityDTO, Errors errors) throws IOException {

		// name duplicate
		if (cityService.isNameDuplicate(null,cityDTO.getCityName())) {
			errors.rejectValue("cityName", "error.name", CommonUtil.getLocalizeMessage("field.name", null) + " "
					+ CommonUtil.getLocalizeMessage("error.duplicated", null));
		}

		// If error, just return a 400 bad request, along with the error message
		if (errors.hasErrors()) {
			return CommonUtil.getFieldErrors(errors);
		}

		cityService.save(cityDTO, false);

		return new ResponseEntity<>(CommonUtil.responseSuccessMessage("City registered"), HttpStatus.CREATED);
	}
	
	
	@PreAuthorize("hasAnyAuthority('SYSADMIN', 'OWNER')")
	@PutMapping
	public ResponseEntity<?> update(@Valid @RequestBody CityDTO cityDTO, Errors errors) throws IOException {

		// name duplicate
		if (cityService.isNameDuplicate(cityDTO.getId(),cityDTO.getCityName())) {
			errors.rejectValue("cityName", "error.name", CommonUtil.getLocalizeMessage("field.name", null) + " "
					+ CommonUtil.getLocalizeMessage("error.duplicated", null));
		}

		// If error, just return a 400 bad request, along with the error message
		if (errors.hasErrors()) {
			return CommonUtil.getFieldErrors(errors);
		}

		cityService.save(cityDTO, true);

		return new ResponseEntity<>(CommonUtil.responseString("City updated"), HttpStatus.OK);
	}

	
	@PreAuthorize("hasAnyAuthority('SYSADMIN', 'OWNER')")
	@GetMapping("/by-name/{cityName}")
	public ResponseEntity<List<CityDTO>> getByCityName(@PathVariable(required = true, name = "cityName") String cityName) {

		List<CityDTO> cities=cityService.getByCityName(cityName);
		return ResponseEntity.ok(cities);
	}

	@PreAuthorize("hasAnyAuthority('SYSADMIN', 'OWNER')")
	@RequestMapping(value = "/by-status/{status}", method = RequestMethod.GET)
	public ResponseEntity<?> getCityByStatus(@PathVariable(required = true, name = "status") Status status) {

		List<CityDTO> cityDTOList = cityService.getCityByStatus(status);

		return new ResponseEntity<>(cityDTOList, HttpStatus.OK);
	}
	
	
	@PreAuthorize("hasAnyAuthority('SYSADMIN', 'OWNER')")
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getCityId(@PathVariable(required = true, name = "id")Long id){
		CityDTO cityDTO=cityService.getById(id);
		return new ResponseEntity<>(cityDTO, HttpStatus.OK);
	}
	
	
	

}
