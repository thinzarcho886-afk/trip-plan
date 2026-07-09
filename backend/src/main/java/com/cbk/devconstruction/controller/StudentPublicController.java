package com.cbk.devconstruction.controller;

import java.util.List;

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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cbk.devconstruction.dto.PageableDTO;
import com.cbk.devconstruction.dto.TownshipDTO;
import com.cbk.devconstruction.dto.UserDTO;
import com.cbk.devconstruction.enums.Status;
import com.cbk.devconstruction.enums.UserRole;
import com.cbk.devconstruction.service.UserService;

@RestController
@RequestMapping("/api/student")
public class StudentPublicController {

	@Autowired
	private UserService userService;
	
	

	@GetMapping
	public ResponseEntity<?> getStudentPageable(Pageable pageable) {
		return ResponseEntity.ok(userService.getAllUser(null, null, null, UserRole.STUDENT, Status.ACTIVE, pageable));
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<UserDTO> getStudentById(@PathVariable(required = true, name = "id")Long id) {
		return ResponseEntity.ok(userService.getById(id));
	}
	
	@GetMapping("/{name}")
	public ResponseEntity<List<UserDTO>> getByStudentName(@PathVariable(required = true, name = "name") String studentName) {

		List<UserDTO> student=userService.getStudentByName(studentName);
		return ResponseEntity.ok(student);
	}
	
	
	@GetMapping("/{studentName}")
	public ResponseEntity<String> getByStudentEmail(@PathVariable(required = true, name = "studentName") String studentName) {
	    List<UserDTO> students = userService.getStudentByName(studentName);
	    
	    if (students != null && !students.isEmpty()) {
	        String studentEmail = students.get(0).getStudentEmail(); 
	        return ResponseEntity.ok(studentEmail);
	    }
	    
	    return ResponseEntity.notFound().build();
	}
	
	
	

	
}