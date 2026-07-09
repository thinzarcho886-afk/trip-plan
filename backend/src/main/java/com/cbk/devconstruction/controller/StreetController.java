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

import com.cbk.devconstruction.dto.PageableDTO;
import com.cbk.devconstruction.dto.RoomTypeDTO;
import com.cbk.devconstruction.dto.StreetDTO;
import com.cbk.devconstruction.dto.TownshipDTO;
import com.cbk.devconstruction.enums.Status;
import com.cbk.devconstruction.service.StreetService;
import com.cbk.devconstruction.utils.CommonUtil;

@RestController
@RequestMapping("/api/auth/street")
public class StreetController {
	
	@Autowired
	StreetService streetService;

	@PreAuthorize("hasAnyAuthority('SYSADMIN','OWNER')")
	@GetMapping
	public ResponseEntity<?> getStreets(@Param("townshipId") Long townshipId, @Param("streetName") String streetName,@Param("status") Status status, Boolean has,@PageableDefault(size = Integer.MAX_VALUE) Pageable pageable) {

		PageableDTO streetPageDTO =streetService.getStreets(townshipId, streetName, status, has, pageable);

		return new ResponseEntity<>(streetPageDTO, HttpStatus.OK);
	}
	
	
	
	@PreAuthorize("hasAnyAuthority('SYSADMIN', 'OWNER')")
	@PostMapping
	public ResponseEntity<?> register(@Valid @RequestBody StreetDTO streetDTO, Errors errors) throws IOException {

		// name duplicate
		if (streetService.isNameDuplicate(null,streetDTO.getStreetName())) {
			errors.rejectValue("streetName", "error.name", CommonUtil.getLocalizeMessage("field.name", null) + " "
					+ CommonUtil.getLocalizeMessage("error.duplicated", null));
		}

		// If error, just return a 400 bad request, along with the error message
		if (errors.hasErrors()) {
			return CommonUtil.getFieldErrors(errors);
		}

		streetService.save(streetDTO, false);

		return new ResponseEntity<>(CommonUtil.responseSuccessMessage("Street registered"), HttpStatus.CREATED);
	}
	
	
	@PreAuthorize("hasAnyAuthority('SYSADMIN', 'OWNER')")
	@PutMapping
	public ResponseEntity<?> update(@Valid @RequestBody StreetDTO streetDTO, Errors errors) throws IOException {

		// name duplicate
		if (streetService.isNameDuplicate(streetDTO.getId(), streetDTO.getStreetName())) {
			errors.rejectValue("streetName", "error.name", CommonUtil.getLocalizeMessage("field.name", null) + " "
					+ CommonUtil.getLocalizeMessage("error.duplicated", null));
		}

		// If error, just return a 400 bad request, along with the error message
		if (errors.hasErrors()) {
			return CommonUtil.getFieldErrors(errors);
		}

		streetService.save(streetDTO, true);

		return new ResponseEntity<>(CommonUtil.responseString("Street updated"), HttpStatus.OK);
	}

	
	@PreAuthorize("hasAnyAuthority('SYSADMIN', 'OWNER')")
	@GetMapping("/by-name/{streetName}")
	public ResponseEntity<List<StreetDTO>> getByStreetName(@PathVariable(required = true, name = "streetName") String streetName) {

		List<StreetDTO> streets=streetService.getByStreetName(streetName);
		return ResponseEntity.ok(streets);
	}


	@PreAuthorize("hasAnyAuthority('SYSADMIN','OWNER')")
	@GetMapping("/by-township/{townshipId}")
	public ResponseEntity<List<StreetDTO>> getByTownshipId(@PathVariable(required = true, name = "townshipId") Long townshipId) {

		List<StreetDTO> streets=streetService.getByTownshipId(townshipId);
		return ResponseEntity.ok(streets);
	}
	
	@PreAuthorize("hasAnyAuthority('SYSADMIN','OWNER')")
	@GetMapping("/{id}")
	public ResponseEntity<?> getById(@PathVariable(required = true, name = "id") Long id) {

		StreetDTO streetDTO = streetService.getById(id);

		return new ResponseEntity<>(streetDTO, HttpStatus.OK);
	}

	
	@PreAuthorize("hasAnyAuthority('SYSADMIN', 'OWNER')")
	@RequestMapping(value = "/by-status/{status}", method = RequestMethod.GET)
	public ResponseEntity<?> getStreetByStatus(@PathVariable(required = true, name = "status") Status status) {

		List<StreetDTO> streetDTOList = streetService.getStreetByStatus(status);

		return new ResponseEntity<>(streetDTOList, HttpStatus.OK);
	}
	
	

}
