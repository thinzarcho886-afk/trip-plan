package com.cbk.devconstruction.specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;
import com.cbk.devconstruction.entity.SearchHistory;
import com.cbk.devconstruction.enums.Gender;
import com.cbk.devconstruction.enums.HostelStatus;

public class SearchHistorySpecs {
    public static Specification<SearchHistory> getByFilter(LocalDate fromDate, LocalDate toDate, String cityName, String townshipName, String streetName, String ownerName, String hostelName, HostelStatus hostelStatus , Gender gender){
        return new Specification<SearchHistory>() {
            private static final long serialVersionUID = 1L;
            
            @Override
            public Predicate toPredicate(Root<SearchHistory> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                final Collection<Predicate> predicates = new ArrayList<>();
                
                Join<Object, Object> hostelJoin = root.join("hostel", JoinType.LEFT);
                
                if (fromDate != null) {
                    predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("datetime"), fromDate));
                }
                if (toDate != null) {
                    predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("datetime"), toDate));
                }
                
                if (StringUtils.hasText(hostelName)) { 
                    predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(hostelJoin.get("hostelName")), "%" + hostelName.toLowerCase() + "%"
                    ));
                }
                
                if (StringUtils.hasText(cityName)) {
                    Join<Object, Object> cityJoin = hostelJoin.join("city", JoinType.LEFT);
                    predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(cityJoin.get("cityName")), "%" + cityName.toLowerCase() + "%"
                    ));
                }
                
                if (StringUtils.hasText(townshipName)) {
                    Join<Object, Object> townshipJoin = hostelJoin.join("township", JoinType.LEFT);
                    predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(townshipJoin.get("townshipName")), "%" + townshipName.toLowerCase() + "%"
                    ));
                }

                if (StringUtils.hasText(streetName)) {
                    Join<Object, Object> streetJoin = hostelJoin.join("street", JoinType.LEFT);
                    predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(streetJoin.get("streetName")), "%" + streetName.toLowerCase() + "%"
                    ));
                }
                
                if (StringUtils.hasText(ownerName)) {
                    Join<Object, Object> ownerJoin = hostelJoin.join("owner", JoinType.LEFT);
                    predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(ownerJoin.get("name")), "%" + ownerName.toLowerCase() + "%"
                    ));
                }
                if (!Objects.isNull(hostelStatus)) {
                    Predicate statusPredicate = criteriaBuilder.equal(hostelJoin.get("hostelStatus"), hostelStatus);
                   predicates.add(statusPredicate);
               }
                
                if (!Objects.isNull(gender)) {
                    Predicate genderPredicate = criteriaBuilder.equal(hostelJoin.get("gender"), gender);
                   predicates.add(genderPredicate);
               }
                
                query.orderBy(criteriaBuilder.desc(root.get("id")));
                
                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            }
        };
    }
}