package com.cbk.devconstruction.dto;

import java.time.LocalDate;

import com.cbk.devconstruction.entity.SearchHistory;
import com.cbk.devconstruction.enums.Gender;
import com.cbk.devconstruction.enums.HostelStatus;
import com.cbk.devconstruction.enums.Status;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
@NoArgsConstructor
public class SearchHistoryDTO {
	
	public static final long serialVersionUID=1L;
	
	private Long id;
	
	private Long userId;
	private String userName;
	
	private Long hostelId;
	private String hostelName;
	private String hostelImage;
	
	private String cityName;
	
	private String townshipName;
	
	private String streetName;
	
	private String ownerName;
	private Gender gender;
	private String phone;
	private HostelStatus hostelStatus;
	private Status status;
	private String descriiption;
	private LocalDate datetime;
	
	public SearchHistoryDTO (SearchHistory entity) {
		this.id=entity.getId();
		
		if(entity.getUser()!=null) {
			this.userId=entity.getUser().getId();
			this.userName=entity.getUser().getUsername();
		}
		if(entity.getHostel()!=null) {
			this.hostelId=entity.getHostel().getId();
			this.hostelName=entity.getHostel().getHostelName();
			this.hostelImage=entity.getHostel().getHostelImage();
			this.gender=entity.getHostel().getGender();
			this.hostelStatus=entity.getHostel().getHostelStatus();
			this.status=entity.getHostel().getStatus();
			this.descriiption=entity.getHostel().getDescription();
			
			if(entity.getHostel().getCity()!=null) {
				this.cityName=entity.getHostel().getCity().getCityName();
			}
			
			if(entity.getHostel().getTownship()!=null) {
				this.townshipName=entity.getHostel().getTownship().getTownshipName();
			}
			
			if(entity.getHostel().getStreet()!=null) {
				this.streetName=entity.getHostel().getStreet().getStreetName();
			}
			
			if(entity.getHostel().getUser()!=null) {
				this.ownerName=entity.getHostel().getUser().getUsername();
			}
			
		}
		this.datetime=entity.getDatetime();
		
	}
	
		

}
