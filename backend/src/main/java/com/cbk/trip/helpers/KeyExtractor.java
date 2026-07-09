package com.cbk.trip.helpers;

/**
 * SAM to get key from DTO object
 * 
 * @author sansintkyaw
 */
@FunctionalInterface
public interface KeyExtractor<D, K> {

	K getKey(D dto);

}
