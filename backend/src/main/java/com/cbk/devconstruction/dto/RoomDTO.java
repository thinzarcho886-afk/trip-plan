package com.cbk.devconstruction.dto;



import com.cbk.devconstruction.entity.Room;
import com.cbk.devconstruction.enums.RoomStatus;
import com.cbk.devconstruction.enums.Status;
import com.cbk.devconstruction.utils.NginxUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
@NoArgsConstructor
public class RoomDTO extends CommonDTO{
	private static final long serialVersionUID=1L;
	
	private Long hostelId;
	private String hostelName;
	private String  hostelImage;
	
	private Long roomTypeId;
	private String name;
	
	
	private String roomImageUrl;
	private String roomImage;
	
	private String roomNo;
	 
	private String capacityPerRoom;
	
	private Long price;
	
	private RoomStatus roomStatus;
	
	private Integer availableUnit;
	
	private Status status;
	
	public RoomDTO(Room entity) {
		super(entity);
		if(entity.getHostel()!=null) {
			this.hostelId=entity.getHostel().getId();
			this.hostelName=entity.getHostel().getHostelName();
			this.hostelImage=entity.getHostel().getHostelImage();
			
		}
		
		if(entity.getRoomType()!=null) {
			this.roomTypeId=entity.getRoomType().getId();
			this.name=entity.getRoomType().getName();
			
		}
		
		this.roomImage = NginxUtil.getFileUrl(entity.getRoomImage(), true);
		this.roomImageUrl=entity.getRoomImage();
		this.roomNo=entity.getRoomNo();
		this.capacityPerRoom=entity.getCapacityPerRoom();
		this.price=entity.getPrice();
		this.roomStatus=entity.getRoomStatus();
		this.availableUnit=entity.getAvailableUnit();
		this.status=entity.getStatus();
		
		
		
	}

}
