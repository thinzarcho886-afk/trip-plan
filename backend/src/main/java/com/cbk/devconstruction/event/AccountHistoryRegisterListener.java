package com.cbk.devconstruction.event;

import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@EnableAsync
@Slf4j
public class AccountHistoryRegisterListener implements ApplicationListener<OnAccountHistoryRegisterEvent> {

//	@Autowired
//	AccountHistoryService accountHistoryService;

	@Async
	@Override
	public void onApplicationEvent(OnAccountHistoryRegisterEvent event) {
		// List<AccountHistoryDTO> accountHistoryList = event.getAccountHistoryList();
		// try {
		// accountHistoryService.saveAccountHistories(accountHistoryList);
		// } catch (ParseException e) {
		// log.error("Parse error on account history registration event.", e);
		// }
	}

}
