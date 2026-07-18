package com.cbk.trip.service;

import java.io.IOException;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.cbk.trip.dto.BookingDTO;
import com.cbk.trip.dto.PageableDTO;
import com.cbk.trip.entity.Booking;
import com.cbk.trip.enums.Status;
import com.cbk.trip.repository.BookingRepository;
import com.cbk.trip.repository.CustomerRepository;
import com.cbk.trip.repository.PackageRepository;
import com.cbk.trip.repository.PaymentMethodRepository;
import com.cbk.trip.specification.BookingSpecs;
import com.cbk.trip.utils.CommonUtil;
import com.cbk.trip.utils.NginxUtil;

@Service
public class BookingService {

	@Autowired
	private BookingRepository bookingRepository;

	@Autowired
	private PackageRepository packageRepository;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private PaymentMethodRepository paymentMethodRepository;

	public PageableDTO getBookings(Long packageId, Long customerId, Long paymentMethodId, Status status,
			Pageable pageable) {
		Specification<Booking> specs = BookingSpecs.getByFilter(packageId, customerId, paymentMethodId, status);
		Page<Booking> page = bookingRepository.findAll(specs, pageable);
		List<BookingDTO> dtoList = CommonUtil.getDTOList(page.getContent(), BookingDTO::new);
		return new PageableDTO(dtoList, page);
	}

	@Transactional(rollbackFor = Exception.class)
	public BookingDTO save(@Valid BookingDTO dto, boolean isUpdate) throws IOException {
		Booking booking = new Booking();
		if (isUpdate) {
			booking = CommonUtil.checkValidById(dto.getId(), bookingRepository);
			booking.setPaymentReceiveImageUrl(NginxUtil.updateImage(dto.getPaymentReceiveImage(),
					booking.getPaymentReceiveImageUrl(), "booking_receive", StringUtils.isEmpty(dto.getPaymentReceiveImageUrl())));
		} else {
			booking.setPaymentReceiveImageUrl(NginxUtil.saveImage(dto.getPaymentReceiveImage(), "booking_receive"));
		}

		booking.setPkg(CommonUtil.checkValidById(dto.getPackageId(), packageRepository));
		booking.setCustomer(CommonUtil.checkValidById(dto.getCustomerId(), customerRepository));
		booking.setPaymentMethod(CommonUtil.checkValidById(dto.getPaymentMethodId(), paymentMethodRepository));
		booking.setTravelersQty(dto.getTravelersQty());
		booking.setStatus(dto.getStatus());
		booking.setNote(dto.getNote());

		Booking savedBooking = bookingRepository.save(booking);
		return new BookingDTO(savedBooking);
	}

	public BookingDTO getById(Long id) {
		Booking booking = bookingRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Booking not found with id: " + id));
		return new BookingDTO(booking);
	}
}