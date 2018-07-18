package com.bwa.manager.service;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.bwa.manager.dto.BWATask;
import com.bwa.manager.dto.BWAWorker;
import com.bwa.manager.dto.SAMTask;
import com.bwa.manager.dto.WorkerStatusEnum;
import com.bwa.manager.properties.BWAProperties;
import com.bwa.manager.util.BWAUtil;

@Component
public class BWACommunicator {

	protected static final Logger logger = LoggerFactory.getLogger(BWACommunicator.class);
	
	private RestTemplate httpClient = new RestTemplate();
	
	@Autowired
	private BWAProperties properties;
	
	private String workerHealthCheck;
	
	
	  private BWAWorker getAvailableWorker() {
		BWAWorker bwaWorker = null;
		while (true) {
			try {
				ResponseEntity<BWAWorker> workerResponse = httpClient.getForEntity(getWorkerHealthCheck(), BWAWorker.class);
				if (workerResponse.getStatusCode().is2xxSuccessful()
						&& workerResponse.getBody().getStatus().equals(WorkerStatusEnum.AVAILABLE)) {
					bwaWorker = workerResponse.getBody();
					break;
				}
			} catch (HttpServerErrorException e) {
				logger.info("Error occurred while sending request.. Retrying", e.getMessage());
			}
			logger.info("Workers are not available waiting for 15sec");
			BWAUtil.sleep(15000);
		}
		return bwaWorker;
	}
	
	BWATask executeTask(BWATask task) {
		BWAWorker availalbeWorker = getAvailableWorker();
		logger.info("Got BWA Worker {} ", availalbeWorker.getIpAddress());
		String executionUrl = availalbeWorker.getExecutionUrl();
		task.setWorkerIp(availalbeWorker.getIpAddress());
		logger.info("Execute task  {}", task);
		logger.info("Execution URL  {}", executionUrl);
		ResponseEntity<BWATask> taskResonseEntity = httpClient.postForEntity(executionUrl + "/bwa", task, BWATask.class);
		return taskResonseEntity.getBody();
	}
	
	SAMTask executeTask(SAMTask task) {
		BWAWorker availalbeWorker = getAvailableWorker();
		logger.info("Got SAM Worker {} ", availalbeWorker.getIpAddress());
		String executionUrl = availalbeWorker.getExecutionUrl();
		task.setWorkerIp(availalbeWorker.getIpAddress());
		ResponseEntity<SAMTask> taskResonseEntity = httpClient.postForEntity(executionUrl + "/sam", task, SAMTask.class);
		return taskResonseEntity.getBody();
	}
	
	
	
	private String getWorkerHealthCheck() {
		if(Objects.isNull(workerHealthCheck)) {
			workerHealthCheck = properties.getLoadBalancer() + "/api/worker/healthcheck";
		} 
		return workerHealthCheck;
	}
	
	public BWATask getBWATaskStatus(BWATask task) {
		BWATask taskResponse = null;
		while (true) {
			try {
				String bwaStatusUrl = getBWAStatusUrl(task.getWorkerIp(), task.getTaskName());
				logger.info("Checking BWA task status {}", bwaStatusUrl);
				
				ResponseEntity<BWATask> bwaTaskResponse = httpClient.getForEntity(bwaStatusUrl, BWATask.class);
				taskResponse = bwaTaskResponse.getBody();
				break;
			} catch (Exception e) {
				logger.info("Workers are not available waiting for 30sec");
				BWAUtil.sleep(30000);
			}
		}
		return taskResponse;
	}
	
	public SAMTask getSAMTaskStatus(SAMTask task) {
		SAMTask taskResponse = null;
		while (true) {
			try {
				String samStatusUrl = getSAMStatusUrl(task.getWorkerIp(), task.getTaskName());
				logger.info("Checking SAM task status {}", samStatusUrl);
				ResponseEntity<SAMTask> bwaTaskResponse = httpClient.getForEntity(samStatusUrl, SAMTask.class);
				taskResponse = bwaTaskResponse.getBody();
				break;
			} catch (Exception e) {
				logger.info("Workers are not available waiting for 30sec");
				BWAUtil.sleep(30000);
			}
		}
		return taskResponse;
	}
	
	private String getBWAStatusUrl(String ip, String taskName) {
		return "http://" + ip + ":8081/api/worker/status/bwa/" + taskName;
	}
	
	private String getSAMStatusUrl(String ip, String taskName) {
		return "http://" + ip + ":8081/api/worker/status/sam/" + taskName;
	}
}
