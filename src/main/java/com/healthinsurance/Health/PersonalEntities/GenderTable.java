package com.healthinsurance.Health.PersonalEntities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="gender_table")
public class GenderTable {
	
	@Id
	@Column(name="gender_id")
	private Integer genderId;
	
	@Column(name="gender_type")
	private String genderType;

	public GenderTable(Integer genderId, String genderType) {
		super();
		this.genderId = genderId;
		this.genderType = genderType;
	}

	public GenderTable() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Integer getGenderId() {
		return genderId;
	}

	public void setGenderId(Integer genderId) {
		this.genderId = genderId;
	}

	public String getGenderType() {
		return genderType;
	}

	public void setGenderType(String genderType) {
		this.genderType = genderType;
	}
	
	
}
