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

import com.cbk.trip.entity.Hotel;
import com.cbk.trip.enums.Status;

public class HotelSpecs {
    
    public static Specification<Hotel> getByFilter(String name, Long destinationId, Status status) {
        return new Specification<Hotel>() {
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<Hotel> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                final Collection<Predicate> predicates = new ArrayList<>();

                if (!StringUtils.isEmpty(name)) {
                    final Predicate namePredicate = criteriaBuilder.like(
                            criteriaBuilder.lower(root.get("name")),
                            "%" + name.toLowerCase() + "%"
                    );
                    predicates.add(namePredicate);
                }

                if (!Objects.isNull(destinationId)) {
                    Predicate destinationPredicate = criteriaBuilder.equal(root.get("destination").get("id"), destinationId);
                    predicates.add(destinationPredicate);
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