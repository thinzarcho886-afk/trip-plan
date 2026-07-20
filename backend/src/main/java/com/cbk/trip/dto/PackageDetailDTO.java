package com.cbk.trip.dto;

import java.io.Serializable;

import com.cbk.trip.entity.PackageDetail;
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
public class PackageDetailDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String placeToVisit;
    private String image;
    private String imageUrl;

    public PackageDetailDTO(PackageDetail entity) {
    	this.id = entity.getId();
        this.placeToVisit = entity.getPlaceToVisit();
        this.image = entity.getImageUrl();
        this.imageUrl = NginxUtil.getFileUrl(entity.getImageUrl(), true);
    }
}