package com.healthinsurance.Health.PersonalEntities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "queue_table")
public class QueueTable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "queue_id")
	private Integer queueId;
	
	@Column(name = "file_path")
	private String filePath;
	
	@Column(name = "row_read")
	private Integer rowRead;
	
	@Column(name = "row_count")
	private Integer rowCount;
	
	@Column(name = "is_processed")
	private Character isProcessed = 'N';
	
	@Column(name = "status")
	private Character status;
	
	@Column(name = "is_last_process")
	private Integer isLastProcess;

	public QueueTable(Integer queueId,String filePath, Integer rowRead, Integer rowCount, Character isProcessed, Character status,Integer isLastProcess) {
		super();
		this.queueId = queueId;
		this.rowRead = rowRead;
		this.rowCount = rowCount;
		this.isProcessed = isProcessed;
		this.status = status;
		this.filePath=filePath;
		this.isLastProcess=isLastProcess;
	}

	public QueueTable() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Integer getQueueId() {
		return queueId;
	}

	
	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public void setQueueId(Integer queueId) {
		this.queueId = queueId;
	}

	public Integer getRowRead() {
		return rowRead;
	}

	public void setRowRead(Integer rowRead) {
		this.rowRead = rowRead;
	}

	public Integer getRowCount() {
		return rowCount;
	}

	public void setRowCount(Integer rowCount) {
		this.rowCount = rowCount;
	}

	public Character getIsProcessed() {
		return isProcessed;
	}

	public void setIsProcessed(Character isProcessed) {
		this.isProcessed = isProcessed;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public Integer getIsLastProcess() {
		return isLastProcess;
	}

	public void setIsLastProcess(Integer isLastProcess) {
		this.isLastProcess = isLastProcess;
	}
}
