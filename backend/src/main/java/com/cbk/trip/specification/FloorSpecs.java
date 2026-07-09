package com.cbk.trip.specification;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import com.cbk.trip.entity.Floor;
import com.cbk.trip.entity.Floor_;
import com.cbk.trip.enums.Status;

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

				if (!StringUtils.isEmpty(status)) {
					Predicate statusPredicate = criteriaBuilder.equal(root.get(Floor_.STATUS), status);
					predicates.add(statusPredicate);
				}

				return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
			}

		};
	}
}
