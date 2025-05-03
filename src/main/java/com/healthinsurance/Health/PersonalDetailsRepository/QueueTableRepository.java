package com.healthinsurance.Health.PersonalDetailsRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.healthinsurance.Health.PersonalEntities.QueueTable;

public interface QueueTableRepository extends JpaRepository<QueueTable, Integer>{

//	List<QueueTable> findByIsProcessed(char c);

	List<QueueTable> findByIsProcessedOrderByQueueIdAsc(char c);


}
