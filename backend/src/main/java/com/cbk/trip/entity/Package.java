package com.cbk.trip.entity;

import java.time.Instant;
import java.util.List;
import javax.persistence.*;
import com.cbk.trip.enums.Status;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "package")
public class Package extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @ManyToOne
    @JoinColumn(name = "destination_id")
    private Destination destination;

    @ManyToOne
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;

    @ManyToOne
    @JoinColumn(name = "transport_id")
    private Transport transport;

    @ManyToOne
    @JoinColumn(name = "duration_id")
    private Duration duration;

    @Column(name = "departure_date")
    private Instant departureDate;

    @Column(name = "transport_fee")
    private Double transportFee;

    @Column(name = "hotel_fee")
    private Double hotelFee;

    @Column(name = "service_fee")
    private Double serviceFee;

    @Column(name = "budget_amount")
    private Double budgetAmount;

    @Column(name = "description")
    private String description;

    @Column(name = "extra_service")
    private String extraService;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status = Status.ACTIVE;

    @OneToMany(mappedBy = "packageEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PackageDetail> packageDetails;
}