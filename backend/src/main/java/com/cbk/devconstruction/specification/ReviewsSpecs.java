package com.cbk.devconstruction.specification;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Predicate;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;
import com.cbk.devconstruction.entity.Reviews;
import com.cbk.devconstruction.entity.Student;


public class ReviewsSpecs {
  public static Specification<Reviews> getByFilter(String studentName){
    return new Specification<Reviews>() {
      private static final long serialVersionUID = 1L;
      
      @Override
      public Predicate toPredicate(Root<Reviews> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        final Collection<Predicate> predicates = new ArrayList<>();
        
     
        if (!StringUtils.isEmpty(studentName)) {
          Join<Reviews, Student> reviewStudentJoin = root.join("student"); 
          
          Predicate reviewPredicate = criteriaBuilder.like(criteriaBuilder.lower(reviewStudentJoin.get("name")), "%" + studentName.toLowerCase() + "%");
          predicates.add(reviewPredicate);
        }
        
        
          query.orderBy(criteriaBuilder.desc(root.get("id")));
          
          return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
        }
      };
    }
  }
        
        