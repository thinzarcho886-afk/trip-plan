package com.cbk.trip.specification;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import com.cbk.trip.entity.BusType;
import com.cbk.trip.entity.Destination;
import com.cbk.trip.entity.Duration;
import com.cbk.trip.entity.Package;
import com.cbk.trip.entity.Package_;
import com.cbk.trip.entity.Transport;
import com.cbk.trip.enums.Status;

public class PackageSpecs {

    public static Specification<Package> getByFilter(String name, Long destinationId,String destinationName, Long durationId,String durationName, Status status) {
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
                if (!StringUtils.isEmpty(destinationName)) {
					Join<Package, Destination> packDestinationJoin = root.join("destination");
					Predicate destNamePredicate = criteriaBuilder.like(
							criteriaBuilder.lower(packDestinationJoin.get("name")),
							"%" + destinationName.toLowerCase() + "%"
					);
					predicates.add(destNamePredicate);
				}
                
                if (durationId != null) {
                    final Predicate durationPredicate = criteriaBuilder.equal(root.get(Package_.DURATION).get("id"), durationId);
                    predicates.add(durationPredicate);
                }
                
                if (!Objects.isNull(durationId)) {
					Join<Package, Duration> packDurationJoin = root.join("duration");

					Predicate packagePredicate = criteriaBuilder.equal(packDurationJoin.get("id"),durationId);
					predicates.add(packagePredicate);
				}
                if (!StringUtils.isEmpty(durationName)) {
					Join<Package, Duration> packDurationJoin = root.join("duration");
					Predicate durationNamePredicate = criteriaBuilder.like(
							criteriaBuilder.lower(packDurationJoin.get("name")),
							"%" + durationName.toLowerCase() + "%"
					);
					predicates.add(durationNamePredicate);
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