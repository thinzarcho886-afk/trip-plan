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

import com.cbk.trip.dto.BookingDTO;
import com.cbk.trip.enums.BookingStatus;
import com.cbk.trip.enums.Status;
import com.cbk.trip.service.BookingService;
import com.cbk.trip.utils.CommonUtil;

@RestController
@RequestMapping("/api/booking")
public class BookingPublicController {

	@Autowired
	private BookingService bookingService;

    @GetMapping
    public ResponseEntity<?> getBookings(
            @Param("packageId") Long packageId,
            @Param("packageName") String packageName,
            @Param("customerId") Long customerId,
            @Param("customerName") String customerName,
            @Param("paymentMethodId") Long paymentMethodId,
            @Param("paymentMethodName") String paymentMethodName,
            @Param("status") BookingStatus status,
            @PageableDefault(size = 10) Pageable pageable) {
        
        return new ResponseEntity<>(bookingService.getBookings(packageId,packageName, customerId,customerName, paymentMethodId,paymentMethodName, status, pageable), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody BookingDTO bookingDTO, Errors errors) throws IOException{
        if (errors.hasErrors()) {
            return CommonUtil.getFieldErrors(errors);
        }
        
        bookingService.save(bookingDTO, false);
        return new ResponseEntity<>(CommonUtil.responseSuccessMessage("Booking created successfully"), HttpStatus.CREATED);
    }

	
    
    @GetMapping("/{customerId}")
    public ResponseEntity<List<BookingDTO>> getByCustomerId(@PathVariable Long customerId) {
        List<BookingDTO> historyList = bookingService.getByCustomerId(customerId);
        return ResponseEntity.ok(historyList);
    }
}