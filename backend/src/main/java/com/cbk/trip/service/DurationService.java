package com.cbk.trip.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cbk.trip.dto.DurationDTO;
import com.cbk.trip.dto.PageableDTO;
import com.cbk.trip.entity.Duration;
import com.cbk.trip.enums.Status;
import com.cbk.trip.repository.DurationRepository;
import com.cbk.trip.specification.DurationSpecs;
import com.cbk.trip.utils.CommonUtil;

@Service
public class DurationService {

    @Autowired
    private DurationRepository durationRepository;

    public PageableDTO getDurations(String name, Status status, Pageable pageable) {
        Specification<Duration> specs = DurationSpecs.getByFilter(name, status);
        Page<Duration> page = durationRepository.findAll(specs, pageable);
        List<DurationDTO> dtoList = CommonUtil.getDTOList(page.getContent(), DurationDTO::new);
        return new PageableDTO(dtoList, page);
    }

    @Transactional(rollbackFor = Exception.class)
    public void save(DurationDTO dto, boolean isUpdate) {
        Duration duration;
        if (isUpdate) {
            duration = CommonUtil.checkValidById(dto.getId(), durationRepository);
        } else {
            duration = new Duration();
        }

        duration.setName(dto.getName());
        duration.setDescription(dto.getDescription());
        duration.setStatus(dto.getStatus());

        durationRepository.save(duration);
    }

    public DurationDTO getById(Long id) {
        Duration duration = durationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Duration not found with id: " + id));
        return new DurationDTO(duration);
    }

    public List<DurationDTO> getByStatus(Status status) {
        return CommonUtil.getDTOList(durationRepository.findByStatus(status), DurationDTO::new);
    }

    public boolean isNameDuplicate(String name, Long id) {
        return durationRepository.findByName(name)
                .map(duration -> !duration.getId().equals(id))
                .orElse(false);
    }
}