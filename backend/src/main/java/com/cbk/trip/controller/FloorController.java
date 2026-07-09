package com.cbk.trip.controller;

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

import com.cbk.trip.dto.FloorDTO;
import com.cbk.trip.dto.PageableDTO;
import com.cbk.trip.enums.Status;
import com.cbk.trip.service.FloorService;
import com.cbk.trip.utils.CommonUtil;

/**
 * 
 * @author HtetAungThan
 * @since 11/Jan/2025
 *
 */
@RestController
@PreAuthorize("hasAnyAuthority('SYSADMIN','ADMIN','OPERATION')")
@RequestMapping("/api/auth/floor")
public class FloorController {
	@Autowired
	FloorService floorService;

	@GetMapping
	public ResponseEntity<?> getFloors(@Param("name") String name, @Param("companyId") Long companyId,
			@Param("status") Status status,
			@PageableDefault(size = Integer.MAX_VALUE, sort = "updatedDate") Pageable pageable) {

		PageableDTO floorDTO = floorService.getFloors(name, companyId, status, pageable);

		return new ResponseEntity<>(floorDTO, HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<?> register(@Valid @RequestBody FloorDTO floorDTO, Errors errors) {

		// name duplicate
//		if (floorService.isNameCompanyDuplicate(floorDTO.getName(), floorDTO.getCompanyId(), null)) {
//			errors.rejectValue("name", "error.name", CommonUtil.getLocalizeMessage("field.name", null) + " "
//					+ CommonUtil.getLocalizeMessage("error.duplicated", null));
//		}

		// If error, just return a 400 bad request, along with the error message
		if (errors.hasErrors()) {
			return CommonUtil.getFieldErrors(errors);
		}

		FloorDTO respondDTO = floorService.save(floorDTO, false);

		return new ResponseEntity<>(respondDTO, HttpStatus.CREATED);
	}

	@PutMapping
	public ResponseEntity<?> update(@Valid @RequestBody FloorDTO floorDTO, Errors errors) {

		// name duplicate
//		if (floorService.isNameCompanyDuplicate(floorDTO.getName(), floorDTO.getCompanyId(), floorDTO.getId())) {
//			errors.rejectValue("name", "error.name", CommonUtil.getLocalizeMessage("field.name", null) + " "
//					+ CommonUtil.getLocalizeMessage("error.duplicated", null));
//		}

		// If error, just return a 400 bad request, along with the error message
		if (errors.hasErrors()) {
			return CommonUtil.getFieldErrors(errors);
		}

		floorService.save(floorDTO, true);

		return new ResponseEntity<>(CommonUtil.responseSuccessMessage("Floor updated"), HttpStatus.CREATED);
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getById(@PathVariable(required = true, name = "id") Long id) {

		FloorDTO currencyDTO = floorService.getById(id);

		return new ResponseEntity<>(currencyDTO, HttpStatus.OK);
	}

	/**
	 * 
	 * @param status
	 * @param companyId
	 * @return
	 */
	@RequestMapping(value = "/by-status/{status}", method = RequestMethod.GET)
	public ResponseEntity<?> getFloorByStatus(@PathVariable(required = true, name = "status") Status status,
			@Param("companyId") Long companyId) {

		List<FloorDTO> floorDTOList = floorService.getFloorByStatus(status, companyId);

		return new ResponseEntity<>(floorDTOList, HttpStatus.OK);
	}
}
