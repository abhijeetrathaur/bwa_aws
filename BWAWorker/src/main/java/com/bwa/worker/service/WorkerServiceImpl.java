package com.bwa.worker.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bwa.worker.dto.BWATask;
import com.bwa.worker.dto.TaskStatusEnum;
import com.bwa.worker.executor.CommandExecutor;

/**
 * The Interface WorkerService.
 */
@Service
public class WorkerServiceImpl implements WorkerService {

	@Autowired
	WorkerStatus workerStatus;
	
	@Autowired
	CommandExecutor executor;
	
	@Override
	public BWATask execute(BWATask task) {
		if(workerStatus.isWorkerAvailable()) {
			task.setStatus(TaskStatusEnum.STARTED);
			workerStatus.updateWorkerTask(task);
			new Thread(new WorkerTask(workerStatus, task, executor)).start();
		} else {
			task.setStatus(TaskStatusEnum.NOT_STARTED);
		}
		return task;
	}	
	
}
