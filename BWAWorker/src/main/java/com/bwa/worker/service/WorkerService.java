package com.bwa.worker.service;

import com.bwa.worker.dto.BWATask;
import com.bwa.worker.dto.SAMTask;

/**
 * The Interface WorkerService.
 */
public interface WorkerService {
	
	/**
	 * Execute.
	 */
	BWATask execute(BWATask task);
	
	SAMTask execute(SAMTask task);
}
