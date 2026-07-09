package com.cbk.devconstruction.specification;

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

import com.cbk.devconstruction.entity.Student;
import com.cbk.devconstruction.entity.Student_;
import com.cbk.devconstruction.entity.Owner;
import com.cbk.devconstruction.entity.Owner_;
import com.cbk.devconstruction.entity.User;
import com.cbk.devconstruction.entity.User_;
import com.cbk.devconstruction.enums.Status;
import com.cbk.devconstruction.enums.UserRole;

public class UserSpecs {

	public static Specification<User> getAllUsers(String studentName, String ownerName, String username, UserRole role, Status status) {
		return new Specification<User>() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				final Collection<Predicate> predicates = new ArrayList<>();

				
				

				if (!StringUtils.isEmpty(studentName)) {
					Join<User, Student> userStudentJoin = root.join(User_.student);
					final Predicate userStudentPredicate = criteriaBuilder.like(criteriaBuilder.lower(userStudentJoin.get("name")), "%" + studentName.toLowerCase() + "%");
					predicates.add(userStudentPredicate);
				}
				
				
				if (!StringUtils.isEmpty(ownerName)) {
					Join<User, Owner> userOwnerJoin = root.join(User_.owner);
					final Predicate userOwnerPredicate = criteriaBuilder.like(criteriaBuilder.lower(userOwnerJoin.get("name")), "%" + ownerName.toLowerCase() + "%");
					predicates.add(userOwnerPredicate);
				}

				
				
				
				
				if (!StringUtils.isEmpty(username)) {
					final Predicate namePredicate = criteriaBuilder.like(root.get(User_.username),
							"%" + username + "%");
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
