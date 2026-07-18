package com.cbk.trip.service;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.cbk.trip.dto.CustomerDTO;
import com.cbk.trip.dto.PageableDTO;
import com.cbk.trip.dto.UserDTO;
import com.cbk.trip.entity.Customer;
import com.cbk.trip.enums.Status;
import com.cbk.trip.enums.UserRole;
import com.cbk.trip.exception.BadRequestException;
import com.cbk.trip.repository.CustomerRepository;
import com.cbk.trip.specification.CustomerSpecs;
import com.cbk.trip.utils.CommonUtil;
import com.cbk.trip.utils.NginxUtil;

@Service
public class CustomerService {

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private UserService userService;

	public PageableDTO getCustomers(String name, String email, String phoneNumber, Status status, Pageable pageable) {
		Specification<Customer> specs = CustomerSpecs.getByFilter(name, email, phoneNumber, status);
		Page<Customer> page = customerRepository.findAll(specs, pageable);
		List<CustomerDTO> dtoList = CommonUtil.getDTOList(page.getContent(), CustomerDTO::new);
		return new PageableDTO(dtoList, page);
	}

	@Transactional(rollbackFor = Exception.class)
	public UserDTO save(@Valid CustomerDTO dto, boolean isUpdate) throws IOException {
		Customer customer = new Customer();
		if (isUpdate) {
			customer = CommonUtil.checkValidById(dto.getId(), customerRepository);
			customer.setProfileImageUrl(NginxUtil.updateImage(dto.getProfileImage(), customer.getProfileImageUrl(),
					"profile_image", StringUtils.isEmpty(dto.getProfileImageUrl())));
		} else {
			customer.setProfileImageUrl(NginxUtil.saveImage(dto.getProfileImage(), "profile_image"));
		}

		Customer emailExists = customerRepository.findByEmail(dto.getEmail());
		if (!Objects.isNull(emailExists)) {
			if (dto.getId() == null) {
				throw new BadRequestException("Email is already used.");
			} else {
				if (!emailExists.getId().equals(dto.getId())) {
					throw new BadRequestException("Email is already used.");
				}
			}
		}

		Customer nameExists = customerRepository.findByName(dto.getName());
		if (!Objects.isNull(nameExists)) {
			if (dto.getId() == null) {
				throw new BadRequestException("Customer name is already used.");
			} else {
				if (!emailExists.getId().equals(dto.getId())) {
					throw new BadRequestException("Customer name is already used.");
				}
			}
		}

		customer.setName(dto.getName());
		customer.setEmail(dto.getEmail());
		customer.setPhoneNumber(dto.getPhoneNumber());
		customer.setStatus(dto.getStatus());

		Customer savedCustomer = customerRepository.save(customer);

		UserDTO userDTO = new UserDTO();
		if (!isUpdate) {
			// User user = new User();
			userDTO.setUsername(savedCustomer.getName());
			userDTO.setPassword(dto.getPassword());
			userDTO.setConfirmPassword(dto.getPassword());
			userDTO.setRole(UserRole.CUSTOMER);
			userDTO.setCustomerId(savedCustomer.getId());
			userDTO.setStatus(Status.ACTIVE);
			userService.save(userDTO, false);
		}

		return userDTO;
	}

	public CustomerDTO getById(Long id) {
		Customer customer = customerRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
		return new CustomerDTO(customer);
	}

	public List<CustomerDTO> getByStatus(Status status) {
		return CommonUtil.getDTOList(customerRepository.findByStatus(status), CustomerDTO::new);
	}
}