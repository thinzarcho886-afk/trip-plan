package com.cbk.trip.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.cbk.trip.dto.PageableDTO;
import com.cbk.trip.dto.RoomDTO;
import com.cbk.trip.entity.Hostel;
import com.cbk.trip.entity.Room;
import com.cbk.trip.entity.User;
import com.cbk.trip.enums.Status;
import com.cbk.trip.enums.UserRole;
import com.cbk.trip.exception.ResourceNotFoundException;
import com.cbk.trip.repository.HostelRepository;
import com.cbk.trip.repository.RoomRepository;
import com.cbk.trip.repository.RoomTypeRepository;
import com.cbk.trip.repository.UserRepository;
import com.cbk.trip.specification.RoomSpecs;
import com.cbk.trip.utils.CommonUtil;
import com.cbk.trip.utils.NginxUtil;

@Service
public class RoomService {

	@Autowired
	RoomTypeRepository roomTypeRepository;

	@Autowired
	RoomRepository roomRepository;

	@Autowired
	HostelRepository hostelRepository;

	@Autowired
	UserRepository userRepository;

	public PageableDTO getRooms(String roomNo, String roomTypeName, Status status, Boolean has, String hostelName,
			Long hostelId, String ownerName, Pageable pageable) {
		String currentUserId = CommonUtil.getUsernameFromAuthentication();
		if (currentUserId != null) {
			User currUser = userRepository.findByUsername(currentUserId)
					.orElseThrow(() -> new ResourceNotFoundException("Not found user"));

			boolean isOwner = currUser.getRole().equals(UserRole.OWNER);
			if (isOwner) {
//		          ownerName = currUser.getOwner().getName();
			}
		}

		Specification<Room> roomSpecs = RoomSpecs.getByFilter(roomNo, roomTypeName, status, has, hostelName, hostelId,
				ownerName);
		Page<Room> page = roomRepository.findAll(roomSpecs, pageable);
		List<Room> roomList = page.getContent();
		List<RoomDTO> dtoList = CommonUtil.getDTOList(roomList, RoomDTO::new);
		PageableDTO pageableDTO = new PageableDTO(dtoList, page);
		return pageableDTO;

	}

	@Transactional(rollbackFor = Exception.class)
	public RoomDTO save(@Valid RoomDTO dto, boolean isUpdate) throws IOException {
		Room room = new Room();
		if (isUpdate)
			room = CommonUtil.checkValidById(dto.getId(), roomRepository);

		if (dto.getHostelId() != null) {
			Hostel hostel = hostelRepository.findById(dto.getHostelId())
					.orElseThrow(() -> new EntityNotFoundException("Hostel Not found with id:" + dto.getHostelId()));
			room.setHostel(hostel);

		} else {
			room.setHostel(null);
		}
		if (dto.getRoomImageUrl() == null) {
			room.setRoomImage(null);
		}

		if (dto.getRoomImageUrl() != null && dto.getRoomImageUrl().startsWith("data:image")) {
			if (!isUpdate) {
				room.setRoomImage(NginxUtil.saveImage(dto.getRoomImageUrl(), "room_image"));
			} else {
				room.setRoomImage(NginxUtil.updateImage(dto.getRoomImageUrl(), room.getRoomImage(), "room_image",
						StringUtils.isEmpty(dto.getRoomImage())));
			}

		}
		room.setRoomNo(dto.getRoomNo());
		room.setCapacityPerRoom(dto.getCapacityPerRoom());
		room.setPrice(dto.getPrice());
		room.setAvailableUnit(dto.getAvailableUnit());
		room.setStatus(dto.getStatus());

		roomRepository.save(room);

		RoomDTO saveRoom = new RoomDTO(room);

		return saveRoom;

	}

	public RoomDTO getById(Long id) {
		Room room = CommonUtil.checkValidById(id, roomRepository);

		RoomDTO roomDTO = new RoomDTO(room);

		return roomDTO;
	}

	public boolean isNameDuplicate(Long id, String roomNo) {
		Optional<Room> roomExist = roomRepository.findByRoomNo(roomNo);
		if (roomExist.isPresent() && !roomExist.get().getId().equals(id)) {
			return true;
		}
		return false;
	}

	Room checkValidRoom(Long id) {
		return CommonUtil.checkValidById(id, roomRepository);
	}

}
