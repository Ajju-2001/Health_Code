package com.healthinsurance.Health.PersonalDetailsRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.healthinsurance.Health.PersonalEntities.ResponseExcelTable;
@Repository
public interface ResponseExcelRepository extends JpaRepository<ResponseExcelTable,Integer>{
 
}
