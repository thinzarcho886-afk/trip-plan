package com.cbk.trip.specification;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import com.cbk.trip.entity.Booking;
import com.cbk.trip.entity.Booking_;
import com.cbk.trip.entity.Customer;
import com.cbk.trip.entity.Destination;
import com.cbk.trip.entity.Package;
import com.cbk.trip.entity.PaymentMethod;
import com.cbk.trip.enums.BookingStatus;
import com.cbk.trip.enums.Status;

public class BookingSpecs {

    public static Specification<Booking> getByFilter(Long packageId,String packageName, Long customerId,String customerName, Long paymentMethodId,String paymentMethodName, BookingStatus status) {
        return new Specification<Booking>() {
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<Booking> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                final Collection<Predicate> predicates = new ArrayList<>();

                if (!Objects.isNull(packageId)) {
                    final Predicate packagePredicate = criteriaBuilder.equal(root.get(Booking_.PKG).get("id"), packageId);
                    predicates.add(packagePredicate);
    				}
                if (!StringUtils.isEmpty(packageName)) {
					Join<Booking, Package> bookingPackageJoin = root.join("pkg");
					Predicate bookingPredicate = criteriaBuilder.like(
							criteriaBuilder.lower(bookingPackageJoin.get("name")),
							"%" + packageName.toLowerCase() + "%"
					);
					predicates.add(bookingPredicate);
				}
                if (!Objects.isNull(customerId)) {
                    final Predicate customerPredicate = criteriaBuilder.equal(root.get(Booking_.CUSTOMER).get("id"), customerId);
                    predicates.add(customerPredicate);
                }
                if (!StringUtils.isEmpty(customerName)) {
					Join<Booking, Customer> bookingCustomerJoin = root.join("customer");
					Predicate bookingPredicate = criteriaBuilder.like(
							criteriaBuilder.lower(bookingCustomerJoin.get("name")),
							"%" + customerName.toLowerCase() + "%"
					);
					predicates.add(bookingPredicate);
				}
                if (!Objects.isNull(paymentMethodId)) {
                    final Predicate paymentMethodPredicate = criteriaBuilder.equal(root.get(Booking_.PAYMENT_METHOD).get("id"), paymentMethodId);
                    predicates.add(paymentMethodPredicate);
                }
                if (!StringUtils.isEmpty(paymentMethodName)) {
					Join<Booking, PaymentMethod> bookingPaymentJoin = root.join("paymentMethod");
					Predicate bookingPredicate = criteriaBuilder.like(
							criteriaBuilder.lower(bookingPaymentJoin.get("name")),
							"%" + paymentMethodName.toLowerCase() + "%"
					);
					predicates.add(bookingPredicate);
				}
                if (!Objects.isNull(status)) {
                    final Predicate statusPredicate = criteriaBuilder.equal(root.get(Booking_.STATUS), status);
                    predicates.add(statusPredicate);
                }

                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }
}