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

import com.cbk.trip.entity.Customer;
import com.cbk.trip.entity.Customer_; 
import com.cbk.trip.enums.Status;

public class CustomerSpecs {

    public static Specification<Customer> getByFilter(String name, String email, String phoneNumber, Status status) {
        return new Specification<Customer>() {
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<Customer> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                final Collection<Predicate> predicates = new ArrayList<>();

                if (!StringUtils.isEmpty(name)) {
                    final Predicate namePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get(Customer_.NAME)),
                            "%" + name.toLowerCase() + "%");
                    predicates.add(namePredicate);
                }

                if (!StringUtils.isEmpty(email)) {
                    final Predicate emailPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get(Customer_.EMAIL)),
                            "%" + email.toLowerCase() + "%");
                    predicates.add(emailPredicate);
                }

                if (!StringUtils.isEmpty(phoneNumber)) {
                    final Predicate phonePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get(Customer_.PHONE_NUMBER)),
                            "%" + phoneNumber.toLowerCase() + "%");
                    predicates.add(phonePredicate);
                }

                if (!Objects.isNull(status)) {
                    final Predicate statusPredicate = criteriaBuilder.equal(root.get(Customer_.STATUS), status);
                    predicates.add(statusPredicate);
                }

                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }
}