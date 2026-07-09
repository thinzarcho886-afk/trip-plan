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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cbk.devconstruction.dto.PageableDTO;
import com.cbk.devconstruction.dto.ReviewsDTO;
import com.cbk.devconstruction.service.ReviewsService;
import com.cbk.devconstruction.utils.CommonUtil;

@RestController
@RequestMapping("/api/auth/reviews")
public class ReviewsController {
	
	@Autowired
	ReviewsService reviewsService;

	@PreAuthorize("hasAnyAuthority('SYSADMIN','OWNER')")
	@GetMapping
	public ResponseEntity<?> getReviews(@Param("studentName") String studentName, @PageableDefault(size = Integer.MAX_VALUE) Pageable pageable) {

		PageableDTO  reviewsPageDTO =reviewsService.getReviews(studentName, pageable);

		return new ResponseEntity<>(reviewsPageDTO, HttpStatus.OK);
	}
	
	
	
	@PreAuthorize("hasAnyAuthority('STUDENT')")
	@PostMapping
	public ResponseEntity<?> register(@Valid @RequestBody ReviewsDTO reviewsDTO, Errors errors) throws IOException {

		
		// If error, just return a 400 bad request, along with the error message
		if (errors.hasErrors()) {
			return CommonUtil.getFieldErrors(errors);
		}

		reviewsService.save(reviewsDTO);

		return new ResponseEntity<>(CommonUtil.responseSuccessMessage("Reviews registered"), HttpStatus.CREATED);
	}
	
	
	
	
	@PreAuthorize("hasAnyAuthority('SYSADMIN', 'OWNER')")
	@GetMapping("/by-student-name/{studentName}")
	public ResponseEntity<List<ReviewsDTO>> getByStudentName(@PathVariable(required = true, name = "studentName") String studentName) {

		List<ReviewsDTO> reviews=reviewsService.getByStudentName(studentName);
		return ResponseEntity.ok(reviews);
	}

	
	
	
	
	

	

	
	

}
