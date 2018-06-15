package com.bwa.worker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bwa.worker.dto.BWATask;
import com.bwa.worker.dto.BWAWorker;
import com.bwa.worker.executor.CommandExecutor;
import com.bwa.worker.service.HealthCheckService;
import com.bwa.worker.service.WorkerService;
import com.bwa.worker.service.WorkerStatus;

@RestController
@CrossOrigin
@RequestMapping(value = "/api/worker/")
public class WorkerController {

	@Autowired
	HealthCheckService healthCheckService;
	
	@Autowired
	CommandExecutor executor;
	
	@Autowired
	WorkerService worker;
	
	@Autowired
	WorkerStatus status;
	
	@GetMapping(value = "/healthcheck")
	BWAWorker healthCheck() {
		return healthCheckService.getHealthCheck();
	}
	
	@GetMapping(value = "/status/{taskName}")
	BWATask taskStatus(@PathVariable("taskName") String taskName) {
		return status.getBWATask(taskName);
	}
	
	@GetMapping(value = "/execute/{command}")
	void commandRunner(@PathVariable("command") String command) {
		executor.executeCommand(command);
	}
	
	@PostMapping(path = "/execute" , consumes= {MediaType.APPLICATION_JSON_VALUE})
	BWATask execute(@RequestBody BWATask task) {
		return worker.execute(task);
	}
}