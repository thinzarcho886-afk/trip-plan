package com.cbk.trip.controller;

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

import com.cbk.trip.dto.DurationDTO;
import com.cbk.trip.enums.Status;
import com.cbk.trip.service.DurationService;
import com.cbk.trip.utils.CommonUtil;

@RestController
@RequestMapping("/api/auth/duration")
public class DurationController {

    @Autowired
    private DurationService durationService;

    @GetMapping
    public ResponseEntity<?> getDurations(
            @Param("name") String name,
            @Param("status") Status status,
            @PageableDefault(size = 10) Pageable pageable) {
        
        return new ResponseEntity<>(durationService.getDurations(name, status, pageable), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('SYSADMIN')")
    @PostMapping
    public ResponseEntity<?> register(@Valid @RequestBody DurationDTO durationDTO, Errors errors) throws IOException {
        if (errors.hasErrors()) {
            return CommonUtil.getFieldErrors(errors);
        }
        
        durationService.save(durationDTO, false);
        return new ResponseEntity<>(CommonUtil.responseSuccessMessage("Duration registered successfully"), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyAuthority('SYSADMIN')")
    @PutMapping
    public ResponseEntity<?> update(@Valid @RequestBody DurationDTO durationDTO, Errors errors) throws IOException {
        if (errors.hasErrors()) {
            return CommonUtil.getFieldErrors(errors);
        }
        
        durationService.save(durationDTO, true);
        return new ResponseEntity<>(CommonUtil.responseString("Duration updated successfully"), HttpStatus.OK);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable(required = true, name = "id") Long id) {
        
        DurationDTO durationDTO = durationService.getById(id);
        
        return new ResponseEntity<>(durationDTO, HttpStatus.OK);
    }

    @GetMapping("/status")
    public ResponseEntity<?> getByStatus(@Param("status") Status status) {
        return new ResponseEntity<>(durationService.getByStatus(status), HttpStatus.OK);
    }
}