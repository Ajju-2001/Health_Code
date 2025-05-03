package com.healthinsurance.Health.PersonalDetailsRepository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.healthinsurance.Health.PersonalEntities.GenderTable;

public interface GenderTableRepository extends JpaRepository<GenderTable, Integer>{
	Optional<GenderTable> findByGenderType(String genderType);
}
