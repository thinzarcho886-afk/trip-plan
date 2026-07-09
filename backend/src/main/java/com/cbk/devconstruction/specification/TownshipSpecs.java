package com.cbk.devconstruction.specification;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Predicate;


import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import com.cbk.devconstruction.entity.Township;
import com.cbk.devconstruction.entity.Township_;
import com.cbk.devconstruction.enums.Status;
import com.cbk.devconstruction.entity.City;
import com.cbk.devconstruction.entity.City_;

public class TownshipSpecs {
	public static Specification<Township> getByFilter(Long cityId,String townshipName,Status status, Boolean has){
		return new Specification<Township>() {
			private static final long serialVersionUID=1L;
			@Override
			public Predicate toPredicate(Root<Township> root,CriteriaQuery<?> query,CriteriaBuilder criteriaBuilder) {
				final Collection<Predicate> predicates=new ArrayList<>();
				if(!Objects.isNull(cityId)) {
					Join<Township, City> townshipCityJoin=root.join(Township_.CITY);
					Predicate townshipPredicate=criteriaBuilder.equal(townshipCityJoin.get(City_.ID), cityId);
					predicates.add(townshipPredicate);
					
				}
				
				
				if(!Objects.isNull(has)) {
					if(has) {
						Predicate hasCity=criteriaBuilder.isNotNull(root.get(Township_.CITY));
						predicates.add(hasCity);
						
					}else {
						Predicate noCity=criteriaBuilder.isNull(root.get(Township_.CITY));
						predicates.add(noCity);
					}
				}
				
				
				if(!StringUtils.isEmpty(townshipName)) {
					final Predicate namePredicate=criteriaBuilder.like(criteriaBuilder.lower(root.get(Township_.TOWNSHIP_NAME)),"%"+townshipName.toLowerCase()+"%");
					predicates.add(namePredicate);
				}
				
				if (!Objects.isNull(status)) {
					Predicate statusPredicate = criteriaBuilder.equal(root.get(Township_.STATUS), status);
					predicates.add(statusPredicate);
				}

				
				
				query.orderBy(criteriaBuilder.desc(root.get(Township_.ID)));
				return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
			}
			
		};
	}

}
