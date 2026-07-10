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

import com.cbk.trip.entity.User;
import com.cbk.trip.entity.User_; 
import com.cbk.trip.enums.Status;
import com.cbk.trip.enums.UserRole;

public class UserSpecs {

    public static Specification<User> getByFilter(String username, UserRole role, Status status) {
        return new Specification<User>() {
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                final Collection<Predicate> predicates = new ArrayList<>();

                if (!StringUtils.isEmpty(username)) {
                    final Predicate namePredicate = criteriaBuilder.like(
                            criteriaBuilder.lower(root.get(User_.username)),
                            "%" + username.toLowerCase() + "%");
                    predicates.add(namePredicate);
                }

                if (!Objects.isNull(role)) {
                    final Predicate rolePredicate = criteriaBuilder.equal(root.get(User_.role), role);
                    predicates.add(rolePredicate);
                }

                if (!Objects.isNull(status)) {
                    final Predicate statusPredicate = criteriaBuilder.equal(root.get(User_.status), status);
                    predicates.add(statusPredicate);
                }

                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }
}