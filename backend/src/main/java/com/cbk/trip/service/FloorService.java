package com.cbk.trip.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cbk.trip.dto.FloorDTO;
import com.cbk.trip.dto.PageableDTO;
import com.cbk.trip.entity.Floor;
import com.cbk.trip.enums.Status;
import com.cbk.trip.repository.FloorRepository;
import com.cbk.trip.specification.FloorSpecs;
import com.cbk.trip.utils.CommonUtil;

/**
 * 
 * @author HtetAungThan
 * @since 11/Jan/2025
 *
 */
@Service
public class FloorService {
	@Autowired
	FloorRepository floorRepository;

	@Autowired
	MessageSource messageSource;

	@Autowired
	UserService userService;

	/**
	 * Check if Product with provided name-company combination already exist
	 * 
	 * @param name
	 * @param companyId
	 * @param id        need to provided when update
	 * @return
	 */
//	public boolean isNameCompanyDuplicate(String name, Long companyId, Long id) {
//		Optional<Floor> floorList = floorRepository.findByNameAndCompanyId(name, companyId);
//		return CommonUtil.checkDuplicate(floorList, id);
//	}

	/**
	 * 
	 * @param id
	 * @return
	 * @throws CustomWebServiceException
	 */
	Floor checkValidFloor(Long id, Long companyId) {
		Floor floor = CommonUtil.checkValidById(id, floorRepository);
		// validate Floor_company
//		if (!floor.getCompany().getId().equals(companyId)) {
//			throw new BadRequestException(CommonUtil.getLocalizeMessage("floor", null) + " "
//					+ CommonUtil.getLocalizeMessage("error.invalid", null));
//		}
		return floor;
	}

	/**
	 * 
	 * @param name
	 * @param companyId
	 * @param status
	 * @param pageable
	 * @return
	 */
	public PageableDTO getFloors(String name, Long roomTypeId, Status status, Pageable pageable) {

		// validate user's company
		// userService.checkUserCompany(companyId);

		Specification<Floor> floorSpecs = FloorSpecs.getByFilter(name, roomTypeId, status);
		Page<Floor> page = floorRepository.findAll(floorSpecs, pageable);
		List<Floor> floorList = page.getContent();

		List<FloorDTO> floorDTOList = CommonUtil.getDTOList(floorList, FloorDTO::new);

		PageableDTO pageableDTO = new PageableDTO(floorDTOList, page);

		return pageableDTO;
	}

	@Transactional(rollbackFor = Exception.class)
	public FloorDTO save(@Valid FloorDTO floorDTO, boolean isUpdate) {

		Floor floor = new Floor();

		if (isUpdate)
			floor = CommonUtil.checkValidById(floorDTO.getId(), floorRepository);

		// validate user's company
//		userService.checkUserCompany(floorDTO.getCompanyId());
//		if (isUpdate)
//			userService.checkUserCompany(floor.getCompany().getId());

		floor.setName(floorDTO.getName());
		floor.setDescription(floorDTO.getDescription());
		floor.setStatus(floorDTO.getStatus());

		Floor cu = floorRepository.save(floor);

		FloorDTO saveFloor = new FloorDTO(cu);

		return saveFloor;
	}

	public FloorDTO getById(Long id) {
		Floor floor = CommonUtil.checkValidById(id, floorRepository);

		// validate user's company
		// userService.checkUserCompany(floor.getCompany().getId());

		FloorDTO floorDTO = new FloorDTO(floor);

		return floorDTO;
	}

	/**
	 * 
	 * @param status
	 * @param companyId
	 * @return
	 */
	public List<FloorDTO> getFloorByStatus(Status status, Long companyId) {
		List<FloorDTO> floorDTOList = new ArrayList<>();
		Optional<List<Floor>> floorListOpt = null;

		if (!Objects.isNull(companyId)) {
//			floorListOpt = floorRepository.findByStatusAndCompanyId(status, companyId);
		} else {
			floorListOpt = floorRepository.findByStatus(status);
		}

		if (floorListOpt.isPresent()) {
			for (Floor floor : floorListOpt.get()) {
				FloorDTO floorDTO = new FloorDTO(floor);

				floorDTOList.add(floorDTO);
			}
		}

		return floorDTOList;
	}
}
