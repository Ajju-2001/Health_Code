package com.healthinsurance.Health.PersonalDetailsService;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.healthinsurance.Health.PersonalDTO.PersonalDetailsDTO;
import com.healthinsurance.Health.PersonalEntities.PersonalDetails;
import com.healthinsurance.Health.PersonalEntities.QueueTable;
import com.healthinsurance.Health.PraposalListing.PraposalListing;


public interface PersonalDetailsService {
	//Save the data in database
	PersonalDetails savePersonalDetails(PersonalDetailsDTO personalDetailsdto);    //access dto class for add update
	//Update the data in database
	PersonalDetails updatePersonalDetails(Integer id, PersonalDetailsDTO personalDetailsdto);
	//Fetch all the data in database
	List<PersonalDetails> getAllPersonalDetails();
	//Fetch the data by using id
	PersonalDetails getPersonalDetailsById(Integer id);
	//Delete the data by using id
	void deletePersonalDetailsById(Integer id); 
	//Delete all the data
	void deleteAllPersonalDetails();
	//Fetch pagination listing 
	List<Map<String, Object>> getPersonalDetails(PraposalListing praposalListing); 
	//Fetch total count method
	public Integer getTotalCount(); 
	//Sample Export Data
	public String exportPersonalSampleData() throws IOException;
	//Import Excel Data
//	List<PersonalDetails> importPersonalDetailsFromExcel(MultipartFile file,Map<String, Integer> recordCounts) throws IOException;
	//Export Excel Data
	public String exportPersonalData() throws IOException; 
	//Export PDF Data
	public String exportPersonalDataToPdf() throws Exception;
	//fetchAllProducts
	List<Map<String, Object>> fetchAllProducts() throws Exception;
	//QueTable data
//	public QueueTable processExcelQueue() throws Exception;
	List<PersonalDetails> importScheduleDetailsFromExcel(MultipartFile file, Map<String, Integer> recordCount)
			throws IOException;
	void scheduleQueueProcessing();
}
