package com.cbk.trip.dto;

import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import com.cbk.trip.entity.Duration;
import com.cbk.trip.enums.Status;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DurationDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    private String description;

    @NotNull(message = "Status is required")
    private Status status = Status.ACTIVE;

    private String createdBy;
    private Long createdDateInMilliSeconds;
    private String updatedBy;
    private Long updatedDateInMilliSeconds;

    public DurationDTO() {}

    public DurationDTO(Duration entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.description = entity.getDescription();
        this.status = entity.getStatus();
        this.createdBy = entity.getCreatedBy();
        this.createdDateInMilliSeconds = entity.getCreatedDate() != null ? entity.getCreatedDate().toEpochMilli() : null;          
        this.updatedBy = entity.getUpdatedBy();
        this.updatedDateInMilliSeconds = entity.getUpdatedDate() != null ? entity.getUpdatedDate().toEpochMilli() : null;
    }
}