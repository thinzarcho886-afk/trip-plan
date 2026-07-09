package com.cbk.trip.enums;

/**
 * 
 * type with resource location, used in attachment and excel
 * 
 * @author sansintkyaw
 *
 */
public enum ClassType {

	CUSTOMER("com.cbk.devconstruction.service.CustomerService",
			"com.cbk.devconstruction.service.CustomerAttachmentService"),
	BOOKING("com.cbk.devconstruction.service.BookingService",
			"com.cbk.devconstruction.service.BookingAttachmentService"),
	CONTRACT("com.cbk.devconstruction.service.ContractService",
			"com.cbk.devconstruction.service.ContractAttachmentService");

	private final String exportValue;
	private final String attachmentValue;

	ClassType(String exportValue, String attachmentValue) {
		this.exportValue = exportValue;
		this.attachmentValue = attachmentValue;
	}

	public String getExportValue() {
		return exportValue;
	}

	public String getAttachmentValue() {
		return attachmentValue;
	}

}
