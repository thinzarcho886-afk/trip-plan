package com.cbk.devconstruction.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cbk.devconstruction.entity.BatchJob;

/**
 * 
 * @author sansintkyaw
 * 
 */
@Repository
public interface BatchJobRepository extends JpaRepository<BatchJob, Long> {
	
	Optional<BatchJob> findByJobId(String jobId);

	List<BatchJob> findByDownloadedAndCreatedUserIdOrderByCreatedDateDesc(Boolean downloaded, Long userId);
	
	@Modifying
	@Query(value = "UPDATE batch_job "
			+ "SET status='Failed', error_message='Job Interrupted' "
			+ "WHERE status='In_Progress' OR status='Waiting'",
			nativeQuery = true)
	void setPreviousJobsAsFailed();

}
