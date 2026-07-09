package com.cbk.trip.dto;

import java.util.ArrayList;
import java.util.List;

import com.cbk.trip.entity.Hostel;
import com.cbk.trip.enums.HostelStatus;
import com.cbk.trip.enums.Status;
import com.cbk.trip.utils.CommonUtil;
import com.cbk.trip.utils.NginxUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
@NoArgsConstructor
public class HostelDTO extends CommonDTO {

	private static final long serialVersionUID = 1L;

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

	private String description;

	private HostelStatus hostelStatus;

	private Status status;

	private Double latitude;
	private Double longitude;

	private List<RoomDTO> roomList = new ArrayList<>();
	private Long[] deleteRoomDetailIds;

	public HostelDTO(Hostel entity) {
		super(entity);

		if (entity.getUser() != null) {
			this.userId = entity.getUser().getId();
			this.userName = entity.getUser().getUsername();
		}

		this.hostelImage = NginxUtil.getFileUrl(entity.getHostelImage(), true);
		this.hostelImageUrl = entity.getHostelImage();
		this.hostelName = entity.getHostelName();
		this.description = entity.getDescription();
		this.hostelStatus = entity.getHostelStatus();
		this.status = entity.getStatus();
		this.latitude = entity.getLatitude();
		this.longitude = entity.getLongitude();

		if (entity.getRoomList() != null) {
			this.roomList = CommonUtil.getDTOList(entity.getRoomList(), RoomDTO::new);

		}
	}

}
