package com.cbk.devconstruction.controller;
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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cbk.devconstruction.dto.RoomTypeDTO;
import com.cbk.devconstruction.enums.Status;
import com.cbk.devconstruction.dto.PageableDTO;
import com.cbk.devconstruction.dto.RoomDTO;
import com.cbk.devconstruction.service.RoomTypeService;
import com.cbk.devconstruction.utils.CommonUtil;
@RestController
@RequestMapping("/api/room_type")
public class RoomTypePublicController {
	
	@Autowired
	RoomTypeService roomTypeService;
	@GetMapping
	public ResponseEntity<?> getRoomTypes(@Param("name") String name,@Param("status") Status status,
			@PageableDefault(size = Integer.MAX_VALUE, sort = "updatedDate") Pageable pageable) {

		PageableDTO roomtypeDTO =roomTypeService.getRoomTypes(name, status,pageable);

		return new ResponseEntity<>(roomtypeDTO, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getById(@PathVariable(required = true, name = "id") Long id) {

		RoomTypeDTO currencyDTO = roomTypeService.getById(id);

		return new ResponseEntity<>(currencyDTO, HttpStatus.OK);
	}
	
	
}
