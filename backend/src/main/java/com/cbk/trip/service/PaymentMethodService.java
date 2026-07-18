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
		boolean isDuplicate = false;
		Optional<PaymentMethod> accountNumberExists = paymentMethodRepository.findByAccountNumber(accountNumber);
		if (accountNumberExists.isPresent()) {
			if (id == null) {
				isDuplicate = true;
			} else {
				if (!accountNumberExists.get().getId().equals(id)) {
					isDuplicate = true;
				}
			}
		}
		return isDuplicate;
	}

	public PageableDTO getPaymentMethods(String name, String accountNumber, String accountName, Status status,
			Pageable pageable) {
		Specification<PaymentMethod> spec = PaymentMethodSpecs.getByFilter(name, accountNumber, accountName, status);
		Page<PaymentMethod> page = paymentMethodRepository.findAll(spec, pageable);

		List<PaymentMethodDTO> dtoList = page.getContent().stream().map(PaymentMethodDTO::new)
				.collect(Collectors.toList());

		return new PageableDTO(dtoList, page);
	}

	@Transactional(rollbackFor = Exception.class)
	public PaymentMethodDTO save(@Valid PaymentMethodDTO dto, boolean isUpdate) throws IOException {
		PaymentMethod paymentMethod = isUpdate ? CommonUtil.checkValidById(dto.getId(), paymentMethodRepository)
				: new PaymentMethod();
		if (isUpdate) {
			paymentMethod = CommonUtil.checkValidById(dto.getId(), paymentMethodRepository);
			paymentMethod.setImageUrl(NginxUtil.updateImage(dto.getImage(), paymentMethod.getImageUrl(),
					"payment_method_image", StringUtils.isEmpty(dto.getImageUrl())));
		} else {
			paymentMethod.setImageUrl(NginxUtil.saveImage(dto.getImage(), "payment_method_image"));
		}
		paymentMethod.setName(dto.getName());
		paymentMethod.setAccountNumber(dto.getAccountNumber());
		paymentMethod.setAccountName(dto.getAccountName());
		paymentMethod.setDescription(dto.getDescription());
		paymentMethod.setStatus(dto.getStatus());

		return new PaymentMethodDTO(paymentMethodRepository.save(paymentMethod));
	}

	public List<PaymentMethodDTO> getByStatus(Status status) {
		List<PaymentMethod> list = paymentMethodRepository.findByStatus(status);
		return list.stream().map(PaymentMethodDTO::new).collect(Collectors.toList());
	}

	public PaymentMethodDTO getById(Long id) {
		return new PaymentMethodDTO(CommonUtil.checkValidById(id, paymentMethodRepository));
	}
}