package com.cbk.trip.specification;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import com.cbk.trip.entity.Destination;
import com.cbk.trip.entity.Destination_; 
import com.cbk.trip.enums.Status;

/**
 * * @author HtetAungThan
 * @since 11/Jan/2025
 *
 */
public class DestinationSpecs {

    public static Specification<Destination> getByFilter(String name, Status status) {
        return new Specification<Destination>() {

            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<Destination> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                final Collection<Predicate> predicates = new ArrayList<>();

                // Name (Like search)
                if (!StringUtils.isEmpty(name)) {
                    final Predicate namePredicate = criteriaBuilder.like(
                            criteriaBuilder.lower(root.get(Destination_.NAME)), 
                            "%" + name.toLowerCase() + "%"
                    );
                    predicates.add(namePredicate);
                }

                // Status (Equal)
                if (status != null) {
                    final Predicate statusPredicate = criteriaBuilder.equal(root.get(Destination_.STATUS), status);
                    predicates.add(statusPredicate);
                }

                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }
}