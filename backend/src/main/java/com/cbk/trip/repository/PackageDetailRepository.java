package com.cbk.trip.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.cbk.trip.entity.PackageDetail;

@Repository
public interface PackageDetailRepository extends JpaRepository<PackageDetail, Long> {

    boolean existsByPackageEntityIdAndPlaceToVisit(Long packageId, String placeToVisit);
    boolean existsByPackageEntityIdAndPlaceToVisitAndIdNot(Long packageId, String placeToVisit, Long id);
}