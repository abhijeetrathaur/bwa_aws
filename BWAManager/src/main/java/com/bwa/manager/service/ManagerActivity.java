package com.bwa.manager.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import com.bwa.manager.dto.BWATask;
import com.bwa.manager.dto.BWAWorker;

@Component
public class ManagerActivity {
	
	private final Map<String, BWAWorker> bwaWorkerMap = new ConcurrentHashMap<>();
	
	private final Map<String, BWATask> bwaTaskMap = new ConcurrentHashMap<>();
	
	
	void updateWorker(BWAWorker worker) {
		bwaWorkerMap.put(worker.getIpAddress(), worker);
	}
	
	void removeWorker(BWAWorker worker) {
		bwaWorkerMap.remove(worker.getIpAddress());
	}
	
	void updateTask(BWATask task) {
		bwaTaskMap.put(task.getTaskName(), task);
	}
	
	List<BWATask> getTasks() {
		return new ArrayList<>(bwaTaskMap.values());
	}
	
}
