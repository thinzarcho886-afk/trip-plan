package com.cbk.devconstruction.specification;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import com.cbk.devconstruction.entity.Company;
import com.cbk.devconstruction.entity.Company_;
import com.cbk.devconstruction.enums.Status;

public class CompanySpecs {

	public static Specification<Company> getAllCompanies(String code, String name, Status status) {
		return new Specification<Company>() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<Company> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				final Collection<Predicate> predicates = new ArrayList<>();

				if (!StringUtils.isEmpty(code)) {
					final Predicate codePredicate = criteriaBuilder.equal(root.get(Company_.CODE), code);
					predicates.add(codePredicate);
				}

				if (!StringUtils.isEmpty(name)) {
					final Predicate namePredicate = criteriaBuilder.like(root.get(Company_.NAME), "%" + name + "%");
					predicates.add(namePredicate);
				}

				if (!Objects.isNull(status)) {
					final Predicate statusPredicate = criteriaBuilder.equal(root.get(Company_.STATUS), status);
					predicates.add(statusPredicate);
				}

				return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
			}

		};
	}

}
