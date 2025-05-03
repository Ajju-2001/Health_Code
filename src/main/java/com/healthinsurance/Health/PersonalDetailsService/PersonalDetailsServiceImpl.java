package com.healthinsurance.Health.PersonalDetailsService;

import static org.mockito.ArgumentMatchers.intThat;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.healthinsurance.Health.PersonalDTO.PersonalDetailsDTO;
import com.healthinsurance.Health.PersonalDetailsRepository.GenderTableRepository;
import com.healthinsurance.Health.PersonalDetailsRepository.PersonalDetailsRepository;
import com.healthinsurance.Health.PersonalDetailsRepository.QueueTableRepository;
import com.healthinsurance.Health.PersonalDetailsRepository.ResponseExcelRepository;
import com.healthinsurance.Health.PersonalEntities.Gender;
import com.healthinsurance.Health.PersonalEntities.GenderTable;
import com.healthinsurance.Health.PersonalEntities.MaritalStatus;
import com.healthinsurance.Health.PersonalEntities.Occupation;
import com.healthinsurance.Health.PersonalEntities.PersonalDetails;
import com.healthinsurance.Health.PersonalEntities.Profession;
import com.healthinsurance.Health.PersonalEntities.QueueTable;
import com.healthinsurance.Health.PersonalEntities.ResponseExcelTable;
import com.healthinsurance.Health.PraposalListing.PraposalListing;
import com.healthinsurance.Health.PraposalListing.SearchFilter;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
public class PersonalDetailsServiceImpl implements PersonalDetailsService {

	Integer totaRecords = 0;

	@Override
	public Integer getTotalCount() {
		return totaRecords;
	}

	@Autowired
	private PersonalDetailsRepository personalDetailsRepository;

	@Autowired
	private GenderTableRepository genderTableRepository;

	@Autowired
	private ResponseExcelRepository responseExcelRepository;

	@Autowired
	private QueueTableRepository queueTableRepository;

	@Override
	public PersonalDetails savePersonalDetails(PersonalDetailsDTO personalDetailsdto) {

		if (personalDetailsdto.getTitle() == null || personalDetailsdto.getTitle().trim().isEmpty()) {
			throw new IllegalArgumentException("Title cannot be null or empty");
		}
		if (!personalDetailsdto.getTitle().matches("[a-zA-Z]+")) {
			throw new IllegalArgumentException("Title must contain only letters");
		}

		if (personalDetailsdto.getFullName() == null || personalDetailsdto.getFullName().trim().isEmpty()) {
			throw new IllegalArgumentException("Full name cannot be null or empty");
		}
		if (!personalDetailsdto.getFullName().matches("[a-zA-Z ]+")) {
			throw new IllegalArgumentException("Full name must contain only letters and spaces");
		}

		if (personalDetailsdto.getDateOfBirth() == null) {
			throw new IllegalArgumentException("Date of birth cannot be null");
		}

		if (personalDetailsdto.getPanNumber() == null) {
			throw new IllegalArgumentException("PAN number must be a valid format");
		}

		if (personalDetailsdto.getPanNumber() == null || personalDetailsdto.getPanNumber().isEmpty()) {
			throw new IllegalArgumentException("PAN number cannot be null or empty");
		}
		String panNumberString = String.valueOf(personalDetailsdto.getPanNumber());
		if (!panNumberString.matches("[A-Z]{5}\\d{4}[A-Z]{1}")) {
			throw new IllegalArgumentException("PAN number must be in the format: XXXXX9999X");
		}

		if (personalDetailsdto.getGender() == null) {
			throw new IllegalArgumentException("Gender cannot be null or empty");
		}

		if (personalDetailsdto.getMaritalStatus() == null) {
			throw new IllegalArgumentException("Marital status cannot be null");
		}

		if (personalDetailsdto.getOccupation() == null) {
			throw new IllegalArgumentException("Occupation cannot be null");
		}

		if (personalDetailsdto.getProfession() == null) {
			throw new IllegalArgumentException("Profession cannot be null");
		}
		if (personalDetailsdto.getEmail() == null || personalDetailsdto.getEmail().trim().isEmpty()) {
			throw new IllegalArgumentException("Email cannot be null or empty");
		}
		if (!personalDetailsdto.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
			throw new IllegalArgumentException("Invalid email format");
		}

		if (personalDetailsdto.getMobileNumber() == null || personalDetailsdto.getMobileNumber().trim().isEmpty()) {
			throw new IllegalArgumentException("Mobile number cannot be null or empty");
		}
		if (!personalDetailsdto.getMobileNumber().matches("\\d{10}")) {
			throw new IllegalArgumentException("Mobile number must be 10 digits");
		}
		if (personalDetailsdto.getAlternateMobileNumber() != null
				&& !personalDetailsdto.getAlternateMobileNumber().trim().isEmpty()) {
			if (!personalDetailsdto.getAlternateMobileNumber().matches("\\d{10}")) {
				throw new IllegalArgumentException("Alternate mobile number must be 10 digits");
			}
		}

		if (personalDetailsdto.getAddressLine1() == null || personalDetailsdto.getAddressLine1().trim().isEmpty()) {
			throw new IllegalArgumentException("Address Line 1 cannot be null or empty");
		}
		if (personalDetailsdto.getAddressLine2() == null || personalDetailsdto.getAddressLine2().trim().isEmpty()) {
			throw new IllegalArgumentException("Address Line 2 cannot be null or empty");
		}
		if (personalDetailsdto.getAddressLine3() == null || personalDetailsdto.getAddressLine3().trim().isEmpty()) {
			throw new IllegalArgumentException("Address Line 3 cannot be null or empty");
		}

		if (personalDetailsdto.getPincode() == null || personalDetailsdto.getPincode().trim().isEmpty()) {
			throw new IllegalArgumentException("Pincode cannot be null or empty");
		}
		if (!personalDetailsdto.getPincode().matches("\\d{6}")) {
			throw new IllegalArgumentException("Pincode must be 6 digits");
		}

		if (personalDetailsdto.getCity() == null || personalDetailsdto.getCity().trim().isEmpty()) {
			throw new IllegalArgumentException("City cannot be null or empty");
		}

		if (personalDetailsdto.getState() == null || personalDetailsdto.getState().trim().isEmpty()) {
			throw new IllegalArgumentException("State cannot be null or empty");
		}

		// Check if the pan number already exists
		Optional<PersonalDetails> existingpanNumber = personalDetailsRepository
				.findBypanNumber(personalDetailsdto.getPanNumber());
		if (existingpanNumber.isPresent()) {
			throw new IllegalArgumentException("Pan Number is already in use.");
		}

		// Check if the email already exists
		Optional<PersonalDetails> existingEmail = personalDetailsRepository.findByEmail(personalDetailsdto.getEmail());
		if (existingEmail.isPresent()) {
			throw new IllegalArgumentException("Email is already in use.");
		}

		// Check if the mobile number already exists
		Optional<PersonalDetails> existingMobileNumber = personalDetailsRepository
				.findByMobileNumber(personalDetailsdto.getMobileNumber());
		if (existingMobileNumber.isPresent()) {
			throw new IllegalArgumentException("Mobile number is already in use.");
		}

		// Check if the alternate mobile number already exists
		Optional<PersonalDetails> existingalternateMobileNumber = personalDetailsRepository
				.findByalternateMobileNumber(personalDetailsdto.getAlternateMobileNumber());
		if (existingalternateMobileNumber.isPresent()) {
			throw new IllegalArgumentException("Alternate Mobile number is already in use.");
		}

		PersonalDetails prDetails = new PersonalDetails(); // create object of entity class to set data

		prDetails.setTitle(personalDetailsdto.getTitle()); // set the data with entity object refernce name and get the
															// data with dto object refernce name for update
		prDetails.setFullName(personalDetailsdto.getFullName());
		prDetails.setDateOfBirth(personalDetailsdto.getDateOfBirth());
		prDetails.setPanNumber(personalDetailsdto.getPanNumber());
		// For Gender Drop down
		String gender = personalDetailsdto.getGender();
		if (gender != null && !gender.isEmpty()) {
			if (gender.equalsIgnoreCase("MALE")) {
				prDetails.setGender(Gender.MALE);
			} else if (gender.equalsIgnoreCase("FEMALE")) {
				prDetails.setGender(Gender.FEMALE);
			} else if (gender.equalsIgnoreCase("OTHER")) {
				prDetails.setGender(Gender.OTHER);
			} else {
				throw new IllegalArgumentException("Invalid gender value provided");
			}
		} else {
			throw new IllegalArgumentException("Gender cannot be null or empty");
		}

		// For Marital Status Drop down
		String maritalStatus = personalDetailsdto.getMaritalStatus();
		if (maritalStatus != null && !maritalStatus.isEmpty()) {
			if (maritalStatus.equalsIgnoreCase("SINGLE")) {
				prDetails.setMaritalStatus(MaritalStatus.SINGLE);
			} else if (maritalStatus.equalsIgnoreCase("MARRIED")) {
				prDetails.setMaritalStatus(MaritalStatus.MARRIED);
			} else if (maritalStatus.equalsIgnoreCase("DIVORCED")) {
				prDetails.setMaritalStatus(MaritalStatus.DIVORCED);
			} else if (maritalStatus.equalsIgnoreCase("WIDOWED")) {
				prDetails.setMaritalStatus(MaritalStatus.WIDOWED);
			} else {
				throw new IllegalArgumentException("Invalid  Marital Status value provided");
			}
		} else {
			throw new IllegalArgumentException(" Marital Status cannot be null or empty");
		}

		// For occupation Drop down
		String occupation = personalDetailsdto.getOccupation();
		if (occupation != null && !occupation.isEmpty()) {
			if (occupation.equalsIgnoreCase("EMPLOYED")) {
				prDetails.setOccupation(Occupation.EMPLOYED);
			} else if (occupation.equalsIgnoreCase("UNEMPLOYED")) {
				prDetails.setOccupation(Occupation.UNEMPLOYED);
			} else if (occupation.equalsIgnoreCase("STUDENT")) {
				prDetails.setOccupation(Occupation.STUDENT);
			} else if (occupation.equalsIgnoreCase("SELF_EMPLOYED")) {
				prDetails.setOccupation(Occupation.SELF_EMPLOYED);
			} else {
				throw new IllegalArgumentException("Invalid  Occupation value provided");
			}
		} else {
			throw new IllegalArgumentException(" Occupation cannot be null or empty");
		}

		// For Profession Drop down
		String profession = personalDetailsdto.getProfession();
		if (profession != null && !profession.isEmpty()) {
			if (profession.equalsIgnoreCase("DOCTOR")) {
				prDetails.setProfession(Profession.DOCTOR);
			} else if (profession.equalsIgnoreCase("ENGINEER")) {
				prDetails.setProfession(Profession.ENGINEER);
			} else if (profession.equalsIgnoreCase("TEACHER")) {
				prDetails.setProfession(Profession.TEACHER);
			} else if (profession.equalsIgnoreCase("LAWYER")) {
				prDetails.setProfession(Profession.LAWYER);
			} else if (profession.equalsIgnoreCase("OTHER")) {
				prDetails.setProfession(Profession.OTHER);
			} else {
				throw new IllegalArgumentException("Invalid  Profession value provided");
			}
		} else {
			throw new IllegalArgumentException(" Profession cannot be null or empty");
		}

		String genderType = personalDetailsdto.getGender();
		if (genderType != null && !genderType.isEmpty()) {
			Optional<GenderTable> gto = genderTableRepository.findByGenderType(genderType);
			if (gto.isPresent()) {
				prDetails.setGenderId(gto.get().getGenderId());
			} else {
				throw new IllegalArgumentException("Invalid gender value provided");
			}
		} else {
			throw new IllegalArgumentException("Gender cannot be null or empty");
		}

		prDetails.setEmail(personalDetailsdto.getEmail());
		prDetails.setMobileNumber(personalDetailsdto.getMobileNumber());
		prDetails.setAlternateMobileNumber(personalDetailsdto.getAlternateMobileNumber());
		prDetails.setAddressLine1(personalDetailsdto.getAddressLine1());
		prDetails.setAddressLine2(personalDetailsdto.getAddressLine2());
		prDetails.setAddressLine3(personalDetailsdto.getAddressLine3());
		prDetails.setPincode(personalDetailsdto.getPincode());
		prDetails.setCity(personalDetailsdto.getCity());
		prDetails.setState(personalDetailsdto.getState());

		prDetails.setStatus('Y');
		return personalDetailsRepository.save(prDetails);
	}

