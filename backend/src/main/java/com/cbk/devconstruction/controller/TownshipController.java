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
import com.cbk.devconstruction.dto.TownshipDTO;
import com.cbk.devconstruction.enums.Status;
import com.cbk.devconstruction.service.TownshipService;
import com.cbk.devconstruction.utils.CommonUtil;

@RestController
@RequestMapping("/api/auth/township")
public class TownshipController {
	private static final long seralVersionUID=1L;
	
	@Autowired
	TownshipService townshipService;

	@PreAuthorize("hasAnyAuthority('SYSADMIN','OWNER')")
	@GetMapping
	public ResponseEntity<?> getTownships(@Param("cityId") Long cityId, @Param("townshipName") String townshipName,@Param("status") Status status,@Param("has") Boolean has, @PageableDefault(size = Integer.MAX_VALUE) Pageable pageable) {

		PageableDTO townshipPageDTO =townshipService.getTownships(cityId, townshipName, status, has, pageable);

		return new ResponseEntity<>(townshipPageDTO, HttpStatus.OK);
	}
	
	
	
	@PreAuthorize("hasAnyAuthority('SYSADMIN', 'OWNER')")
	@PostMapping
	public ResponseEntity<?> register(@Valid @RequestBody TownshipDTO townshipDTO, Errors errors) throws IOException {

		// name duplicate
		if (townshipService.isNameDuplicate(null,townshipDTO.getTownshipName())) {
			errors.rejectValue("townshipName", "error.name", CommonUtil.getLocalizeMessage("field.name", null) + " "
					+ CommonUtil.getLocalizeMessage("error.duplicated", null));
		}

		// If error, just return a 400 bad request, along with the error message
		if (errors.hasErrors()) {
			return CommonUtil.getFieldErrors(errors);
		}

		townshipService.save(townshipDTO, false);

		return new ResponseEntity<>(CommonUtil.responseSuccessMessage("Township registered"), HttpStatus.CREATED);
	}
	
	
	@PreAuthorize("hasAnyAuthority('SYSADMIN', 'OWNER')")
	@PutMapping
	public ResponseEntity<?> update(@Valid @RequestBody TownshipDTO townshipDTO, Errors errors) throws IOException {

		// name duplicate
		if (townshipService.isNameDuplicate(townshipDTO.getId(), townshipDTO.getTownshipName())) {
			errors.rejectValue("townshipName", "error.name", CommonUtil.getLocalizeMessage("field.name", null) + " "
					+ CommonUtil.getLocalizeMessage("error.duplicated", null));
		}

		// If error, just return a 400 bad request, along with the error message
		if (errors.hasErrors()) {
			return CommonUtil.getFieldErrors(errors);
		}

		townshipService.save(townshipDTO, true);

		return new ResponseEntity<>(CommonUtil.responseString("Township updated"), HttpStatus.OK);
	}

	
	@PreAuthorize("hasAnyAuthority('SYSADMIN', 'OWNER')")
	@GetMapping("/by-name/{townshipName}")
	public ResponseEntity<List<TownshipDTO>> getByTownshipName(@PathVariable(required = true, name = "townshipName") String townshipName) {

		List<TownshipDTO> townships=townshipService.getByTownshipName(townshipName);
		return ResponseEntity.ok(townships);
	}

	

	@PreAuthorize("hasAnyAuthority('SYSADMIN','OWNER')")
	@GetMapping("/by-city/{cityId}")
	public ResponseEntity<List<TownshipDTO>> getByCityId(@PathVariable(required = true, name = "cityId") Long cityId) {

		List<TownshipDTO> townships=townshipService.getByCityId(cityId);
		return ResponseEntity.ok(townships);
	}
	
	
	
	@PreAuthorize("hasAnyAuthority('SYSADMIN','OWNER')")
	@GetMapping("/{id}")
	public ResponseEntity<?> getById(@PathVariable(required = true, name = "id") Long id) {

		TownshipDTO township=townshipService.getById(id);
		return new ResponseEntity<>(township, HttpStatus.OK);
	}

	
	@PreAuthorize("hasAnyAuthority('SYSADMIN', 'OWNER')")
	@RequestMapping(value = "/by-status/{status}", method = RequestMethod.GET)
	public ResponseEntity<?> getTownshipByStatus(@PathVariable(required = true, name = "status") Status status) {

		List<TownshipDTO> townshipDTOList = townshipService.getTownshipByStatus(status);

		return new ResponseEntity<>(townshipDTOList, HttpStatus.OK);
	}
	
	

}
