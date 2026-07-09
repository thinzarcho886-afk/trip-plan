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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cbk.devconstruction.dto.PageableDTO;
import com.cbk.devconstruction.dto.HostelDTO;

import com.cbk.devconstruction.dto.SearchHistoryDTO;
import com.cbk.devconstruction.enums.Gender;
import com.cbk.devconstruction.enums.HostelStatus;
import com.cbk.devconstruction.service.SearchHistoryService;

@RestController
@RequestMapping("/api/search_history")
public class SearchHistoryPublicController {

    @Autowired
    private SearchHistoryService searchHistoryService;
    
    @GetMapping("/filter")
    public ResponseEntity<?> getMostSearchedHostelsByFilter(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate, 
            @RequestParam(required = false) String cityName, 
            @RequestParam(required = false) String townshipName, 
            @RequestParam(required = false) String streetName, 
            @RequestParam(required = false) String ownerName, 
            @RequestParam(required = false) String hostelName,
            @RequestParam(required = false) HostelStatus hostelStatus,
            @RequestParam(required = false) Gender gender,
            @PageableDefault(size = 10) Pageable pageable) {

        PageableDTO popularPageDTO = searchHistoryService.getMostSearchedHostelsByFilter(
                fromDate, toDate, cityName, townshipName, streetName, ownerName, hostelName, hostelStatus, gender, pageable);
        
        return new ResponseEntity<>(popularPageDTO, HttpStatus.OK);
    }
    
    @GetMapping
	public ResponseEntity<?> gethistorys(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
		    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate, @Param("cityName") String cityName, @Param("townshipName") String townshipName, @Param("streetName") String streetName, @Param("ownerName") String ownerName, @Param("hostelName") String hostelName,@Param("hostelStatus") HostelStatus hostelStatus,@Param("gender")Gender gender,@PageableDefault(size = Integer.MAX_VALUE) Pageable pageable) {

		PageableDTO  historyPageDTO =searchHistoryService.getHistorys(fromDate, toDate, cityName, townshipName,streetName, ownerName, hostelName, hostelStatus,gender, pageable);
		
		return new ResponseEntity<>(historyPageDTO, HttpStatus.OK);
	}

    @PostMapping
    public ResponseEntity<?> saveSearchHistory(@RequestBody SearchHistoryDTO dto) {
        boolean isAlreadySearchedToday = searchHistoryService.existsByUserIdAndHostelIdAndDatetime(
            dto.getUserId(), 
            dto.getHostelId(), 
            LocalDate.now()
        );
        
        if (!isAlreadySearchedToday) {
            dto.setDatetime(LocalDate.now());
            searchHistoryService.save(dto);
            return ResponseEntity.ok("Search history saved successfully.");
        }
        
        return ResponseEntity.ok("Already searched today. Skipped saving.");
    }
}