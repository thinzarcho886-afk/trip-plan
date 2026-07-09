package com.cbk.trip.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.cbk.trip.entity.Hostel;
import com.cbk.trip.enums.HostelStatus;
import com.cbk.trip.enums.Status;

@Repository
public interface HostelRepository extends JpaRepository<Hostel, Long>, JpaSpecificationExecutor<Hostel> {

	Optional<Hostel> findByHostelName(String hostelName);

	Optional<List<Hostel>> findByStatus(Status status);

	Optional<List<Hostel>> findByHostelStatus(HostelStatus hostelStatus);

	Optional<Hostel> findById(Long hostelId);

	Optional<Hostel> findByHostelNameAndId(String hostelName, Long hostelId);

}
