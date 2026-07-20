package com.cbk.trip.service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.cbk.trip.dto.PackageDTO;
import com.cbk.trip.dto.PackageDetailDTO;
import com.cbk.trip.dto.PageableDTO;
import com.cbk.trip.entity.*;
import com.cbk.trip.entity.Package;
import com.cbk.trip.enums.Status;
import com.cbk.trip.exception.BadRequestException;
import com.cbk.trip.exception.ResourceNotFoundException;
import com.cbk.trip.repository.*;
import com.cbk.trip.specification.PackageSpecs;
import com.cbk.trip.utils.CommonUtil;
import com.cbk.trip.utils.NginxUtil;

@Service
public class PackageService {

    @Autowired
    private PackageRepository packageRepository;
    
    @Autowired
    private PackageDetailRepository packageDetailRepository;

    @Autowired
    private DestinationRepository destinationRepository;

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private TransportRepository transportRepository;

    @Autowired
    private DurationRepository durationRepository;

    public PageableDTO getPackages(String name, Long destinationId, Long durationId, Status status, Pageable pageable) {
        Specification<Package> specs = PackageSpecs.getByFilter(name, destinationId, durationId, status);
        Page<Package> page = packageRepository.findAll(specs, pageable);
        List<PackageDTO> dtoList = CommonUtil.getDTOList(page.getContent(), PackageDTO::new);
        return new PageableDTO(dtoList, page);
    }
    @Transactional(rollbackFor = Exception.class)
    public void save(PackageDTO dto, boolean isUpdate) throws IOException {
        Package pkg;

        if (isUpdate) {
            pkg = CommonUtil.checkValidById(dto.getId(), packageRepository);
            if (packageRepository.existsByNameAndIdNot(dto.getName(), dto.getId())) {
                throw new BadRequestException("Package name is already exists.");
            }
            
            pkg.setPackageImageUrl(NginxUtil.updateImage(dto.getPackageImage(), pkg.getPackageImageUrl(),
					"package_image", StringUtils.isEmpty(dto.getPackageImageUrl())));
	
            
            if (dto.getDeletePackageDetailIds() != null && dto.getDeletePackageDetailIds().length > 0) {
	              for (Long packageDetailId : dto.getDeletePackageDetailIds()) {
	                  if (packageDetailId != null) {
	                      packageDetailRepository.findById(packageDetailId).ifPresent(packageDetail -> {
	                          packageDetail.setPackageEntity(null);
	                          packageDetailRepository.save(packageDetail);
	                      });
	                  }
	              }
	          }
            
        } else {
            if (packageRepository.existsByName(dto.getName())) {
                throw new BadRequestException("Package name is already exists.");
            }
            pkg = new Package();
			pkg.setPackageImageUrl(NginxUtil.saveImage(dto.getPackageImage(), "package_image"));

        }

        // ... Package Field mapping များ (အပြောင်းအလဲမရှိပါ) ...
        pkg.setName(dto.getName());
        pkg.setDestination(destinationRepository.findById(dto.getDestinationId()).orElse(null));
        pkg.setHotel(hotelRepository.findById(dto.getHotelId()).orElse(null));
        pkg.setTransport(transportRepository.findById(dto.getTransportId()).orElseThrow(()-> new ResourceNotFoundException("Invalid transport id.")));
        pkg.setDuration(durationRepository.findById(dto.getDurationId()).orElse(null));
        pkg.setDepartureDate(dto.getDepartureDate());
        pkg.setTransportFee(dto.getTransportFee());
        pkg.setHotelFee(dto.getHotelFee());
        pkg.setServiceFee(dto.getServiceFee());
        pkg.setBudgetAmount(dto.getBudgetAmount());
        pkg.setDescription(dto.getDescription());
        pkg.setExtraService(dto.getExtraService());
        pkg.setStatus(dto.getStatus());

        Package savedPackage = packageRepository.save(pkg);

        if (dto.getPackageDetails() != null) {
            List<String> places = dto.getPackageDetails().stream()
                    .map(PackageDetailDTO::getPlaceToVisit).collect(Collectors.toList());
            if (places.size() != places.stream().distinct().count()) {
                throw new BadRequestException("Duplicate place to visit name in the list.");
            }

            for (PackageDetailDTO detailDto : dto.getPackageDetails()) {
                PackageDetail detail;
                
                // 🛠️ ပြင်ဆင်ချက် - Package ကို update လုပ်နေပေမယ့်လည်း 
                // Child list ထဲမှာ id မပါလာတဲ့ Place အသစ်ဖြစ်နေရင် new အဖြစ် ဆောက်ပေးရပါမယ်။
                if (isUpdate && detailDto.getId() != null) {
                    // repository နေရာမှာ null ဖြစ်နေတာကို packageDetailRepository လို့ မှန်အောင် ပြင်လိုက်ပါတယ်
                    detail = CommonUtil.checkValidById(detailDto.getId(), packageDetailRepository);
                } else {
                    detail = new PackageDetail();
                }
                
                detail.setPackageEntity(savedPackage);
                detail.setPlaceToVisit(detailDto.getPlaceToVisit());
                
                // Image Upload Logic
                if (StringUtils.isEmpty(detailDto.getImageUrl())) {
                    detail.setImageUrl(null);
                } else if (detailDto.getImageUrl().startsWith("data:image")) {
                    // detail.getId() ပေါ်မူတည်ပြီး အသစ်သိမ်းမလား၊ အဟောင်းပေါ် update လုပ်မလား ခွဲခြားခြင်း
                    if (detail.getId() == null) {
                        detail.setImageUrl(NginxUtil.saveImage(detailDto.getImageUrl(), "place_detail"));
                    } else {
                        detail.setImageUrl(NginxUtil.updateImage(detailDto.getImageUrl(), detail.getImageUrl(), "place_detail", false));
                    }
                } else {
                    detail.setImageUrl(detailDto.getImageUrl());
                }
                
                packageDetailRepository.save(detail);
            }
        }
    }
    public PackageDTO getById(Long id) {
        Package pkg = packageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Package not found with id: " + id));
        return new PackageDTO(pkg);
    }
}