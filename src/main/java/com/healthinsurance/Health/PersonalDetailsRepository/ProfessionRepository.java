package com.healthinsurance.Health.PersonalDetailsRepository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.healthinsurance.Health.PersonalEntities.Profession;

public interface ProfessionRepository extends JpaRepository<Profession, Integer>{

}
