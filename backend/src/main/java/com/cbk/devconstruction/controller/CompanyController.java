package com.cbk.devconstruction.controller;

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

import com.cbk.devconstruction.dto.CompanyDTO;
import com.cbk.devconstruction.dto.PageableDTO;
import com.cbk.devconstruction.enums.Status;
import com.cbk.devconstruction.service.CompanyService;
import com.cbk.devconstruction.utils.CommonUtil;

@RestController
@RequestMapping(value = "/api/auth/company")
public class CompanyController {

	@Autowired
	CompanyService companyService;

	@PreAuthorize("hasAnyAuthority('SYSADMIN','ADMIN','OPERATION')")
	@GetMapping
	public ResponseEntity<?> getCompanies(@Param("name") String name, @Param("code") String code,
			@Param("status") Status status, @PageableDefault(size = Integer.MAX_VALUE) Pageable pageable) {

		PageableDTO companyPageDTO = companyService.getCompanies(name, code, status, pageable);

		return new ResponseEntity<>(companyPageDTO, HttpStatus.OK);
	}

	@PreAuthorize("hasAuthority('SYSADMIN')")
	@PostMapping
	public ResponseEntity<?> register(@Valid @RequestBody CompanyDTO companyDTO, Errors errors) throws IOException {

		// name duplicate
		if (companyService.isNameDuplicate(companyDTO.getName(), null)) {
			errors.rejectValue("name", "error.name", CommonUtil.getLocalizeMessage("field.name", null) + " "
					+ CommonUtil.getLocalizeMessage("error.duplicated", null));
		}

		// If error, just return a 400 bad request, along with the error message
		if (errors.hasErrors()) {
			return CommonUtil.getFieldErrors(errors);
		}

		companyService.save(companyDTO, false);

		return new ResponseEntity<>(CommonUtil.responseSuccessMessage("Company registered"), HttpStatus.CREATED);
	}

	@PreAuthorize("hasAnyAuthority('SYSADMIN','ADMIN','OPERATION')")
	@PutMapping
	public ResponseEntity<?> update(@Valid @RequestBody CompanyDTO companyDTO, Errors errors) throws IOException {

		// name duplicate
		if (companyService.isNameDuplicate(companyDTO.getName(), companyDTO.getId())) {
			errors.rejectValue("name", "error.name", CommonUtil.getLocalizeMessage("field.name", null) + " "
					+ CommonUtil.getLocalizeMessage("error.duplicated", null));
		}

		// If error, just return a 400 bad request, along with the error message
		if (errors.hasErrors()) {
			return CommonUtil.getFieldErrors(errors);
		}

		companyService.save(companyDTO, true);

		return new ResponseEntity<>(CommonUtil.responseString("Company updated"), HttpStatus.OK);
	}

	@PreAuthorize("hasAnyAuthority('SYSADMIN','ADMIN','OPERATION')")
	@GetMapping("/{id}")
	public ResponseEntity<?> getById(@PathVariable(required = true, name = "id") Long id) {

		CompanyDTO companyDTO = companyService.getById(id);

		return new ResponseEntity<>(companyDTO, HttpStatus.OK);
	}

}
