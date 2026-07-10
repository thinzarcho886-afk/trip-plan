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

import com.cbk.trip.dto.DestinationDTO;
import com.cbk.trip.dto.PageableDTO;
import com.cbk.trip.entity.Destination;
import com.cbk.trip.enums.Status;
import com.cbk.trip.repository.DestinationRepository;
import com.cbk.trip.specification.DestinationSpecs;
import com.cbk.trip.utils.CommonUtil;

@Service
public class DestinationService {

    @Autowired
    DestinationRepository destinationRepository;

    public PageableDTO getDestinations(String name, Status status, Pageable pageable) {
        
        Specification<Destination> spec = DestinationSpecs.getByFilter(name, status);

        Page<Destination> page = destinationRepository.findAll(spec, pageable);
        List<DestinationDTO> dtoList = page.getContent().stream()
                                           .map(DestinationDTO::new)
                                           .collect(Collectors.toList());

        return new PageableDTO(dtoList, page);
    }

    public boolean isNameDuplicate(String name, Long id) {
        Optional<Destination> exist = destinationRepository.findByNameAndIdNot(name, id != null ? id : -1L);
        return exist.isPresent();
    }

    @Transactional
    public DestinationDTO save(DestinationDTO dto, boolean isUpdate) {
        Destination destination;
        if (isUpdate) {
            destination = CommonUtil.checkValidById(dto.getId(), destinationRepository);
        } else {
            destination = new Destination();
        }

        destination.setName(dto.getName());
        destination.setDescription(dto.getDescription());
        destination.setStatus(dto.getStatus());

        return new DestinationDTO(destinationRepository.save(destination));
    }

    public List<DestinationDTO> getByStatus(Status status) {
        List<Destination> list = destinationRepository.findByStatus(status);
        return list.stream().map(DestinationDTO::new).collect(Collectors.toList());
    }

    public DestinationDTO getById(Long id) {
        return new DestinationDTO(CommonUtil.checkValidById(id, destinationRepository));
    }
    
}