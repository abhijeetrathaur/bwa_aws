package com.bwa.manager.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bwa.manager.aws.AWSService;

@Service
public class BWAManagerImpl implements BWAManager {
	
	protected static final Logger logger = LoggerFactory.getLogger(BWAManagerImpl.class);
	
	@Autowired
	BWAService bwaService;

	@Autowired
	AWSService awsService;
	
	@Autowired
	ManagerActivity activity;
	
	@Autowired
	BWATaskExecutor taskExecutor;
	
	@Override
	public String start(String ftpLocation) {
		
		int workerCount = bwaService.getWorkerCount(ftpLocation);
		if(workerCount < 1) {
			return "Manager is unable to process the request";
		}
		awsService.createAutoScalingGroup("BWAAutoScaleGroup", workerCount);
		//startTaskExecutor(workerCount);
		return "Manager has started processing the request";
	}
	
	private void startTaskExecutor(int count) {
		taskExecutor.setTaskQueue(count);
		taskExecutor.submitTask(activity.getTasks());
		Thread taskThread = new Thread(taskExecutor);
		taskThread.start();
	}
	
}
