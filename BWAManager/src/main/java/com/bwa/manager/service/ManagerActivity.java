package com.bwa.manager.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import com.bwa.manager.dto.BWATask;
import com.bwa.manager.dto.BWAWorker;
import com.bwa.manager.dto.SAMTask;

@Component
public class ManagerActivity {
	
	private final Map<String, BWAWorker> bwaWorkerMap = new ConcurrentHashMap<>();
	
	private final Map<String, BWATask> bwaTaskMap = new ConcurrentHashMap<>();
	
	private final Map<String, SAMTask> samTaskMap = new ConcurrentHashMap<>();
	
	
	void updateWorker(BWAWorker worker) {
		bwaWorkerMap.put(worker.getIpAddress(), worker);
	}
	
	void removeWorker(BWAWorker worker) {
		bwaWorkerMap.remove(worker.getIpAddress());
	}
	
	void updateTask(BWATask task) {
		if(Objects.nonNull(task)) {
			bwaTaskMap.put(task.getTaskName(), task);
		}
	}
	
	void updateTask(SAMTask task) {
		if(Objects.nonNull(task)) {
			samTaskMap.put(task.getTaskName(), task);
		}
	}
	
	List<BWATask> getBWATasks() {
		return new ArrayList<>(bwaTaskMap.values());
	}
	
	List<SAMTask> getSAMTasks() {
		return new ArrayList<>(samTaskMap.values());
	}
	
}
