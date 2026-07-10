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
import org.springframework.web.bind.annotation.*;

import com.cbk.trip.dto.DestinationDTO;
import com.cbk.trip.dto.PageableDTO;
import com.cbk.trip.dto.PaymentMethodDTO;
import com.cbk.trip.enums.Status;
import com.cbk.trip.service.PaymentMethodService;
import com.cbk.trip.utils.CommonUtil;

/**
 * * @author HtetAungThan
 * @since 11/Jan/2025
 *
 */
@RestController
@RequestMapping("/api/auth/payment-method")
public class PaymentMethodController {

	@Autowired
	PaymentMethodService paymentMethodService;

	@PreAuthorize("hasAnyAuthority('ADMIN','CUSTOMER')")
	@GetMapping
	public ResponseEntity<?> getPaymentMethods(@Param("name") String name, 
			@Param("accountNumber") String accountNumber,
			@Param("accountName") String accountName,
			@Param("status") Status status,
			@PageableDefault(size = 10, sort = "updatedDate") Pageable pageable) {

		PageableDTO result = paymentMethodService.getPaymentMethods(name, accountNumber, accountName, status, pageable);
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@PostMapping
	public ResponseEntity<?> register(@Valid @RequestBody PaymentMethodDTO dto, Errors errors) {

		if (paymentMethodService.isAccountNumberDuplicate(dto.getAccountNumber(), null)) {
			errors.rejectValue("accountNumber", "error.duplicate", "Account number is already duplicated.");
		}

		if (errors.hasErrors()) {
			return CommonUtil.getFieldErrors(errors);
		}

		return new ResponseEntity<>(paymentMethodService.save(dto, false), HttpStatus.CREATED);
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@PutMapping
	public ResponseEntity<?> update(@Valid @RequestBody PaymentMethodDTO dto, Errors errors) {

		if (paymentMethodService.isAccountNumberDuplicate(dto.getAccountNumber(), dto.getId())) {
			errors.rejectValue("accountNumber", "error.duplicate", "Account number is already duplicated.");
		}

		if (errors.hasErrors()) {
			return CommonUtil.getFieldErrors(errors);
		}

		paymentMethodService.save(dto, true);
		return new ResponseEntity<>(CommonUtil.responseSuccessMessage("Payment Method updated"), HttpStatus.OK);
	}

	@PreAuthorize("hasAnyAuthority('ADMIN','CUSTOMER')")
	@GetMapping("/by-status/{status}")
	public ResponseEntity<?> getByStatus(@PathVariable Status status) {
		List<PaymentMethodDTO> list = paymentMethodService.getByStatus(status);
		return new ResponseEntity<>(list, HttpStatus.OK);
	}
	@GetMapping("/{id}")
	public ResponseEntity<?> getById(@PathVariable(required = true, name = "id") Long id) {
		PaymentMethodDTO currencyDTO =paymentMethodService.getById(id);
		return new ResponseEntity<>(currencyDTO, HttpStatus.OK);
	}
}