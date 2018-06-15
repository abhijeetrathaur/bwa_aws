package com.bwa.manager.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "aws")
public class BWAProperties {

	private String loadBalancer;
	
	public String getLoadBalancer() {
		return loadBalancer;
	}
	
	public void setLoadBalancer(String loadBalancer) {
		this.loadBalancer = loadBalancer;
	}
}
