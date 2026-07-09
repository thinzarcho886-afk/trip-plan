package com.cbk.devconstruction.exception;

public class InternalServerException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InternalServerException() {
		super();
	}

	public InternalServerException(String message) {
		super(message);
	}

	public InternalServerException(Throwable cause) {
		super(cause);
	}

	public InternalServerException(String message, Throwable cause) {
		super(message, cause);
	}
}
