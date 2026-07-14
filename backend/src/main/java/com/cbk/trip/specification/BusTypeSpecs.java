package com.cbk.trip.specification;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import com.cbk.trip.entity.BusType;
import com.cbk.trip.entity.BusType_;
import com.cbk.trip.enums.Status;

/**
 * @author HtetAungThan
 * @since 11/Jan/2025
 */
public class BusTypeSpecs {

	public static Specification<BusType> getByFilter(String name, Integer availableSeats, Status status) {
		return new Specification<BusType>() {

			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<BusType> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				final Collection<Predicate> predicates = new ArrayList<>();

				if (StringUtils.hasText(name)) {
					final Predicate namePredicate = criteriaBuilder.like(root.get(BusType_.name), "%" + name + "%");
					predicates.add(namePredicate);
				}

				if (availableSeats != null) {
					Predicate seatPredicate = criteriaBuilder.equal(root.get(BusType_.availableSeats), availableSeats);
					predicates.add(seatPredicate);
				}

				if (status != null) {
					Predicate statusPredicate = criteriaBuilder.equal(root.get(BusType_.status), status);
					predicates.add(statusPredicate);
				}

				return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}
}