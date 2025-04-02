package com.healthinsurance.Health.PersonalDetailsService;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.healthinsurance.Health.PersonalDetailsRepository.PersonalDetailsRepository;
import com.healthinsurance.Health.PersonalEntities.PersonalDetails;

@Service
public class PersonalDetailsServiceImpl implements PersonalDetailsService{

	@Autowired
	private PersonalDetailsRepository personalDetailsRepository;

	@Override
	public PersonalDetails createPersonalDetails(PersonalDetails personalDetails) {
		return personalDetailsRepository.save(personalDetails); 
	}

	@Override
	public List<PersonalDetails> getAllPersonalDetails() {
		return personalDetailsRepository.findAll();
	}

	@Override
	public Optional<PersonalDetails> getPersonalDetailsById(int pid) {
		return personalDetailsRepository.findById(pid);
	}

	@Override
	public PersonalDetails updatePersonalDetails(PersonalDetails personalDetails) {

		personalDetails.setFullName(personalDetails.getFullName());
		personalDetails.setDateOfBirth(personalDetails.getDateOfBirth());
		personalDetails.setPanNumber(personalDetails.getPanNumber());
		personalDetails.setTitle(personalDetails.getTitle());


		return personalDetailsRepository.save(personalDetails);
	} 

	@Override
	public void deletePersonalDetails(int pid) {
		personalDetailsRepository.deleteById(pid);
	}



}
