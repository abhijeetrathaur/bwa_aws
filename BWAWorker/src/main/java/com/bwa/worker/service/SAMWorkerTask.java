package com.bwa.worker.service;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bwa.worker.dto.SAMTask;
import com.bwa.worker.dto.TaskStatusEnum;
import com.bwa.worker.executor.CommandExecutor;

public class SAMWorkerTask implements Runnable {
	
	protected static final Logger logger = LoggerFactory.getLogger(BWAWorkerTask.class);

	WorkerStatus status;
	
	SAMTask task;
	
	CommandExecutor executor;

	public SAMWorkerTask(WorkerStatus workerStatus, SAMTask task, CommandExecutor executor) {
		this.status = workerStatus;
		this.task = task;
		this.executor = executor;
	}

	@Override
	public void run() {
		List<String> files = task.getFiles();
		try {
			String exportVariable = "source ~/.bash_profile";
			executeCommand(exportVariable);

			for (String file : files) {
				String copyFile = command("scp -o StrictHostKeyChecking=no -o UserKnownHostsFile=/dev/null -i pem.key ec2-user@{1}:~/{0} .", file, task.getRequestorIp());
				executeCommand(copyFile);
				String access = command("sudo chmod 0777 {0}", file);
				executeCommand(access);
			}
			
			String fileNames = files.stream().collect(Collectors.joining(" "));
			String samGenerationCommand = command("bwa sampe ./bwa/hg19bwaidx {0} > {1}", fileNames, task.getTaskName() + ".sam");
			executeCommand(samGenerationCommand);
			
			String copySam = command("scp -o StrictHostKeyChecking=no -o UserKnownHostsFile=/dev/null -i pem.key {0}.sam ec2-user@{1}:~", task.getTaskName(), task.getRequestorIp());
			executeCommand(copySam);

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
