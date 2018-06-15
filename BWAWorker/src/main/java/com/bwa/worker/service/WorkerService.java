package com.bwa.worker.service;

import com.bwa.worker.dto.BWATask;

/**
 * The Interface WorkerService.
 */
public interface WorkerService {
	
	/**
	 * Execute.
	 */
	BWATask execute(BWATask task);
}
