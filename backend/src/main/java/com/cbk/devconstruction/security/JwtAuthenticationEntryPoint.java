package com.cbk.devconstruction.security;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.cbk.devconstruction.common.MessageConst;

/**
 * This is invoked when user tries to access a secured REST resource without
 * supplying any credentials We should just send a 401 Unauthorized response
 * because there is no 'login page' to redirect to
 * 
 * @author sansintkyaw
 *
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
	private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationEntryPoint.class);

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException ex)
			throws IOException {
		logger.error("Responding with unauthorized error. Message - {}", ex.getMessage());
		response.sendError(HttpServletResponse.SC_UNAUTHORIZED, MessageConst.UNAUTHORIZED);

	}

}
