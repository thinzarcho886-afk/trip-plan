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
import com.cbk.devconstruction.dto.RoomTypeDTO;
import com.cbk.devconstruction.dto.StreetDTO;
import com.cbk.devconstruction.entity.City;
import com.cbk.devconstruction.entity.Company;
import com.cbk.devconstruction.entity.RoomType;
import com.cbk.devconstruction.entity.Street;
import com.cbk.devconstruction.entity.Township;
import com.cbk.devconstruction.enums.Status;
import com.cbk.devconstruction.repository.CityRepository;
import com.cbk.devconstruction.repository.StreetRepository;
import com.cbk.devconstruction.repository.TownshipRepository;
import com.cbk.devconstruction.specification.StreetSpecs;
import com.cbk.devconstruction.specification.TownshipSpecs;
import com.cbk.devconstruction.utils.CommonUtil;

@Service
public class StreetService {
	@Autowired
	TownshipRepository townshipRepository;
	
	@Autowired
	StreetRepository streetRepository;
	
	public PageableDTO getStreets(Long townshipId,String streetName,Status status,Boolean has,Pageable pageable) {
		Specification<Street> streetSpecs=StreetSpecs.getByFilter(townshipId, streetName,status,has);
		Page<Street> page=streetRepository.findAll(streetSpecs,pageable);
		List<Street> streetList=page.getContent();
		List<StreetDTO> dtoList=CommonUtil.getDTOList(streetList, StreetDTO::new);
		PageableDTO pageableDTO=new PageableDTO(dtoList,page);
		return pageableDTO;
		
		
	}
	
	@Transactional(rollbackFor = Exception.class)
	public StreetDTO save(@Valid StreetDTO dto, boolean isUpdate) {
		Street street=new Street();
		if(isUpdate)street=CommonUtil.checkValidById(dto.getId(), streetRepository);
		
		street.setStreetName(dto.getStreetName());
		street.setStatus(dto.getStatus());
		if(dto.getTownshipId() != null) {
			Township township=townshipRepository.findById(dto.getTownshipId()).orElseThrow(() -> new EntityNotFoundException("Township Not found with id:"+dto.getTownshipId()));
			street.setTownship(township);
			
		}else {
			street.setTownship(null);
		}
		streetRepository.save(street);

		StreetDTO saveStreet = new StreetDTO(street);

		return saveStreet;

	}
	
	public List<StreetDTO> getByStreetName(String streetName) {
		
		Specification<Street> streetSpecs=StreetSpecs.getByFilter(null,streetName,null,null);
		List<Street> streetList=streetRepository.findAll(streetSpecs);
		return CommonUtil.getDTOList(streetList, StreetDTO::new);
		
	
	}
	
	
	
	public List<StreetDTO> getStreetByStatus(Status status) {
		List<StreetDTO> streetDTOList=new ArrayList<>();
		Optional<List<Street>> streetOpt=null;
		streetOpt=streetRepository.findByStatus(status);
		if (streetOpt.isPresent()) {
			for (Street street : streetOpt.get()) {
				StreetDTO streetList = new StreetDTO(street);

				streetDTOList.add(streetList);
			}
		}

		return streetDTOList;
	
	}
	
	
	
	public List<StreetDTO> getByTownshipId(Long townshipId) {
		Specification<Street> streetSpecs=StreetSpecs.getByFilter(townshipId, null,null,null);
		List<Street> streetList=streetRepository.findAll(streetSpecs);
		return CommonUtil.getDTOList(streetList, StreetDTO::new);
		
		
		
	}
	
	
	public StreetDTO getById(Long id) {
		Street street = CommonUtil.checkValidById(id, streetRepository);


		StreetDTO streetDTO = new StreetDTO(street);

		return streetDTO;
	}
	
	
	public boolean isNameDuplicate(Long id,String name) {
		Optional<Street> streetExist = streetRepository.findByStreetName(name);
		if(streetExist.isPresent() && !streetExist.get().getId().equals(id)) {
			return true;
		}
		return false;
	}
	
	Street checkValidStreet(Long id) {
		return CommonUtil.checkValidById(id, streetRepository);
	}

	
	
}
