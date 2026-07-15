package com.cbk.trip.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import com.cbk.trip.enums.Status;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "booking")
public class Booking extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "package_id", nullable = false)
    @NotNull(message = "Package is required")
    private Package pkg;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    @NotNull(message = "Customer is required")
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_method_id", nullable = false)
    @NotNull(message = "Payment Method is required")
    private PaymentMethod paymentMethod;

    @Column(name = "travelers_qty")
    @Positive(message = "Travelers quantity must be greater than zero")
    private Integer travelersQty;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status = Status.DRAFT;

    @Column(name = "payment_receive_image")
    private String paymentReceiveImage;

    @Column(name = "note")
    private String note;
}