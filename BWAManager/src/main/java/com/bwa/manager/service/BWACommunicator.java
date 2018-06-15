package com.bwa.manager.service;

import java.util.Random;

import org.springframework.stereotype.Component;

import com.bwa.manager.dto.BWATask;
import com.bwa.manager.dto.BWAWorker;
import com.bwa.manager.dto.TaskStatusEnum;
import com.bwa.manager.dto.WorkerStatusEnum;

@Component
public class BWACommunicator {

	synchronized BWAWorker getAvailableWorker() {
		BWAWorker bwaWorker;
		Random random = new Random();
	    if(random.nextBoolean()) {
	    	bwaWorker = new BWAWorker();
			bwaWorker.setIpAddress("10.10.10.10");
			bwaWorker.setStatus(WorkerStatusEnum.AVAILABLE);
	    } else {
	    	bwaWorker = new BWAWorker();
			bwaWorker.setIpAddress("20.20.20.20");
			bwaWorker.setStatus(WorkerStatusEnum.AVAILABLE);
	    }
		return bwaWorker;
	}
	
	BWATask executeTask(BWATask task) {
		task.setStatus(TaskStatusEnum.STARTED);
		return task;
	}
}
