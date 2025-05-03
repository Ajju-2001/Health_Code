package com.healthinsurance.Health.PersonalEntities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "response_excel_table")
public class ResponseExcelTable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	@Column(name = "response_excel_id")
	private Integer responseExcelId;
	@Column(name = "status")
	private Boolean status; 
	@Column(name = "error")
	private String error; 
	@Column(name = "success")
	private String success; 
	@Column(name = "error_fields")
	private String errorFields;
	@Column(name = "queue_id")
	private Integer queueId;

	
	public ResponseExcelTable(Integer responseExcelId,Boolean status, String error, String success, String errorFields,Integer queueId) {
		super();
		this.status = status;
		this.error = error;
		this.success = success;
		this.errorFields = errorFields;
		this.responseExcelId=responseExcelId;
		this.queueId=queueId;
	}

	public ResponseExcelTable() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public Integer getResponseExcelId() {
		return responseExcelId;
	}

	public void setResponseExcelId(Integer responseExcelId) {
		this.responseExcelId = responseExcelId;
	}

	

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getSuccess() {
		return success;
	}

	public void setSuccess(String success) {
		this.success = success;
	}

	public String getErrorFields() {
		return errorFields;
	}

	public void setErrorFields(String errorFields) {
		this.errorFields = errorFields;
	}

	public Integer getQueueId() {
		return queueId;
	}

	public void setQueueId(Integer queueId) {
		this.queueId = queueId;
	} 
	

}
