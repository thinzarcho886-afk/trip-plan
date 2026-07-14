package com.cbk.trip.dto;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.cbk.trip.entity.Package;
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
public class PackageDTO extends CommonDTO {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "Destination is required")
    private Long destinationId;
    private String destinationName;

    @NotNull(message = "Hotel is required")
    private Long hotelId;
    private String hotelName;

    @NotNull(message = "Transport is required")
    private Long transportId;
    private String transportName;

    @NotNull(message = "Duration is required")
    private Long durationId;
    private String durationName;

    @NotNull(message = "Departure date is required")
    private Instant departureDate;

    @Min(value = 1, message = "Transport fee must be greater than zero")
    private Double transportFee;

    @Min(value = 1, message = "Hotel fee must be greater than zero")
    private Double hotelFee;

    @Min(value = 1, message = "Service fee must be greater than zero")
    private Double serviceFee;

    private Double budgetAmount;
    private String description;
    private String extraService;

    @NotNull(message = "Status is required")
    private Status status;

    private List<PackageDetailDTO> packageDetails;

    public PackageDTO(Package entity) {
        super(entity);
        this.name = entity.getName();

        if (entity.getDestination() != null) {
            this.destinationId = entity.getDestination().getId();
            this.destinationName = entity.getDestination().getName();
        }

        if (entity.getHotel() != null) {
            this.hotelId = entity.getHotel().getId();
            this.hotelName = entity.getHotel().getName();
        }

        if (entity.getTransport() != null) {
            this.transportId = entity.getTransport().getId();
            // Transport name ကို ရယူရန် Bus name ကို အသုံးပြုပါ
            if (entity.getTransport().getBus() != null) {
                this.transportName = entity.getTransport().getBus().getName();
            }
        }

        if (entity.getDuration() != null) {
            this.durationId = entity.getDuration().getId();
            this.durationName = entity.getDuration().getName();
        }

        this.departureDate = entity.getDepartureDate();
        this.transportFee = entity.getTransportFee();
        this.hotelFee = entity.getHotelFee();
        this.serviceFee = entity.getServiceFee();
        this.budgetAmount = entity.getBudgetAmount();
        this.description = entity.getDescription();
        this.extraService = entity.getExtraService();
        this.status = entity.getStatus();

        if (entity.getPackageDetails() != null) {
            this.packageDetails = entity.getPackageDetails().stream()
                    .map(PackageDetailDTO::new)
                    .collect(Collectors.toList());
        }
    }
}