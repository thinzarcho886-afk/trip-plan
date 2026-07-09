package com.cbk.trip.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.cbk.trip.entity.Room;
import com.cbk.trip.enums.Status;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long>, JpaSpecificationExecutor<Room> {
	List<Room> findByHostelId(Long hostelId);

	List<Room> findByRoomTypeId(Long roomTypeId);

	Optional<Room> findByRoomNo(String roomNo);

	Optional<List<Room>> findByStatus(Status status);

	Optional<Room> findById(Long roomId);

	Optional<Room> findByRoomNoAndId(String roomNo, Long roomId);

}
