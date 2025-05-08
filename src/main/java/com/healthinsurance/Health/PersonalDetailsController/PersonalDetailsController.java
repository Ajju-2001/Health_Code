package com.healthinsurance.Health.PersonalDetailsController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.healthinsurance.Health.PersonalDTO.PersonalDetailsDTO;
import com.healthinsurance.Health.PersonalDetailsRepository.QueueTableRepository;
import com.healthinsurance.Health.PersonalDetailsService.PersonalDetailsService;
import com.healthinsurance.Health.PersonalEntities.PersonalDetails;
import com.healthinsurance.Health.PraposalListing.PraposalListing;
import com.healthinsurance.Health.Response.ResponseHandler;
import com.healthinsurance.Health.hepler.JwtUtil;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;


@RestController
@RequestMapping("/proposer")
public class PersonalDetailsController {

	@Autowired
	private PersonalDetailsService personalDetailsService;
	
	@Autowired
	private QueueTableRepository queueTableRepository;
	
	@Autowired
	private JwtUtil jwtUtil; 
 

	@PostMapping("add")
	public ResponseHandler addPersonalDetails(@RequestBody PersonalDetailsDTO personalDetailsdto) {
		ResponseHandler responseHandler = new ResponseHandler();
		try {

			PersonalDetails savedDetails = personalDetailsService.savePersonalDetails(personalDetailsdto);
			responseHandler.setTotalRecords(personalDetailsService.getTotalCount()); 
			responseHandler.setStatus(true); 
			responseHandler.setData(savedDetails); 
			responseHandler.setMessage("Personal details saved successfully");

		} catch (IllegalArgumentException e) {  
			e.printStackTrace(); 
			responseHandler.setTotalRecords(0); 
			responseHandler.setStatus(false);
			responseHandler.setData(new ArrayList<>()); 
			responseHandler.setMessage(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			responseHandler.setStatus(false);
			responseHandler.setData(new ArrayList<>());
			responseHandler.setMessage(e.getMessage());  
		}
		return responseHandler;  
	}

	@PutMapping("updtae_id/{id}")
	public ResponseHandler updatePersonalDetails(@PathVariable Integer id, @RequestBody PersonalDetailsDTO personalDetailsDto) {
	    ResponseHandler responseHandler = new ResponseHandler();
	    try {
	        PersonalDetails updatedPersonalDetails = personalDetailsService.updatePersonalDetails(id, personalDetailsDto);
	        responseHandler.setTotalRecords(personalDetailsService.getTotalCount()); 
	        responseHandler.setStatus(true);
	        responseHandler.setData(updatedPersonalDetails);
	        responseHandler.setMessage("Personal details updated successfully");
 
	    } catch (IllegalArgumentException e) {
	    	e.printStackTrace();
	    	responseHandler.setTotalRecords(0); 
	        responseHandler.setStatus(false);
	        responseHandler.setData(new ArrayList<>());
	        responseHandler.setMessage("Failed to update personal details: " + e.getMessage());

	    } catch (ResponseStatusException e) {  
	    	e.printStackTrace();
	    	responseHandler.setTotalRecords(0); 
	        responseHandler.setStatus(false);
	        responseHandler.setData(new ArrayList<>());
	        responseHandler.setMessage("Personal details not found for the given ID");
	    } catch (Exception e) {
	    	e.printStackTrace();
	    	responseHandler.setTotalRecords(0); 
	        responseHandler.setStatus(false);
	        responseHandler.setData(new ArrayList<>());
	        responseHandler.setMessage("An error occurred while updating personal details");

	    }
		return responseHandler; 
	}


	@GetMapping("get_all")
	public ResponseHandler getAllPersonalDetails(HttpServletRequest request) {
	    ResponseHandler responseHandler = new ResponseHandler();
	    try {
	        String authHeader = request.getHeader("Authorization");
	        String jwt = null;
	        String username = null;
	        Integer userId = null;

	        if (authHeader != null && authHeader.startsWith("Bearer ")) {
	            jwt = authHeader.substring(7); 
	            username = jwtUtil.extractUsername(jwt);
	            userId = jwtUtil.extractUserId(jwt);
	            System.out.println("Extracted Username: " + username);
	            System.out.println("Extracted UserId: " + userId);
	        }

	        List<PersonalDetails> allPersonalDetails = personalDetailsService.getAllPersonalDetails();
	        responseHandler.setTotalRecords(personalDetailsService.getTotalCount());
	        responseHandler.setMessage("Personal details fetched successfully for user: " + username); // ✅ only this one
	        responseHandler.setStatus(true);
	        responseHandler.setData(allPersonalDetails);

	    } catch (Exception e) {
	        e.printStackTrace();
	        responseHandler.setTotalRecords(0);
	        responseHandler.setStatus(false);
	        responseHandler.setData(new ArrayList<>());
	        responseHandler.setMessage("An error occurred while fetching personal details.");
	    }
	    return responseHandler;
	}


	
	
	
	@GetMapping("get_id/{id}")
	public ResponseHandler getPersonalDetailsById(@PathVariable int id) {
	    ResponseHandler responseHandler = new ResponseHandler();
	    try {
	        PersonalDetails personalDetails = personalDetailsService.getPersonalDetailsById(id);
	        responseHandler.setTotalRecords(personalDetailsService.getTotalCount());
	        responseHandler.setStatus(true);
	        responseHandler.setData(personalDetails);
	        responseHandler.setMessage("Personal details retrieved successfully");

	    } catch (IllegalArgumentException e) {
	    	e.printStackTrace();
	    	responseHandler.setTotalRecords(0);
	        responseHandler.setStatus(false);
	        responseHandler.setData(new ArrayList<>());
	        responseHandler.setMessage("Failed to retrieve personal details: " + e.getMessage());

	    } catch (Exception e) {
	    	e.printStackTrace();
	    	responseHandler.setTotalRecords(0);
	        responseHandler.setStatus(false);
	        responseHandler.setData(new ArrayList<>());
	        responseHandler.setMessage("An error occurred while retrieving personal details");

	    }
		return responseHandler;
	}

 
	@DeleteMapping("delete_id/{id}")  
	public ResponseHandler deletePersonalDetailsById(@PathVariable int id) {
	    ResponseHandler responseHandler = new ResponseHandler();
	    try {
	        personalDetailsService.deletePersonalDetailsById(id);
	        responseHandler.setTotalRecords(personalDetailsService.getTotalCount());
	        responseHandler.setStatus(true);
	        responseHandler.setData(new ArrayList<>()); 
	        responseHandler.setMessage("Personal details with ID " + id + " have been deleted.");

	    } catch (IllegalArgumentException e) {
	    	e.printStackTrace();
	    	responseHandler.setTotalRecords(0);
	        responseHandler.setStatus(false);
	        responseHandler.setData(new ArrayList<>());
	        responseHandler.setMessage("Failed to delete personal details: " + e.getMessage());

	    } catch (Exception e) {
	    	e.printStackTrace();
	    	responseHandler.setTotalRecords(0);
	        responseHandler.setStatus(false);
	        responseHandler.setData(new ArrayList<>());
	        responseHandler.setMessage("An error occurred while deleting personal details.");

	    }
		return responseHandler;
	}


	@DeleteMapping("delete_all")
	public ResponseHandler deleteAllPersonalDetails() {
	    ResponseHandler responseHandler = new ResponseHandler();
	    try {
	        personalDetailsService.deleteAllPersonalDetails();
	        responseHandler.setTotalRecords(personalDetailsService.getTotalCount());
	        responseHandler.setStatus(true);
	        responseHandler.setData(new ArrayList<>());  
	        responseHandler.setMessage("All personal details have been deleted.");

	    } catch (Exception e) {
	    	e.printStackTrace();
	    	responseHandler.setTotalRecords(0);
	        responseHandler.setStatus(false);
	        responseHandler.setData(new ArrayList<>());
	        responseHandler.setMessage("An error occurred while deleting all personal details: " + e.getMessage());
	    }
		return responseHandler; 
	}
	
	
	@PostMapping("list")
	public ResponseHandler getPersonalDetails(@RequestBody PraposalListing praposalListing) {
	    ResponseHandler responseHandler = new ResponseHandler(); 
	    try {
	    	List<Map<String, Object>> personalDetailsList = personalDetailsService.getPersonalDetails(praposalListing);
	        responseHandler.setTotalRecords(personalDetailsService.getTotalCount());    
	        responseHandler.setStatus(true); 
	        responseHandler.setData(personalDetailsList); 
	        responseHandler.setMessage("Personal details fetched successfully.");
	        

	    } catch (Exception e) {
	        e.printStackTrace();
	        responseHandler.setTotalRecords(0); 
	        responseHandler.setStatus(false);
	        responseHandler.setData(new ArrayList<>()); 
	        responseHandler.setMessage("An error occurred while fetching personal details.");
  
	    }
	    return responseHandler; 
	}
	
	@GetMapping("sample_data_export")
	public ResponseHandler exportPersonalSampleData() {
	    ResponseHandler responseHandler = new ResponseHandler();
	    try { 
	        String fileName = personalDetailsService.exportPersonalSampleData();
	        String filePath = "C:\\download\\" + fileName;

	        responseHandler.setStatus(true);
	        responseHandler.setMessage("Sample Excel file generated successfully.");
	        responseHandler.setData(filePath);
	    } catch (Exception e) {
	        e.printStackTrace();
	        responseHandler.setStatus(false);
	        responseHandler.setMessage("Failed to generate sample Excel file.");
	        responseHandler.setData(new ArrayList<>());
	    }
	    return responseHandler;
	}

//	

	@PostMapping(value = "/import_personal_data_shedule", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseHandler importPersonalDetailsShedule(
        @Parameter(description = "Excel file to upload", required = true)
        @RequestParam("file") MultipartFile file
    ) {
        ResponseHandler response = new ResponseHandler();
        try {
        	 
        	Map<String, Integer> recordCounts = new HashMap<>();
            List<PersonalDetails> savedExcelList = personalDetailsService.importScheduleDetailsFromExcel(file, recordCounts);
           
            Integer totalExcelCount = recordCounts.getOrDefault("totalExcelCount", 0);
            Integer errorExcelCount = recordCounts.getOrDefault("errorExcelCount", 0);  
            
            response.setStatus(true);
            response.setMessage("Excel imported successfully. Rows added "+savedExcelList.size()+" Out of "+ totalExcelCount   + " and Errors " + errorExcelCount); 
            response.setData(savedExcelList);
        } catch (Exception e) {
            e.printStackTrace(); 
            response.setStatus(false);
            response.setMessage("Failed to import Excel file.");
            response.setData(new ArrayList<>());
        }
        return response;
    }
	
	@GetMapping("/export_excel_data")
	public ResponseEntity<String> exportExcel() throws IOException {
	    String filePath = personalDetailsService.exportPersonalData();
	    return ResponseEntity.ok("File successfully saved at: " + filePath);
	}
	
	@GetMapping("/export_pdf_data")
	public ResponseEntity<String> exportPdf() throws Exception { 
	    String filePath = personalDetailsService.exportPersonalDataToPdf();
	    return ResponseEntity.ok("File successfully saved at: " + filePath);
	}
	
	@GetMapping("/get_products") 
	public ResponseHandler getAllProducts() {
	    ResponseHandler response = new ResponseHandler();
	    try {
	        List<Map<String, Object>> products = personalDetailsService.fetchAllProducts();
	        response.setStatus(true);
	        response.setMessage("Products fetched successfully.");
	        response.setData(products);
	    } catch (Exception e) {
	        response.setStatus(false);
	        response.setMessage("Failed to fetch products.");
	        response.setData(new ArrayList<>());
	    }
	    return response;
	}
	






	
}
