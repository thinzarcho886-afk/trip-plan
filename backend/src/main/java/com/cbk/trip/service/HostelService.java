package com.cbk.trip.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cbk.trip.dto.HostelDTO;
import com.cbk.trip.dto.PageableDTO;
import com.cbk.trip.dto.RoomDTO;
import com.cbk.trip.entity.Hostel;
import com.cbk.trip.entity.Room;
import com.cbk.trip.enums.HostelStatus;
import com.cbk.trip.enums.Status;
import com.cbk.trip.exception.BadRequestException;
import com.cbk.trip.repository.HostelRepository;
import com.cbk.trip.repository.RoomRepository;
import com.cbk.trip.repository.UserRepository;
import com.cbk.trip.specification.HostelSpecs;
import com.cbk.trip.utils.CommonUtil;
import com.cbk.trip.utils.NginxUtil;

@Service
public class HostelService {

	@Autowired
	HostelRepository hostelRepository;

	@Autowired
	UserService userService;

	@Autowired
	RoomRepository roomRepository;

	@Autowired
	UserRepository userRepository;

	public PageableDTO getHostels(String cityName, String townshipName, String streetName, String ownerName,
			String hostelName, HostelStatus hostelStatus, String currUserId, Status status, Pageable pageable) {

//		String currentUserId = CommonUtil.getUsernameFromAuthentication();
//		if (currentUserId != null) {
//			User currUser = userRepository.findByUsername(currentUserId)
//					.orElseThrow(() -> new ResourceNotFoundException("Not found user"));
//
////			boolean isOwner = currUser.getRole().equals(UserRole.OWNER);
////			if (isOwner) {
////				ownerName = currUser.getOwner().getName();
////			}
//		}

		Specification<Hostel> hostelSpecs = HostelSpecs.getByFilter(cityName, townshipName, streetName, ownerName,
				hostelName, hostelStatus, status, currUserId);
		Page<Hostel> page = hostelRepository.findAll(hostelSpecs, pageable);
		List<Hostel> hostelList = page.getContent();
		List<HostelDTO> dtoList = CommonUtil.getDTOList(hostelList, HostelDTO::new);

		PageableDTO pageableDTO = new PageableDTO(dtoList, page);

		return pageableDTO;

	}

	@Transactional(rollbackFor = Exception.class)
	public HostelDTO save(@Valid HostelDTO dto, boolean isUpdate) throws IOException {

		if (dto.getRoomList() == null && dto.getRoomList().isEmpty()) {
			throw new BadRequestException("Please select at least one room.");
		}

		long distinctCount = dto.getRoomList().stream().map(RoomDTO::getId).filter(id -> id != null).distinct().count();

		long totalCount = dto.getRoomList().stream().map(RoomDTO::getId).filter(id -> id != null).count();

		if (distinctCount != totalCount) {
			throw new BadRequestException("Duplicate rooms are not allowed in the selection!");
		}

		Hostel hostel = new Hostel();

		if (isUpdate) {
			hostel = CommonUtil.checkValidById(dto.getId(), hostelRepository);

			if (dto.getDeleteRoomDetailIds() != null && dto.getDeleteRoomDetailIds().length > 0) {
				for (Long roomId : dto.getDeleteRoomDetailIds()) {
					if (roomId != null) {
						roomRepository.findById(roomId).ifPresent(room -> {
							room.setHostel(null);
							roomRepository.save(room);
						});
					}
				}
			}

		}
		hostel.setUser(userService.checkValidUser(dto.getUserId()));

		if (dto.getHostelImageUrl() == null) {
			hostel.setHostelImage(null);
		}

		if (dto.getHostelImageUrl() != null && dto.getHostelImageUrl().startsWith("data:image")) {
			if (!isUpdate) {
				hostel.setHostelImage(NginxUtil.saveImage(dto.getHostelImageUrl(), "hostel_image"));
			} else {
				hostel.setHostelImage(NginxUtil.updateImage(dto.getHostelImageUrl(), hostel.getHostelImage(),
						"hostel_image", org.springframework.util.StringUtils.isEmpty(dto.getHostelImage())));
			}
		}

		hostel.setHostelName(dto.getHostelName());
		hostel.setDescription(dto.getDescription());
		hostel.setHostelStatus(dto.getHostelStatus());
		hostel.setStatus(dto.getStatus());
		hostel.setLatitude(dto.getLatitude());
		hostel.setLongitude(dto.getLongitude());

		Hostel saveHostel = hostelRepository.save(hostel);

		for (RoomDTO roomDTO : dto.getRoomList()) {
			if (roomDTO.getId() == null) {
				throw new BadRequestException("Room ID cannot be null");
			}
			Room existingRoom = CommonUtil.checkValidById(roomDTO.getId(), roomRepository);
			existingRoom.setHostel(saveHostel);
			roomRepository.save(existingRoom);
		}

		List<Room> updatedRooms = roomRepository.findByHostelId(saveHostel.getId());
		List<RoomDTO> roomDTOList = CommonUtil.getDTOList(updatedRooms, RoomDTO::new);

		HostelDTO saveHostels = new HostelDTO(saveHostel);
		saveHostels.setRoomList(roomDTOList);

		return saveHostels;
	}

	public HostelDTO getById(Long id) {
		Hostel hostel = CommonUtil.checkValidById(id, hostelRepository);

		HostelDTO hostelDTO = new HostelDTO(hostel);

		return hostelDTO;
	}

	public boolean isNameDuplicate(Long id, String name) {
		Optional<Hostel> hostelExist = hostelRepository.findByHostelName(name);
		if (hostelExist.isPresent() && !hostelExist.get().getId().equals(id)) {
			return true;
		}
		return false;
	}

	public Hostel checkValidHostel(Long id) {

		return CommonUtil.checkValidById(id, hostelRepository);

	}

}