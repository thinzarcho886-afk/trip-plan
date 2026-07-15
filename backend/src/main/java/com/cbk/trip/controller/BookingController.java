package com.cbk.trip.controller;

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

import com.cbk.trip.dto.BookingDTO;
import com.cbk.trip.enums.Status;
import com.cbk.trip.service.BookingService;
import com.cbk.trip.utils.CommonUtil;

@RestController
@RequestMapping("/api/booking")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    // List View နှင့် Filter လုပ်ဆောင်ချက်
    @GetMapping
    public ResponseEntity<?> getBookings(
            @Param("packageId") Long packageId,
            @Param("customerId") Long customerId,
            @Param("paymentMethodId") Long paymentMethodId,
            @Param("status") Status status,
            @PageableDefault(size = 10) Pageable pageable) {
        
        return new ResponseEntity<>(bookingService.getBookings(packageId, customerId, paymentMethodId, status, pageable), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody BookingDTO bookingDTO, Errors errors) {
        if (errors.hasErrors()) {
            return CommonUtil.getFieldErrors(errors);
        }
        
        bookingService.save(bookingDTO, false);
        return new ResponseEntity<>(CommonUtil.responseSuccessMessage("Booking created successfully"), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping
    public ResponseEntity<?> update(@Valid @RequestBody BookingDTO bookingDTO, Errors errors) {
        if (errors.hasErrors()) {
            return CommonUtil.getFieldErrors(errors);
        }
        
        bookingService.save(bookingDTO, true);
        return new ResponseEntity<>(CommonUtil.responseString("Booking updated successfully"), HttpStatus.OK);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable(required = true, name = "id") Long id) {
        return new ResponseEntity<>(bookingService.getById(id), HttpStatus.OK);
    }
}