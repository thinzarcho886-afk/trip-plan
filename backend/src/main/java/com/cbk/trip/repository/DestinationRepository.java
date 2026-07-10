package com.cbk.trip.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.cbk.trip.entity.Destination;
import com.cbk.trip.enums.Status;

/**
 * * @author 
 * @since 
 *
 */
@Repository
public interface DestinationRepository extends JpaRepository<Destination, Long>, JpaSpecificationExecutor<Destination> {

    List<Destination> findByStatus(Status status);
    Optional<Destination> findByName(String name);
    Optional<Destination> findByNameAndIdNot(String name, Long id);
}