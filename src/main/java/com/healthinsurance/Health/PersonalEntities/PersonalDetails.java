package com.healthinsurance.Health.PersonalEntities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class PersonalDetails {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int pid;
	private String title;
	private String fullName;
	private String dateOfBirth;
	private long panNumber;
	
	public PersonalDetails(int pid, String title, String fullName, String dateOfBirth, long panNumber) {
		super();
		this.pid = pid;
		this.title = title;
		this.fullName = fullName;
		this.dateOfBirth = dateOfBirth;
		this.panNumber = panNumber;
	}

	public PersonalDetails() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	@ManyToOne
    private Gender gender;
    
    @ManyToOne
    private Profession profession;
    
    @ManyToOne
    private Occupation occupation;
    
    @ManyToOne
    private MaritalStatus maritalStatus;

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
	
	
	
}