	@Override
	public PersonalDetails updatePersonalDetails(Integer id, PersonalDetailsDTO personalDetailsdto) {

		Optional<PersonalDetails> pd = personalDetailsRepository.findById(id);
		if (!pd.isPresent()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "PersonalDetails not found for ID: " + id);
		}

		if (personalDetailsdto.getTitle() == null || personalDetailsdto.getTitle().trim().isEmpty()) {
			throw new IllegalArgumentException("Title cannot be null or empty");
		}
		if (!personalDetailsdto.getTitle().matches("[a-zA-Z]+")) {
			throw new IllegalArgumentException("Title must contain only letters");
		}

		if (personalDetailsdto.getFullName() == null || personalDetailsdto.getFullName().trim().isEmpty()) {
			throw new IllegalArgumentException("Full name cannot be null or empty");
		}
		if (!personalDetailsdto.getFullName().matches("[a-zA-Z ]+")) {
			throw new IllegalArgumentException("Full name must contain only letters and spaces");
		}

		if (personalDetailsdto.getDateOfBirth() == null) {
			throw new IllegalArgumentException("Date of birth cannot be null");
		}

		if (personalDetailsdto.getPanNumber() == null || personalDetailsdto.getPanNumber().isEmpty()) {
			throw new IllegalArgumentException("PAN number cannot be null or empty");
		}
		String panNumberString = String.valueOf(personalDetailsdto.getPanNumber());
		// Regular expression to match PAN format (5 uppercase letters, 4 digits, 1
		// uppercase letter)
		if (!panNumberString.matches("[A-Z]{5}\\d{4}[A-Z]{1}")) {
			throw new IllegalArgumentException("PAN number must be in the format: XXXXX9999X");
		}

		if (personalDetailsdto.getGender() == null) {
			throw new IllegalArgumentException("Gender cannot be null");
		}

		if (personalDetailsdto.getMaritalStatus() == null) {
			throw new IllegalArgumentException("Marital status cannot be null");
		}

		if (personalDetailsdto.getOccupation() == null) {
			throw new IllegalArgumentException("Occupation cannot be null");
		}

		if (personalDetailsdto.getProfession() == null) {
			throw new IllegalArgumentException("Profession cannot be null");
		}

		// Email Validation
		if (personalDetailsdto.getEmail() == null || personalDetailsdto.getEmail().trim().isEmpty()) {
			throw new IllegalArgumentException("Email cannot be null or empty");
		}
		if (!personalDetailsdto.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
			throw new IllegalArgumentException("Invalid email format");
		}

		if (personalDetailsdto.getMobileNumber() == null || personalDetailsdto.getMobileNumber().trim().isEmpty()) {
			throw new IllegalArgumentException("Mobile number cannot be null or empty");
		}
		if (!personalDetailsdto.getMobileNumber().matches("\\d{10}")) {
			throw new IllegalArgumentException("Mobile number must be 10 digits");
		}

		if (personalDetailsdto.getAlternateMobileNumber() != null
				&& !personalDetailsdto.getAlternateMobileNumber().trim().isEmpty()) {
			if (!personalDetailsdto.getAlternateMobileNumber().matches("\\d{10}")) {
				throw new IllegalArgumentException("Alternate mobile number must be 10 digits");
			}
		}

		if (personalDetailsdto.getAddressLine1() == null || personalDetailsdto.getAddressLine1().trim().isEmpty()) {
			throw new IllegalArgumentException("Address Line 1 cannot be null or empty");
		}
		if (personalDetailsdto.getAddressLine2() == null || personalDetailsdto.getAddressLine2().trim().isEmpty()) {
			throw new IllegalArgumentException("Address Line 2 cannot be null or empty");
		}
		if (personalDetailsdto.getAddressLine3() == null || personalDetailsdto.getAddressLine3().trim().isEmpty()) {
			throw new IllegalArgumentException("Address Line 3 cannot be null or empty");
		}

		if (personalDetailsdto.getPincode() == null || personalDetailsdto.getPincode().trim().isEmpty()) {
			throw new IllegalArgumentException("Pincode cannot be null or empty");
		}
		if (!personalDetailsdto.getPincode().matches("\\d{6}")) {
			throw new IllegalArgumentException("Pincode must be 6 digits");
		}

