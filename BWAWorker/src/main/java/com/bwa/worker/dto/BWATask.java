package com.bwa.worker.dto;

/**
 * The Class BWATask.
 */
public class BWATask extends WorkerTask{

	private String fileLocation;
	
	public String getFileLocation() {
		return fileLocation;
	}
	
	public void setFileLocation(String fileLocation) {
		this.fileLocation = fileLocation;
	}
}
