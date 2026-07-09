package com.cbk.trip.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.cbk.trip.dto.MessageDTO;
import com.cbk.trip.utils.CommonUtil;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	/**
	 * Handle others exception
	 * 
	 * @param exception
	 * @return
	 */
	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> exceptionHandler(Exception exception) {
		logger.error("Exception :", exception);
		MessageDTO message = new MessageDTO();
		message.setError(true);
		message.setMessage(exception.getLocalizedMessage());
		return new ResponseEntity<Object>(message, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	/**
	 * File Upload size exception
	 * 
	 * @param exception
	 * @return
	 */
	@ExceptionHandler(MaxUploadSizeExceededException.class)
	public ResponseEntity<Object> fileUploaderExceptionHandler(MaxUploadSizeExceededException exception) {
		logger.error("Exception :", exception);
		final String[] params = { "30" };
		MessageDTO message = new MessageDTO();
		message.setError(true);
		message.setMessage(CommonUtil.getLocalizeMessage("error.file.size", params));
		return new ResponseEntity<Object>(message, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	/**
	 * Resource not found exception
	 * 
	 * @param exception
	 * @return
	 */
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<Object> resourceNotFoundException(ResourceNotFoundException exception) {
		logger.error("Exception :", exception);
		MessageDTO message = new MessageDTO();
		message.setError(true);
		message.setMessage(exception.getLocalizedMessage());
		return new ResponseEntity<Object>(message, new HttpHeaders(), HttpStatus.NOT_FOUND);
	}

	/**
	 * Bad Request From client exception
	 * 
	 * @param exception
	 * @return
	 */
	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<Object> badRequestException(BadRequestException exception) {
		logger.error("Exception :", exception);
		MessageDTO message = new MessageDTO();
		message.setError(true);
		message.setMessage(exception.getLocalizedMessage());
		return new ResponseEntity<Object>(message, new HttpHeaders(), HttpStatus.BAD_REQUEST);
	}

	/**
	 * Internal Server exception
	 * 
	 * @param exception
	 * @return
	 */
	@ExceptionHandler(InternalServerException.class)
	public ResponseEntity<Object> internalServerException(InternalServerException exception) {
		logger.error("Exception :", exception);
		MessageDTO message = new MessageDTO();
		message.setError(true);
		message.setMessage(exception.getLocalizedMessage());
		return new ResponseEntity<Object>(message, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	/**
	 * Access Denied Exception
	 * 
	 * @param exception
	 * @return
	 */
	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<Object> accessDeniedException(AccessDeniedException exception) {
		logger.error("Exception :", exception);
		MessageDTO message = new MessageDTO();
		message.setError(true);
		message.setMessage(exception.getLocalizedMessage());
		return new ResponseEntity<Object>(message, new HttpHeaders(), HttpStatus.FORBIDDEN);
	}

	/**
	 * Login credentials exception
	 * 
	 * @param exception
	 * @return
	 */
	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<Object> badCredentialsException(BadCredentialsException exception) {
		logger.error("Exception :", exception);
		MessageDTO message = new MessageDTO();
		message.setError(true);
		message.setMessage(CommonUtil.getLocalizeMessage("error.auth.invalidLogin", null));
		return new ResponseEntity<Object>(message, new HttpHeaders(), HttpStatus.UNAUTHORIZED);
	}

	/**
	 * Login user disable exception
	 * 
	 * @param exception
	 * @return
	 */
	@ExceptionHandler(DisabledException.class)
	public ResponseEntity<Object> disabledException(DisabledException exception) {
		logger.error("Exception :", exception);
		MessageDTO message = new MessageDTO();
		message.setError(true);
		message.setMessage(CommonUtil.getLocalizeMessage("error.auth.inactiveUser", null));
		return new ResponseEntity<Object>(message, new HttpHeaders(), HttpStatus.UNAUTHORIZED);
	}

}
