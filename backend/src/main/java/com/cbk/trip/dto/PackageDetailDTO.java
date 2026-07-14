package com.cbk.trip.dto;

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
public class PackageDetailDTO extends CommonDTO {

    private static final long serialVersionUID = 1L;

    private String placeToVisit;
    private String imageUrl;
    private String imageFullUrl;

    public PackageDetailDTO(PackageDetail entity) {
        super(entity);
        this.placeToVisit = entity.getPlaceToVisit();
        this.imageUrl = entity.getImageUrl();
        this.imageFullUrl = NginxUtil.getFileUrl(entity.getImageUrl(), false);
    }
}