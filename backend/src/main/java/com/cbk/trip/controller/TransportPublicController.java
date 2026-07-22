package com.cbk.trip.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam; // 👈 @RequestParam ကို ပြောင်းလဲ import လုပ်ထားသည်
import org.springframework.web.bind.annotation.RestController;

import com.cbk.trip.entity.Transport;
import com.cbk.trip.service.TransportService;

@RestController
@RequestMapping("/api/transports")
public class TransportPublicController {

    @Autowired
    private TransportService transportService;

    // 📍 Query Parameters (e.g., /api/transports?busTypeId=1&busId=2)
    @GetMapping
    public ResponseEntity<?> getTransports(
            @RequestParam(name = "busTypeId", required = false) Long busTypeId,
            @RequestParam(name = "busId", required = false) Long busId,
            @PageableDefault(size = 10) Pageable pageable) {
        
        return new ResponseEntity<>(transportService.getTransports(busTypeId, busId, pageable), HttpStatus.OK);
    }
    
    // 📍 Path Variables (e.g., /api/transports/bus-type/1/bus/2)
    @GetMapping("/bus-type/{busTypeId}/bus/{busId}")
    public ResponseEntity<?> getByTransportId(
            @PathVariable(name = "busTypeId") Long busTypeId, 
            @PathVariable(name = "busId") Long busId) {
        
        Transport transport = transportService.getTransportId(busTypeId, busId);
        if (transport != null) {
            return new ResponseEntity<>(transport.getId(), HttpStatus.OK); 
        }
        
        return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Data မရှိပါက 404 Not Found ပြန်ပေးခြင်း
    }
}