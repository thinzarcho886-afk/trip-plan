package com.cbk.trip.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.cbk.trip.entity.BusType;
import com.cbk.trip.enums.Status;

/**
 * @author HtetAungThan
 * @since 11/Jan/2025
 */
@Repository
public interface BusTypeRepository extends JpaRepository<BusType, Long>, JpaSpecificationExecutor<BusType> {

	Optional<List<BusType>> findByStatus(Status status);

	Optional<BusType> findByNameAndIdNot(String name, Long id);

}