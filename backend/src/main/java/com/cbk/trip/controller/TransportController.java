package com.cbk.trip.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cbk.trip.dto.TransportDTO;
import com.cbk.trip.enums.Status;
import com.cbk.trip.repository.TransportRepository; // သင့် Repository path အတိုင်းပြင်ပါ
import com.cbk.trip.service.TransportService; // သင့် Service path အတိုင်းပြင်ပါ

@RestController
@RequestMapping("/api/auth/transports")
public class TransportController {

    @Autowired
    private TransportService transportService;
    @GetMapping
    public ResponseEntity<?> getTransports(
            @Param("busTypeId") Long busTypeId,
            @Param("busId") Long busId,
            @PageableDefault(size = 10) Pageable pageable) {
        
        return new ResponseEntity<>(transportService.getTransports(busTypeId, busId,pageable), HttpStatus.OK);
    }

  
}