package com.cbk.trip.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cbk.trip.entity.Room;
import com.cbk.trip.entity.Transport;

/**
 * @author HtetAungThan
 * @since 11/Jan/2025
 */
@Repository
public interface TransportRepository extends JpaRepository<Transport, Long>, JpaSpecificationExecutor<Transport> {
	
	List<Transport> findByBusTypeId(Long busTypeId);
	boolean existsByBusTypeIdAndBusId(Long busTypeId, Long busId);
    
	@Modifying
	@Transactional 
	@Query("DELETE FROM Transport t WHERE t.busType.id = :busTypeId AND t.bus.id = :busId")
	void deleteByBusTypeIdAndBusId(@Param("busTypeId") Long busTypeId, @Param("busId") Long busId);
}