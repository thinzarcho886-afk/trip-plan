package com.cbk.trip.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cbk.trip.common.MessageConst;
import com.cbk.trip.dto.PageableDTO;
import com.cbk.trip.dto.UserDTO;
import com.cbk.trip.entity.User;
import com.cbk.trip.enums.Status;
import com.cbk.trip.enums.UserRole;
import com.cbk.trip.exception.ResourceNotFoundException;
import com.cbk.trip.repository.UserRepository;
import com.cbk.trip.specification.UserSpecs;
import com.cbk.trip.utils.CommonUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	UserRepository userRepository;

	public User getUserByName(String username) {
		return userRepository.findByUsername(username)
				.orElseThrow(() -> new ResourceNotFoundException(MessageConst.USER_NOT_FOUND));
	}

	public User getCurrentUser() {
		String username = CommonUtil.getUsernameFromAuthentication();
		return userRepository.findByUsername(username)
				.orElseThrow(() -> new ResourceNotFoundException(MessageConst.USER_NOT_FOUND));
	}

	/**
	 * Check if user with user_name already exist
	 */
	public boolean isUsernameDuplicate(String username, Long id) {
		Optional<User> user = userRepository.findByUsername(username);
		return CommonUtil.checkDuplicate(user, id);
	}

	@Transactional(rollbackFor = Exception.class)
	public void changePassword(Long userId, String password) {
		User user = checkValidUser(userId);
		user.setPassword(passwordEncoder.encode(password));
		userRepository.save(user);
	}

	User checkValidUser(Long id) {
		return CommonUtil.checkValidById(id, userRepository);
	}

	public PageableDTO getAllUser(String studentName, String ownerName, String username, UserRole role, Status status,
			Pageable pageable) {
		Specification<User> userSpecs = UserSpecs.getAllUsers(studentName, ownerName, username, role, status);
		Page<User> userPage = userRepository.findAll(userSpecs, pageable);

		List<User> userList = userPage.getContent();
		List<UserDTO> userDTOList = CommonUtil.getDTOList(userList, UserDTO::new);

		PageableDTO pageableDTO = new PageableDTO(userDTOList, userPage);

		return pageableDTO;
	}

	@Transactional(rollbackFor = Exception.class)
	public void save(UserDTO userDTO, boolean isUpdate) throws IOException {

		User user = new User();

		if (isUpdate)
			user = checkValidUser(userDTO.getId());
		user.setUsername(userDTO.getUsername().trim());

		if (!isUpdate) {
			String encodedPassword = passwordEncoder.encode(userDTO.getPassword());
			user.setPassword(encodedPassword);

			UserRole userRole = userDTO.getRole();

			user.setRole(userRole);
		}

//		if (userDTO.getRole().equals(UserRole.STUDENT)) {
//			Student student = (isUpdate && user.getStudent() != null) ? user.getStudent() : new Student();
//
//			if (userDTO.getStudentImageUrl() == null) {
//				student.setImage(null);
//			}
//			if (userDTO.getStudentImageUrl() != null && userDTO.getStudentImageUrl().startsWith("data:image")) {
//				if (!isUpdate) {
//					student.setImage(NginxUtil.saveImage(userDTO.getStudentImageUrl(), "student_image"));
//				} else {
//					student.setImage(NginxUtil.updateImage(userDTO.getStudentImageUrl(), student.getImage(),
//							"student_image", StringUtils.isEmpty(userDTO.getStudentImage())));
//				}
//
//			}
//			student.setName(userDTO.getStudentName());
//			student.setEmail(userDTO.getStudentEmail());
//			student.setPhone(userDTO.getStudentPhone());
//			student.setAddress(userDTO.getStudentAddress());
//			student.setStatus(userDTO.getStudentStatus());
//			user.setStudent(student);
//
//		} else if (userDTO.getRole().equals(UserRole.OWNER)) {
//			Owner owner = (isUpdate && user.getOwner() != null) ? user.getOwner() : new Owner();
//
//			if (userDTO.getOwnerImageUrl() == null) {
//				owner.setImage(null);
//			}
//
//			if (userDTO.getOwnerImageUrl() != null && userDTO.getOwnerImageUrl().startsWith("data:image")) {
//				if (!isUpdate) {
//					owner.setImage(NginxUtil.saveImage(userDTO.getOwnerImageUrl(), "owner_image"));
//				} else {
//					owner.setImage(NginxUtil.updateImage(userDTO.getOwnerImageUrl(), owner.getImage(), "owner_image",
//							StringUtils.isEmpty(userDTO.getOwnerImage())));
//				}
//
//			}
//			owner.setName(userDTO.getOwnerName());
//			owner.setEmail(userDTO.getOwnerEmail());
//			owner.setPhone(userDTO.getOwnerPhone());
//			owner.setAddress(userDTO.getOwnerAddress());
//			owner.setStatus(userDTO.getOwnerStatus());
//			user.setOwner(owner);
//
//		}

		user.setStatus(userDTO.getStatus());

//    if (!userDTO.getRoleName().equals(UserRole.SYSADMIN.toString())) {
//      Company company = companyService.checkValidCompany(userDTO.getCompanyId());
//      user.setCompany(company);
//    }

		userRepository.save(user);

	}

	public UserDTO getById(Long id) {
		String currentUsername = CommonUtil.getUsernameFromAuthentication();
		User currentUser = getUserByName(currentUsername);

		// set id to current user if not 'SYSADMIN'
		if (!currentUser.getRole().equals(UserRole.SYSADMIN) && !currentUser.getRole().equals(UserRole.OWNER)) {
			id = currentUser.getId();
		}

		User user = checkValidUser(id);

		UserDTO userDTO = new UserDTO(user);
		return userDTO;
	}

	public List<UserDTO> getUserByStatus(Status status) {
		List<UserDTO> userDTOList = new ArrayList<>();
		Optional<List<User>> userOpt = null;
		userOpt = userRepository.findByStatus(status);
		if (userOpt.isPresent()) {
			for (User user : userOpt.get()) {
				UserDTO userList = new UserDTO(user);

				userDTOList.add(userList);
			}
		}

		return userDTOList;

	}

	public List<UserDTO> getUserByRole(UserRole userRole) {
		List<UserDTO> userDTOList = new ArrayList<>();
		Optional<List<User>> userOpt = null;
		userOpt = userRepository.findByRole(userRole);
		if (userOpt.isPresent()) {
			for (User user : userOpt.get()) {
				UserDTO userList = new UserDTO(user);

				userDTOList.add(userList);
			}
		}
		return userDTOList;

	}

}