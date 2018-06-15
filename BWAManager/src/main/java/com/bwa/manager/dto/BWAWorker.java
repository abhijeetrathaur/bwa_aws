package com.bwa.manager.dto;

public class BWAWorker {
	
	private String ipAddress;
	private WorkerStatusEnum status;
	
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
	
}
