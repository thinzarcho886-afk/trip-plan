package com.cbk.devconstruction.controller;

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

import com.cbk.devconstruction.dto.PageableDTO;
import com.cbk.devconstruction.dto.RoomDTO;
import com.cbk.devconstruction.enums.Gender;
import com.cbk.devconstruction.enums.RoomStatus;
import com.cbk.devconstruction.enums.Status;
import com.cbk.devconstruction.service.RoomService;
import com.cbk.devconstruction.utils.CommonUtil;

@RestController
@RequestMapping("/api/auth/room")
public class RoomController {
	
	@Autowired
	RoomService roomService;

	@PreAuthorize("hasAnyAuthority('SYSADMIN','OWNER')")
	@GetMapping
	public ResponseEntity<?> getRooms(@Param("roomNo") String roomNo, @Param("roomTypeName") String roomTypeName, @Param("roomStatus") RoomStatus roomStatus ,@Param("status") Status status, @Param("has") Boolean has, Gender gender, String hostelName,@Param("hostelId")Long hostelId, @Param("ownerName") String ownerName,@PageableDefault(size = Integer.MAX_VALUE) Pageable pageable) {

		PageableDTO  roomPageDTO =roomService.getRooms(roomNo, roomTypeName, roomStatus, status, has, gender, hostelName,hostelId, ownerName, pageable);

		return new ResponseEntity<>(roomPageDTO, HttpStatus.OK);
	}
	
	
	
	@PreAuthorize("hasAnyAuthority('SYSADMIN', 'OWNER')")
	@PostMapping
	public ResponseEntity<?> register(@Valid @RequestBody RoomDTO roomDTO, Errors errors) throws IOException {

		// room no duplicate
		if (roomService.isNameDuplicate(null,roomDTO.getRoomNo())) {
			errors.rejectValue("roomNo", "error.name", CommonUtil.getLocalizeMessage("field.name", null) + " "
					+ CommonUtil.getLocalizeMessage("error.duplicated", null));
		}

		// If error, just return a 400 bad request, along with the error message
		if (errors.hasErrors()) {
			return CommonUtil.getFieldErrors(errors);
		}

		roomService.save(roomDTO, false);

		return new ResponseEntity<>(CommonUtil.responseSuccessMessage("Room registered"), HttpStatus.CREATED);
	}
	
	
	@PreAuthorize("hasAnyAuthority('SYSADMIN', 'OWNER')")
	@PutMapping
	public ResponseEntity<?> update(@Valid @RequestBody RoomDTO roomDTO, Errors errors) throws IOException {

		// room no duplicate
		if (roomService.isNameDuplicate(roomDTO.getId(), roomDTO.getRoomNo())) {
			errors.rejectValue("roomNo", "error.name", CommonUtil.getLocalizeMessage("field.name", null) + " "
					+ CommonUtil.getLocalizeMessage("error.duplicated", null));
		}

		// If error, just return a 400 bad request, along with the error message
		if (errors.hasErrors()) {
			return CommonUtil.getFieldErrors(errors);
		}

		roomService.save(roomDTO, true);

		return new ResponseEntity<>(CommonUtil.responseString("Room updated"), HttpStatus.OK);
	}

	
	
	@PreAuthorize("hasAnyAuthority('SYSADMIN','OWNER')")
	@GetMapping("/{id}")
	public ResponseEntity<?> getById(@PathVariable(required = true, name = "id") Long id) {

		RoomDTO room=roomService.getById(id);
		return new ResponseEntity<>(room, HttpStatus.OK);
	}
	
	
	
	
	

	

	
	

}
