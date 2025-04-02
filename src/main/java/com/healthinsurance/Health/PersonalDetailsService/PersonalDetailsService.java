package com.healthinsurance.Health.PersonalDetailsService;

import java.util.List;
import java.util.Optional;

import com.healthinsurance.Health.PersonalEntities.PersonalDetails;

public interface PersonalDetailsService {
	PersonalDetails createPersonalDetails(PersonalDetails personalDetails);
    List<PersonalDetails> getAllPersonalDetails(); 
    Optional<PersonalDetails> getPersonalDetailsById(int pid);
    PersonalDetails updatePersonalDetails(PersonalDetails personalDetails);
    void deletePersonalDetails(int pid);
}
