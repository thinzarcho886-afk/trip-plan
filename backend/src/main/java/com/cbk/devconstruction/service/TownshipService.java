package com.cbk.devconstruction.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.cbk.devconstruction.dto.TownshipDTO;
import com.cbk.devconstruction.dto.PageableDTO;
import com.cbk.devconstruction.dto.StreetDTO;
import com.cbk.devconstruction.entity.City;
import com.cbk.devconstruction.entity.Street;
import com.cbk.devconstruction.entity.Township;
import com.cbk.devconstruction.enums.Status;
import com.cbk.devconstruction.exception.BadRequestException;
import com.cbk.devconstruction.repository.CityRepository;
import com.cbk.devconstruction.repository.StreetRepository;
import com.cbk.devconstruction.repository.TownshipRepository;
import com.cbk.devconstruction.specification.TownshipSpecs;
import com.cbk.devconstruction.utils.CommonUtil;

@Service
public class TownshipService {
	@Autowired
	TownshipRepository townshipRepository;
	
	@Autowired
	CityRepository cityRepository;
	
	@Autowired
	StreetRepository streetRepository;
	
	public PageableDTO getTownships(Long cityId,String townshipName,Status status,Boolean has,Pageable pageable) {
		Specification<Township> townshipSpecs=TownshipSpecs.getByFilter(cityId,townshipName, status, has);
		Page<Township> page=townshipRepository.findAll(townshipSpecs,pageable);
		List<Township> townshipList=page.getContent();
		List<TownshipDTO> dtoList=CommonUtil.getDTOList(townshipList, TownshipDTO::new);
		PageableDTO pageableDTO=new PageableDTO(dtoList,page);
		return pageableDTO;
		
		
	}
	
	@Transactional(rollbackFor = Exception.class)
	  public TownshipDTO save(@Valid TownshipDTO dto, boolean isUpdate) {
 if (dto.getStreetDetailList() == null || dto.getStreetDetailList().isEmpty()) {
	          throw new BadRequestException("Please select at least one street.");
	      }

	        long distinctCount = dto.getStreetDetailList().stream()
	              .map(StreetDTO::getId)
	              .filter(id -> id != null)
	              .distinct()
	              .count();

	      long totalCount = dto.getStreetDetailList().stream()
	              .map(StreetDTO::getId)
	              .filter(id -> id != null)
	              .count();

	      if (distinctCount != totalCount) {
	          throw new BadRequestException("Duplicate streets are not allowed in the selection!");
	      }

	      Township township = new Township();
	      
	      if (isUpdate) {
	          township = CommonUtil.checkValidById(dto.getId(), townshipRepository);

	          if (dto.getDeleteStreetDetailIds() != null && dto.getDeleteStreetDetailIds().length > 0) {
	              for (Long streetId : dto.getDeleteStreetDetailIds()) {
	                  if (streetId != null) {
	                      streetRepository.findById(streetId).ifPresent(street -> {
	                          street.setTownship(null);
	                          streetRepository.save(street);
	                      });
	                  }
	              }
	          }

	        
	      }

	      township.setTownshipName(dto.getTownshipName());
	      township.setStatus(dto.getStatus());

	      if (dto.getCityId() != null) {
	          City city = cityRepository.findById(dto.getCityId())
	                  .orElseThrow(() -> new EntityNotFoundException("City Not found with id:" + dto.getCityId()));
	          township.setCity(city);
	      } else {
	          township.setCity(null);
	      }

	      Township saveTownships = townshipRepository.save(township);

	      for (StreetDTO streetDTO : dto.getStreetDetailList()) {
	          if (streetDTO.getId() == null) {
	              throw new BadRequestException("Street ID cannot be null");
	          }
	          Street existingStreet = CommonUtil.checkValidById(streetDTO.getId(), streetRepository);
	          existingStreet.setTownship(saveTownships);
	          streetRepository.save(existingStreet);
	      }

	      List<Street> updatedStreet = streetRepository.findByTownshipId(saveTownships.getId());
	      List<StreetDTO> streetDTOList = CommonUtil.getDTOList(updatedStreet, StreetDTO::new);

	      TownshipDTO saveTownship = new TownshipDTO(saveTownships);
	      saveTownship.setStreetDetailList(streetDTOList);

	      return saveTownship;
	  }
	
	public List<TownshipDTO> getByTownshipName(String townshipName) {
		
		Specification<Township> townshipSpecs=TownshipSpecs.getByFilter(null,townshipName, null, null);
		List<Township> townshipList=townshipRepository.findAll(townshipSpecs);
		return CommonUtil.getDTOList(townshipList, TownshipDTO::new);
		
	
	}
	
	
	public List<TownshipDTO> getTownshipByStatus(Status status) {
		List<TownshipDTO> townshipDTOList=new ArrayList<>();
		Optional<List<Township>> townshipOpt=null;
		townshipOpt=townshipRepository.findByStatus(status);
		if (townshipOpt.isPresent()) {
			for (Township township : townshipOpt.get()) {
				TownshipDTO townshipList = new TownshipDTO(township);

				townshipDTOList.add(townshipList);
			}
		}

		return townshipDTOList;
	
	}
	
	
	public List<TownshipDTO> getByCityId(Long cityId) {
		Specification<Township> townshipSpecs=TownshipSpecs.getByFilter(cityId, null,null,null);
		List<Township> townshipList=townshipRepository.findAll(townshipSpecs);
		return CommonUtil.getDTOList(townshipList, TownshipDTO::new);
		
		
		
	}
	
	
	public TownshipDTO getById(Long id) {
		Township township = CommonUtil.checkValidById(id, townshipRepository);


		TownshipDTO townshipDTO = new TownshipDTO(township);

		return townshipDTO;
	}
	
	
	
	public boolean isNameDuplicate(Long id,String name) {
		Optional<Township> townshipExist = townshipRepository.findByTownshipName(name);
		if(townshipExist.isPresent() && !townshipExist.get().getId().equals(id)) {
			return true;
		}
		return false;
	}

	public Township checkValidTownship(Long townshipId) {
		// TODO Auto-generated method stub
		return CommonUtil.checkValidById(townshipId, townshipRepository);
	}
	
}
