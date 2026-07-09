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
@PreAuthorize("hasAnyAuthority('SYSADMIN','SALE_AND_ADMIN', 'ACCOUNTANT','ADMIN','DEVELOPER_ADMIN','OWNER')")
@RequestMapping("/api/auth/room_type")
public class RoomTypeController {
	@Autowired
	RoomTypeService roomTypeService;
	@GetMapping
	public ResponseEntity<?> getRoomTypes(@Param("name") String name,@Param("status") Status status,
			@PageableDefault(size = Integer.MAX_VALUE, sort = "updatedDate") Pageable pageable) {

		PageableDTO roomtypeDTO =roomTypeService.getRoomTypes(name, status,pageable);

		return new ResponseEntity<>(roomtypeDTO, HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<?> register(@Valid @RequestBody RoomTypeDTO roomtypeDTO, Errors errors) {

		// name duplicate
		if (roomTypeService.isNameRoomTypeDuplicate(roomtypeDTO.getName(), null)) {
			errors.rejectValue("name", "error.name", CommonUtil.getLocalizeMessage("field.name", null) + " "
					+ CommonUtil.getLocalizeMessage("error.duplicated", null));
		}

		// If error, just return a 400 bad request, along with the error message
		if (errors.hasErrors()) {
			return CommonUtil.getFieldErrors(errors);
		}

		RoomTypeDTO respondDTO = roomTypeService.save(roomtypeDTO, false);

		return new ResponseEntity<>(respondDTO, HttpStatus.CREATED);
	}

	@PutMapping
	public ResponseEntity<?> update(@Valid @RequestBody RoomTypeDTO roomtypeDTO, Errors errors) {

		// name duplicate
		if (roomTypeService.isNameRoomTypeDuplicate(roomtypeDTO.getName(), roomtypeDTO.getId())) {
			errors.rejectValue("name", "error.name", CommonUtil.getLocalizeMessage("field.name", null) + " "
					+ CommonUtil.getLocalizeMessage("error.duplicated", null));
		}

		// If error, just return a 400 bad request, along with the error message
		if (errors.hasErrors()) {
			return CommonUtil.getFieldErrors(errors);
		}

		roomTypeService.save(roomtypeDTO, true);

		return new ResponseEntity<>(CommonUtil.responseSuccessMessage("RoomType updated"), HttpStatus.CREATED);
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getById(@PathVariable(required = true, name = "id") Long id) {

		RoomTypeDTO currencyDTO = roomTypeService.getById(id);

		return new ResponseEntity<>(currencyDTO, HttpStatus.OK);
	}
	@PreAuthorize("hasAnyAuthority('SYSADMIN', 'OWNER')")
	  @GetMapping("/by-name/{name}")
	  public ResponseEntity<List<RoomTypeDTO>> getByName(@PathVariable(required = true, name = "name") String name) {

	    List<RoomTypeDTO> roomType=roomTypeService.getByName(name);
	    return ResponseEntity.ok(roomType);
	  }
	
	@RequestMapping(value = "/by-status/{status}", method = RequestMethod.GET)
	public ResponseEntity<?> getByStatus(@PathVariable(required = true, name = "status") Status status) {

		List<RoomTypeDTO> roomTypeDTOList = roomTypeService.getByStatus(status);

		return new ResponseEntity<>(roomTypeDTOList, HttpStatus.OK);
	}

	
}
