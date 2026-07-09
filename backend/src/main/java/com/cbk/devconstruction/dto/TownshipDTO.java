package com.cbk.devconstruction.dto;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;

import com.cbk.devconstruction.entity.Township;
import com.cbk.devconstruction.enums.Status;
import com.cbk.devconstruction.utils.CommonUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
@NoArgsConstructor
public class TownshipDTO extends CommonDTO{
	
	private static final long serialVersionUID=1L;
	

	private Long cityId;
	private String cityName;
	
	@NotBlank
	@Length(max=50)
	private String townshipName;
	
	private Status status;
	
	private List<StreetDTO> streetDetailList=new ArrayList<>();
	private Long[] deleteStreetDetailIds;
	
	public TownshipDTO(Township entity) {
		super(entity);
		if(entity.getCity()!=null) {
			this.cityId=entity.getCity().getId();
			this.cityName=entity.getCity().getCityName();
			
		}
		this.townshipName=entity.getTownshipName();
		this.status=entity.getStatus();
		if(entity.getStreetDetailList() != null) {
			this.streetDetailList=CommonUtil.getDTOList(entity.getStreetDetailList(), StreetDTO::new);

		}
		
	}
	
	
	
	

}
