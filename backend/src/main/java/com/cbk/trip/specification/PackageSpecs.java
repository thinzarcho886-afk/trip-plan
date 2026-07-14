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

import com.cbk.trip.entity.Package;
import com.cbk.trip.entity.Package_;
import com.cbk.trip.enums.Status;

public class PackageSpecs {

    public static Specification<Package> getByFilter(String name, Long destinationId, Long durationId, Status status) {
        return new Specification<Package>() {
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<Package> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                final Collection<Predicate> predicates = new ArrayList<>();

                if (!StringUtils.isEmpty(name)) {
                    final Predicate namePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get(Package_.NAME)),
                            "%" + name.toLowerCase() + "%");
                    predicates.add(namePredicate);
                }

                if (destinationId != null) {
                    final Predicate destPredicate = criteriaBuilder.equal(root.get(Package_.DESTINATION).get("id"), destinationId);
                    predicates.add(destPredicate);
                }

                if (durationId != null) {
                    final Predicate durationPredicate = criteriaBuilder.equal(root.get(Package_.DURATION).get("id"), durationId);
                    predicates.add(durationPredicate);
                }

                if (!Objects.isNull(status)) {
                    final Predicate statusPredicate = criteriaBuilder.equal(root.get(Package_.STATUS), status);
                    predicates.add(statusPredicate);
                }

                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }
}