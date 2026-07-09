package com.cbk.devconstruction.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.cbk.devconstruction.dto.CompanyDTO;
import com.cbk.devconstruction.dto.PageableDTO;
import com.cbk.devconstruction.entity.Company;

import com.cbk.devconstruction.entity.User;
import com.cbk.devconstruction.enums.SerializeType;
import com.cbk.devconstruction.enums.Status;
import com.cbk.devconstruction.repository.CompanyRepository;
import com.cbk.devconstruction.specification.CompanySpecs;
import com.cbk.devconstruction.utils.CommonUtil;
import com.cbk.devconstruction.utils.NginxUtil;

@Service
public class CompanyService {

	@Autowired
	CompanyRepository companyRepository;

	

	@Autowired
	UserService userService;

	/**
	 * Check if company with provided name already exist
	 * 
	 * @param name
	 * @param id   need to provided when update
	 * @return
	 */
	public boolean isNameDuplicate(String name, Long id) {
		Optional<Company> companyExist = companyRepository.findByName(name);
		return CommonUtil.checkDuplicate(companyExist, id);
	}

	public PageableDTO getCompanies(String name, String code, Status status, Pageable pageable) {

		// set company, if user has company
		User currentUser = userService.getCurrentUser();
//		if (currentUser.getCompany() != null) {
//			code = currentUser.getCompany().getCode();
//		}

		Specification<Company> companySpecs = CompanySpecs.getAllCompanies(code, name, status);
		Page<Company> page = companyRepository.findAll(companySpecs, pageable);

		List<Company> companyList = page.getContent();
		List<CompanyDTO> companyDTOList = CommonUtil.getDTOList(companyList, CompanyDTO::new);

		PageableDTO pageableDTO = new PageableDTO(companyDTOList, page);

		return pageableDTO;
	}

	@Transactional(rollbackFor = Exception.class)
	public void save(CompanyDTO companyDTO, boolean isUpdate) throws IOException {
		Company company = new Company();
		if (isUpdate) {
			company = checkValidCompany(companyDTO.getId());
			// check user's company
			//userService.checkUserCompany(companyDTO.getId());
		}

		company.setCode(companyDTO.getCode());
		company.setName(companyDTO.getName());
		company.setCompanyType(companyDTO.getCompanyType());
		company.setAddress(companyDTO.getAddress());
		company.setPhoneNo(companyDTO.getPhoneNo());
		company.setEmail(companyDTO.getEmail());
		company.setDescription(companyDTO.getDescription());
		company.setStatus(companyDTO.getStatus());
		if (!isUpdate) {
			company.setCompanyLetterHeadUrl(
					NginxUtil.saveImage(companyDTO.getCompanyLetterHeadImage(), "company_letter_head"));
		} else {
			company.setCompanyLetterHeadUrl(
					NginxUtil.updateImage(companyDTO.getCompanyLetterHeadImage(), company.getCompanyLetterHeadUrl(),
							"company_letter_head", StringUtils.isEmpty(companyDTO.getCompanyLetterHeadUrl())));
		}

		companyRepository.save(company);
	}

	public CompanyDTO getById(Long id) {
		Company company = null;

		// set company, if user has company
//		User currentUser = userService.getCurrentUser();
//		if (currentUser.getCompany() != null) {
//			company = currentUser.getCompany();
//		} else {
//			company = checkValidCompany(id);
//		}

		CompanyDTO companyDTO = new CompanyDTO(company);
		return companyDTO;
	}

	Company checkValidCompany(Long id) {
		return CommonUtil.checkValidById(id, companyRepository);
	}

	Optional<Company> getCompanyByName(String name) {
		return companyRepository.findByName(name);
	}

//	public Map<String, Integer> getCompanyActivity(String month) {
//		List<Object[]> objList = companyRepository.getCompanyActivity(month);
//		Integer total = 0, active = 0, inactive = 0;
//
//		for (Object[] obj : objList) {
//			total++;
//			if (((BigInteger) obj[1]).intValue() > 0)
//				active++;
//			else
//				inactive++;
//		}
//
//		Map<String, Integer> activity = new HashMap<>();
//		activity.put("total", total);
//		activity.put("active", active);
//		activity.put("inactive", inactive);
//
//		return activity;
//	}

}
