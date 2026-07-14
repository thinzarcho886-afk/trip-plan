package com.cbk.trip.entity;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "package_detail")
public class PackageDetail extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "place_to_visit")
    private String placeToVisit;

    @Column(name = "image_url")
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "package_id")
    private Package packageEntity;
}