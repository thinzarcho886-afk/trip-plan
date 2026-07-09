package com.cbk.devconstruction.dto;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;

import com.cbk.devconstruction.entity.City;
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
public class CityDTO extends CommonDTO{
	
	public static final long serialVersionUID=1L;
	@NotBlank
	@Length(max= 50)
	private String cityName;
	
	private Status status;
	
	private List<TownshipDTO> townshipDetailList=new ArrayList<>();
	private Long[] deleteTownshipDetailIds;
	public CityDTO(City entity) {
		super(entity);
		this.cityName=entity.getCityName();
		this.status=entity.getStatus();
		if(entity.getTownshipDetailList() != null) {
			this.townshipDetailList=CommonUtil.getDTOList(entity.getTownshipDetailList(), TownshipDTO::new);

		}
		
	}

}
