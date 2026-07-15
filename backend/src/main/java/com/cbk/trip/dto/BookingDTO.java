package com.cbk.trip.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import com.cbk.trip.entity.Booking;
import com.cbk.trip.enums.Status;
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
    private Status status;

    private String paymentReceiveImage;
    private String note;

    private String packageName;
    private String customerName;
    private String paymentMethodName;
    private Double totalAmount;
    private String departureDate;

    public BookingDTO(Booking entity) {
        super(entity);
        this.packageId = entity.getPkg().getId();
        this.customerId = entity.getCustomer().getId();
        this.paymentMethodId = entity.getPaymentMethod().getId();
        this.travelersQty = entity.getTravelersQty();
        this.status = entity.getStatus();
        this.paymentReceiveImage = entity.getPaymentReceiveImage();
        this.note = entity.getNote();
    }
}