package com.cbk.devconstruction.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.cbk.devconstruction.entity.Student;
import com.cbk.devconstruction.entity.User;
import com.cbk.devconstruction.enums.Status;
import com.cbk.devconstruction.enums.UserRole;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

	Optional<User> findByUsername(String username);

	Optional<List<User>> findByStatus(Status status);

	Optional<List<User>> findByRole(UserRole userRole);

	Optional<List<User>> findByOwnerName(String ownerName);

	Optional<List<User>> findByOwnerPhone(String phone);

	Optional<List<User>> findByOwnerAddress(String address);

	Optional<List<User>> findByStudentName(String studentName);

	Optional<List<User>> findByStudentPhone(String phone);

	Optional<List<User>> findByStudentAddress(String address);

	Student findByStudentId(Long studentId);

}
