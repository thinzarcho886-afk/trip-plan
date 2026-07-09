package com.cbk.devconstruction.utils;

import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;

import org.springframework.util.StringUtils;

import com.cbk.devconstruction.common.MessageConst;
import com.cbk.devconstruction.exception.BadRequestException;

public class DateUtils {

	public static final String TIMEZONE = "Asia/Rangoon";

	/**
	 * Convert LocalDateTime to ISO-8601 formatted string
	 * 
	 * @param dateTime
	 * @return
	 */
	public static String convertDateTimeToISO(LocalDateTime dateTime) {
		if (dateTime != null) {
			String formatted = dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
			return formatted;
		} else {
			return null;
		}
	}

	/**
	 * Convert LocalDate to ISO-8601 formatted string
	 * 
	 * @param date
	 * @return
	 */
	public static String convertDateToISO(LocalDate date) {
		if (date != null) {
			String formatted = date.format(DateTimeFormatter.ISO_LOCAL_DATE);
			return formatted;
		} else {
			return null;
		}
	}

	/**
	 * Convert ISO-8601 formatted string to LocalDateTime
	 * 
	 * @param isoDateTime
	 * @return
	 */
	public static LocalDateTime convertISOStringToDateTime(String isoDateTime) {
		if (!StringUtils.isEmpty(isoDateTime)) {
			try {
				LocalDateTime dateTime = LocalDateTime.parse(isoDateTime);
				return dateTime;
			} catch (DateTimeException e) {
				throw new BadRequestException(MessageConst.INVALID_DATE);
			}
		} else {
			return null;
		}
	}

	/**
	 * Convert ISO-8601 formatted string to LocalDate
	 * 
	 * @param isoDate
	 * @return
	 */
	public static LocalDate convertISOStringToDate(String isoDate) {
		if (!StringUtils.isEmpty(isoDate)) {
			try {
				LocalDate dateTime = LocalDate.parse(isoDate);
				return dateTime;
			} catch (DateTimeException e) {
				throw new BadRequestException(MessageConst.INVALID_DATE);
			}
		} else {
			return null;
		}
	}

	/**
	 * Convert ISO-8601 formatted string to LocalTime
	 * 
	 * @param isoTime
	 * @return
	 */
	public static LocalTime convertISOStringToTime(String isoTime) {
		if (!StringUtils.isEmpty(isoTime)) {
			try {
				LocalTime dateTime = LocalTime.parse(isoTime);
				return dateTime;
			} catch (DateTimeException e) {
				throw new BadRequestException(MessageConst.INVALID_DATE);
			}
		} else {
			return null;
		}
	}

	/**
	 * Convert Local Date to String of pattern
	 * 
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String convertDateToString(LocalDate date, String pattern) {
		if (date != null) {
			String formatted = date.format(DateTimeFormatter.ofPattern(pattern));
			return formatted;
		} else {
			return "";
		}
	}

	/**
	 * Convert Local Time to String of pattern
	 * 
	 * @param time
	 * @param pattern
	 * @return
	 */
	public static String convertTimeToString(LocalTime time, String pattern) {
		if (time != null) {
			String formatted = time.format(DateTimeFormatter.ofPattern(pattern));
			return formatted;
		} else {
			return "";
		}
	}

	/**
	 * Get last date of year-month with desired format. For example, {@code 2024-05}
	 * will return {@code 2024-05-31} assuming input date format is {@code yyyy-MM}
	 * and output date format is {@code yyyy-MM-dd}
	 * 
	 * @param yearMonthStr
	 * @param inputFormat
	 * @param outputFormat
	 * @return
	 */
	public static String getLastDateOfMonthStr(String yearMonthStr, String inputFormat, String outputFormat) {
		YearMonth yearMonth = YearMonth.parse(yearMonthStr, DateTimeFormatter.ofPattern(inputFormat));
		LocalDate lastDayOfMonth = yearMonth.atEndOfMonth();

		String result = lastDayOfMonth.format(DateTimeFormatter.ofPattern(outputFormat));

		return result;
	}

	/**
	 * Get last day of month of provided ISO date string with month or year added.
	 * For example {@code 2024-05-15} will return {@code 2025-06-30} if
	 * {@code monthToAdd} and {@code yearToAdd} are {@code 1}
	 * 
	 * @param date
	 * @param monthToAdd
	 * @param yearToAdd
	 * @return
	 */
	public static LocalDate getLastDayAndAddDate(String isoDate, int monthToAdd, int yearToAdd) {
		LocalDate convertedDate = convertISOStringToDate(isoDate);

		convertedDate = convertedDate.plusMonths(monthToAdd);
		convertedDate = convertedDate.plusYears(yearToAdd);

		return convertedDate.withDayOfMonth(convertedDate.getMonth().length(convertedDate.isLeapYear()));
	}

	/**
	 * Subtract days from given ISO date string
	 * 
	 * @param isoDate
	 * @param days
	 * @return
	 */
	public static LocalDate subtractDate(String isoDate, long days) {
		LocalDate convertedDate = convertISOStringToDate(isoDate);
		LocalDate newDate = convertedDate.minusDays(days);
		return newDate;
	}

	/**
	 * Convert LocalDate to Date
	 * 
	 * @param localDate
	 * @return
	 */
	public static Date convertLocalDateToDate(LocalDate localDate) {
		return Date.from(localDate.atStartOfDay(ZoneId.of(TIMEZONE)).toInstant());
	}

	/**
	 * Convert Date to LocalDate
	 * 
	 * @param date
	 * @return
	 */
	public static LocalDate convertDateToLocalDate(Date date) {
		Instant instant = date.toInstant();
		ZoneId zoneId = ZoneId.of(TIMEZONE);
		ZonedDateTime zonedDateTime = instant.atZone(zoneId);
		return zonedDateTime.toLocalDate();
	}

	/**
	 * check date format
	 * 
	 * @param date
	 * @return
	 */
	public static boolean isValidDateFormat(String date, String format) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
		try {
			LocalTime.parse(date, formatter);
			return true;
		} catch (DateTimeParseException e) {
			return false;
		}
	}

}
