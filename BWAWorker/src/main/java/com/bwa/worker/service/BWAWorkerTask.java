package com.bwa.worker.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bwa.worker.dto.BWATask;
import com.bwa.worker.dto.TaskStatusEnum;
import com.bwa.worker.executor.CommandExecutor;

public class BWAWorkerTask implements Runnable{
	
	protected static final Logger logger = LoggerFactory.getLogger(BWAWorkerTask.class);

	WorkerStatus status;
	
	BWATask task;
	
	CommandExecutor executor;
	
	public BWAWorkerTask(WorkerStatus status, BWATask task, CommandExecutor executor) {
		this.status = status;
		this.task = task;
		this.executor = executor;
	}
	
	@Override
	public void run() {
		
		try {
			
			String exportVariable = "source ~/.bash_profile";
			executeCommand(exportVariable);
			
			String fileLocation = task.getFileLocation();
			String downloadCommand = command("sudo curl {1} > {0}", task.getTaskName(), fileLocation);
			executeCommand(downloadCommand);
			
			Thread.sleep(10000);
			
			String access = command("sudo chmod 0777 {0}", task.getTaskName());
			executeCommand(access);
			
			String alignmentCommand = command("bwa aln -t 4 ./bwa/hg19bwaidx {0} > {1}", task.getTaskName(), task.getOutputFileName());
			executeCommand(alignmentCommand);
			
			String copyOriginal = command("scp -o StrictHostKeyChecking=no -o UserKnownHostsFile=/dev/null -i pem.key {0} ec2-user@{1}:~", task.getInputFileName(), task.getRequestorIp());
			executeCommand(copyOriginal);
			
			String copyBWA = command("scp -o StrictHostKeyChecking=no -o UserKnownHostsFile=/dev/null -i pem.key {0} ec2-user@{1}:~", task.getOutputFileName(), task.getRequestorIp());
			executeCommand(copyBWA);
			
			task.setStatus(TaskStatusEnum.COMPLETED);
			status.updateWorkerTask(task);
		} catch (Exception e) {
			logger.error("Task failed " + task.getTaskName());
			task.setStatus(TaskStatusEnum.FAILED);
		}
		
		
	}
	
	private void executeCommand(String command) throws Exception {
		boolean status = executor.executeCommand(command);
		if(!status) {
			throw new Exception();
		}
	}
	
	private String command(String command, Object... args) {
		return java.text.MessageFormat.format(command, args);
	}
	
	

}
