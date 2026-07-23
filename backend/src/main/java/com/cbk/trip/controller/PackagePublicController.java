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
import org.springframework.web.bind.annotation.*;

import com.cbk.trip.dto.PackageDTO;
import com.cbk.trip.enums.Status;
import com.cbk.trip.service.PackageService;
import com.cbk.trip.utils.CommonUtil;

@RestController
@RequestMapping("/api/package")
public class PackagePublicController {

    @Autowired
    private PackageService packageService;

    @GetMapping
    public ResponseEntity<?> getPackages(
            @Param("name") String name,
            @Param("destinationId") Long destinationId,
            @Param("destinationName") String destinationName,
            @Param("durationId") Long durationId,
            @Param("durationName") String durationName,
            @Param("status") Status status,
            @PageableDefault(size = 10) Pageable pageable) {
        
        return new ResponseEntity<>(packageService.getPackages(name, destinationId,destinationName, durationId,durationName, status, pageable), HttpStatus.OK);
    }

    
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable(required = true, name = "id") Long id) {
        return new ResponseEntity<>(packageService.getById(id), HttpStatus.OK);
    }
  
}