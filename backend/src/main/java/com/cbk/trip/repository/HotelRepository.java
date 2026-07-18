package com.cbk.trip.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.cbk.trip.entity.Hotel;
import com.cbk.trip.enums.Status;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long>, JpaSpecificationExecutor<Hotel> {
	Optional<Hotel> findByName(String name);

	List<Hotel> findByStatus(Status status);
}