package com.bwa.manager.service;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bwa.manager.aws.AWSService;

@Service
public class BWAManagerImpl implements BWAManager {
	
	protected static final Logger logger = LoggerFactory.getLogger(BWAManagerImpl.class);
	
	private boolean managerRunning = false;
	
	@Autowired
	BWAService bwaService;

	@Autowired
	AWSService awsService;
	
	@Autowired
	ManagerActivity activity;
	
	@Autowired
	BWACommunicator communicator;

	@Override
	public String start(String ftpLocation) {
		
		if(managerRunning) {
			return "Manager is already processing the request.. Please retry after sometime.";
		}
		
		int workerCount = bwaService.getWorkerCount(ftpLocation);
		workerCount = 1;
		if(workerCount < 1) {
			return "Manager is unable to process the request";
		}
		awsService.createWorkerGroup("BWAAutoScaleGroup", workerCount);
		startTaskExecutor(workerCount);
		return "Manager has started processing the request";
	}
	
	@Override
	public void startSAMWorkerTask() {
		awsService.createSAMWorker("BWASAMWorker");
	}
	
	private void startTaskExecutor(int count) {
		BWATaskExecutor taskExecutor = new BWATaskExecutor(activity, communicator);
		taskExecutor.setTaskQueue(count + 10);
		taskExecutor.submitTask(activity.getBWATasks());
		
		TaskWatcher taskWatcher = new TaskWatcher(activity, communicator, this);
		
		new Thread(taskExecutor).start();
		new Thread(taskWatcher).start();
	}

	@Override
	public boolean stopWorkers() {
		return Objects.nonNull(awsService.deleteWorkerGroup("BWAAutoScaleGroup")) ? true : false;
	}

	@Override
	public boolean stopSAMWorker() {
		managerRunning = false;
		return Objects.nonNull(awsService.deleteWorkerGroup("BWASAMWorker")) ? true : false;
	}
	
}
