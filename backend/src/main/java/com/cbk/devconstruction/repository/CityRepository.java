package com.cbk.devconstruction.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import com.cbk.devconstruction.entity.City;
import com.cbk.devconstruction.enums.Status;

@Repository
public interface CityRepository extends JpaRepository<City, Long>,JpaSpecificationExecutor<City> {
	
	Optional<City> findById(Long cityId);
	Optional<City> findByCityName(String cityName);
	Optional<City> findByCityNameAndId(String cityName,Long cityId);
	Optional<List<City>> findByStatus(Status status);
	
	
}
