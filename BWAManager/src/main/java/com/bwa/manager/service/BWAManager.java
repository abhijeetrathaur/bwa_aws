package com.bwa.manager.service;

/**
 * The Interface BWAManager.
 */
public interface BWAManager {
	
	/**
	 * Start.
	 *
	 * @param ftpLocation the ftp location
	 * @return the string
	 */
	String start(String ftpLocation);
	
	/**
	 * Stop workers.
	 *
	 * @return true, if successful
	 */
	boolean stopWorkers();
	
	/**
	 * Stop SAM worker.
	 *
	 * @return true, if successful
	 */
	boolean stopSAMWorker();

	/**
	 * Start SAM worker task.
	 */
	void startSAMWorkerTask();

}
