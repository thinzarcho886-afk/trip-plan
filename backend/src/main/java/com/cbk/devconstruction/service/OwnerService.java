package com.cbk.devconstruction.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cbk.devconstruction.entity.Owner;
import com.cbk.devconstruction.repository.OwnerRepository;
import com.cbk.devconstruction.utils.CommonUtil;

@Service
public class OwnerService {
	public static final long serialVersionUID=1L;
	
	@Autowired
	OwnerRepository ownerRepository;
	
	public Owner checkValidTOwner(Long ownerId) {
		// TODO Auto-generated method stub
		return CommonUtil.checkValidById(ownerId, ownerRepository);
	}

}
