package com.cbk.trip.service;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cbk.trip.dto.CustomerDTO;
import com.cbk.trip.dto.PageableDTO;
import com.cbk.trip.dto.UserDTO;
import com.cbk.trip.entity.User;
import com.cbk.trip.entity.Customer;

import com.cbk.trip.enums.Status;
import com.cbk.trip.enums.UserRole;
import com.cbk.trip.exception.BadRequestException;
import com.cbk.trip.repository.UserRepository;
import com.cbk.trip.repository.CustomerRepository;
import com.cbk.trip.specification.UserSpecs; 
import com.cbk.trip.utils.CommonUtil;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private CustomerRepository customerRepository;
    
    

    @Autowired
    private CustomerService customerService;
    
    
    @Autowired
    private org.springframework.security.authentication.AuthenticationManager authenticationManager;

    @Autowired
    private com.cbk.trip.security.JwtTokenProvider tokenProvider;

    @Autowired
    private org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    public com.cbk.trip.security.JwtAuthenticationResponse login(UserDTO dto) {
        User user = userRepository.findByUsername(dto.getUsername())
                .orElseThrow(() -> new BadRequestException("Invalid username!"));

        if (Status.INACTIVE.equals(user.getStatus())) {
            throw new BadRequestException("User is inactive!");
        }

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new BadRequestException("Incorrect password!");
        }

        org.springframework.security.core.Authentication authentication = authenticationManager.authenticate(
                new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                        dto.getUsername(), 
                        dto.getPassword()
                )
        );
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);
        UserDTO response = new UserDTO(user);

        Long customerId = response.getCustomerId();
        if (customerId != null) {
            CustomerDTO customer = customerService.getById(customerId);
            if (customer != null) {
            		response.setCustomerId(customerId);
                response.setEmail(customer.getEmail());
                response.setPhone(customer.getPhoneNumber());
            }
        }

        return new com.cbk.trip.security.JwtAuthenticationResponse(jwt, response);
    }

    public PageableDTO getUsers(String username, UserRole role, Status status, Pageable pageable) {
        Specification<User> specs = UserSpecs.getByFilter(username, role, status);
        Page<User> page = userRepository.findAll(specs, pageable);
        List<UserDTO> dtoList = CommonUtil.getDTOList(page.getContent(), UserDTO::new);
        return new PageableDTO(dtoList, page);
    }

    @Transactional(rollbackFor = Exception.class)
    public void save(@Valid UserDTO dto, boolean isUpdate) {
        
        if (isUsernameDuplicate(dto.getId(), dto.getUsername())) {
            throw new BadRequestException("Username already exists!");
        }

        User user;
        if (isUpdate) {
            user = CommonUtil.checkValidById(dto.getId(), userRepository);
            
            user.setStatus(dto.getStatus());
        } else {
            if (!dto.getPassword().equals(dto.getConfirmPassword())) {
                throw new BadRequestException("Password and Confirm Password do not match!");
            }
            
            user = new User();
            user.setUsername(dto.getUsername());
            user.setPassword(CommonUtil.passwordEncoder(dto.getPassword()));
            user.setRole(dto.getRole());
            user.setCustomerId(dto.getCustomerId());
            user.setStatus(dto.getStatus());
        }

        userRepository.save(user);
    }
    
    public UserDTO getById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        UserDTO userDTO = new UserDTO(user);
      Long customerId = userDTO.getCustomerId();
      CustomerDTO customer=customerService.getById(customerId);
      userDTO.setCustomerName(customer.getName());
      return userDTO;
    }

    public boolean isUsernameDuplicate(Long id, String username) {
        Optional<User> userExist = userRepository.findByUsername(username);
        return userExist.isPresent() && !userExist.get().getId().equals(id);
    }

    public User checkValidUser(Long id) {
        return CommonUtil.checkValidById(id, userRepository);
    }
    

	@Transactional(rollbackFor = Exception.class)
	public void changePassword(Long userId, String password) {
		User user = checkValidUser(userId);
		user.setPassword(passwordEncoder.encode(password));
		userRepository.save(user);
	}

	public User getByName(String name) {
		return userRepository.getByUsername(name);
	}
}