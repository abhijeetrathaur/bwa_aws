package com.bwa.worker.dto;

/**
 * The Class BWATask.
 */
public class BWATask {

	private String taskName;
	private String workerIp;
	private TaskStatusEnum status;
	private String fileLocation;
	private String requestorIp;

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getWorkerIp() {
		return workerIp;
	}

	public void setWorkerIp(String workerIp) {
		this.workerIp = workerIp;
	}

	public TaskStatusEnum getStatus() {
		return status;
	}

	public void setStatus(TaskStatusEnum status) {
		this.status = status;
	}
	
	public String getFileLocation() {
		return fileLocation;
	}
	
	public void setFileLocation(String fileLocation) {
		this.fileLocation = fileLocation;
	}
	
	public String getRequestorIp() {
		return requestorIp;
	}
	
	public void setRequestorIp(String requestorIp) {
		this.requestorIp = requestorIp;
	}
}
