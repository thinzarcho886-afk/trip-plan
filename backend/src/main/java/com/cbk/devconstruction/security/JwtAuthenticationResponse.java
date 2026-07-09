package com.cbk.devconstruction.security;

import java.io.Serializable;

import com.cbk.devconstruction.dto.UserDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author sansintkyaw
 *
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JwtAuthenticationResponse implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String token;
	private UserDTO user;
}
