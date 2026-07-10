package com.cbk.trip.dto;

import javax.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;
import com.cbk.trip.entity.Destination;
import com.cbk.trip.enums.Status;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author HtetAungThan
 * @since 11/Jan/2025
 */
@Data
@JsonInclude(Include.NON_NULL)
@NoArgsConstructor
public class DestinationDTO extends CommonDTO {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "Name is required")
    @Length(max = 100)
    private String name;

    @Length(max = 500)
    private String description;

    private Status status = Status.ACTIVE;

    public DestinationDTO(Destination entity) {
        super(entity); 
        this.name = entity.getName();
        this.description = entity.getDescription();
        this.status = entity.getStatus();
    }
}