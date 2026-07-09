package com.cbk.devconstruction.event;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.ApplicationEvent;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OnAccountHistoryRegisterEvent extends ApplicationEvent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// List<AccountHistoryDTO> accountHistoryList = new ArrayList<>();
	List<String> dumpList = new ArrayList<>();

	public OnAccountHistoryRegisterEvent(List<String> dumpList) {
		super(dumpList);
		// this.accountHistoryList = accountHistoryList;
		this.dumpList = dumpList;
	}

}
