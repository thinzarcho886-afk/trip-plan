package com.cbk.devconstruction.service;




import com.cbk.devconstruction.repository.StreetRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;

import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.cbk.devconstruction.dto.CityDTO;
import com.cbk.devconstruction.dto.PageableDTO;
import com.cbk.devconstruction.dto.TownshipDTO;
import com.cbk.devconstruction.entity.City;
import com.cbk.devconstruction.entity.Township;
import com.cbk.devconstruction.enums.Status;
import com.cbk.devconstruction.exception.BadRequestException;
import com.cbk.devconstruction.repository.CityRepository;
import com.cbk.devconstruction.repository.TownshipRepository;
import com.cbk.devconstruction.specification.CitySpecs;
import com.cbk.devconstruction.utils.CommonUtil;

@Service
public class CityService{
	
	
	@Autowired
	StreetRepository streetRepository;



	@Autowired
	CityRepository cityRepository;
	
	
	
	@Autowired
	TownshipRepository townshipRepository;

	
	
	public PageableDTO getCities(String cityName,Status status,Pageable pageable) {
		Specification<City> citySpecs=CitySpecs.getByFilter(cityName, status);
		Page<City> page = cityRepository.findAll(citySpecs, pageable);
		List<City> cityList = page.getContent();
		List<CityDTO> cityDTOList=CommonUtil.getDTOList(cityList,CityDTO::new);
		
		PageableDTO pageableDTO = new PageableDTO(cityDTOList, page);

		return pageableDTO;

		
	}
	
	@Transactional(rollbackFor = Exception.class)
	public CityDTO save(@Valid CityDTO cityDTO, boolean isUpdate) {
	    
	  
	    if (cityDTO.getTownshipDetailList() == null || cityDTO.getTownshipDetailList().isEmpty()) {
	        throw new BadRequestException("Please select at least one township.");
	    }
	    long distinctCount = cityDTO.getTownshipDetailList().stream()
	            .map(TownshipDTO::getId)
	            .filter(id -> id != null)
	            .distinct()
	            .count();

	    long totalCount = cityDTO.getTownshipDetailList().stream()
	            .map(TownshipDTO::getId)
	            .filter(id -> id != null)
	            .count();

	    if (distinctCount != totalCount) {
	        throw new BadRequestException("Duplicate townships are not allowed in the selection!");
	    }

	    City city = new City();
	    
	    if (isUpdate) {
	        city = CommonUtil.checkValidById(cityDTO.getId(), cityRepository);

	        if (cityDTO.getDeleteTownshipDetailIds() != null && cityDTO.getDeleteTownshipDetailIds().length > 0) {
	            for (Long townshipId : cityDTO.getDeleteTownshipDetailIds()) {
	                if (townshipId != null) {
	                    if (!streetRepository.findByTownshipId(townshipId).isEmpty()) {
	                        throw new BadRequestException("Deleted Township is already used in street Registration!");
	                    }
	                    
	                    townshipRepository.findById(townshipId).ifPresent(township -> {
	                        township.setCity(null); 
	                        townshipRepository.save(township);
	                    });
	                }
	            }
	        }

	        List<Township> oldTownships = townshipRepository.findByCityId(city.getId());
	        if (oldTownships != null) {
	            for (Township oldTownship : oldTownships) {
	                boolean isStillPresent = cityDTO.getTownshipDetailList().stream()
	                        .anyMatch(newTownship -> newTownship.getId() != null && newTownship.getId().equals(oldTownship.getId()));
	                
	                if (!isStillPresent) {
	                    oldTownship.setCity(null);
	                    townshipRepository.save(oldTownship);
	                }
	            }
	        }
	    }

	    city.setCityName(cityDTO.getCityName());
	    city.setStatus(cityDTO.getStatus());
	    City saveCity = cityRepository.save(city);

	    for (TownshipDTO townshipDTO : cityDTO.getTownshipDetailList()) {
	        if (townshipDTO.getId() == null) {
	            throw new BadRequestException("Township ID cannot be null");
	        }
	        Township existingTownship = CommonUtil.checkValidById(townshipDTO.getId(), townshipRepository);
	        existingTownship.setCity(saveCity);
	        townshipRepository.save(existingTownship);
	    }

	    List<Township> updatedTownships = townshipRepository.findByCityId(saveCity.getId());
	    List<TownshipDTO> townshipDTOList = CommonUtil.getDTOList(updatedTownships, TownshipDTO::new);

	    CityDTO saveCityDTO = new CityDTO(saveCity);
	    saveCityDTO.setTownshipDetailList(townshipDTOList);
	    
	    return saveCityDTO;
	}
	
	public List<CityDTO> getByCityName(String cityName) {
		
		Specification<City> citySpecs=CitySpecs.getByFilter(cityName,null);
		List<City> cityList=cityRepository.findAll(citySpecs);
		return CommonUtil.getDTOList(cityList, CityDTO::new);
		
	
	}
	
	
	
	public boolean isNameDuplicate(Long id, String name) {
		Optional<City> cityExist = cityRepository.findByCityName(name);
		if(cityExist.isPresent() && !cityExist.get().getId().equals(id)) {
			return true;
		}
		return false;
	}
	
	public CityDTO getById(Long id) {
		City city=CommonUtil.checkValidById(id, cityRepository);
		return new CityDTO(city);
	}

	public List<CityDTO> getCityByStatus(Status status) {
		List<CityDTO> cityDTOList=new ArrayList<>();
		Optional<List<City>> cityOpt=null;
		cityOpt=cityRepository.findByStatus(status);
		if (cityOpt.isPresent()) {
			for (City city : cityOpt.get()) {
				CityDTO cityList = new CityDTO(city);

				cityDTOList.add(cityList);
			}
		}

		return cityDTOList;
	
	}

	public City checkValidCity(Long cityId) {
		// TODO Auto-generated method stub
		return CommonUtil.checkValidById(cityId, cityRepository);
	}
	
	
	
	

}
