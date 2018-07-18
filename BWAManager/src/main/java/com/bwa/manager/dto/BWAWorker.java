package com.bwa.manager.dto;

/**
 * The Class BWAWorker.
 */
public class BWAWorker {
	
	private String ipAddress;
	private WorkerStatusEnum status;
	private String executionUrl;
	
	public String getIpAddress() {
		return ipAddress;
	}
	
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	
	public WorkerStatusEnum getStatus() {
		return status;
	}
	
	public void setStatus(WorkerStatusEnum status) {
		this.status = status;
	}
	
	public String getExecutionUrl() {
		return executionUrl;
	}
	
	public void setExecutionUrl(String executionUrl) {
		this.executionUrl = executionUrl;
	}
	
}
