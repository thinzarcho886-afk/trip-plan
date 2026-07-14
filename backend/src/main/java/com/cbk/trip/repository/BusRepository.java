package com.cbk.trip.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.cbk.trip.entity.Bus;
import com.cbk.trip.enums.Status;

/**
 * * @author HtetAungThan
 * @since 11/Jan/2025
 *
 */
@Repository
public interface BusRepository extends JpaRepository<Bus, Long>, JpaSpecificationExecutor<Bus> {

	Optional<List<Bus>> findByStatus(Status status);

	Optional<Bus> findByNameAndIdNot(String name, Long id);

}