package com.cbk.devconstruction.helpers;

import java.lang.reflect.Field;
import java.util.Map;

import org.springframework.batch.core.JobParameters;

/**
 * To map parameters of Map or JobParameters to desired parameters class
 * 
 * @author sansintkyaw
 * 
 */
public class ParametersMapper {
	
	/**
	 * Map parameters to given Constructor class
	 * 
	 * @param <T>
	 * @param params
	 * @param targetClass
	 * @return
	 */
	public static <T> T mapParameters(Map<String, String> params, Class<T> targetClass) {
		try {
			T instance = targetClass.getDeclaredConstructor().newInstance();
			Field[] fields = targetClass.getDeclaredFields();

			for (Field field : fields) {
				String fieldName = field.getName();
				if (params.containsKey(fieldName)) {
					field.setAccessible(true);
					String value = params.get(fieldName);
					Object convertedValue = convertValue(field.getType(), value);
					field.set(instance, convertedValue);
				}
			}

			return instance;
		} catch (Exception e) {
			throw new RuntimeException("Error mapping parameters to class: " + targetClass.getName(), e);
		}
	}

	private static Object convertValue(Class<?> type, String value) {
		if (value == null || value.trim().isEmpty()) {
			return null;
		}

		if (type == String.class) {
			return value;
		} else if (type == Long.class || type == long.class) {
			return Long.parseLong(value);
		}

		return value; // Default to String if no conversion is supported
	}

	/**
	 * Map Job Parameters to given Constructor class
	 * 
	 * @param <T>
	 * @param jobParameters
	 * @param targetClass
	 * @return
	 */
	public static <T> T mapJobParameters(JobParameters jobParameters, Class<T> targetClass) {
		try {
			T instance = targetClass.getDeclaredConstructor().newInstance();
			Field[] fields = targetClass.getDeclaredFields();

			for (Field field : fields) {
				String fieldName = field.getName();
				if (field.getType() == String.class) {
					if (jobParameters.getString(fieldName) != null) {
						field.setAccessible(true);
						String value = jobParameters.getString(fieldName);
						field.set(instance, value);
					}
				} else if (field.getType() == Long.class || field.getType() == long.class) {
					if (jobParameters.getLong(fieldName) != null && jobParameters.getLong(fieldName) != 0) {
						field.setAccessible(true);
						Long value = jobParameters.getLong(fieldName);
						field.set(instance, value);
					}
				}
			}

			return instance;
		} catch (Exception e) {
			throw new RuntimeException("Error mapping JobParameters to class: " + targetClass.getName(), e);
		}
	}

}
