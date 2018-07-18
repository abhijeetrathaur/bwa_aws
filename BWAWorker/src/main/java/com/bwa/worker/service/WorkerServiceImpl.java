package com.bwa.worker.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bwa.worker.dto.BWATask;
import com.bwa.worker.dto.SAMTask;
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
			task.setInputFileName(task.getTaskName());
			task.setOutputFileName(task.getTaskName() + ".bwa");
			workerStatus.updateWorkerTask(task);
			new Thread(new BWAWorkerTask(workerStatus, task, executor)).start();
		} else {
			task.setStatus(TaskStatusEnum.NOT_STARTED);
		}
		return task;
	}

	@Override
	public SAMTask execute(SAMTask task) {
		if(workerStatus.isWorkerAvailable()) {
			task.setStatus(TaskStatusEnum.STARTED);
			task.setInputFileName(task.getTaskName());
			task.setOutputFileName(task.getTaskName() + ".sam");
			workerStatus.updateWorkerTask(task);
			new Thread(new SAMWorkerTask(workerStatus, task, executor)).start();
		} else {
			task.setStatus(TaskStatusEnum.NOT_STARTED);
		}
		return task;
	}	
	
}
