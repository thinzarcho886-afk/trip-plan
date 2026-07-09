package com.cbk.devconstruction.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import com.cbk.devconstruction.entity.Student;
import com.cbk.devconstruction.enums.Status;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long>,JpaSpecificationExecutor<Student> {
	
	Optional<Student> findById(Long studentId);
	Optional<Student> findByName(String studentName);
	Optional<Student> findByNameAndId(String studentName,Long studentId);
	Optional<List<Student>> findByStatus(Status status);
	
	
}
