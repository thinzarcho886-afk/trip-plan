package com.cbk.devconstruction.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import com.cbk.devconstruction.entity.Owner;
import com.cbk.devconstruction.enums.Status;

@Repository
public interface OwnerRepository extends JpaRepository<Owner, Long>,JpaSpecificationExecutor<Owner> {
	
	Optional<Owner> findById(Long ownerId);
	Optional<Owner> findByName(String ownerName);
	Optional<Owner> findByNameAndId(String ownerName,Long ownerId);
	Optional<List<Owner>> findByStatus(Status status);
	
	
}
