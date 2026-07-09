package com.cbk.devconstruction.controller;

import java.time.LocalDate;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cbk.devconstruction.dto.PageableDTO;
import com.cbk.devconstruction.dto.SearchHistoryDTO;
import com.cbk.devconstruction.entity.SearchHistory;
import com.cbk.devconstruction.enums.Gender;
import com.cbk.devconstruction.enums.HostelStatus;
import com.cbk.devconstruction.service.SearchHistoryService;

@RestController
@RequestMapping("/api/auth/search_history")
public class SearchHistoryController {
	
	@Autowired
	SearchHistoryService historyService;
	
	
	@PreAuthorize("hasAnyAuthority('SYSADMIN','OWNER')")
	@GetMapping
	public ResponseEntity<?> gethistorys(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
		    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate, @Param("cityName") String cityName, @Param("townshipName") String townshipName, @Param("streetName") String streetName, @Param("ownerName") String ownerName, @Param("hostelName") String hostelName,@Param("hostelStatus") HostelStatus hostelStatus,@Param("gender")Gender gender,@PageableDefault(size = Integer.MAX_VALUE) Pageable pageable) {

		PageableDTO  historyPageDTO =historyService.getHistorys(fromDate, toDate, cityName, townshipName,streetName, ownerName, hostelName, hostelStatus,gender, pageable);
		
		return new ResponseEntity<>(historyPageDTO, HttpStatus.OK);
	}
	
	

	
	
	
	
	@PreAuthorize("hasAnyAuthority('SYSADMIN','OWNER')")
	@GetMapping("/{id}")
	public ResponseEntity<?> getById(@PathVariable(required = true, name = "id") Long id) {

		SearchHistory history=historyService.getById(id);
		return new ResponseEntity<>(history, HttpStatus.OK);
	}

	
	
	
	
	
	
	

	

	
	

}
