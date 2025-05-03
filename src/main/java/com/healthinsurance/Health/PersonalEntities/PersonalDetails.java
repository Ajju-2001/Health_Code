package com.healthinsurance.Health.PersonalEntities;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "personalInfo")
public class PersonalDetails {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	@Column(name = "personal_id")
	private Integer personalId;   
	
	@Column(name = "title")
	private String title;
	
	@Column(name = "full_name")
	private String fullName;
	
	@Column(name = "date_of_birth")
	private String dateOfBirth; 
	
	@Column(name = "pan_number")
	private String panNumber;
	
	@Enumerated(EnumType.STRING)
    @Column(name="gender")
    private Gender gender; 

    @Enumerated(EnumType.STRING)
    @Column(name = "marital_status")
    private MaritalStatus maritalStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "occupation")
    private Occupation occupation;

    @Enumerated(EnumType.STRING)
    @Column(name = "profession")
    private Profession profession;
    
    @Column(name = "email")
    private String email;

    @Column(name = "mobile_number")
    private String mobileNumber;

    @Column(name = "alternate_mobile_number")
    private String alternateMobileNumber;

    @Column(name = "address_line1")
    private String addressLine1;

    @Column(name = "address_line2")
    private String addressLine2;

    @Column(name = "address_line3")
    private String addressLine3;

    @Column(name = "pincode")
    private String pincode;

    @Column(name = "city")
    private String city;
    

    @Column(name = "state")
    private String state;
    
    @Column(name = "status")
    private Character status; 
    
    @CreationTimestamp 
    @Column(name="created_at")
    private LocalDateTime createdAt; 
    
    @UpdateTimestamp 
    @Column(name="updated_at")
    private LocalDateTime updatedAt;   
   
    @Column(name="gender_id")
	private Integer genderId;
    
    @Transient
    private List<Map<String, Object>> products;
 
	public PersonalDetails(Integer personalId, String title, String fullName, String dateOfBirth, String panNumber, Gender gender,
			MaritalStatus maritalStatus, Occupation occupation, Profession profession,char status, String email, String mobileNumber, String alternateMobileNumber, String addressLine1, 
            String addressLine2, String addressLine3, String pincode, String city, String state , LocalDateTime createdAt, LocalDateTime updatedAt,Integer genderId,List<Map<String, Object>> products) {
		super();
		this.personalId = personalId;
		this.title = title;
		this.fullName = fullName;
		this.dateOfBirth = dateOfBirth;
		this.panNumber = panNumber;
		this.gender = gender;
		this.maritalStatus = maritalStatus;
		this.occupation = occupation;
		this.profession = profession;
		this.status = status;
		this.email = email;
	    this.mobileNumber = mobileNumber;
	    this.alternateMobileNumber = alternateMobileNumber;
	    this.addressLine1 = addressLine1;
	    this.addressLine2 = addressLine2;
	    this.addressLine3 = addressLine3;
	    this.pincode = pincode;
	    this.city = city;
	    this.state = state;
	    this.createdAt = createdAt;
	    this.updatedAt = updatedAt;
	    this.genderId=genderId;
	    this.products=products; 
	}

	public PersonalDetails() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Integer getPersonalId() {
		return personalId;
	}

	public void setPersonalId(Integer personalId) {
		this.personalId = personalId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getPanNumber() {
		return panNumber;
	}

	public void setPanNumber(String panNumber) {
		this.panNumber = panNumber;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public MaritalStatus getMaritalStatus() {
		return maritalStatus; 
	}

	public void setMaritalStatus(MaritalStatus maritalStatus) {
		this.maritalStatus = maritalStatus;
	}

	public Occupation getOccupation() {
		return occupation;
	}

	public void setOccupation(Occupation occupation) {
		this.occupation = occupation;
	}

	public Profession getProfession() {
		return profession;
	}

	public void setProfession(Profession profession) {
		this.profession = profession;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getAlternateMobileNumber() {
		return alternateMobileNumber;
	}

	public void setAlternateMobileNumber(String alternateMobileNumber) {
		this.alternateMobileNumber = alternateMobileNumber;
	}

	public String getAddressLine1() {
		return addressLine1;
	}

	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	public String getAddressLine2() {
		return addressLine2;
	}

	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}

	public String getAddressLine3() {
		return addressLine3;
	}

	public void setAddressLine3(String addressLine3) {
		this.addressLine3 = addressLine3;
	}

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public Integer getGenderId() {
		return genderId;
	}

	public void setGenderId(Integer genderId) {
		this.genderId = genderId;
	}

	public List<Map<String, Object>> getProducts() {
		return products;
	}

	public void setProducts(List<Map<String, Object>> products) {
		this.products = products;
	}
		
	
}
