package com.healthinsurance.Health.PersonalDetailsRepository;


import java.util.Optional;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.healthinsurance.Health.PersonalEntities.PersonalDetails;


@Repository
public interface PersonalDetailsRepository extends JpaRepository<PersonalDetails, Integer>{ 
	Optional<PersonalDetails> findByEmail(String email);
	Optional<PersonalDetails> findByMobileNumber(String mobileNumber);
	Optional<PersonalDetails> findByalternateMobileNumber(String alternateMobileNumber); 
	Optional<PersonalDetails> findBypanNumber(String panNumber);
	boolean existsByEmail(String email); 
} 