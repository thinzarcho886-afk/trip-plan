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

import com.cbk.trip.entity.Hostel;
import com.cbk.trip.enums.HostelStatus;
import com.cbk.trip.enums.Status;

public class HostelSpecs {
	public static Specification<Hostel> getByFilter(String cityName, String townshipName, String streetName,
			String ownerName, String hostelName, HostelStatus hostelStatus, Status status, String currUserId) {
		return new Specification<Hostel>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<Hostel> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				final Collection<Predicate> predicates = new ArrayList<>();

//        if (!StringUtils.isEmpty(ownerName)) {
//            Join<Hostel, User> hostelUserJoin = root.join("user"); 
//            Join<User, Owner> userOwnerJoin = hostelUserJoin.join("owner"); 
//            
//            Predicate ownerPredicate = criteriaBuilder.like(criteriaBuilder.lower(userOwnerJoin.get("name")), "%" + ownerName.toLowerCase() + "%");
//            predicates.add(ownerPredicate);
//          }

				if (!StringUtils.isEmpty(hostelName)) {
					final Predicate namePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("hostelName")),
							"%" + hostelName.toLowerCase() + "%");
					predicates.add(namePredicate);
				}

				if (!Objects.isNull(hostelStatus)) {
					Predicate hostelStatusPredicate = criteriaBuilder.equal(root.get("hostelStatus"), hostelStatus);
					predicates.add(hostelStatusPredicate);
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
