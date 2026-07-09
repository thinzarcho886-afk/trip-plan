package com.cbk.devconstruction.repository;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.cbk.devconstruction.entity.Street;
import com.cbk.devconstruction.enums.Status;

import java.util.List;

@Repository
public interface StreetRepository  extends JpaRepository<Street, Long>, JpaSpecificationExecutor<Street>{
	
	List<Street> findByTownshipId(Long townshipId);
	Optional<Street> findByStreetName(String streetName);
	Optional<List<Street>> findByStatus(Status status);

}
