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

import com.cbk.trip.entity.Bus;
import com.cbk.trip.entity.BusType;
import com.cbk.trip.entity.Hostel;
import com.cbk.trip.entity.Room;
import com.cbk.trip.entity.RoomType;
import com.cbk.trip.entity.Room_;
import com.cbk.trip.entity.Transport;
import com.cbk.trip.entity.Transport_;
import com.cbk.trip.entity.User;
import com.cbk.trip.enums.Status;

public class TransportSpecs {
	public static Specification<Transport> getByFilter(Long  busTypeId, Long  busId) {
		return new Specification<Transport>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<Transport> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				final Collection<Predicate> predicates = new ArrayList<>();

				

				if (!Objects.isNull(busTypeId)) {
					Join<Transport, BusType> tranBusTypeJoin = root.join("busType");

					Predicate transportPredicate = criteriaBuilder.equal(tranBusTypeJoin.get("id"),busTypeId);
					predicates.add(transportPredicate);
				}
				
				if (!Objects.isNull(busId)) {
					Join<Transport, Bus> tranBusJoin = root.join("bus");

					Predicate transportPredicate = criteriaBuilder.equal(tranBusJoin.get("id"),busId);
					predicates.add(transportPredicate);
				}

			

				query.orderBy(criteriaBuilder.desc(root.get(Transport_.ID)));
				return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
			}

		};
	}

}