		if (personalDetailsdto.getCity() == null || personalDetailsdto.getCity().trim().isEmpty()) {
			throw new IllegalArgumentException("City cannot be null or empty");
		}

		if (personalDetailsdto.getState() == null || personalDetailsdto.getState().trim().isEmpty()) {
			throw new IllegalArgumentException("State cannot be null or empty");
		}

		PersonalDetails prDetails = pd.get();

		prDetails.setTitle(personalDetailsdto.getTitle());
		prDetails.setFullName(personalDetailsdto.getFullName());
		prDetails.setDateOfBirth(personalDetailsdto.getDateOfBirth());
		prDetails.setPanNumber(personalDetailsdto.getPanNumber());
		// For Gender Drop down
		String gender = personalDetailsdto.getGender();
		if (gender != null && !gender.isEmpty()) {
			if (gender.equalsIgnoreCase("MALE")) {
				prDetails.setGender(Gender.MALE);
			} else if (gender.equalsIgnoreCase("FEMALE")) {
				prDetails.setGender(Gender.FEMALE);
			} else if (gender.equalsIgnoreCase("OTHER")) {
				prDetails.setGender(Gender.OTHER);
			} else {
				throw new IllegalArgumentException("Invalid gender value provided");
			}
		} else {
			throw new IllegalArgumentException("Gender cannot be null or empty");
		}

		// For Marital Status Drop down
		String maritalStatus = personalDetailsdto.getMaritalStatus();
		if (maritalStatus != null && !maritalStatus.isEmpty()) {
			if (maritalStatus.equalsIgnoreCase("SINGLE")) {
				prDetails.setMaritalStatus(MaritalStatus.SINGLE);
			} else if (maritalStatus.equalsIgnoreCase("MARRIED")) {
				prDetails.setMaritalStatus(MaritalStatus.MARRIED);
			} else if (maritalStatus.equalsIgnoreCase("DIVORCED")) {
				prDetails.setMaritalStatus(MaritalStatus.DIVORCED);
			} else if (maritalStatus.equalsIgnoreCase("WIDOWED")) {
				prDetails.setMaritalStatus(MaritalStatus.WIDOWED);
			} else {
				throw new IllegalArgumentException("Invalid  Marital Status value provided");
			}
		} else {
			throw new IllegalArgumentException(" Marital Status cannot be null or empty");
		}

		// For occupation Drop down
		String occupation = personalDetailsdto.getOccupation();
		if (occupation != null && !occupation.isEmpty()) {
			if (occupation.equalsIgnoreCase("EMPLOYED")) {
				prDetails.setOccupation(Occupation.EMPLOYED);
			} else if (occupation.equalsIgnoreCase("UNEMPLOYED")) {
				prDetails.setOccupation(Occupation.UNEMPLOYED);
			} else if (occupation.equalsIgnoreCase("STUDENT")) {
				prDetails.setOccupation(Occupation.STUDENT);
			} else if (occupation.equalsIgnoreCase("SELF_EMPLOYED")) {
				prDetails.setOccupation(Occupation.SELF_EMPLOYED);
			} else {
				throw new IllegalArgumentException("Invalid  Occupation value provided");
			}
		} else {
			throw new IllegalArgumentException(" Occupation cannot be null or empty");
		}

		// For Profession Drop down
		String profession = personalDetailsdto.getProfession();
		if (profession != null && !profession.isEmpty()) {
			if (profession.equalsIgnoreCase("DOCTOR")) {
				prDetails.setProfession(Profession.DOCTOR);
			} else if (profession.equalsIgnoreCase("ENGINEER")) {
				prDetails.setProfession(Profession.ENGINEER);
			} else if (profession.equalsIgnoreCase("TEACHER")) {
				prDetails.setProfession(Profession.TEACHER);
			} else if (profession.equalsIgnoreCase("LAWYER")) {
				prDetails.setProfession(Profession.LAWYER);
			} else if (profession.equalsIgnoreCase("OTHER")) {
				prDetails.setProfession(Profession.OTHER);
			} else {
				throw new IllegalArgumentException("Invalid  Profession value provided");
			}
		} else {
			throw new IllegalArgumentException(" Profession cannot be null or empty");
		}

		String genderType = personalDetailsdto.getGender();
		if (genderType != null && !genderType.isEmpty()) {
			Optional<GenderTable> gto = genderTableRepository.findByGenderType(genderType);
			if (gto.isPresent()) {
				prDetails.setGenderId(gto.get().getGenderId());
			} else {
				throw new IllegalArgumentException("Invalid gender value provided");
			}
		} else {
			throw new IllegalArgumentException("Gender cannot be null or empty");
		}

		prDetails.setEmail(personalDetailsdto.getEmail());
		prDetails.setMobileNumber(personalDetailsdto.getMobileNumber());
		prDetails.setAlternateMobileNumber(personalDetailsdto.getAlternateMobileNumber());
		prDetails.setAddressLine1(personalDetailsdto.getAddressLine1());
		prDetails.setAddressLine2(personalDetailsdto.getAddressLine2());
		prDetails.setAddressLine3(personalDetailsdto.getAddressLine3());
		prDetails.setPincode(personalDetailsdto.getPincode());
		prDetails.setCity(personalDetailsdto.getCity());
		prDetails.setState(personalDetailsdto.getState());

		return personalDetailsRepository.save(prDetails);
	}

	@Override
	public List<PersonalDetails> getAllPersonalDetails() {
		List<PersonalDetails> personalDetailsList = personalDetailsRepository.findAll();
		
		if (personalDetailsList.isEmpty()) {
			throw new IllegalArgumentException("No personal details found");
		}
		return personalDetailsList;
	}

	@Override
	public PersonalDetails getPersonalDetailsById(Integer id) {
		if (id <= 0) {
			throw new IllegalArgumentException("Invalid ID. ID must be a positive integer.");
		}

		Optional<PersonalDetails> personalDetailsOptional = personalDetailsRepository.findById(id);
		if (!personalDetailsOptional.isPresent()) {
			throw new IllegalArgumentException("Personal details not found for ID: " + id);
		}
		return personalDetailsOptional.get();
	}

	@Override
	public void deletePersonalDetailsById(Integer id) {
		if (id <= 0) {
			throw new IllegalArgumentException("Invalid ID. ID must be a positive integer.");
		}
		Optional<PersonalDetails> personalDetailsOptional = personalDetailsRepository.findById(id);
		if (!personalDetailsOptional.isPresent()) {
			throw new IllegalArgumentException("Personal details not found for ID: " + id);
		}
		PersonalDetails personalDetails = personalDetailsOptional.get();
		personalDetails.setStatus('N');
		personalDetailsRepository.save(personalDetails);
	}

	@Override
	public void deleteAllPersonalDetails() {
		personalDetailsRepository.deleteAll();
	}

	@Autowired
	private EntityManager entityManager;

	@Override
	public List<Map<String, Object>> getPersonalDetails(PraposalListing praposalListing) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<PersonalDetails> criteriaQuery = criteriaBuilder.createQuery(PersonalDetails.class);
		Root<PersonalDetails> root = criteriaQuery.from(PersonalDetails.class);

		List<Predicate> predicates = new ArrayList<>();

		List<SearchFilter> searchFilters = praposalListing.getSearchFilters();

		if (searchFilters != null) {
			for (SearchFilter filter : searchFilters) {
				if (filter.getFullName() != null && !filter.getFullName().isEmpty()) {
					predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("fullName")),
							"%" + filter.getFullName().toLowerCase() + "%"));
				}
				if (filter.getEmail() != null && !filter.getEmail().isEmpty()) {
					predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("email")),
							"%" + filter.getEmail().toLowerCase() + "%"));
				}
				if (filter.getCity() != null && !filter.getCity().isEmpty()) {
					predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("city")),
							"%" + filter.getCity().toLowerCase() + "%"));
				}
				if (filter.getStatus() != null) {
					predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("status")),
							String.valueOf(filter.getStatus()).toLowerCase()));
				} else {
					predicates.add(criteriaBuilder.equal(root.get("status"), 'Y'));
				}
			}
		}

		if (!predicates.isEmpty()) {
			criteriaQuery.where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));
		}

		Integer page = praposalListing.getPage();
		Integer size = praposalListing.getSize();

		if (page == null || size == null || page <= 0 || size <= 0) {
			page = 1;
			size = Integer.MAX_VALUE;
		}

		if (praposalListing.getSortBy() == null || praposalListing.getSortBy().isEmpty()) {
			praposalListing.setSortBy("id");
		}
		if (praposalListing.getSortOrder() == null || praposalListing.getSortOrder().isEmpty()) {
			praposalListing.setSortOrder("DESC");
		}

		String sortBy = praposalListing.getSortBy();
		String sortOrder = praposalListing.getSortOrder();
		if ("ASC".equalsIgnoreCase(sortOrder)) {
			criteriaQuery.orderBy(criteriaBuilder.asc(root.get(sortBy)));
		} else {
			criteriaQuery.orderBy(criteriaBuilder.desc(root.get(sortBy)));
		}

		TypedQuery<PersonalDetails> typedQuery = entityManager.createQuery(criteriaQuery);

		List<PersonalDetails> resultList = typedQuery.getResultList();
		totaRecords = resultList.size();

		typedQuery.setFirstResult((page - 1) * size);
		typedQuery.setMaxResults(size);

		List<Map<String, Object>> result = new ArrayList<>();
		for (PersonalDetails personalDetail : typedQuery.getResultList()) {
			Map<String, Object> map = new HashMap<>();
			map.put("fullName", personalDetail.getFullName());
			map.put("email", personalDetail.getEmail());
			map.put("city", personalDetail.getCity());
			map.put("status", personalDetail.getStatus());
			result.add(map);
		}

		return result;
	}

	@Override
	public String exportPersonalSampleData() throws IOException {
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("Personal Details");

		List<String> headers = Arrays.asList("Title", "Full Name*", "Date of Birth*", "PAN Number*", "Gender*",
				"Marital Status*", "Occupation*", "Profession*S", "Email*", "Mobile Number*", "Alternate Mobile Number",
				"Address Line 1", "Address Line 2", "Address Line 3", "Pincode*", "City*", "State*");

		XSSFRow headerRow = sheet.createRow(0);
		for (int i = 0; i < headers.size(); i++) {
			headerRow.createCell(i).setCellValue(headers.get(i));
		}

		String uid = UUID.randomUUID().toString().replace("-", "");
		String fileName = "sample_personal_details_" + uid + ".xlsx";
		String filePath = "C:\\download\\" + fileName;

		try (FileOutputStream out = new FileOutputStream(filePath)) {
			workbook.write(out);
		}

		workbook.close();
		return fileName;
	}

	Integer batchSize = 3;

