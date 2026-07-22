package com.cbk.trip.controller;

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
import org.springframework.web.bind.annotation.RestController;

import com.cbk.trip.dto.PageableDTO;
import com.cbk.trip.dto.PaymentMethodDTO;
import com.cbk.trip.enums.Status;
import com.cbk.trip.service.PaymentMethodService;
import com.cbk.trip.utils.CommonUtil;

/**
 * @author HtetAungThan
 * @since 11/Jan/2025
 */
@RestController
@RequestMapping("/api/payment-method")
public class PaymentPublicController {

	@Autowired
	PaymentMethodService paymentMethodService;

	@GetMapping
	public ResponseEntity<?> getPaymentMethods(@Param("name") String name, @Param("accountNumber") String accountNumber,
			@Param("accountName") String accountName, @Param("status") Status status,
			@PageableDefault(size = 10, sort = "updatedDate") Pageable pageable) {

		PageableDTO result = paymentMethodService.getPaymentMethods(name, accountNumber, accountName, status, pageable);
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	
	@GetMapping("/{id}")
	public ResponseEntity<?> getById(@PathVariable(required = true, name = "id") Long id) {
		PaymentMethodDTO currencyDTO = paymentMethodService.getById(id);
		return new ResponseEntity<>(currencyDTO, HttpStatus.OK);
	}
}