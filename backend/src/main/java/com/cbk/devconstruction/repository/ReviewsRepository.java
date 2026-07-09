package com.cbk.devconstruction.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import com.cbk.devconstruction.entity.Reviews;

@Repository
public interface ReviewsRepository extends JpaRepository<Reviews, Long>,JpaSpecificationExecutor<Reviews> {
	
	Optional<Reviews> findById(Long reviewId);
	Optional<Reviews> findByStudentName(String studentName);
	
	
	
}
