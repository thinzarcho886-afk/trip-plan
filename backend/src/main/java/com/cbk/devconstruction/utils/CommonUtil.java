package com.cbk.devconstruction.utils;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import com.cbk.devconstruction.common.MessageConst;
import com.cbk.devconstruction.dto.MessageDTO;
import com.cbk.devconstruction.entity.BaseEntity;
import com.cbk.devconstruction.entity.Student;
import com.cbk.devconstruction.exception.BadRequestException;
import com.cbk.devconstruction.exception.ResourceNotFoundException;
import com.cbk.devconstruction.helpers.KeyExtractor;

@Component
public class CommonUtil {

	private static MessageSource messageSource;

	@Autowired
	public CommonUtil(MessageSource messageSource) {
		CommonUtil.messageSource = messageSource;
	}

	/**
	 * Change String to JSON Format String
	 * 
	 * @param message
	 * @return
	 */
	public static String responseString(String message) {
		Map<String, String> responseMap = new HashMap<>();
		responseMap.put("message", message);

		return new JSONObject(responseMap).toString();
	}

	/**
	 * Response success message
	 * 
	 * @param message
	 * @return
	 */
	public static MessageDTO responseSuccessMessage(String message) {
		MessageDTO messageDTO = new MessageDTO();
		messageDTO.setMessage(message);
		return messageDTO;
	}

	/**
	 * Get userName from authentication object
	 * 
	 * @return
	 */
	public static String getUsernameFromAuthentication() {
		String username = null;
		Authentication auth = (Authentication) SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			Object p = auth.getPrincipal();
			if (p instanceof UserDetails) {
				username = ((UserDetails) p).getUsername();
			}
		}
		return username;
	}

	/**
	 * @param errors
	 * @return
	 */
	public static ResponseEntity<?> getFieldErrors(Errors errors) {
		MessageDTO message = new MessageDTO();
		message.setError(true);
		HashMap<String, String> errorMessages = new HashMap<>();
		for (FieldError fieldErr : errors.getFieldErrors()) {
			errorMessages.put(fieldErr.getField(), fieldErr.getDefaultMessage());
		}
		message.setFieldErrorMessages(errorMessages);
		return new ResponseEntity<>(message, HttpStatus.UNPROCESSABLE_ENTITY);
	}

	/**
	 * Get entity object instance from id and validate
	 * 
	 * @param <E>        entity
	 * @param <R>        repository object
	 * @param id
	 * @param repository
	 * @return
	 */
	public static <E, R extends JpaRepository<E, Long>> E checkValidById(Long id, R repository) {
		return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(MessageConst.INVALID_ID));
	}
	

	/**
	 * check if entity already exist
	 * 
	 * @param <E>    entity that extends BaseEntity
	 * @param entity object that's been retrieved by unique field
	 * @param id     from DTO, used to check for update
	 * @return
	 */
	public static <E extends BaseEntity> boolean checkDuplicate(Optional<E> entity, Long id) {
		boolean flag = false;
		if (entity.isPresent()) {
			if (Objects.isNull(id)) {
				// register
				flag = true;
			} else {
				// update
				if (!entity.get().getId().equals(id))
					flag = true;
			}
		}
		return flag;
	}

	/**
	 * Convert joined string to list of any type
	 * 
	 * @param <T>
	 * @param str
	 * @param delimiter
	 * @param mapper    function to convert string to desired type
	 * @return
	 */
	public static <T> List<T> stringToList(String str, String delimiter, Function<String, T> mapper) {
		List<T> list = null;
		if (!StringUtils.isEmpty(str)) {
			String[] t = str.split(delimiter);
			list = Arrays.asList(t).stream().map(mapper).collect(Collectors.toList());
		}
		return list;
	}

	/**
	 * Get Joined String of enum values
	 * 
	 * @param <E>       must be of enumerated type class
	 * @param enumClass
	 * @param joiner
	 * @return
	 */
	public static <E extends Enum<E>> String getEnumString(Class<E> enumClass, String joiner) {
		return Arrays.stream(enumClass.getEnumConstants()).map(Enum::name).collect(Collectors.joining(joiner));
	}

	/**
	 * Map Entity List to DTO List
	 * 
	 * @param <D>
	 * @param <E>
	 * @param entityList
	 * @param mapper     function to map entity to DTO
	 * @return
	 */
	public static <D, E> List<D> getDTOList(List<E> entityList, Function<E, D> mapper) {
		List<D> dtoList = entityList.stream().map(mapper).collect(Collectors.toList());
		return dtoList;
	}

	/**
	 * To check if there is duplicate provided key in a DTO List
	 * 
	 * @param <D>
	 * @param <K>
	 * @param dtoList
	 * @param extractor function to get field
	 * @param errorMsg
	 */
	public static <D, K> void checkDuplicateInList(List<D> dtoList, KeyExtractor<D, K> extractor, String errorMsg) {
		Set<K> set = new HashSet<K>();
		boolean flag = false;
		for (int i = 0; i < dtoList.size(); i++) {
			K key = extractor.getKey(dtoList.get(i));
			flag = !set.add(key);
			if (flag)
				break;
		}
		if (flag) {
			throw new BadRequestException(errorMsg);
		}

	}

	/**
	 * Delete details from entity
	 * 
	 * @param <E>
	 * @param <R>        detailRepository
	 * @param deleteIds
	 * @param repository
	 */
	public static <E, R extends JpaRepository<E, Long>> void deleteDetails(Long[] deleteIds, R repository) {
		if (deleteIds != null) {
			List<Long> idsList = Arrays.asList(deleteIds);
			List<E> entityList = repository.findAllById(idsList);
			if (!entityList.isEmpty()) {
				repository.deleteInBatch(entityList);
			}
		}
	}

	/**
	 * Generate issue no. Example format with serialNo 0 `
	 * {@code prefix-202304260000-suffix} `
	 * 
	 * @param prefix
	 * @param serialNo
	 * @param suffix
	 * @return
	 */
	public static String generateIssueNo(String prefix, Integer serialNo, String suffix) {
		LocalDate todayDate = LocalDate.now();
		String issueNo = prefix + "-" + DateUtils.convertDateToString(todayDate, "yyyyMMdd")
				+ String.format("%04d", serialNo) + "-" + suffix;

		return issueNo;
	}

	/**
	 * Generate code, Example format with serialNo 1 `{@code prefix-000001}`
	 * 
	 * @param prefix
	 * @param serialNo
	 * @return
	 */
	public static String generateCode(String prefix, Integer serialNo) {
		return prefix + String.format("%06d", serialNo);
	}

	/**
	 * Round Double value to decimal places
	 * 
	 * @param number
	 * @param decimalPlaces
	 * @return
	 */
	public static double roundToDecimal(Double number, int decimalPlaces) {
		if (Objects.isNull(number))
			number = 0d;

		if (decimalPlaces < 0) {
			decimalPlaces = 0;
		}
		double roundValue = Math.pow(10, decimalPlaces);

		double roundOff = Math.round(number * roundValue) / roundValue;
		return roundOff;
	}

	/**
	 * Get localize message
	 * 
	 * @param code
	 * @param args
	 * @return
	 */
	public static String getLocalizeMessage(String code, Object[] args) {
		Locale locale = LocaleContextHolder.getLocale();
		return messageSource.getMessage(code, args, locale);
	}

	/**
	 * Check if value is null, and return provided value if null
	 * 
	 * @param <T>
	 * @param value
	 * @param res
	 * @return
	 */
	public static <T> T checkNull(T value, T res) {
		return value == null ? res : value;
	}

}
