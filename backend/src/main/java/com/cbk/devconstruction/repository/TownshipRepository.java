package com.cbk.devconstruction.repository;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.cbk.devconstruction.entity.Township;
import com.cbk.devconstruction.enums.Status;

import java.util.List;

@Repository
public interface TownshipRepository extends JpaRepository<Township, Long>, JpaSpecificationExecutor<Township>{
	List<Township> findByCityId(Long cityId);
	Optional<Township> findByTownshipName(String townshipName);
	Optional<List<Township>> findByStatus(Status status);
	
	Optional<Township> findById(Long townshipId);
	Optional<Township> findByTownshipNameAndId(String townshipName,Long townshipId);

}
