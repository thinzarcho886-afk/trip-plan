package com.cbk.trip.dto;

import java.io.Serializable;

import com.cbk.trip.entity.Transport;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class TransportDTO implements Serializable{

    private static final long serialVersionUID = 1L;

    private Long id;
    private Long busTypeId;
    
    private Long busId;
    private String busName;

    public TransportDTO() { super(); }

    public TransportDTO(Transport entity) {
    	this.id = entity.getId();
        if (entity.getBusType() != null) {
            this.busTypeId = entity.getBusType().getId();
        }
        if (entity.getBus() != null) {
            this.busId = entity.getBus().getId();
            this.busName = entity.getBus().getName();
        }
    }
}