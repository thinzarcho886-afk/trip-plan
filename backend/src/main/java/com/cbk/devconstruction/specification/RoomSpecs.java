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

import com.cbk.devconstruction.enums.Gender;
import com.cbk.devconstruction.enums.RoomStatus;
import com.cbk.devconstruction.enums.Status;
import com.cbk.devconstruction.entity.City;
import com.cbk.devconstruction.entity.City_;
import com.cbk.devconstruction.entity.Hostel;
import com.cbk.devconstruction.entity.Owner;
import com.cbk.devconstruction.entity.Room;
import com.cbk.devconstruction.entity.RoomType;
import com.cbk.devconstruction.entity.Room_;
import com.cbk.devconstruction.entity.Township;
import com.cbk.devconstruction.entity.Township_;
import com.cbk.devconstruction.entity.User;

public class RoomSpecs {
	public static Specification<Room> getByFilter(String roomNo,String roomTypeName, RoomStatus roomStatus,  Status status, Boolean has, Gender gender,String hostelName,Long hostelId, String ownerName){
		return new Specification<Room>() {
			private static final long serialVersionUID=1L;
			@Override
			public Predicate toPredicate(Root<Room> root,CriteriaQuery<?> query,CriteriaBuilder criteriaBuilder) {
				final Collection<Predicate> predicates=new ArrayList<>();
				
				
				if(!StringUtils.isEmpty(roomNo)) {
					final Predicate roomNoPredicate=criteriaBuilder.like(criteriaBuilder.lower(root.get(Room_.ROOM_NO)),"%"+roomNo.toLowerCase()+"%");
					predicates.add(roomNoPredicate);
				}
				
				if(!Objects.isNull(has)) {
					if(has) {
						Predicate hasHostel=criteriaBuilder.isNotNull(root.get(Room_.HOSTEL));
						predicates.add(hasHostel);
					}else {
						Predicate noHostel=criteriaBuilder.isNull(root.get(Room_.HOSTEL));
						predicates.add(noHostel);
					}
				}
				
				if (!StringUtils.isEmpty(roomTypeName)) {
			          Join<Room, RoomType> roomRoomTypeJoin = root.join("roomType");
			          
			          Predicate roomPredicate = criteriaBuilder.like(criteriaBuilder.lower(roomRoomTypeJoin.get("name")), "%" + roomTypeName.toLowerCase() + "%");
			          predicates.add(roomPredicate);
			    }
				
				if (!StringUtils.isEmpty(hostelName)) {
			          Join<Room, Hostel> roomHostelJoin = root.join("hostel");
			          
			          Predicate roomPredicate = criteriaBuilder.like(criteriaBuilder.lower(roomHostelJoin.get("hostelName")), "%" + hostelName.toLowerCase() + "%");
			          predicates.add(roomPredicate);
			    }
				if (!StringUtils.isEmpty(ownerName)) {
		            Join<Room, Hostel> roomhostelJoin = root.join("hostel"); 
		            Join<Hostel, User> hostelUserJoin = roomhostelJoin.join("user"); 
		            
		            Predicate ownerPredicate = criteriaBuilder.like(criteriaBuilder.lower(hostelUserJoin.get("username")), "%" + ownerName.toLowerCase() + "%");
		            predicates.add(ownerPredicate);
		          }
			    
//				if (!Objects.nonNull(hostelId)) {
//			          Join<Room, Hostel> roomHostelJoin = root.join("hostel");
//			          
//			          Predicate roomPredicate = criteriaBuilder.equal(roomHostelJoin.get("id"),hostelId);
//			          predicates.add(roomPredicate);
//			    }
				if (hostelId != null) {
				    Predicate hostelIdPredicate = criteriaBuilder.equal(root.get("hostel").get("id"), hostelId);
				    predicates.add(hostelIdPredicate);
				}
				if (!Objects.isNull(roomStatus)) {
					Predicate roomStatusPredicate = criteriaBuilder.equal(root.get(Room_.ROOM_STATUS), roomStatus);
					predicates.add(roomStatusPredicate);
				}
				
				if (!Objects.isNull(status)) {
					Predicate statusPredicate = criteriaBuilder.equal(root.get(Room_.STATUS), status);
					predicates.add(statusPredicate);
				}
				
				if (!Objects.isNull(gender)) {
					Join<Room, Hostel> roomHostelJoin = root.join("hostel");
				      Predicate roomPredicate = criteriaBuilder.equal(roomHostelJoin.get("gender"), gender);
				      predicates.add(roomPredicate);
			    }

				
				query.orderBy(criteriaBuilder.desc(root.get(Room_.ID)));
				return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
			}
			
			
			
		};
	}

	

}
