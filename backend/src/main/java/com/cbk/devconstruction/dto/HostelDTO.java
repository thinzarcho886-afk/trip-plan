package com.cbk.devconstruction.dto;

import java.util.ArrayList;
import java.util.List;

import com.cbk.devconstruction.entity.Hostel;
import com.cbk.devconstruction.enums.Gender;
import com.cbk.devconstruction.enums.HostelStatus;
import com.cbk.devconstruction.enums.Status;
import com.cbk.devconstruction.utils.CommonUtil;
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
public class HostelDTO extends CommonDTO{
	
	private static final long serialVersionUID=1L;
	
	private Long cityId;
	private String cityName;
	
	private Long townshipId;
	private String townshipName;
	
	private Long streetId;
	private String streetName;
	
	private Long userId;
	private String userName;
	private String phone;
	
	private String hostelImage;
	private String hostelImageUrl;
	
	private String hostelName;
	
	private Gender gender;
	
	private String description;
	
	private HostelStatus hostelStatus;
	
	private Status status;
	
	private Double latitude;
	private Double longitude;
	
	private List<RoomDTO> roomList=new ArrayList<>();
	private Long[] deleteRoomDetailIds;
	
	
	public HostelDTO(Hostel entity) {
		super(entity);
		
		if(entity.getCity()!=null) {
			this.cityId=entity.getCity().getId();
			this.cityName=entity.getCity().getCityName();
		}
		
		if(entity.getTownship()!=null) {
			this.townshipId=entity.getTownship().getId();
			this.townshipName=entity.getTownship().getTownshipName();
		}
		
		if(entity.getStreet()!=null) {
			this.streetId=entity.getStreet().getId();
			this.streetName=entity.getStreet().getStreetName();
		}
		
		if(entity.getUser()!= null) {
			this.userId=entity.getUser().getId();
			this.userName=entity.getUser().getUsername();
			this.phone=entity.getUser().getOwner().getPhone();
			
		}
		
		
		this.hostelImage = NginxUtil.getFileUrl(entity.getHostelImage(), true);
		this.hostelImageUrl=entity.getHostelImage();
		this.hostelName=entity.getHostelName();
		this.gender=entity.getGender();
		this.description=entity.getDescription();
		this.hostelStatus=entity.getHostelStatus();
		this.status=entity.getStatus();
		this.latitude=entity.getLatitude();
		this.longitude=entity.getLongitude();
		
		if(entity.getRoomList() != null) {
			this.roomList=CommonUtil.getDTOList(entity.getRoomList(), RoomDTO::new);

		}
	}
	
	

}
