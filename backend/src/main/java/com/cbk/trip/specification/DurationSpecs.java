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

import com.cbk.trip.entity.Duration;
import com.cbk.trip.entity.Duration_;
import com.cbk.trip.enums.Status;

public class DurationSpecs {

    public static Specification<Duration> getByFilter(String name, Status status) {
        return new Specification<Duration>() {
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<Duration> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                final Collection<Predicate> predicates = new ArrayList<>();

                // Name (Like search)
                if (!StringUtils.isEmpty(name)) {
                    final Predicate namePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get(Duration_.NAME)),
                            "%" + name.toLowerCase() + "%");
                    predicates.add(namePredicate);
                }

                // Status Filter
                if (!Objects.isNull(status)) {
                    final Predicate statusPredicate = criteriaBuilder.equal(root.get(Duration_.STATUS), status);
                    predicates.add(statusPredicate);
                }

                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }
}