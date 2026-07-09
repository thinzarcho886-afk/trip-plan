package com.cbk.trip.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.cbk.trip.common.MessageConst;
import com.cbk.trip.utils.CommonUtil;

/**
 * JWT Authentication Filter
 * 
 * @author sansintkyaw
 *
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	private JwtTokenProvider tokenProvider;

	@Autowired
	private CustomUserDetailsService customUserDetailsService;

	private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			String jwt = getJwtFromRequest(request);

			if (!StringUtils.hasText(jwt)) {
				// auth key missing
				response.setStatus(HttpStatus.UNAUTHORIZED.value());
				response.getWriter().write(CommonUtil.responseString(MessageConst.AUTH_KEY_MISSING));
				return;
			}

			Long userId = tokenProvider.getUserIdFromJWT(jwt);
			if (!tokenProvider.validateToken(jwt) || userId == null) {
				// invalid token
				response.setStatus(HttpStatus.UNAUTHORIZED.value());
				response.getWriter().write(CommonUtil.responseString(MessageConst.INVALID_JWT));
				return;
			}

			UserDetails userDetails = customUserDetailsService.loadUserById(userId);
			UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails,
					null, userDetails.getAuthorities());

			SecurityContextHolder.getContext().setAuthentication(authentication);

		} catch (Exception ex) {
			logger.error("Could not set user authentication in security context", ex);
			response.setStatus(HttpStatus.UNAUTHORIZED.value());
			response.getWriter().write(CommonUtil.responseString(MessageConst.INVALID_JWT));
			return;
		}

		filterChain.doFilter(request, response);
	}

	/**
	 * Get JWT From Request
	 * 
	 * @param request
	 * @return
	 */
	private String getJwtFromRequest(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7, bearerToken.length());
		}
		return null;
	}

	@Override
	public boolean shouldNotFilter(HttpServletRequest request) {
		if (request.getRequestURI().contains("/api/") && request.getRequestURI().contains("/auth/")) {
			return false;
		}
		return true;
	}
}
