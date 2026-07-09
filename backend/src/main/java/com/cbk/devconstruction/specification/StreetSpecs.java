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
import com.cbk.devconstruction.entity.Street;
import com.cbk.devconstruction.entity.Street_;

public class StreetSpecs {
	public static Specification<Street> getByFilter(Long townshipId,String streetName, Status status, Boolean has){
		return new Specification<Street>() {
			private static final long serialVersionUID=1L;
			@Override
			public Predicate toPredicate(Root<Street> root,CriteriaQuery<?> query,CriteriaBuilder criteriaBuilder) {
				final Collection<Predicate> predicates=new ArrayList<>();
				if(!Objects.isNull(townshipId)) {
					Join<Street, Township> streetTownshipJoin=root.join(Street_.TOWNSHIP);
					Predicate streetPredicate=criteriaBuilder.equal(streetTownshipJoin.get(Township_.ID), townshipId);
					predicates.add(streetPredicate);
					
				}
				
				if(!Objects.isNull(has)) {
					if(has) {
						Predicate hasTownship=criteriaBuilder.isNotNull(root.get(Street_.TOWNSHIP));
						predicates.add(hasTownship);
					}else {
						Predicate noTownship=criteriaBuilder.isNull(root.get(Street_.TOWNSHIP));
						predicates.add(noTownship);
					}
				}
				
				if(!StringUtils.isEmpty(streetName)) {
					final Predicate namePredicate=criteriaBuilder.like(criteriaBuilder.lower(root.get(Street_.STREET_NAME)),"%"+streetName.toLowerCase()+"%");
					predicates.add(namePredicate);
				}
				
				
				if (!StringUtils.isEmpty(status)) {
					Predicate statusPredicate = criteriaBuilder.equal(root.get(Street_.STATUS), status);
					predicates.add(statusPredicate);
				}

				
				query.orderBy(criteriaBuilder.desc(root.get(Street_.ID)));
				return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
			}
			
			
			
		};
	}

	

}
