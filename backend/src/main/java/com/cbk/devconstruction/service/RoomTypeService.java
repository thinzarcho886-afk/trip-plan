package com.cbk.devconstruction.service;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import javax.validation.Valid;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import com.cbk.devconstruction.dto.ExcelData;
import com.cbk.devconstruction.dto.FloorDTO;
import com.cbk.devconstruction.dto.RoomTypeDTO;
import com.cbk.devconstruction.dto.PageableDTO;
import com.cbk.devconstruction.dto.ReviewsDTO;
import com.cbk.devconstruction.entity.City;
import com.cbk.devconstruction.entity.Floor;
import com.cbk.devconstruction.entity.Reviews;
import com.cbk.devconstruction.dto.RoomDTO;
import com.cbk.devconstruction.entity.City;
import com.cbk.devconstruction.entity.Floor;
import com.cbk.devconstruction.entity.Room;
import com.cbk.devconstruction.entity.RoomType;
import com.cbk.devconstruction.enums.Status;
import com.cbk.devconstruction.exception.BadRequestException;
import com.cbk.devconstruction.repository.RoomTypeRepository;
import com.cbk.devconstruction.specification.ReviewsSpecs;
import com.cbk.devconstruction.specification.RoomSpecs;
import com.cbk.devconstruction.specification.RoomTypeSpecs;
import com.cbk.devconstruction.utils.CommonUtil;
import com.cbk.devconstruction.utils.ExcelUtils;
@Service
public  class RoomTypeService{
	@Autowired
	RoomTypeRepository roomTypeRepository;
	public boolean isNameRoomTypeDuplicate(String name, Long id) {
		Optional<RoomType>roomTypeList = roomTypeRepository.findByName(name);
		return CommonUtil.checkDuplicate(roomTypeList, id);
	}
	
	public PageableDTO getRoomTypes(String name,Status status, Pageable pageable) {

		Specification<RoomType> roomTypeSpecs =RoomTypeSpecs.getByFilter(name, status);
		Page<RoomType> page =roomTypeRepository.findAll(roomTypeSpecs, pageable);
		List<RoomType> roomTypeList = page.getContent();

		List<RoomTypeDTO> roomTypeDTOList = CommonUtil.getDTOList(roomTypeList, RoomTypeDTO::new);

		PageableDTO pageableDTO = new PageableDTO(roomTypeDTOList, page);

		return pageableDTO;
	}

	@Transactional(rollbackFor = Exception.class)
	public RoomTypeDTO save(@Valid RoomTypeDTO roomTypeDTO, boolean isUpdate) {

		RoomType  roomType = new RoomType();

		if (isUpdate)
			roomType = CommonUtil.checkValidById(roomTypeDTO.getId(), roomTypeRepository);
	
		roomType.setName(roomTypeDTO.getName());
		roomType.setStatus(roomTypeDTO.getStatus());
		roomTypeRepository.save(roomType);
		RoomTypeDTO saveRoomType = new RoomTypeDTO(roomType);
		return saveRoomType; 
	}
	public RoomTypeDTO getById(Long id) {
		RoomType roomType = CommonUtil.checkValidById(id, roomTypeRepository);
		return new RoomTypeDTO(roomType);
	}
	public List<RoomTypeDTO> getByName(String name) {
	    
	    Specification<RoomType> roomTypeSpecs=RoomTypeSpecs.getByFilter(name,null);
	    List<RoomType> reviewsList=roomTypeRepository.findAll(roomTypeSpecs);
	    return CommonUtil.getDTOList(reviewsList,RoomTypeDTO::new);
	    
	  
	  }
public List<RoomTypeDTO> getByStatus(Status status) {
		
		Specification<RoomType> roomTypeSpecs=RoomTypeSpecs.getByFilter(null,status);
		List<RoomType> reviewsList=roomTypeRepository.findAll(roomTypeSpecs);
		return CommonUtil.getDTOList(reviewsList,RoomTypeDTO::new);
		
	}
	
	public RoomType checkValidRoomType(Long roomTypeId) {
		// TODO Auto-generated method stub
		return CommonUtil.checkValidById(roomTypeId, roomTypeRepository);
	}


}

