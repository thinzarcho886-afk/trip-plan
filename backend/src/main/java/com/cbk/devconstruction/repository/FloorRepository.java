package com.cbk.devconstruction.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.cbk.devconstruction.entity.Floor;
import com.cbk.devconstruction.enums.Status;

/**
 * 
 * @author HtetAungThan
 * @since 11/Jan/2025
 *
 */
@Repository
public interface FloorRepository extends JpaRepository<Floor, Long>, JpaSpecificationExecutor<Floor> {

	Optional<Floor> findByNameAndCompanyId(String name, Long companyId);

	Optional<List<Floor>> findByStatusAndCompanyId(Status status, Long companyId);

	Optional<List<Floor>> findByStatus(Status status);

}
