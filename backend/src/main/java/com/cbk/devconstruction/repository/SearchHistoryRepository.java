package com.cbk.devconstruction.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cbk.devconstruction.entity.SearchHistory;

@Repository
public interface SearchHistoryRepository extends JpaRepository<SearchHistory, Long>, JpaSpecificationExecutor<SearchHistory> {
	
	boolean existsByUserIdAndHostelIdAndDatetime(Long userId, Long hostelId, LocalDate datetime);

	@Query("SELECT sh.hostel.id FROM SearchHistory sh " +
		       "WHERE (:from IS NULL OR sh.datetime >= :from) " +
		       "AND (:to IS NULL OR sh.datetime <= :to) " +
		       "GROUP BY sh.hostel.id " +
		       "ORDER BY COUNT(sh.hostel.id) DESC")
		Page<Long> findMostSearchedHostelIdsByFilter(
		        @Param("from") LocalDate fromDate, 
		        @Param("to") LocalDate toDate, 
		        Pageable pageable);
	
}