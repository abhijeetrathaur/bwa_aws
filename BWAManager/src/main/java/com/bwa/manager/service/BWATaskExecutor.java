package com.bwa.manager.service;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bwa.manager.dto.BWATask;
import com.bwa.manager.dto.BWAWorker;

@Component
public class BWATaskExecutor implements Runnable {
	protected static final Logger logger = LoggerFactory.getLogger(BWATaskExecutor.class);

	@Autowired
	private ManagerActivity activity;

	@Autowired
	private BWACommunicator communicator;

	private BlockingQueue<BWATask> taskQueue;

	private volatile boolean isStopped;

	void setTaskQueue(int count) {
		taskQueue = new ArrayBlockingQueue<>(count);
	}

	public void submitTask(List<BWATask> tasks) {
		taskQueue.addAll(tasks);
	}

	void stopBWATaskExecutor(boolean isStopped) {
		this.isStopped = isStopped;
	}

	@Override
	public void run() {
		logger.info("Inside Runnn");
		while (!isStopped) {
			try {
				BWATask bwaTask = taskQueue.poll(30, TimeUnit.SECONDS);
				logger.info("Task {} ", bwaTask.getTaskName());
				BWAWorker availalbeWorker = communicator.getAvailableWorker();
				logger.info("Worker {} ", availalbeWorker.getIpAddress());
				bwaTask.setWorkerIp(availalbeWorker.getIpAddress());
				BWATask executedTask = communicator.executeTask(bwaTask);
				activity.updateTask(executedTask);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
