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
import com.cbk.devconstruction.entity.User;
import com.cbk.devconstruction.entity.City;
import com.cbk.devconstruction.entity.Hostel;
import com.cbk.devconstruction.entity.Owner;
import com.cbk.devconstruction.entity.Street;
import com.cbk.devconstruction.enums.Gender;
import com.cbk.devconstruction.enums.HostelStatus;
import com.cbk.devconstruction.enums.Status;

public class HostelSpecs {
  public static Specification<Hostel> getByFilter(String cityName, String townshipName, String streetName, String ownerName, String hostelName, Gender gender, HostelStatus hostelStatus, Status status,String currUserId){
    return new Specification<Hostel>() {
      private static final long serialVersionUID = 1L;
      
      @Override
      public Predicate toPredicate(Root<Hostel> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        final Collection<Predicate> predicates = new ArrayList<>();
        
     
        if (!StringUtils.isEmpty(cityName)) {
          Join<Hostel, City> hostelCityJoin = root.join("city"); 
          
          Predicate cityPredicate = criteriaBuilder.like(criteriaBuilder.lower(hostelCityJoin.get("cityName")), "%" + cityName.toLowerCase() + "%");
          predicates.add(cityPredicate);
        }
        
        if (!StringUtils.isEmpty(townshipName)) {
          Join<Hostel, Township> hostelTownshipJoin = root.join("township");
          
          Predicate townshipPredicate = criteriaBuilder.like(criteriaBuilder.lower(hostelTownshipJoin.get("townshipName")), "%" + townshipName.toLowerCase() + "%");
          predicates.add(townshipPredicate);
        }
        
        if (!StringUtils.isEmpty(streetName)) {
          Join<Hostel, Street> hostelStreetJoin = root.join("street");
          
          Predicate streetPredicate = criteriaBuilder.like(criteriaBuilder.lower(hostelStreetJoin.get("streetName")), "%" + streetName.toLowerCase() + "%");
          predicates.add(streetPredicate);
        }
        
        if (!StringUtils.isEmpty(ownerName)) {
            Join<Hostel, User> hostelUserJoin = root.join("user"); 
            Join<User, Owner> userOwnerJoin = hostelUserJoin.join("owner"); 
            
            Predicate ownerPredicate = criteriaBuilder.like(criteriaBuilder.lower(userOwnerJoin.get("name")), "%" + ownerName.toLowerCase() + "%");
            predicates.add(ownerPredicate);
          }
        
        if (!StringUtils.isEmpty(hostelName)) {
          final Predicate namePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("hostelName")), "%" + hostelName.toLowerCase() + "%");
          predicates.add(namePredicate);
        }
        
        if (!Objects.isNull(gender)) {
            Predicate genderPredicate = criteriaBuilder.equal(root.get("gender"), gender);
            predicates.add(genderPredicate);
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
        
        