//	@Override
//	public List<PersonalDetails> importPersonalDetailsFromExcel(MultipartFile file, Map<String, Integer> recordCounts)
//			throws IOException {
//		List<PersonalDetails> savedExcelList = new ArrayList<>();
//
//		recordCounts.put("totalExcelCount", 0);
//		recordCounts.put("errorExcelCount", 0);
//		
//		int validRowCount = 0;
//		
//		 Map<String, Object> queueTableData = new HashMap<>();
//
//		XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
//			XSSFSheet sheet = workbook.getSheetAt(0);
//			
//			if (sheet.getLastRowNum() < 5) {
//	            throw new IllegalArgumentException("Minimum 5 valid records required to process the file.");
//	        }
//
//			for (int i = 1; i <= sheet.getLastRowNum(); i++) {
//				ResponseExcelTable response = new ResponseExcelTable();
//				XSSFRow row = sheet.getRow(i);
//				if (row == null) {
//					continue;
//				}
//				boolean isEmptyRow = true;
//				for (int j = row.getFirstCellNum(); j < row.getLastCellNum(); j++) {
//					XSSFCell cell = row.getCell(j);
//					if (cell != null && cell.getCellType() != CellType.BLANK
//							&& (cell.getCellType() != CellType.STRING || !cell.getStringCellValue().trim().isEmpty())) {
//						isEmptyRow = false;
//						break;
//					}
//				}
//
//				// For count total row records
//				if (isEmptyRow) {
//					continue;
//				}
//				recordCounts.put("totalExcelCount", recordCounts.get("totalExcelCount") + 1);
//
//				PersonalDetails entity = new PersonalDetails();
//
//				String title = getCellString(row.getCell(0));
//
//				if (title != null && !title.trim().isEmpty()) {
//					entity.setTitle(title);
////					continue;
//				}
//
//				String fullName = getCellString(row.getCell(1));
//				if (fullName == null || fullName.trim().isEmpty() || !fullName.matches("[a-zA-Z\\s]+")) {
//					response.setErrorFields("fullName");
//					response.setStatus(false);
//					response.setError("Invalid full name");
//					response.setSuccess("Failure!!");
//					responseExcelRepository.save(response);
//					recordCounts.put("errorExcelCount", recordCounts.get("errorExcelCount") + 1);
//					continue;
//				} else {
//					entity.setFullName(fullName);
//				}
//
//				String dob = getCellString(row.getCell(2));
//				if (dob == null || dob.trim().isEmpty()) {
//					response.setErrorFields("dateOfBirth");
//					response.setStatus(false);
//					response.setError("Invalid Date Of Birth");
//					response.setSuccess("Failure!!");
//					responseExcelRepository.save(response);
//					recordCounts.put("errorExcelCount", recordCounts.get("errorExcelCount") + 1);
//					continue;
//				} else {
//					entity.setDateOfBirth(dob);
//				}
//
//				String pan = getCellString(row.getCell(3)).toUpperCase().trim();
//				if (!pan.matches("^[A-Za-z]{5}[0-9]{4}[A-Za-z]{1}$")) {
//					response.setErrorFields("panNumber");
//					response.setStatus(false);
//					response.setError("Invalid Pan Number");
//					response.setSuccess("Failure!!");
//					responseExcelRepository.save(response);
//					recordCounts.put("errorExcelCount", recordCounts.get("errorExcelCount") + 1);
//					continue;
//				} else {
//					entity.setPanNumber(pan);
//				}
//
//				String genderString = getCellString(row.getCell(4)).toUpperCase();
//				Gender gender = null;
//				for (Gender g : Gender.values()) {
//					if (g.name().equalsIgnoreCase(genderString)) {
//						gender = g;
//						break;
//					}
//				}
//				if (gender == null) {
//					response.setErrorFields("gender");
//					response.setStatus(false);
//					response.setError("Invalid Gender");
//					response.setSuccess("Failure!!");
//					responseExcelRepository.save(response);
//					recordCounts.put("errorExcelCount", recordCounts.get("errorExcelCount") + 1);
//					continue;
//				} else {
//					entity.setGender(gender);
//					entity.setGenderId(getGenderId(gender));
//				}
//
//				String maritalStatusStr = getCellString(row.getCell(5)).toUpperCase();
//				MaritalStatus maritalStatus = null;
//				for (MaritalStatus m : MaritalStatus.values()) {
//					if (m.name().equalsIgnoreCase(maritalStatusStr)) {
//						maritalStatus = m;
//						break;
//					}
//				}
//				if (maritalStatus == null) {
//					response.setErrorFields("maritalStatus");
//					response.setStatus(false);
//					response.setError("Invalid Marital Status");
//					response.setSuccess("Failure!!");
//					responseExcelRepository.save(response);
//					recordCounts.put("errorExcelCount", recordCounts.get("errorExcelCount") + 1);
//					continue;
//				} else {
//					entity.setMaritalStatus(maritalStatus);
//				}
//
//				String occupationStr = getCellString(row.getCell(6)).toUpperCase();
//				Occupation occupation = null;
//				for (Occupation o : Occupation.values()) {
//					if (o.name().equalsIgnoreCase(occupationStr)) {
//						occupation = o;
//						break;
//					}
//				}
//				if (occupation == null) {
//					response.setErrorFields("occupation");
//					response.setStatus(false);
//					response.setError("Invalid Occupation");
//					response.setSuccess("Failure!!");
//					responseExcelRepository.save(response);
//					recordCounts.put("errorExcelCount", recordCounts.get("errorExcelCount") + 1);
//					continue;
//				} else {
//					entity.setOccupation(occupation);
//				}
//
//				String professionStr = getCellString(row.getCell(7)).toUpperCase();
//				Profession profession = null;
//				for (Profession p : Profession.values()) {
//					if (p.name().equalsIgnoreCase(professionStr)) {
//						profession = p;
//						break;
//					}
//				}
//				if (profession == null) {
//					response.setErrorFields("profession");
//					response.setStatus(false);
//					response.setError("Invalid Profession");
//					response.setSuccess("Failure!!");
//					responseExcelRepository.save(response);
//					recordCounts.put("errorExcelCount", recordCounts.get("errorExcelCount") + 1);
//					continue;
//				} else {
//					entity.setProfession(profession);
//				}
//
//				String email = getCellString(row.getCell(8));
//				if (email == null || !email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
//					response.setErrorFields("email");
//					response.setStatus(false);
//					response.setError("Invalid Email");
//					response.setSuccess("Failure!!");
//					responseExcelRepository.save(response);
//					recordCounts.put("errorExcelCount", recordCounts.get("errorExcelCount") + 1);
//					continue;
//				} else {
//					entity.setEmail(email);
//				}
//
//				String mobileNumber = getCellString(row.getCell(9));
//				if (mobileNumber == null || mobileNumber.isEmpty() || !mobileNumber.matches("^[6-9]\\d{9}$")) {
//					response.setErrorFields("mobileNumber");
//					response.setStatus(false);
//					response.setError("Invalid Mobile Number");
//					response.setSuccess("Failure!!");
//					responseExcelRepository.save(response);
//					recordCounts.put("errorExcelCount", recordCounts.get("errorExcelCount") + 1);
//					continue;
//				} else {
//					entity.setMobileNumber(mobileNumber);
//				}
//
//				String alternateMobile = getCellString(row.getCell(10));
//				if (alternateMobile != null && !alternateMobile.trim().isEmpty()) {
//					entity.setAlternateMobileNumber(alternateMobile);
////					continue;
//
//				}
//
//				String address1 = getCellString(row.getCell(11));
//				if (address1 != null && !address1.trim().isEmpty()) {
//					entity.setAddressLine1(address1);
////					continue;
//				}
//
//				String address2 = getCellString(row.getCell(12));
//				if (address2 != null && !address2.trim().isEmpty()) {
//					entity.setAddressLine2(address2);
////					continue;
//				}
//
//				String address3 = getCellString(row.getCell(13));
//				if (address3 != null && !address3.trim().isEmpty()) {
//					entity.setAddressLine3(address3);
////					continue;
//				}
//
//				String pincode = getCellString(row.getCell(14));
//				if (pincode == null || !pincode.matches("^\\d{6}$")) {
//					response.setErrorFields("pincode");
//					response.setStatus(false);
//					response.setError("Invalid Pincode");
//					response.setSuccess("Failure!!");
//					responseExcelRepository.save(response);
//					recordCounts.put("errorExcelCount", recordCounts.get("errorExcelCount") + 1);
//					continue;
//				}
//				entity.setPincode(pincode);
//
//				String city = getCellString(row.getCell(15));
//				if (city == null || city.trim().isEmpty()) {
//					response.setErrorFields("city");
//					response.setStatus(false);
//					response.setError("Invalid City");
//					response.setSuccess("Failure!!");
//					responseExcelRepository.save(response);
//					recordCounts.put("errorExcelCount", recordCounts.get("errorExcelCount") + 1);
//					continue;
//				} else {
//					entity.setCity(city);
//				}
//
//				String state = getCellString(row.getCell(16));
//				if (state == null || state.trim().isEmpty()) {
//					response.setErrorFields("state");
//					response.setStatus(false);
//					response.setError("Invalid State");
//					response.setSuccess("Failure!!");
//					responseExcelRepository.save(response);
//					recordCounts.put("errorExcelCount", recordCounts.get("errorExcelCount") + 1);
//					continue;
//				} else {
//					entity.setState(state);
//				}
//				entity.setStatus('Y');
//				
//				PersonalDetails saved = personalDetailsRepository.save(entity);
//				savedExcelList.add(saved);
//				validRowCount++;
//				queueTableData.put("filePath", file.getOriginalFilename());
//		        queueTableData.put("rowCount", recordCounts.get("totalExcelCount"));
//		        queueTableData.put("rowRead", validRowCount);
//		        queueTableData.put("isProcessed", 'N');
//		        queueTableData.put("status", 'Y');
//		        if (validRowCount == recordCounts.get("totalExcelCount")) {
//		            queueTableData.put("isLastProcess", 1);  
//		        } else {
//		            queueTableData.put("isLastProcess", 0); 
//		        }
//		}
//			if (validRowCount >= 5) {
//	            QueueTable queue = new QueueTable();
//	            queue.setFilePath((String) queueTableData.get("filePath"));
//	            queue.setRowCount((Integer) queueTableData.get("rowCount"));
//	            queue.setRowRead((Integer) queueTableData.get("rowRead"));
//	            queue.setIsProcessed((Character) queueTableData.get("isProcessed"));
//	            queue.setStatus((Character) queueTableData.get("status"));
//	            queue.setIsLastProcess(validRowCount == recordCounts.get("totalExcelCount") ? 1 : 0);
//	            queueTableRepository.save(queue);
//	        }
//		return savedExcelList; 
//	}

	@Override
	public List<PersonalDetails> importScheduleDetailsFromExcel(MultipartFile file, Map<String, Integer> recordCount)
			throws IOException {
		String uploadDir = "C:\\download\\";
		File dir = new File(uploadDir);
		if (!dir.exists()) {
			dir.mkdirs();
		}

		String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
		String filePath = uploadDir + fileName;

		recordCount.put("totalExcelCount", 0);
		recordCount.put("errorExcelCount", 0);

		XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
		XSSFSheet sheet = workbook.getSheetAt(0);
		

		int realRowCount = 0;
		for (int i = 1; i <= sheet.getLastRowNum(); i++) {
			XSSFRow row = sheet.getRow(i);
			if (row == null)
				continue;

			boolean isEmptyRow = true;
			for (int j = row.getFirstCellNum(); j < row.getLastCellNum(); j++) {
				XSSFCell cell = row.getCell(j);
				if (cell != null && cell.getCellType() != CellType.BLANK
						&& (cell.getCellType() != CellType.STRING || !cell.getStringCellValue().trim().isEmpty())) {
					isEmptyRow = false;
					break;
				}
			}

			if (!isEmptyRow) {
				realRowCount++;
			}
		}
		
		QueueTable queue = new QueueTable();
		if (sheet.getLastRowNum() > 2) {
			queue.setIsProcessed('N');
			queue.setStatus('Y');
			queue.setIsLastProcess(0);
			queue.setRowCount(realRowCount);
			queue.setRowRead(0);
			queue.setFilePath(filePath); 
			file.transferTo(new File(filePath)); 
			queueTableRepository.save(queue);

			Map<String, Object> scheduledResponse = new HashMap<>();
			scheduledResponse.put("message", "File has been queued for processing in batches.");
			scheduledResponse.put("rowCount", sheet.getLastRowNum());
			scheduledResponse.put("filePath", filePath);
			scheduledResponse.put("queueId", queue.getQueueId()); 

			workbook.close();
			return new ArrayList<>();
		}

		List<PersonalDetails> savedExcelList = new ArrayList<>();
		int totalRows = sheet.getLastRowNum();
		for (int i = 1; i <= totalRows; i++) {
			ResponseExcelTable response = new ResponseExcelTable();
			XSSFRow row = sheet.getRow(i);
			if (row == null) {
				continue;
			}
			boolean isEmptyRow = true;
			List<String> errorFields = new ArrayList<>();
			for (int j = row.getFirstCellNum(); j < row.getLastCellNum(); j++) {
				XSSFCell cell = row.getCell(j);
				if (cell != null && cell.getCellType() != CellType.BLANK
						&& (cell.getCellType() != CellType.STRING || !cell.getStringCellValue().trim().isEmpty())) {
					isEmptyRow = false;
					break;
				}
			}

			if (isEmptyRow)
				continue;

			recordCount.put("totalExcelCount", recordCount.get("totalExcelCount") + 1);

			PersonalDetails entity = new PersonalDetails();

			String title = getCellString(row.getCell(0));
			if (title != null && !title.trim().isEmpty()) {
				entity.setTitle(title);
			}

			String fullName = getCellString(row.getCell(1));
			if (fullName == null || fullName.trim().isEmpty() || !fullName.matches("[a-zA-Z\\s]+")) {
				errorFields.add("fullName");
			} else {
				entity.setFullName(fullName);
			}

			String dob = getCellString(row.getCell(2));
			if (dob == null || dob.trim().isEmpty()) {
				errorFields.add("dateOfBirth");
			} else {
				entity.setDateOfBirth(dob);
			}

			String pan = getCellString(row.getCell(3)).toUpperCase().trim();
			if (!pan.matches("^[A-Za-z]{5}[0-9]{4}[A-Za-z]{1}$")) {
				errorFields.add("panNumber");
			} else {
				entity.setPanNumber(pan);
			}

			String genderString = getCellString(row.getCell(4)).toUpperCase();
			Gender gender = null;
			for (Gender g : Gender.values()) {
				if (g.name().equalsIgnoreCase(genderString)) {
					gender = g;
					break;
				}
			}
			if (gender == null) {
				errorFields.add("gender");
			} else {
				entity.setGender(gender);
				entity.setGenderId(getGenderId(gender));
			}

			String maritalStatusStr = getCellString(row.getCell(5)).toUpperCase();
			MaritalStatus maritalStatus = null;
			for (MaritalStatus m : MaritalStatus.values()) {
				if (m.name().equalsIgnoreCase(maritalStatusStr)) {
					maritalStatus = m;
					break;
				}
			}
			if (maritalStatus != null) {
				entity.setMaritalStatus(maritalStatus);
			}

			String occupationStr = getCellString(row.getCell(6)).toUpperCase();
			Occupation occupation = null;
			for (Occupation o : Occupation.values()) {
				if (o.name().equalsIgnoreCase(occupationStr)) {
					occupation = o;
					break;
				}
			}
			if (occupation != null) {
				entity.setOccupation(occupation);
			}
			
			String proffesionStr = getCellString(row.getCell(7)).toUpperCase(); 
			Profession profession = null; 
			for (Profession p : Profession.values()) {
				if (p.name().equalsIgnoreCase(proffesionStr)) {
					profession = p;
					break;
				}
			}
			if (profession != null) {
				entity.setProfession(profession);
			}

			String email = getCellString(row.getCell(8));
			if (email == null || !email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
				errorFields.add("email");
			} else {
				entity.setEmail(email);
			}

			String mobileNumber = getCellString(row.getCell(9));
			if (mobileNumber == null || !mobileNumber.matches("^[6-9]\\d{9}$")) {
				errorFields.add("mobileNumber");
			} else {
				entity.setMobileNumber(mobileNumber);
			}

			String alternateMobile = getCellString(row.getCell(10));
			if (alternateMobile != null && !alternateMobile.trim().isEmpty()
					&& alternateMobile.matches("^[6-9]\\d{9}$")) {
				entity.setAlternateMobileNumber(alternateMobile);
			}

			String address = getCellString(row.getCell(11));
			if (address == null || address.trim().isEmpty()) {
				errorFields.add("address");
			}
			entity.setAddressLine1(address);

			String address1 = getCellString(row.getCell(12));
			if (address1 == null || address1.trim().isEmpty()) {
				errorFields.add("address1");
			}
			entity.setAddressLine2(address1);

			String address2 = getCellString(row.getCell(13));
			if (address2 == null || address2.trim().isEmpty()) {
				errorFields.add("address2");
			}
			entity.setAddressLine3(address2);

			String pincode = getCellString(row.getCell(14));
			if (pincode == null || !pincode.matches("^\\d{6}$")) {
				errorFields.add("pincode");
			}
			entity.setPincode(pincode);

			String city = getCellString(row.getCell(15));
			if (city == null || city.trim().isEmpty()) {
				errorFields.add("city");
			} else {
				entity.setCity(city);
			}

			String state = getCellString(row.getCell(16));
			if (state == null || state.trim().isEmpty()) {
				errorFields.add("state");
			} else {
				entity.setState(state);
			}

			entity.setStatus('Y');
			if (!errorFields.isEmpty()) {
				response.setErrorFields(String.join(", ", errorFields));
				response.setError("Invalid Mandatory fields");
				response.setStatus(false);
				response.setSuccess("Failed!");
				responseExcelRepository.save(response);
				recordCount.put("errorExcelCount", recordCount.get("errorExcelCount") + 1);
				continue;
			}

			PersonalDetails saved = personalDetailsRepository.save(entity);
			savedExcelList.add(saved);
			response.setError("No Error");
			response.setErrorFields(String.valueOf(saved.getPersonalId()));
			response.setStatus(true);
			response.setSuccess("Success");
			responseExcelRepository.save(response);
		}

		workbook.close();
		return savedExcelList;
	}

	@Override
	@Scheduled(fixedDelay = 10000)
	public void scheduleQueueProcessing() {
//		List<QueueTable> queueTableOpt = queueTableRepository.findByIsProcessed('N');
		List<QueueTable> queueList = queueTableRepository.findByIsProcessedOrderByQueueIdAsc('N');
//		if (queueTableOpt.isPresent()) {
//			QueueTable queue = queueTableOpt.get();

		for(QueueTable queue:queueList) {
			int lastProcessedRow = queue.getRowRead() != null ? queue.getRowRead() : 0; 
			String filePath = queue.getFilePath();
			File file = new File(filePath);
		
			if (file.exists()) {
				Map<String, Integer> recordCount = new HashMap<>();
				recordCount.put("totalExcelCount", 0);
				recordCount.put("errorExcelCount", 0);

				List<PersonalDetails> savedExcelList = new ArrayList<>();

				try (FileInputStream fileInputStream = new FileInputStream(file);
						XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream)) {

					XSSFSheet sheet = workbook.getSheetAt(0);

					int realRowCount = 0;
					Iterator<Row> rowIterator = sheet.iterator();
					boolean isFirstRow = true;
					while (rowIterator.hasNext()) {
						Row row = rowIterator.next();
						if (isFirstRow) {
							isFirstRow = false; 
							continue;
						}
						boolean isEmptyRow = true;
						for (int j = row.getFirstCellNum(); j < row.getLastCellNum(); j++) {
							Cell cell = row.getCell(j);
							if (cell != null && cell.getCellType() != CellType.BLANK
									&& (cell.getCellType() != CellType.STRING
											|| !cell.getStringCellValue().trim().isEmpty())) {
								isEmptyRow = false;
								break;
							}
						}
						if (!isEmptyRow) {
							realRowCount++;
						}
					}

					int rowsToProcess = 5;
					int startRow = lastProcessedRow + 1;
					int endRow = startRow + rowsToProcess - 1;

					int realRowSeen = 0;

					for (int i = 1; i <= sheet.getLastRowNum(); i++) {
						XSSFRow row = sheet.getRow(i);
						if (row == null)
							continue;

						boolean isEmptyRow = true;
						for (int j = row.getFirstCellNum(); j < row.getLastCellNum(); j++) {
							XSSFCell cell = row.getCell(j);
							if (cell != null && cell.getCellType() != CellType.BLANK
									&& (cell.getCellType() != CellType.STRING
											|| !cell.getStringCellValue().trim().isEmpty())) {
								isEmptyRow = false;
								break;
							}
						}
						if (isEmptyRow)
							continue;

						realRowSeen++;

						if (realRowSeen <= lastProcessedRow)
							continue;
						if (realRowSeen > endRow)
							break;
						PersonalDetails entity = new PersonalDetails();
			
						List<String> errorFields = new ArrayList<>();

						recordCount.put("totalExcelCount", recordCount.get("totalExcelCount") + 1);

						String title = getCellString(row.getCell(0));
						if (title != null && !title.trim().isEmpty()) {
							entity.setTitle(title);
						}

						String fullName = getCellString(row.getCell(1));
						if (fullName == null || fullName.trim().isEmpty() || !fullName.matches("[a-zA-Z\\s]+")) {
							errorFields.add("fullName");
						} else {
							entity.setFullName(fullName);
						} 

						String dob = getCellString(row.getCell(2));
						if (dob == null || dob.trim().isEmpty()) {
							errorFields.add("dateOfBirth");
						} else {
							entity.setDateOfBirth(dob);
						}

					           

						String genderString = getCellString(row.getCell(4));
						Gender gender = null;
						for (Gender g : Gender.values()) {
							if (g.name().equalsIgnoreCase(genderString)) {
								gender = g;
								break;
							}
						}
						if (gender == null) {
							errorFields.add("gender");
						} else {
							entity.setGender(gender);
							entity.setGenderId(getGenderId(gender));
						}

						String maritalStatusStr = getCellString(row.getCell(5));
						if (maritalStatusStr != null) {
							for (MaritalStatus m : MaritalStatus.values()) {
								if (m.name().equalsIgnoreCase(maritalStatusStr)) {
									entity.setMaritalStatus(m);
									break;
								}
							}
						}

						String occupationStr = getCellString(row.getCell(6));
						if (occupationStr != null) {
							for (Occupation o : Occupation.values()) {
								if (o.name().equalsIgnoreCase(occupationStr)) {
									entity.setOccupation(o);
									break;
								}
							}
						}
						
						String professionStr = getCellString(row.getCell(7));
						if (professionStr != null) {
							for (Profession p : Profession.values()) {
								if (p.name().equalsIgnoreCase(professionStr)) {
									entity.setProfession(p);
									break;
								}
							}
						}

						String email = getCellString(row.getCell(8));
						if (email == null || !email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
							errorFields.add("email");
						} else {
							entity.setEmail(email);
						}

						String mobileNumber = getCellString(row.getCell(9));
						if (mobileNumber == null || !mobileNumber.matches("^[6-9]\\d{9}$")) {
							errorFields.add("mobileNumber");
						} else {
							entity.setMobileNumber(mobileNumber);
						}

						String alternateMobile = getCellString(row.getCell(10));
						if (alternateMobile != null && !alternateMobile.trim().isEmpty()
								&& alternateMobile.matches("^[6-9]\\d{9}$")) {
							entity.setAlternateMobileNumber(alternateMobile);
						}

						String address = getCellString(row.getCell(11));
						if (address == null || address.trim().isEmpty()) {
							errorFields.add("address");
						}
						entity.setAddressLine1(address);
						
						String address1 = getCellString(row.getCell(12));
						if (address1 == null || address1.trim().isEmpty()) {
							errorFields.add("address1");
						}
						entity.setAddressLine2(address1);
						
						String address2 = getCellString(row.getCell(13));
						if (address2 == null || address2.trim().isEmpty()) {
							errorFields.add("address2");
						}
						entity.setAddressLine3(address2);

						String pincode = getCellString(row.getCell(14));
						if (pincode == null || !pincode.matches("^\\d{6}$")) {
							errorFields.add("pincode");
						}
						entity.setPincode(pincode);

						String city = getCellString(row.getCell(15));
						if (city == null || city.trim().isEmpty()) {
							errorFields.add("city");
						} else {
							entity.setCity(city);
						}

						String state = getCellString(row.getCell(16));
						if (state == null || state.trim().isEmpty()) {
							errorFields.add("state");
						} else {
							entity.setState(state);
						}

						entity.setStatus('Y');
						

						PersonalDetails saved = personalDetailsRepository.save(entity);
						savedExcelList.add(saved);

						queue.setRowRead(realRowSeen);
						queue.setIsLastProcess(batchSize);
						
						if (!errorFields.isEmpty()) {
						    for (String field : errorFields) {  
						        ResponseExcelTable errorResponse = new ResponseExcelTable();
						        errorResponse.setQueueId(queue.getQueueId()); 
						        errorResponse.setError("Invalid Mandatory fields");
						        errorResponse.setErrorFields(field);
						        errorResponse.setStatus(false);
						        errorResponse.setSuccess("Failure");
						        responseExcelRepository.save(errorResponse);
						    }
						    recordCount.put("errorExcelCount", recordCount.get("errorExcelCount") + 1);
						    continue;
						} else {
						    ResponseExcelTable successResponse = new ResponseExcelTable();
//						    successResponse.setQueueId(queue.getQueueId()); 
						    successResponse.setErrorFields(String.valueOf(saved.getPersonalId())); 
						    successResponse.setStatus(true);
						    successResponse.setError("No Error");
						    successResponse.setSuccess("Success!!");
						    responseExcelRepository.save(successResponse);
						}
						
					}                  

					if (queue.getRowRead() >= realRowCount) {
						queue.setIsProcessed('Y');
					}
					queueTableRepository.save(queue);

					System.out.println("Batch Processing Summary:");
					System.out.println("Total Records Processed: " + recordCount.get("totalExcelCount"));
					System.out.println("Error Records: " + recordCount.get("errorExcelCount"));
					System.out.println("Saved Records: " + savedExcelList.size());

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	private Integer getGenderId(Gender gender) {
		switch (gender) {
		case MALE:
			return 1;
		case FEMALE:
			return 2;
		case OTHER:
			return 3;
		default:
			return null;
		}
	}

	private String getCellString(Cell cell) {
		if (cell == null) {
			return "";
		}

		if (cell.getCellType() == CellType.STRING) {
			return cell.getStringCellValue().trim();
		} else if (cell.getCellType() == CellType.NUMERIC) {
			if (DateUtil.isCellDateFormatted(cell)) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				return sdf.format(cell.getDateCellValue());
			} else {
				return String.valueOf((long) cell.getNumericCellValue());
			}
		} else if (cell.getCellType() == CellType.BOOLEAN) {
			return String.valueOf(cell.getBooleanCellValue());
		} else if (cell.getCellType() == CellType.FORMULA) {
			return cell.getCellFormula();
		} else {
			return "";
		}
	}

	@Override
	public String exportPersonalData() throws IOException {
		List<PersonalDetails> personalDetailsList = personalDetailsRepository.findAll();
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("Personal Details");

		List<String> headers = Arrays.asList("Title", "Full Name", "Date of Birth", "PAN Number", "Gender",
				"Marital Status", "Occupation", "Profession", "Email", "Mobile Number", "Alternate Mobile Number",
				"Address Line 1", "Address Line 2", "Address Line 3", "Pincode", "City", "State");

		XSSFRow headerRow = sheet.createRow(0);
		for (int i = 0; i < headers.size(); i++) {
			headerRow.createCell(i).setCellValue(headers.get(i));
		}

		int rowNum = 1;
		for (PersonalDetails details : personalDetailsList) {
			XSSFRow row = sheet.createRow(rowNum++);
			row.createCell(0).setCellValue(details.getTitle());
			row.createCell(1).setCellValue(details.getFullName());
			row.createCell(2).setCellValue(details.getDateOfBirth());
			row.createCell(3).setCellValue(details.getPanNumber());
			row.createCell(4).setCellValue(details.getGender() != null ? details.getGender().name() : "");
			row.createCell(5).setCellValue(details.getMaritalStatus() != null ? details.getMaritalStatus().name() : "");
			row.createCell(6).setCellValue(details.getOccupation() != null ? details.getOccupation().name() : "");
			row.createCell(7).setCellValue(details.getProfession() != null ? details.getProfession().name() : "");
			row.createCell(8).setCellValue(details.getEmail());
			row.createCell(9).setCellValue(details.getMobileNumber());
			row.createCell(10).setCellValue(details.getAlternateMobileNumber());
			row.createCell(11).setCellValue(details.getAddressLine1());
			row.createCell(12).setCellValue(details.getAddressLine2());
			row.createCell(13).setCellValue(details.getAddressLine3());
			row.createCell(14).setCellValue(details.getPincode());
			row.createCell(15).setCellValue(details.getCity());
			row.createCell(16).setCellValue(details.getState());
		}

		String uid = UUID.randomUUID().toString().replace("-", "");
		String fileName = "personal_details_" + uid + ".xlsx";
		String filePath = "C:\\download\\" + fileName;

		FileOutputStream fileOut = new FileOutputStream(filePath);
		workbook.write(fileOut);
		workbook.close();
		fileOut.close();

//	    FileInputStream fis = new FileInputStream(new File(filePath));
//	    XSSFWorkbook readWorkbook = new XSSFWorkbook(fis);
//	    XSSFSheet readSheet = readWorkbook.getSheetAt(0);
//
//	    int totalRows = readSheet.getPhysicalNumberOfRows();
//	    int validRowCount = 0;
//
//	    for (int i = 1; i < totalRows; i++) {
//	        XSSFRow row = readSheet.getRow(i);
//	        if (row != null && isValidRow(row)) {
//	            validRowCount++;
//	        }
//	    }
//	    QueueTable queue = new QueueTable();
//	    queue.setFilePath(filePath);
//	    queue.setRowCount(totalRows - 1);
//	    queue.setRowRead(validRowCount);
//	    queue.setIsProcessed('N');
//	    queue.setStatus('Y');
//	    queueTableRepository.save(queue);
//
//	    readWorkbook.close();
//	    fis.close();
		return filePath;
	}
//	========================================
//	FileInputStream fis = new FileInputStream(new File(filePath));
//	XSSFWorkbook readWorkbook = new XSSFWorkbook(fis);
//	XSSFSheet readSheet = readWorkbook.getSheetAt(0);
//
//	int totalRows = readSheet.getPhysicalNumberOfRows();
//	int validRowCount = 0;
//
//	for (int i = 1; i < totalRows; i++) {
//	    XSSFRow row = readSheet.getRow(i);
//	    if (row != null && isValidRow(row)) {
//	        validRowCount++;
//	    }
//	}
//
//	readWorkbook.close();
//	fis.close();
//
//	if (validRowCount > 10) {
//	    QueueTable queue = new QueueTable();
//	    queue.setFilePath(filePath);
//	    queue.setRowCount(totalRows - 1); // exclude header
//	    queue.setRowRead(validRowCount);
//	    queue.setIsProcessed('N');
//	    queue.setStatus('Y');
//	    queueTableRepository.save(queue);
//	    return filePath;
//	} else {
//	    File fileToDelete = new File(filePath);
//	    if (fileToDelete.exists()) {
//	        fileToDelete.delete(); // Optional: clean up unused file
//	    }
//	    return "Queue Data Save Failed.";
//	}

//	=====================================================
//	@Override  
//	public String exportPersonalData() throws IOException {
//	    List<PersonalDetails> personalDetailsList = personalDetailsRepository.findAll();
//	    XSSFWorkbook workbook = new XSSFWorkbook();
//	    XSSFSheet sheet = workbook.createSheet("Personal Details");
//
//	    List<String> headers = Arrays.asList("Title", "Full Name", "Date of Birth", "PAN Number", "Gender",
//	            "Marital Status", "Occupation", "Profession", "Email", "Mobile Number", "Alternate Mobile Number",
//	            "Address Line 1", "Address Line 2", "Address Line 3", "Pincode", "City", "State");
//
//	    XSSFRow headerRow = sheet.createRow(0);
//	    for (int i = 0; i < headers.size(); i++) {
//	        headerRow.createCell(i).setCellValue(headers.get(i));
//	    }
//
//	    int rowNum = 1;
//	    for (PersonalDetails details : personalDetailsList) {
//	        XSSFRow row = sheet.createRow(rowNum++);
//	        row.createCell(0).setCellValue(details.getTitle());
//	        row.createCell(1).setCellValue(details.getFullName());
//	        row.createCell(2).setCellValue(details.getDateOfBirth());
//	        row.createCell(3).setCellValue(details.getPanNumber());
//	        row.createCell(4).setCellValue(details.getGender() != null ? details.getGender().name() : "");
//	        row.createCell(5).setCellValue(details.getMaritalStatus() != null ? details.getMaritalStatus().name() : "");
//	        row.createCell(6).setCellValue(details.getOccupation() != null ? details.getOccupation().name() : "");
//	        row.createCell(7).setCellValue(details.getProfession() != null ? details.getProfession().name() : "");
//	        row.createCell(8).setCellValue(details.getEmail());
//	        row.createCell(9).setCellValue(details.getMobileNumber());
//	        row.createCell(10).setCellValue(details.getAlternateMobileNumber());
//	        row.createCell(11).setCellValue(details.getAddressLine1());
//	        row.createCell(12).setCellValue(details.getAddressLine2());
//	        row.createCell(13).setCellValue(details.getAddressLine3());
//	        row.createCell(14).setCellValue(details.getPincode());
//	        row.createCell(15).setCellValue(details.getCity());
//	        row.createCell(16).setCellValue(details.getState());
//	    }
//
//	    String uid = UUID.randomUUID().toString().replace("-", "");
//	    String fileName = "personal_details_" + uid + ".xlsx";
//	    String filePath = "C:\\download\\" + fileName;
//
//	    FileOutputStream fileOut = new FileOutputStream(filePath);
//	    workbook.write(fileOut);
//	    workbook.close();
//	    fileOut.close();
//
//	    System.out.println("File exported to: " + filePath);
//	    return filePath; 
//	}

	@Override
	public String exportPersonalDataToPdf() throws Exception {
		List<PersonalDetails> personalDetailsList = personalDetailsRepository.findAll();

		Document document = new Document(PageSize.A4.rotate());
		String uid = UUID.randomUUID().toString().replace("-", "");
		String fileName = "personal_details_" + uid + ".pdf";
		String filePath = "C:\\download\\" + fileName;

		PdfWriter.getInstance(document, new FileOutputStream(filePath));
		document.open();

		Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
		Paragraph title = new Paragraph("Personal Details Report", titleFont);
		title.setAlignment(Element.ALIGN_CENTER);
		document.add(title);
		document.add(Chunk.NEWLINE);

		PdfPTable table = new PdfPTable(17);
		table.setWidthPercentage(100f);

		List<String> headers = Arrays.asList("Title", "Full Name", "Date of Birth", "PAN Number", "Gender",
				"Marital Status", "Occupation", "Profession", "Email", "Mobile Number", "Alternate Mobile Number",
				"Address Line 1", "Address Line 2", "Address Line 3", "Pincode", "City", "State");

		for (String header : headers) {
			PdfPCell headerCell = new PdfPCell(new Phrase(header));
			headerCell.setBackgroundColor(Color.LIGHT_GRAY);
			table.addCell(headerCell);
		}

		for (PersonalDetails details : personalDetailsList) {
			table.addCell(details.getTitle());
			table.addCell(details.getFullName());
			table.addCell(String.valueOf(details.getDateOfBirth()));
			table.addCell(details.getPanNumber());
			table.addCell(details.getGender() != null ? details.getGender().name() : "");
			table.addCell(details.getMaritalStatus() != null ? details.getMaritalStatus().name() : "");
			table.addCell(details.getOccupation() != null ? details.getOccupation().name() : "");
			table.addCell(details.getProfession() != null ? details.getProfession().name() : "");
			table.addCell(details.getEmail());
			table.addCell(details.getMobileNumber());
			table.addCell(details.getAlternateMobileNumber());
			table.addCell(details.getAddressLine1());
			table.addCell(details.getAddressLine2());
			table.addCell(details.getAddressLine3());
			table.addCell(details.getPincode());
			table.addCell(details.getCity());
			table.addCell(details.getState());
		}

		document.add(table);
		document.close();

		System.out.println("PDF exported to: " + filePath);
		return filePath;
	}

	private final String url = "https://fakestoreapi.com/products";
	@Autowired
	private RestTemplate restTemplate;

	@Override
	public List<Map<String, Object>> fetchAllProducts() {
		List<Map<String, Object>> response = restTemplate.getForObject(url, List.class);
		return response;
	}

//    @Override
//    public QueueTable processExcelQueue() throws Exception { 
//        String filePath = "C:\\download\\Test\\data.xlsx";
//        File file = new File(filePath);
//
//        if (!file.exists()) {
//            throw new IllegalArgumentException("File not found: " + filePath);
//        }
//
//        try (FileInputStream fis = new FileInputStream(file);
//             XSSFWorkbook workbook = new XSSFWorkbook(fis)) {
//
//            XSSFSheet sheet = workbook.getSheetAt(0);
//            int totalRows = sheet.getPhysicalNumberOfRows();
//            int validRowCount = 0;
//
//            for (int i = 1; i < totalRows; i++) { 
//                XSSFRow row = sheet.getRow(i);
//                if (row != null && isValidRow(row)) {
//                    validRowCount++;
//                }
//            }
//
//            QueueTable queue = new QueueTable();
//            queue.setRowCount(totalRows);
//            queue.setRowRead(validRowCount);
//            queue.setIsProcessed('N');
//            queue.setStatus('Y'); 
//
//            QueueTable savedQueue = queueTableRepository.save(queue); 
//            System.out.println(savedQueue);
//            return savedQueue;
//        }
//    }

	private boolean isValidRow(XSSFRow row) {
		for (Cell cell : row) {
			if (cell == null || cell.getCellType() == CellType.BLANK) {
				return false;
			}
		}
		return true;
	}

    

}
