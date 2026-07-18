package com.cbk.trip.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.cbk.trip.dto.HotelDTO;
import com.cbk.trip.dto.PageableDTO;
import com.cbk.trip.entity.Destination;
import com.cbk.trip.entity.Hotel;
import com.cbk.trip.enums.Status;
import com.cbk.trip.repository.DestinationRepository;
import com.cbk.trip.repository.HotelRepository;
import com.cbk.trip.specification.HotelSpecs;
import com.cbk.trip.utils.CommonUtil;
import com.cbk.trip.utils.NginxUtil;

@Service
public class HotelService {

	@Autowired
	HotelRepository hotelRepository;

	@Autowired
	DestinationRepository destinationRepository;

	public PageableDTO getHotels(String name, Long destinationId, Status status, Pageable pageable) {
		Specification<Hotel> spec = HotelSpecs.getByFilter(name, destinationId, status);
		Page<Hotel> page = hotelRepository.findAll(spec, pageable);

		List<HotelDTO> dtoList = page.getContent().stream().map(HotelDTO::new).collect(Collectors.toList());

		return new PageableDTO(dtoList, page);
	}

	@Transactional(rollbackFor = Exception.class)
	public void save(HotelDTO dto, boolean isUpdate) throws IOException {
		Hotel hotel = new Hotel();
		if (isUpdate) {
			hotel = CommonUtil.checkValidById(dto.getId(), hotelRepository);
			hotel.setImageUrl(NginxUtil.updateImage(dto.getImage(), hotel.getImageUrl(), "hotel_image",
					StringUtils.isEmpty(dto.getImageUrl())));
		} else {
			hotel.setImageUrl(NginxUtil.saveImage(dto.getImage(), "hotel_image"));
		}

		hotel.setName(dto.getName());
		hotel.setAddress(dto.getAddress());
		hotel.setPricePerNight(dto.getPricePerNight());
		hotel.setDescription(dto.getDescription());
		hotel.setStatus(dto.getStatus());

		Destination destination = CommonUtil.checkValidById(dto.getDestinationId(), destinationRepository);
		hotel.setDestination(destination);
		hotelRepository.save(hotel);
	}

	public List<HotelDTO> getByStatus(Status status) {
		List<Hotel> list = hotelRepository.findByStatus(status);
		return list.stream().map(HotelDTO::new).collect(Collectors.toList());
	}

	public boolean isNameDuplicate(Long id, String name) {
		boolean isDuplicate = false;
		Optional<Hotel> nameExists = hotelRepository.findByName(name);
		if (nameExists.isPresent()) {
			if (id == null) {
				isDuplicate = true;
			} else {
				if (!nameExists.get().getId().equals(id)) {
					isDuplicate = true;
				}
			}
		}
		return isDuplicate;
	}

	public HotelDTO getById(Long id) {
		return new HotelDTO(CommonUtil.checkValidById(id, hotelRepository));
	}
}