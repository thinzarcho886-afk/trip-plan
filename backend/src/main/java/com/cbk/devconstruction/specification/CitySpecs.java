package com.cbk.devconstruction.specification;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import com.cbk.devconstruction.entity.City;
import com.cbk.devconstruction.entity.City_;
import com.cbk.devconstruction.enums.Status;


public class CitySpecs {
	public static Specification<City> getByFilter(String cityName,Status status){
		return new Specification<City>() {
			private static final long serialVersionUID=1L;
			
			@Override
			public Predicate toPredicate(Root<City> root,CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				final Collection<Predicate> predicates= new ArrayList<>();
				if(!StringUtils.isEmpty(cityName)) {
					final Predicate namePredicate=criteriaBuilder.like(criteriaBuilder.lower(root.get(City_.CITY_NAME)),"%"+cityName.toLowerCase()+"%");
					predicates.add(namePredicate);
				}
				
				if (!StringUtils.isEmpty(status)) {
					Predicate statusPredicate = criteriaBuilder.equal(root.get(City_.STATUS), status);
					predicates.add(statusPredicate);
				}
				
				query.orderBy(criteriaBuilder.desc(root.get(City_.ID)));
				
				return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
			}
			
		};
		}

}
