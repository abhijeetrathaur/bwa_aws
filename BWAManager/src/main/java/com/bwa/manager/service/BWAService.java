package com.bwa.manager.service;

/**
 * The Interface BWAService.
 */
public interface BWAService {

	/**
	 * Gets the worker count.
	 *
	 * @param ftpLocation the ftp location
	 * @return the worker count
	 */
	int getWorkerCount(String ftpLocation);
}
