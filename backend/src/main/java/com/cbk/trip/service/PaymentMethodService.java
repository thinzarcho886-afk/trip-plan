package com.cbk.trip.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.cbk.trip.dto.PageableDTO;
import com.cbk.trip.dto.PaymentMethodDTO;
import com.cbk.trip.entity.PaymentMethod;
import com.cbk.trip.enums.Status;
import com.cbk.trip.repository.PaymentMethodRepository;
import com.cbk.trip.specification.PaymentMethodSpecs;
import com.cbk.trip.utils.CommonUtil;
import com.cbk.trip.utils.NginxUtil;

@Service
public class PaymentMethodService {

	@Autowired
	PaymentMethodRepository paymentMethodRepository;

	public boolean isAccountNumberDuplicate(String accountNumber, Long id) {
		Optional<PaymentMethod> exist = paymentMethodRepository.findByAccountNumberAndIdNot(accountNumber, id != null ? id : -1L);
		return exist.isPresent();
	}

	public PageableDTO getPaymentMethods(String name, String accountNumber, String accountName, Status status, Pageable pageable) {
	    Specification<PaymentMethod> spec = PaymentMethodSpecs.getByFilter(name, accountNumber, accountName, status);
	    Page<PaymentMethod> page = paymentMethodRepository.findAll(spec, pageable);
	    
	    List<PaymentMethodDTO> dtoList = page.getContent().stream()
	            .map(PaymentMethodDTO::new)
	            .collect(Collectors.toList());

	    return new PageableDTO(dtoList, page);
	}

	@Transactional(rollbackFor = Exception.class)
	public PaymentMethodDTO save(@Valid PaymentMethodDTO dto, boolean isUpdate) throws IOException{
		PaymentMethod entity = isUpdate ? CommonUtil.checkValidById(dto.getId(), paymentMethodRepository) : new PaymentMethod();

		entity.setName(dto.getName());
		entity.setAccountNumber(dto.getAccountNumber());
		entity.setAccountName(dto.getAccountName());
		entity.setDescription(dto.getDescription());
		if (StringUtils.isEmpty(dto.getImageUrl())) {
		    entity.setImageUrl(null);
		} else if (dto.getImageUrl().startsWith("data:image")) {
		    if (!isUpdate) {
		        entity.setImageUrl(NginxUtil.saveImage(dto.getImageUrl(), "payment_method"));
		    } else {
		        entity.setImageUrl(NginxUtil.updateImage(dto.getImageUrl(), entity.getImageUrl(), "payment_method", false));
		    }
		} else {
		    entity.setImageUrl(dto.getImageUrl());
		}
		
		entity.setStatus(dto.getStatus());

		return new PaymentMethodDTO(paymentMethodRepository.save(entity));
	}

	public List<PaymentMethodDTO> getByStatus(Status status) {
		List<PaymentMethod> list = paymentMethodRepository.findByStatus(status);
		return list.stream().map(PaymentMethodDTO::new).collect(Collectors.toList());
	}

	public PaymentMethodDTO getById(Long id) {
		return new PaymentMethodDTO(CommonUtil.checkValidById(id, paymentMethodRepository));
	}
}