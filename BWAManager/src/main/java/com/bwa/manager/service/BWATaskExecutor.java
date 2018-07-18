package com.bwa.manager.service;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bwa.manager.dto.BWATask;

public class BWATaskExecutor implements Runnable {
	protected static final Logger logger = LoggerFactory.getLogger(BWATaskExecutor.class);

	private ManagerActivity activity;

	private BWACommunicator communicator;
	
	public BWATaskExecutor(ManagerActivity activity, BWACommunicator communicator) {
		this.activity = activity;
		this.communicator = communicator;
	}

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
		logger.info("Inside BWA Task Executor");
		while (!isStopped) {
			try {
				BWATask bwaTask = taskQueue.poll(30, TimeUnit.SECONDS);
				if (Objects.nonNull(bwaTask)) {
					logger.info("Trying to execute BWA Task {} ", bwaTask.getTaskName());
					BWATask executedTask = communicator.executeTask(bwaTask);
					activity.updateTask(executedTask);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
