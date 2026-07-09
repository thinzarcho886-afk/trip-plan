package com.cbk.devconstruction.dto;

import java.io.Serializable;
import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class MessageDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5398311645012296950L;
	private boolean error;
	private String message;
	private HashMap<String, String> fieldErrorMessages;
}
