package com.cbk.trip.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import com.cbk.trip.dto.PageableDTO;
import com.cbk.trip.dto.TransportDTO;
import com.cbk.trip.entity.Transport;
import com.cbk.trip.enums.Status;
import com.cbk.trip.repository.TransportRepository;
import com.cbk.trip.specification.TransportSpecs;
import com.cbk.trip.utils.CommonUtil;

@Service
public class TransportService {
	 @Autowired
	   private TransportRepository transportRepository;
	 
	 public PageableDTO getTransports( Long busTypeId, Long busId,Pageable pageable) {
			Specification<Transport> spec = TransportSpecs.getByFilter(busTypeId, busId);
			Page<Transport> page = transportRepository.findAll(spec, pageable);
	        List<TransportDTO> dtoList = CommonUtil.getDTOList(page.getContent(), TransportDTO::new);
	        return new PageableDTO(dtoList, page);
			
		}
	 
	 public Transport getTransportId(Long busTypeId, Long busId) {
		    if (busTypeId == null || busId == null) {
		        return null;
		    }
		    
		    Transport transport = transportRepository.findByBusType_IdAndBus_Id(busTypeId, busId);
		    
		    return transport;
		}


}
