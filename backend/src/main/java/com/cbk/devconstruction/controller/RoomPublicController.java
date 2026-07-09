package com.cbk.devconstruction.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cbk.devconstruction.dto.PageableDTO;
import com.cbk.devconstruction.dto.RoomDTO;
import com.cbk.devconstruction.enums.Gender;
import com.cbk.devconstruction.enums.RoomStatus;
import com.cbk.devconstruction.enums.Status;
import com.cbk.devconstruction.service.RoomService;

@RestController
@RequestMapping("/api/room")
public class RoomPublicController {
	
	@Autowired
	RoomService roomService;

	@GetMapping
	public ResponseEntity<?> getRooms(@Param("roomNo") String roomNo, @Param("roomTypeName") String roomTypeName, @Param("roomStatus") RoomStatus roomStatus ,@Param("status") Status status, Boolean has,Gender gender, @Param("hostelName") String hostelName,@Param("hostelId") Long hostelId,@Param("ownerName") String ownerName, @PageableDefault(size = Integer.MAX_VALUE) Pageable pageable) {

		PageableDTO  roomPageDTO =roomService.getRooms(roomNo, roomTypeName, roomStatus, status, has,gender, hostelName,hostelId, ownerName, pageable);

		return new ResponseEntity<>(roomPageDTO, HttpStatus.OK);
	}
	
	
	
	
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getById(@PathVariable(required = true, name = "id") Long id) {

		RoomDTO room=roomService.getById(id);
		return new ResponseEntity<>(room, HttpStatus.OK);
	}
	
	
	
	

	

	
	

}
