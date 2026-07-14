package com.cbk.trip.specification;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import com.cbk.trip.entity.Bus;
import com.cbk.trip.entity.Bus_;
import com.cbk.trip.enums.Status;

/**
 * @author HtetAungThan
 * @since 11/Jan/2025
 */
public class BusSpecs {

	public static Specification<Bus> getByFilter(String name, Status status) {
		return new Specification<Bus>() {

			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<Bus> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				final Collection<Predicate> predicates = new ArrayList<>();

				if (StringUtils.hasText(name)) {
					final Predicate namePredicate = criteriaBuilder.like(root.get(Bus_.name), "%" + name + "%");
					predicates.add(namePredicate);
				}

				if (status != null) {
					Predicate statusPredicate = criteriaBuilder.equal(root.get(Bus_.status), status);
					predicates.add(statusPredicate);
				}

				return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}
}