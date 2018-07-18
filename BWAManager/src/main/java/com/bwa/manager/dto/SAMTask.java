package com.bwa.manager.dto;

import java.util.List;

public class SAMTask extends WorkerTask {
	
	List<String> files;
	
	public List<String> getFiles() {
		return files;
	}
	
	public void setFiles(List<String> files) {
		this.files = files;
	}

}
