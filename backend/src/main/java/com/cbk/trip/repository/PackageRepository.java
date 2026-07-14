package com.cbk.trip.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import com.cbk.trip.entity.Package;

@Repository
public interface PackageRepository extends JpaRepository<Package, Long>, JpaSpecificationExecutor<Package> {

    boolean existsByName(String name);
    boolean existsByNameAndIdNot(String name, Long id);
}