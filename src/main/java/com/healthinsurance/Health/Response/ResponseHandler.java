package com.healthinsurance.Health.Response;

public class ResponseHandler {
	private Long totalRecords;
	private Boolean status;
	private Object data;
	private String message;

	
	public ResponseHandler(Boolean status, Object data, String message, Long totalRecords) {
		super();
		this.status = status;
		this.data = data;
		this.message = message; 
		this.totalRecords = totalRecords;
		
	}
	public ResponseHandler() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Boolean getStatus() {
		return status;
	}
	public void setStatus(Boolean status) {
		this.status = status;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	public String getMessage() {
		return message;
	} 
	public void setMessage(String message) {
		this.message = message;
	}
	public Long getTotalRecords() { 
		return totalRecords;
	}
	public void setTotalRecords(long l) {
		this.totalRecords = l;
	}
	

}
