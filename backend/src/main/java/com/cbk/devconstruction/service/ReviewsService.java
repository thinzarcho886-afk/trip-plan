package com.cbk.devconstruction.service;

import java.util.List;

import javax.validation.Valid;

import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.cbk.devconstruction.dto.PageableDTO;
import com.cbk.devconstruction.dto.ReviewsDTO;
import com.cbk.devconstruction.entity.Reviews;
import com.cbk.devconstruction.entity.Student;
import com.cbk.devconstruction.entity.User;
import com.cbk.devconstruction.exception.BadRequestException;
import com.cbk.devconstruction.exception.ResourceNotFoundException;
import com.cbk.devconstruction.repository.HostelRepository;
import com.cbk.devconstruction.repository.ReviewsRepository;
import com.cbk.devconstruction.repository.UserRepository;
import com.cbk.devconstruction.specification.ReviewsSpecs;
import com.cbk.devconstruction.utils.CommonUtil;

@Service
public class ReviewsService {
	
	@Autowired
	ReviewsRepository reviewsRepository;
	
	@Autowired
	UserRepository userRepository;
	
	
	@Autowired
	HostelRepository hostelRepository;
	
	
	@Autowired
	RoomTypeService roomTypeService;
	
	public PageableDTO getReviews(String studentName,Pageable pageable) {
		Specification<Reviews> reviewSpecs=ReviewsSpecs.getByFilter(studentName);
		Page<Reviews> page=reviewsRepository.findAll(reviewSpecs,pageable);
		List<Reviews> reviewList=page.getContent();
		List<ReviewsDTO> dtoList=CommonUtil.getDTOList(reviewList, ReviewsDTO::new);
		PageableDTO pageableDTO=new PageableDTO(dtoList,page);
		return pageableDTO;
		
		
	}
	
	@Transactional(rollbackFor = Exception.class)
	public ReviewsDTO save(@Valid ReviewsDTO dto) {
		Reviews reviews = new Reviews();
		
		String currentUserName=CommonUtil.getUsernameFromAuthentication();
		User currentUser=userRepository.findByUsername(currentUserName).orElseThrow(() -> new ResourceNotFoundException("Not found user"));
		
		Student currentStudent=currentUser.getStudent();
		
		if(currentStudent ==null) {
			throw new BadRequestException("Current user is not student");
		}
		
		reviews.setStudent(currentStudent);
		reviews.setReview(dto.getReview());
		
		
		
		
		reviewsRepository.save(reviews);

		ReviewsDTO saveReviews = new ReviewsDTO(reviews);

		return saveReviews;

	}
	
	public List<ReviewsDTO> getByStudentName(String studentName) {
		
		Specification<Reviews> reviewSpecs=ReviewsSpecs.getByFilter(studentName);
		List<Reviews> reviewsList=reviewsRepository.findAll(reviewSpecs);
		return CommonUtil.getDTOList(reviewsList,ReviewsDTO::new);
		
	
	}
	
	Reviews checkValidReviews(Long id) {
		return CommonUtil.checkValidById(id, reviewsRepository);
	}
	
}
