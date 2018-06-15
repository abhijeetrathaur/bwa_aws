package com.bwa.manager.aws;

import com.amazonaws.services.autoscaling.model.CreateAutoScalingGroupResult;

public interface AWSService {
	
	 CreateAutoScalingGroupResult createAutoScalingGroup(String groupName, int workerSize);
}
