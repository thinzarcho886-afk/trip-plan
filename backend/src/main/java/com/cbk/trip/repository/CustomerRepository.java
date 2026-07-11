package com.cbk.trip.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.cbk.trip.entity.Customer;
import com.cbk.trip.enums.Status;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>, JpaSpecificationExecutor<Customer> {

    List<Customer> findByStatus(Status status);
    Optional<Customer> findByEmail(String email);
    Optional<Customer> findById(Long id);
	boolean existsByEmail(String email);

}