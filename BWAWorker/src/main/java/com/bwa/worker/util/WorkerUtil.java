package com.bwa.worker.util;

import org.springframework.stereotype.Component;

/**
 * The Class WorkerUtil.
 */
@Component
public class WorkerUtil {

	public String getWorkerExecutionPath(String ipAddress) {
		StringBuilder path = new StringBuilder();
		path.append("http://").append(ipAddress).append(":8081").append("/api/worker/execute");
		return path.toString();
	}
}
