package com.cbk.trip.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cbk.trip.dto.HotelDTO;
import com.cbk.trip.dto.PageableDTO;
import com.cbk.trip.entity.Destination;
import com.cbk.trip.entity.Hotel;
import com.cbk.trip.enums.Status;
import com.cbk.trip.repository.DestinationRepository;
import com.cbk.trip.repository.HotelRepository;
import com.cbk.trip.specification.HotelSpecs;
import com.cbk.trip.utils.CommonUtil;

@Service
public class HotelService {

	@Autowired
	HotelRepository hotelRepository;

	@Autowired
	DestinationRepository destinationRepository;

	public PageableDTO getHotels(String name, Long destinationId, Status status, Pageable pageable) {
		Specification<Hotel> spec = HotelSpecs.getByFilter(name, destinationId, status);
		Page<Hotel> page = hotelRepository.findAll(spec, pageable);
		
		List<HotelDTO> dtoList = page.getContent().stream()
				.map(HotelDTO::new)
				.collect(Collectors.toList());

		return new PageableDTO(dtoList, page);
	}

	@Transactional(rollbackFor = Exception.class)
	public HotelDTO save(HotelDTO dto, boolean isUpdate) {
		Hotel hotel = isUpdate ? CommonUtil.checkValidById(dto.getId(), hotelRepository) : new Hotel();

		hotel.setName(dto.getName());
		hotel.setAddress(dto.getAddress());
		hotel.setImageUrl(dto.getImageUrl());
		hotel.setPricePerNight(dto.getPricePerNight());
		hotel.setDescription(dto.getDescription());
		hotel.setStatus(dto.getStatus());

		Destination destination = CommonUtil.checkValidById(dto.getDestinationId(), destinationRepository);
		hotel.setDestination(destination);

		return new HotelDTO(hotelRepository.save(hotel));
	}

	public List<HotelDTO> getByStatus(Status status) {
		List<Hotel> list = hotelRepository.findByStatus(status);
		return list.stream().map(HotelDTO::new).collect(Collectors.toList());
	}

	public boolean isNameDuplicate(Long id, String name) {
	    Long checkId = (id != null) ? id : -1L;
	    Optional<Hotel> exist = hotelRepository.findByNameAndIdNot(name, checkId);
	    return exist.isPresent();
	}

	public HotelDTO getById(Long id) {
		return new HotelDTO(CommonUtil.checkValidById(id, hotelRepository));
	}
}