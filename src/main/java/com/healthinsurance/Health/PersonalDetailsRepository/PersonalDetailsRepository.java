package com.healthinsurance.Health.PersonalDetailsRepository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.healthinsurance.Health.PersonalEntities.PersonalDetails;

public interface PersonalDetailsRepository extends JpaRepository<PersonalDetails, Integer>{

}
