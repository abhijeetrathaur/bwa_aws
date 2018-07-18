package com.bwa.manager.service;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazonaws.util.CollectionUtils;
import com.bwa.manager.dto.BWATask;
import com.bwa.manager.dto.TaskStatusEnum;
import com.bwa.manager.util.BWAUtil;

/**
 * The Class FileDetailsServiceImpl.
 */
@Service
public class BWAServiceImpl implements BWAService {
	
	@Autowired
	ManagerActivity activity;
	
	private List<String> getFiles(String fileLocation) {
		List<String> files = new ArrayList<>();
		try {
			URL url = new URL(fileLocation);
			String host = url.getHost();
			String path = url.getPath();
			FTPClient client = new FTPClient();
			client.connect(host);
			client.enterLocalPassiveMode();
			client.login("anonymous", "");
			FTPFile[] ftpFiles = client.listFiles(path);
			for (FTPFile ftpFile : ftpFiles) {
				if (ftpFile.getName().equals("ERR034509_2.filt.fastq.gz")
						|| ftpFile.getName().equals("ERR034509_1.filt.fastq.gz")) {
					BWATask bwaTask = new BWATask();
					bwaTask.setTaskName(ftpFile.getName());
					bwaTask.setStatus(TaskStatusEnum.NOT_STARTED);
					activity.updateTask(bwaTask);
					files.add(ftpFile.getName());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return files;
	}
	
	@Override
	public int getWorkerCount(String fileLocation) {
		int size = 0;
		//List<String> files = getFiles(fileLocation);
		List<String> files = Arrays.asList("https://s3.amazonaws.com/bwafiles/sml.11.fastq",
										   "https://s3.amazonaws.com/bwafiles/sml.22.fastq", 
										   "https://s3.amazonaws.com/bwafiles/sml.33.fastq",
										   "https://s3.amazonaws.com/bwafiles/sml.44.fastq");
		if(!CollectionUtils.isNullOrEmpty(files)) {
			size = files.size() - 1;
		}
		for (String file : files) {
			BWATask bwaTask = new BWATask();
			bwaTask.setTaskName(getLastBitFromUrl(file));
			bwaTask.setStatus(TaskStatusEnum.NOT_STARTED);
			bwaTask.setFileLocation(file);
			bwaTask.setRequestorIp(BWAUtil.getIpAddress());
			activity.updateTask(bwaTask);
		}
		return size;
	}
	
	private String getLastBitFromUrl(final String url){
	    return url.replaceFirst(".*/([^/?]+).*", "$1");
	}

}
