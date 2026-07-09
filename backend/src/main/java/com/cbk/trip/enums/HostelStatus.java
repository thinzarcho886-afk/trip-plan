package com.cbk.trip.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum HostelStatus {
    AVAILABLE,
    UNAVAILABLE;

    @JsonCreator
    public static HostelStatus fromString(String value) {
        if (value == null) {
            return null;
        }
        
        if (value.equalsIgnoreCase("ACTIVE") || value.equalsIgnoreCase("AVAILABLE")) {
            return AVAILABLE;
        }
        
        if (value.equalsIgnoreCase("INACTIVE") || value.equalsIgnoreCase("UNAVAILABLE")) {
            return UNAVAILABLE;
        }
        
        return AVAILABLE; 
    }
}
