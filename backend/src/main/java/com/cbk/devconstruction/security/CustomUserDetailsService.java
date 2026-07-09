package com.cbk.devconstruction.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cbk.devconstruction.common.MessageConst;
import com.cbk.devconstruction.entity.User;
import com.cbk.devconstruction.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {
	protected final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

	@Autowired
	UserRepository userRepository;

	/**
	 * Load by user_name
	 */
	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) {
		User user = userRepository.findByUsername(username).orElseThrow(() -> {
			logger.error(MessageConst.USER_NOT_FOUND);
			return new UsernameNotFoundException(MessageConst.USER_NOT_FOUND);
		});
		return UserPrincipal.create(user);

	}

	/**
	 * Load by user_id
	 * 
	 * @param id
	 * @return
	 */
	@Transactional
	public UserDetails loadUserById(Long id) {// This method is used by JWTAuthenticationFilter
		User user = userRepository.findById(id).orElseThrow(() -> {
			logger.error(MessageConst.USER_NOT_FOUND);
			return new UsernameNotFoundException(MessageConst.USER_NOT_FOUND);
		});
		return UserPrincipal.create(user);
	}

}
