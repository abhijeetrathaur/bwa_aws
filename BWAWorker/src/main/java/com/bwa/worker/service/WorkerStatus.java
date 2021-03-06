package com.bwa.worker.service;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import com.bwa.worker.dto.BWATask;
import com.bwa.worker.dto.SAMTask;
import com.bwa.worker.dto.TaskStatusEnum;

/**
 * The Class WorkerStatus.
 */
@Component
public class WorkerStatus {
	
	private final Map<String, BWATask> bwaTaskMap = new ConcurrentHashMap<>();

	private final Map<String, SAMTask> samTaskMap = new ConcurrentHashMap<>();
	
	/**
	 * Update worker task.
	 *
	 * @param task the task
	 */
	public void updateWorkerTask(BWATask task) {
		bwaTaskMap.put(task.getTaskName(), task);
	}
	
	public void updateWorkerTask(SAMTask task) {
		samTaskMap.put(task.getTaskName(), task);
	}
	
	/**
	 * Gets the BWA task.
	 *
	 * @param taskName the task name
	 * @return the BWA task
	 */
	public BWATask getBWATask(String taskName) {
		return bwaTaskMap.get(taskName);
	}
	
	public SAMTask getSAMTask(String taskName) {
		return samTaskMap.get(taskName);
	}
	
	public boolean isWorkerAvailable() {
		int count = 0;
		Set<Entry<String, BWATask>> taskMapEntrySet = bwaTaskMap.entrySet();
		for (Entry<String, BWATask> entry : taskMapEntrySet) {
			BWATask task = entry.getValue();
			if(task.getStatus().compareTo(TaskStatusEnum.STARTED) == 0) {
				count ++;
				break;
			}
		}
		
		Set<Entry<String, SAMTask>> samTaskMapEntrySet = samTaskMap.entrySet();
		for (Entry<String, SAMTask> entry : samTaskMapEntrySet) {
			SAMTask task = entry.getValue();
			if(task.getStatus().compareTo(TaskStatusEnum.STARTED) == 0) {
				count ++;
				break;
			}
		}
		return count == 0 ? true : false;
	}
}
