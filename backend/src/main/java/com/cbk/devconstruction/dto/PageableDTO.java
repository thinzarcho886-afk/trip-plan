package com.cbk.devconstruction.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 
 * @author sansintkyaw
 *
 */
@Getter
@Setter
@NoArgsConstructor
public class PageableDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<?> list = new ArrayList<>();
	private int page;
	private int size;
	private int numberofElements;
	private long totalElements;
	private int totalPages;

	public PageableDTO(List<?> list, Page<?> page) {
		this.list = list;
		this.page = page.getNumber();
		this.size = page.getSize();
		this.numberofElements = page.getNumberOfElements();
		this.totalElements = page.getTotalElements();
		this.totalPages = page.getTotalPages();
	}

}
