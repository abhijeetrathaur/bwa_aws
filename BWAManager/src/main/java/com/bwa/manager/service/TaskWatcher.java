package com.bwa.manager.service;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bwa.manager.dto.BWATask;
import com.bwa.manager.dto.SAMTask;
import com.bwa.manager.dto.TaskStatusEnum;
import com.bwa.manager.util.BWAUtil;

public class TaskWatcher implements Runnable {
	
	protected static final Logger logger = LoggerFactory.getLogger(TaskWatcher.class);
	
	private ManagerActivity activity;
	
	private BWAManager manager;
	
	BWACommunicator communicator;
	
	public TaskWatcher(ManagerActivity activity, BWACommunicator communicator, BWAManager manager) {
		this.activity = activity;
		this.communicator = communicator;
		this.manager = manager;
	}

	@Override
	public void run() {

		boolean samTaskExecuted = false;

		while (true) {
			List<BWATask> tasks = activity.getBWATasks();
			if (isBWATaskFinished(tasks)) {
				if (!samTaskExecuted) {
					executeSAMTask(tasks);
					samTaskExecuted = true;
				}
				if (isSAMTaskFinished(activity.getSAMTasks())) {
					logger.info("SAM Tasks are completed...Stopping SAM worker");
					// manager.stopSAMWorker();
					break;
				} else {
					BWAUtil.sleep(30000);
					logger.info("SAM Tasks are still processing... Waiting for 35 secs");
				}

			} else {
				BWAUtil.sleep(30000);
				logger.info("BWA Tasks are still processing... Waiting for 35 secs");
			}
		}
	}
	
	private boolean isBWATaskFinished(List<BWATask> tasks) {
		boolean taskFinised = false;
		int completedTask = 0;
		for (BWATask bwaTask : tasks) {
			if(bwaTask.getStatus() != null && bwaTask.getWorkerIp() != null) {
				BWATask task = communicator.getBWATaskStatus(bwaTask);
				if(task.getStatus().equals(TaskStatusEnum.COMPLETED)) {
					completedTask ++;
				} 
			}
		}
		if(tasks.size() == completedTask) {
			taskFinised = true;
		}
		return taskFinised;
	}
	
	private boolean isSAMTaskFinished(List<SAMTask> tasks) {
		boolean taskFinised = false;
		int completedTask = 0;
		for (SAMTask samTask : tasks) {
			if(samTask.getStatus() != null && samTask.getWorkerIp() != null) {
				SAMTask task = communicator.getSAMTaskStatus(samTask);
				if(task.getStatus().equals(TaskStatusEnum.COMPLETED)) {
					completedTask ++;
				}
			}
		}
		if(tasks.size() == completedTask) {
			taskFinised = true;
		}
		return taskFinised;
	}
	
	
	private void executeSAMTask(List<BWATask> tasks) {
		logger.info("All Tasks are executed...Starting merging activity");
		//manager.stopWorkers();
		logger.info("Stopping worker ............");
		logger.info("Starting SAM Worker.........Waiting 30Sec");
		manager.startSAMWorkerTask();
		BWAUtil.sleep(30000);
		SAMTask samTask = new SAMTask();
		samTask.setStatus(TaskStatusEnum.NOT_STARTED);
		samTask.setRequestorIp(BWAUtil.getIpAddress());
		List<String> outputNames = tasks.stream().map(BWATask::getOutputFileName).collect(Collectors.toList());
		List<String> inputNames = tasks.stream().map(BWATask::getInputFileName).collect(Collectors.toList());
		outputNames.addAll(inputNames);
		samTask.setFiles(outputNames);
		samTask.setTaskName("SAMMerger");
		samTask.setRequestorIp(BWAUtil.getIpAddress());
		activity.updateTask(samTask);
		SAMTaskExecutor samTaskExecutor = new SAMTaskExecutor(activity, communicator);
		samTaskExecutor.setTaskQueue(10);
		samTaskExecutor.submitTask(samTask);
		new Thread(samTaskExecutor).start();
	}
	
}
