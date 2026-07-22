package com.cbk.trip.dto;

import java.time.Instant;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import com.cbk.trip.entity.Booking;
import com.cbk.trip.enums.BookingStatus;
import com.cbk.trip.enums.Status;
import com.cbk.trip.utils.NginxUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
@NoArgsConstructor
public class BookingDTO extends CommonDTO {

	private static final long serialVersionUID = 1L;

	@NotNull(message = "Package is required")
	private Long packageId;

	@NotNull(message = "Customer is required")
	private Long customerId;

	@NotNull(message = "Payment Method is required")
	private Long paymentMethodId;

	@NotNull(message = "Travelers quantity is required")
	@Positive(message = "Travelers quantity must be greater than zero")
	private Integer travelersQty;

	@NotNull(message = "Status is required")
	private BookingStatus status;

    private String paymentReceiveImage;
    private String paymentReceiveImageUrl;
    private String note;

    private String packageName;
    private String customerName;
    private String paymentMethodName;
    private Double totalAmount;
    private Instant departureDate;
    private String busTypeName;
    private String busName;
    private String destinationName;
    private String durationName;
    private String hotelName;
    private Double hotelFee;
    private Double serviceFee;
    private Double transportFee;
    private Double budgetAmount;
    

    public BookingDTO(Booking entity) {
        super(entity);
        if(entity.getPkg()!= null) {
        this.packageId = entity.getPkg().getId();
        this.packageName = entity.getPkg().getName();
        this.busTypeName = entity.getPkg().getTransport().getBusType().getName();
        this.busName = entity.getPkg().getTransport().getBus().getName();
        this.destinationName = entity.getPkg().getDestination().getName();
        this.durationName = entity.getPkg().getDuration().getName();
        this.hotelName = entity.getPkg().getHotel().getName();
        this.hotelFee = entity.getPkg().getHotelFee();
        this.serviceFee = entity.getPkg().getServiceFee();
        this.transportFee = entity.getPkg().getTransportFee();
        this.budgetAmount = entity.getPkg().getBudgetAmount();
        
        }
        if(entity.getCustomer()!= null) {
        this.customerId = entity.getCustomer().getId();
        this.customerName = entity.getCustomer().getName();
        }
        if(entity.getPaymentMethod()!=null) {
        this.paymentMethodId = entity.getPaymentMethod().getId();
        this.paymentMethodName = entity.getPaymentMethod().getName();
        }
        this.departureDate = entity.getPkg().getDepartureDate();
        this.travelersQty = entity.getTravelersQty();
        this.status = entity.getStatus();
        this.paymentReceiveImage = entity.getPaymentReceiveImageUrl();
        this.paymentReceiveImageUrl = NginxUtil.getFileUrl(entity.getPaymentReceiveImageUrl(), true);
        this.note = entity.getNote();
    }
}