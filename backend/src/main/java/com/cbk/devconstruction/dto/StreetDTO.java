package com.cbk.devconstruction.dto;


import org.hibernate.validator.constraints.Length;

import com.cbk.devconstruction.entity.Street;
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
public class StreetDTO extends CommonDTO{
	private static final long serialVersionUID=1L;
	
	private Long townshipId;
	private String townshipName;
	

	@Length(max=50)
	private String streetName;
	
	private Status status;
	
	public StreetDTO(Street entity) {
		super(entity);
		if(entity.getTownship()!=null) {
			this.townshipId=entity.getTownship().getId();
			this.townshipName=entity.getTownship().getTownshipName();
			
		}
		this.streetName=entity.getStreetName();
		this.status=entity.getStatus();
		
	}

}
