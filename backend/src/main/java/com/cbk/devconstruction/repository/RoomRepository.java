package com.cbk.devconstruction.repository;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.cbk.devconstruction.entity.Room;
import com.cbk.devconstruction.enums.RoomStatus;
import com.cbk.devconstruction.enums.Status;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long>, JpaSpecificationExecutor<Room>{
	List<Room> findByHostelId(Long hostelId);
	List<Room> findByRoomTypeId(Long roomTypeId);

	
	Optional<Room> findByRoomNo(String roomNo);
	Optional<List<Room>> findByStatus(Status status);
	Optional<List<Room>> findByRoomStatus(RoomStatus roomStatus);

	
	Optional<Room> findById(Long roomId);
	Optional<Room> findByRoomNoAndId(String roomNo,Long roomId);
	

}
