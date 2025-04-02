package com.healthinsurance.Health.PersonalDTO;

public class PersonalDetailsDTO {
	private int pid;
    private String title;
    private String fullName;
    private String dateOfBirth;
    private long panNumber;
    private GenderDTO gender;
    private ProfessionDTO profession;
    private OccupationDTO occupation;
    private MaritalStatusDTO maritalStatus; 
	public int getPid() {
		return pid;
	}
	public void setPid(int pid) {
		this.pid = pid;
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
	public long getPanNumber() {
		return panNumber;
	}
	public void setPanNumber(long panNumber) {
		this.panNumber = panNumber;
	}
	public GenderDTO getGender() {
		return gender;
	}
	public void setGender(GenderDTO gender) {
		this.gender = gender;
	}
	public ProfessionDTO getProfession() {
		return profession;
	}
	public void setProfession(ProfessionDTO profession) {
		this.profession = profession;
	}
	public OccupationDTO getOccupation() {
		return occupation;
	}
	public void setOccupation(OccupationDTO occupation) {
		this.occupation = occupation;
	}
	public MaritalStatusDTO getMaritalStatus() {
		return maritalStatus;
	}
	public void setMaritalStatus(MaritalStatusDTO maritalStatus) {
		this.maritalStatus = maritalStatus;
	}
    
    
}
