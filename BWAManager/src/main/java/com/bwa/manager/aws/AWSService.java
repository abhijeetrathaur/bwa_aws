package com.bwa.manager.aws;

import com.amazonaws.services.autoscaling.model.CreateAutoScalingGroupResult;
import com.amazonaws.services.autoscaling.model.DeleteAutoScalingGroupResult;

public interface AWSService {
	
	 CreateAutoScalingGroupResult createWorkerGroup(String groupName, int workerSize);
	 
	 DeleteAutoScalingGroupResult deleteWorkerGroup(String groupName);
	 
	 CreateAutoScalingGroupResult createSAMWorker(String groupName);
	
}
