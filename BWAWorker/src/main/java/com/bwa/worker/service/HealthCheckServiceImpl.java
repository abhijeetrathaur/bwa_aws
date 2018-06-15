package com.bwa.worker.service;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bwa.worker.dto.BWAWorker;
import com.bwa.worker.dto.WorkerStatusEnum;
import com.bwa.worker.util.WorkerUtil;

@Service
public class HealthCheckServiceImpl implements HealthCheckService {

	@Autowired
	WorkerStatus workerStatus;
	
	@Autowired
	WorkerUtil workerUtil;

	@Override
	public BWAWorker getHealthCheck() {
		BWAWorker worker = new BWAWorker();
		try {
			InetAddress ip;
			ip = InetAddress.getLocalHost();
			worker.setIpAddress(ip.getHostName());
			if (workerStatus.isWorkerAvailable()) {
				worker.setStatus(WorkerStatusEnum.AVAILABLE);
				worker.setExecutionUrl(workerUtil.getWorkerExecutionPath(ip.getHostName()));
			} else {
				worker.setStatus(WorkerStatusEnum.UNAVAILABLE);
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return worker;
	}

}
