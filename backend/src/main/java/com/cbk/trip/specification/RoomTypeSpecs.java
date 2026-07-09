package com.cbk.trip.specification;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import com.cbk.trip.entity.RoomType;
import com.cbk.trip.enums.Status;

public class RoomTypeSpecs {
	public static Specification<RoomType> getByFilter(String roomTypeName, Status status) {
		return new Specification<RoomType>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<RoomType> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				final Collection<Predicate> predicates = new ArrayList<>();

				if (!StringUtils.isEmpty(roomTypeName)) {
					final Predicate roomPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("name")),
							"%" + roomTypeName.toLowerCase() + "%");
					predicates.add(roomPredicate);
				}

				if (!Objects.isNull(status)) {
					Predicate statusPredicate = criteriaBuilder.equal(root.get("status"), status);
					predicates.add(statusPredicate);
				}

				query.orderBy(criteriaBuilder.desc(root.get("id")));
				return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
			}

		};
	}

}