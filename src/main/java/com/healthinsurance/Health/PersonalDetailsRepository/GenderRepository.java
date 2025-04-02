package com.healthinsurance.Health.PersonalDetailsRepository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.healthinsurance.Health.PersonalEntities.Gender;

public interface GenderRepository extends JpaRepository<Gender, Integer>{

}
