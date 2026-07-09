package com.cbk.devconstruction.specification;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import com.cbk.devconstruction.entity.Company;
import com.cbk.devconstruction.entity.Company_;
import com.cbk.devconstruction.entity.Floor;
import com.cbk.devconstruction.entity.Floor_;
import com.cbk.devconstruction.enums.Status;

/**
 * 
 * @author HtetAungThan
 * @since 11/Jan/2025
 *
 */
public class FloorSpecs {
	/**
	 * 
	 * @param name
	 * @param companyId
	 * @param status
	 * @return
	 */
	public static Specification<Floor> getByFilter(String name, Long companyId, Status status) {
		return new Specification<Floor>() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<Floor> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				final Collection<Predicate> predicates = new ArrayList<>();

				if (!StringUtils.isEmpty(name)) {
					final Predicate namePredicate = criteriaBuilder.like(root.get(Floor_.NAME), "%" + name + "%");
					predicates.add(namePredicate);
				}

				if (!Objects.isNull(companyId)) {
					Join<Floor, Company> currencyCompanyJoin = root.join(Floor_.COMPANY);
					Predicate companyPredicate = criteriaBuilder.equal(currencyCompanyJoin.get(Company_.ID), companyId);
					predicates.add(companyPredicate);
				}

				if (!StringUtils.isEmpty(status)) {
					Predicate statusPredicate = criteriaBuilder.equal(root.get(Floor_.STATUS), status);
					predicates.add(statusPredicate);
				}

				return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
			}

		};
	}
}
