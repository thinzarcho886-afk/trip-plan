package com.cbk.devconstruction.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.cbk.devconstruction.entity.Company;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long>, JpaSpecificationExecutor<Company> {

	Optional<Company> findByName(String name);

//	@Query(value = "SELECT c.id AS companyId, COALESCE(s.qty, 0) AS qty "
//			+ "FROM company c "
//			+ "LEFT JOIN ("
//			+ "SELECT s.company_id AS companyId, COUNT(*) AS qty "
//			+ "FROM shipment AS s "
//			+ "WHERE DATE_FORMAT(s.created_date, '%Y-%m') = :month "
//			+ "GROUP BY s.company_id"
//			+ ") AS s ON s.companyId = c.id",
//			nativeQuery = true)
//	List<Object[]> getCompanyActivity(@Param("month") String month);

}
