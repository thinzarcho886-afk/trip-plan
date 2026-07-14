package com.cbk.trip.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.cbk.trip.entity.Duration;
import com.cbk.trip.enums.Status;

@Repository
public interface DurationRepository extends JpaRepository<Duration, Long>, JpaSpecificationExecutor<Duration> {

    List<Duration> findByStatus(Status status);
    
    Optional<Duration> findByName(String name);
    
    boolean existsByName(String name);
    
    Optional<Duration> findById(Long id);

}