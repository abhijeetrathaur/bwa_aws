package com.bwa.worker.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "worker")
public class WorkerProperties {

	private String loadBalancer;
	
	private String homeDirectory;
	
	public String getHomeDirectory() {
		return homeDirectory;
	}
	
	public void setHomeDirectory(String homeDirectory) {
		this.homeDirectory = homeDirectory;
	}
	
	public String getLoadBalancer() {
		return loadBalancer;
	}
	
	public void setLoadBalancer(String loadBalancer) {
		this.loadBalancer = loadBalancer;
	}
}
