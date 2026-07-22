package com.cbk.trip.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.cbk.trip.dto.BusDTO;
import com.cbk.trip.dto.BusTypeDTO;
import com.cbk.trip.dto.PageableDTO;
import com.cbk.trip.entity.Bus;
import com.cbk.trip.entity.BusType;
import com.cbk.trip.entity.Transport;
import com.cbk.trip.enums.Status;
import com.cbk.trip.repository.BusRepository;
import com.cbk.trip.repository.BusTypeRepository;
import com.cbk.trip.repository.TransportRepository;
import com.cbk.trip.specification.BusTypeSpecs;
import com.cbk.trip.utils.CommonUtil;
import com.cbk.trip.utils.NginxUtil;

/**
 * @author HtetAungThan
 * @since 11/Jan/2025
 */
@Service
public class BusTypeService {
	@Autowired
	BusTypeRepository busTypeRepository;
	
	@Autowired
	BusRepository busRepository;
	
	@Autowired
	TransportRepository transportRepository;

	public boolean isNameDuplicate(String name, Long id) {
		Optional<BusType> busType = busTypeRepository.findByNameAndIdNot(name, id != null ? id : -1L);
		return busType.isPresent();
	}

	public PageableDTO getBusTypes(String name, Integer availableSeats, Status status, Pageable pageable) {
		Specification<BusType> busTypeSpecs = BusTypeSpecs.getByFilter(name, availableSeats, status);
		Page<BusType> page = busTypeRepository.findAll(busTypeSpecs, pageable);
		
		List<BusTypeDTO> busTypeDTOList = CommonUtil.getDTOList(page.getContent(), BusTypeDTO::new);
		return new PageableDTO(busTypeDTOList, page);
	}

	@Transactional(rollbackFor = Exception.class)
	public BusTypeDTO save(@Valid BusTypeDTO busTypeDTO, boolean isUpdate) throws IOException{
		BusType busType = isUpdate ? CommonUtil.checkValidById(busTypeDTO.getId(), busTypeRepository) : new BusType();

		busType.setName(busTypeDTO.getName());
		busType.setAvailableSeats(busTypeDTO.getAvailableSeats());
		busType.setDescription(busTypeDTO.getDescription());
		busType.setStatus(busTypeDTO.getStatus());
		 // Image Upload Logic
        if (StringUtils.isEmpty(busTypeDTO.getImageUrl())) {
        	busType.setImageUrl(null);
        } else if (busTypeDTO.getImageUrl().startsWith("data:image")) {
            // detail.getId() ပေါ်မူတည်ပြီး အသစ်သိမ်းမလား၊ အဟောင်းပေါ် update လုပ်မလား ခွဲခြားခြင်း
            if (busType.getId() == null) {
            	busType.setImageUrl(NginxUtil.saveImage(busTypeDTO.getImageUrl(), "busType"));
            } else {
            	busType.setImageUrl(NginxUtil.updateImage(busTypeDTO.getImageUrl(), busType.getImageUrl(), "busType", false));
            }
        }
		BusType savedBusType = busTypeRepository.save(busType);
		return new BusTypeDTO(savedBusType);
	}

	public BusTypeDTO getById(Long id) {
		BusType busType = CommonUtil.checkValidById(id, busTypeRepository);
		BusTypeDTO dto = new BusTypeDTO(busType);
		
		// Map Bus List from Transport repository to DTO
		List<Transport> transports = transportRepository.findByBusTypeId(id); // transportRepository တွင် findByBusTypeId ဆောက်ထားရန်လိုသည်
		List<BusDTO> busDTOList = new ArrayList<>();
		for (Transport transport : transports) {
			if (transport.getBus() != null) {
				busDTOList.add(new BusDTO(transport.getBus()));
			}
		}
		dto.setBuses(busDTOList);
		return dto;
	}

	public List<BusTypeDTO> getBusTypeByStatus(Status status) {
		List<BusTypeDTO> busTypeDTOList = new ArrayList<>();
		Optional<List<BusType>> busTypeListOpt = busTypeRepository.findByStatus(status);

		if (busTypeListOpt.isPresent()) {
			for (BusType busType : busTypeListOpt.get()) {
				busTypeDTOList.add(new BusTypeDTO(busType));
			}
		}
		return busTypeDTOList;
	}
	
	
	@Transactional(rollbackFor = Exception.class)
	public void addBusToTransport(Long busTypeId, Long busId) {
	    boolean exists = transportRepository.existsByBusTypeIdAndBusId(busTypeId, busId);
	    if (exists) {
	        throw new RuntimeException("This Bus is already added to this Bus Type.");
	    }
	    
	    BusType busType = busTypeRepository.findById(busTypeId)
	            .orElseThrow(() -> new RuntimeException("BusType not found with id: " + busTypeId));
	            
	    Bus bus = busRepository.findById(busId)
	            .orElseThrow(() -> new RuntimeException("Bus not found with id: " + busId));

	    Transport transport = new Transport();
	    transport.setBusType(busType);
	    transport.setBus(bus);
	    transportRepository.save(transport);
	}

	@Transactional(rollbackFor = Exception.class)
	public void removeBusFromTransport(Long busTypeId, Long busId) {
	    transportRepository.deleteByBusTypeIdAndBusId(busTypeId, busId);
	}
}