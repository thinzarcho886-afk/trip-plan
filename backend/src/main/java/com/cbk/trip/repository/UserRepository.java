package com.cbk.trip.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.cbk.trip.entity.User;
import com.cbk.trip.enums.Status;
import com.cbk.trip.enums.UserRole;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

	Optional<User> findByUsername(String username);

	Optional<List<User>> findByStatus(Status status);

	Optional<List<User>> findByRole(UserRole userRole);

}
