package com.bwa.manager.service;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bwa.manager.dto.SAMTask;

/**
 * The Class SAMTaskExecutor.
 */
public class SAMTaskExecutor implements Runnable {
	protected static final Logger logger = LoggerFactory.getLogger(SAMTaskExecutor.class);

	private ManagerActivity activity;

	private BWACommunicator communicator;
	
	/**
	 * Instantiates a new SAM task executor.
	 *
	 * @param activity the activity
	 * @param communicator the communicator
	 */
	public SAMTaskExecutor(ManagerActivity activity, BWACommunicator communicator) {
		this.activity = activity;
		this.communicator = communicator;
	}

	private BlockingQueue<SAMTask> taskQueue;

	private volatile boolean isStopped;

	public void setTaskQueue(int count) {
		taskQueue = new ArrayBlockingQueue<>(count);
	}

	/**
	 * Submit task.
	 *
	 * @param tasks the tasks
	 */
	public void submitTask(List<SAMTask> tasks) {
		taskQueue.addAll(tasks);
	}
	
	public void submitTask(SAMTask task) {
		taskQueue.add(task);
	}

	/**
	 * Stop BWA task executor.
	 *
	 * @param isStopped the is stopped
	 */
	void stopBWATaskExecutor(boolean isStopped) {
		this.isStopped = isStopped;
	}

	@Override
	public void run() {
		logger.info("Inside SAM Task Executor");
		while (!isStopped) {
			try {
				SAMTask samTask = taskQueue.poll(30, TimeUnit.SECONDS);
				if (Objects.nonNull(samTask)) {
					logger.info("Trying to execute SAM Task {} ", samTask.getTaskName());
					SAMTask executedTask = communicator.executeTask(samTask);
					activity.updateTask(executedTask);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
