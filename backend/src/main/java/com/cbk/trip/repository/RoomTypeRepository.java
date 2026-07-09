package com.cbk.trip.repository;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.cbk.trip.entity.RoomType;
import com.cbk.trip.enums.Status;
@Repository
public interface RoomTypeRepository extends JpaRepository<RoomType,Long>,JpaSpecificationExecutor<RoomType> {
	Optional<RoomType> findByName(String name);

	List<RoomType> findByStatus(Status status);
	
	
}
