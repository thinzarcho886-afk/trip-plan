package com.cbk.trip.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cbk.trip.dto.BusDTO;
import com.cbk.trip.dto.PageableDTO;
import com.cbk.trip.entity.Bus;
import com.cbk.trip.enums.Status;
import com.cbk.trip.repository.BusRepository;
import com.cbk.trip.specification.BusSpecs;
import com.cbk.trip.utils.CommonUtil;

/**
 * @author HtetAungThan
 * @since 11/Jan/2025
 */
@Service
public class BusService {
	
	@Autowired
	BusRepository busRepository;

	public boolean isNameDuplicate(String name, Long id) {
		Optional<Bus> bus = busRepository.findByNameAndIdNot(name, id != null ? id : -1L);
		return bus.isPresent();
	}

	public PageableDTO getBuses(String name, Status status, Long busTypeId, Pageable pageable) {
		Page<Bus> page = busRepository.findAll(BusSpecs.getByFilter(name, status, busTypeId), pageable);
		List<BusDTO> busDTOList = page.getContent().stream().map(BusDTO::new).collect(Collectors.toList());
		return new PageableDTO(busDTOList, page);
	}

	@Transactional(rollbackFor = Exception.class)
	public BusDTO save(@Valid BusDTO busDTO, boolean isUpdate) {
		Bus bus = isUpdate ? CommonUtil.checkValidById(busDTO.getId(), busRepository) : new Bus();
		
		bus.setName(busDTO.getName());
		bus.setStatus(busDTO.getStatus());
		
		return new BusDTO(busRepository.save(bus));
	}

	public BusDTO getById(Long id) {
		Bus bus = CommonUtil.checkValidById(id, busRepository);
		return new BusDTO(bus);
	}

	public List<BusDTO> getBusByStatus(Status status) {
		Optional<List<Bus>> busListOpt = busRepository.findByStatus(status);
		return busListOpt.map(list -> list.stream().map(BusDTO::new).collect(Collectors.toList()))
				         .orElse(null);
	}
}