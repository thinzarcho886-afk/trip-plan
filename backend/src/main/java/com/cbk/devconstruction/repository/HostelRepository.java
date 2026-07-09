package com.cbk.devconstruction.repository;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.cbk.devconstruction.entity.Hostel;
import com.cbk.devconstruction.enums.Gender;
import com.cbk.devconstruction.enums.HostelStatus;
import com.cbk.devconstruction.enums.Status;

import java.util.List;

@Repository
public interface HostelRepository extends JpaRepository<Hostel, Long>, JpaSpecificationExecutor<Hostel>{
	List<Hostel> findByCityId(Long cityId);
	Optional<Hostel> findByHostelName(String hostelName);
	Optional<List<Hostel>> findByStatus(Status status);
	Optional<List<Hostel>> findByHostelStatus(HostelStatus hostelStatus);
	Optional<List<Hostel>> findByGender(Gender gender);

	
	Optional<Hostel> findById(Long hostelId);
	Optional<Hostel> findByHostelNameAndId(String hostelName,Long hostelId);
	

}
