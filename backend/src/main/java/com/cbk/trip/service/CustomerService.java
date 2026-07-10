package com.cbk.trip.service;

import java.io.IOException;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cbk.trip.dto.CustomerDTO;
import com.cbk.trip.dto.PageableDTO;
import com.cbk.trip.entity.Customer;
import com.cbk.trip.entity.User;
import com.cbk.trip.enums.Status;
import com.cbk.trip.enums.UserRole;
import com.cbk.trip.repository.CustomerRepository;
import com.cbk.trip.repository.UserRepository;
import com.cbk.trip.specification.CustomerSpecs;
import com.cbk.trip.utils.CommonUtil;
import com.cbk.trip.utils.NginxUtil;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private UserRepository userRepository;

    public PageableDTO getCustomers(String name, String email, String phoneNumber, Status status, Pageable pageable) {
        Specification<Customer> specs = CustomerSpecs.getByFilter(name, email, phoneNumber, status);
        Page<Customer> page = customerRepository.findAll(specs, pageable);
        List<CustomerDTO> dtoList = CommonUtil.getDTOList(page.getContent(), CustomerDTO::new);
        return new PageableDTO(dtoList, page);
    }

    @Transactional(rollbackFor = Exception.class)
    public void save(@Valid CustomerDTO dto, boolean isUpdate) throws IOException {
        Customer customer;
        if (isUpdate) {
            customer = CommonUtil.checkValidById(dto.getId(), customerRepository);
        } else {
            customer = new Customer();
        }

        customer.setName(dto.getName());
        customer.setEmail(dto.getEmail());
        customer.setPhoneNumber(dto.getPhoneNumber());
        customer.setStatus(dto.getStatus());

        if (dto.getProfileImageUrl() != null && dto.getProfileImageUrl().startsWith("data:image")) {
            if (!isUpdate) {
                customer.setProfileImage(NginxUtil.saveImage(dto.getProfileImageUrl(), "customer_profile"));
            } else {
                customer.setProfileImage(NginxUtil.updateImage(dto.getProfileImageUrl(), customer.getProfileImage(), "customer_profile", false));
            }
        }

        Customer savedCustomer = customerRepository.save(customer);

        if (!isUpdate) {
        	User user = new User();
            user.setUsername(savedCustomer.getEmail());
            user.setPassword(CommonUtil.passwordEncoder("DefaultPass@123")); 
            user.setRole(UserRole.CUSTOMER); 
            user.setCustomerId(savedCustomer.getId()); 
            user.setStatus(Status.ACTIVE); 
            userRepository.save(user);
        }
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