package com.cbk.devconstruction.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.cbk.devconstruction.dto.HostelDTO;
import com.cbk.devconstruction.dto.PageableDTO;
import com.cbk.devconstruction.dto.SearchHistoryDTO;
import com.cbk.devconstruction.entity.Hostel;
import com.cbk.devconstruction.entity.SearchHistory;
import com.cbk.devconstruction.entity.Student;
import com.cbk.devconstruction.entity.User;
import com.cbk.devconstruction.enums.Gender;
import com.cbk.devconstruction.enums.HostelStatus;
import com.cbk.devconstruction.enums.UserRole;
import com.cbk.devconstruction.exception.BadRequestException;
import com.cbk.devconstruction.exception.ResourceNotFoundException;
import com.cbk.devconstruction.repository.HostelRepository;
import com.cbk.devconstruction.repository.SearchHistoryRepository;
import com.cbk.devconstruction.repository.UserRepository;
import com.cbk.devconstruction.specification.SearchHistorySpecs;
import com.cbk.devconstruction.utils.CommonUtil;

@Service
public class SearchHistoryService {
	
	@Autowired
	SearchHistoryRepository searchHistoryRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	UserService userService;
	

	
	@Autowired
	HostelService hostelService;
	
	@Autowired
	HostelRepository hostelRepository;
	
	
	public PageableDTO getMostSearchedHostelsByFilter(
	        LocalDate fromDate, LocalDate toDate, String cityName, String townshipName, 
	        String streetName, String ownerName, String hostelName, 
	        HostelStatus hostelStatus, Gender gender, Pageable pageable) {
	    
	    Specification<SearchHistory> historySpecs = SearchHistorySpecs.getByFilter(
	            fromDate, toDate, cityName, townshipName, streetName, ownerName, hostelName, hostelStatus, gender);
	    
	    List<SearchHistory> filteredHistories = searchHistoryRepository.findAll(historySpecs);
	    
	    if (filteredHistories.isEmpty()) {
	        return new PageableDTO(new ArrayList<>(), Page.empty());
	    }

	    Map<Long, Long> hostelSearchCount = filteredHistories.stream()
	            .collect(Collectors.groupingBy(sh -> sh.getHostel().getId(), Collectors.counting()));

	    List<Long> sortedHostelIds = hostelSearchCount.entrySet().stream()
	            .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue())) // Count အများဆုံးကနေ အနည်းဆုံးသို့ စီခြင်း
	            .map(Map.Entry::getKey)
	            .collect(Collectors.toList());

	    int start = (int) pageable.getOffset();
	    int end = Math.min((start + pageable.getPageSize()), sortedHostelIds.size());
	    
	    if (start > sortedHostelIds.size()) {
	        return new PageableDTO(new ArrayList<>(), Page.empty());
	    }
	    
	    List<Long> pagedHostelIds = sortedHostelIds.subList(start, end);

	    List<Hostel> hostels = hostelRepository.findAllById(pagedHostelIds);
	    
	    hostels.sort(Comparator.comparingInt(hostel -> pagedHostelIds.indexOf(hostel.getId())));

	    List<HostelDTO> hostelDTOList = CommonUtil.getDTOList(hostels, HostelDTO::new);

	    Page<HostelDTO> resultPage = new PageImpl<>(hostelDTOList, pageable, sortedHostelIds.size());
	    
	    return new PageableDTO(hostelDTOList, resultPage);
	}
	
	
	
	
	
	
	public PageableDTO getHistorys(LocalDate fromDate, LocalDate toDate, String cityName, String townshipName, String streetName, String ownerName, String hostelName, HostelStatus hostelStatus,Gender gender,Pageable pageable) {
		Specification<SearchHistory> historySpecs=SearchHistorySpecs.getByFilter(fromDate, toDate, cityName, townshipName, streetName, ownerName, hostelName, hostelStatus, gender);
		Page<SearchHistory> page=searchHistoryRepository.findAll(historySpecs,pageable);
		List<SearchHistory> reviewList=page.getContent();
		List<SearchHistoryDTO> dtoList=CommonUtil.getDTOList(reviewList, SearchHistoryDTO::new);
		PageableDTO pageableDTO=new PageableDTO(dtoList,page);
		return pageableDTO;
		
		
	}
	
	@Transactional(rollbackFor = Exception.class)
	public SearchHistoryDTO save(@Valid SearchHistoryDTO dto) {
		SearchHistory history = new SearchHistory();

		
		User user = userService.checkValidUser(dto.getUserId());
				
		if (!user.getRole().equals(UserRole.STUDENT)) {
					throw new BadRequestException("This user is not a student!");
		}
				
		history.setUser(user);		
		history.setHostel(hostelService.checkValidHostel(dto.getHostelId()));
		history.setDatetime(dto.getDatetime());
		
		
		searchHistoryRepository.save(history);

		SearchHistoryDTO saveHistory = new SearchHistoryDTO(history);

		return saveHistory;

	}
	
	
	
	SearchHistory checkValidHistory(Long id) {
		return CommonUtil.checkValidById(id, searchHistoryRepository);
	}

	public SearchHistory getById(Long id) {
		// TODO Auto-generated method stub
		return CommonUtil.checkValidById(id, searchHistoryRepository);
	}

	

	public boolean existsByUserIdAndHostelIdAndDatetime(Long id, Long id2, LocalDate now) {

		if(searchHistoryRepository.existsByUserIdAndHostelIdAndDatetime(id, id2, now)) {
			return true;
		}else {
			return false;
		}
	}

	
	
}
