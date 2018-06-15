package com.bwa.manager.aws;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.services.autoscaling.AmazonAutoScaling;
import com.amazonaws.services.autoscaling.AmazonAutoScalingClientBuilder;
import com.amazonaws.services.autoscaling.model.CreateAutoScalingGroupRequest;
import com.amazonaws.services.autoscaling.model.CreateAutoScalingGroupResult;import com.amazonaws.services.autoscaling.model.Tag;

@Service
public class AWSServiceImpl implements AWSService {
	
	protected static final Logger logger = LoggerFactory.getLogger(AWSServiceImpl.class);
	
	@Autowired
	AWSHelper awsHelper;

	@Override
	public CreateAutoScalingGroupResult createAutoScalingGroup(String groupName, int workerSize) {
		logger.info("Creating auto scaling group : {} with Size : {}", groupName, workerSize);
		AmazonAutoScaling autoScalingGroup = AmazonAutoScalingClientBuilder.standard()
				.withCredentials(new AWSStaticCredentialsProvider(awsHelper.getCredentials())).withRegion("us-east-1").build();

		Tag tag = new Tag();
		tag.setKey("Name");
		tag.setValue("BWAWorker");
		CreateAutoScalingGroupRequest request = new CreateAutoScalingGroupRequest()
				.withAutoScalingGroupName(groupName)
				.withVPCZoneIdentifier("subnet-05f72f314e95edf26, subnet-0491d3faae2f8adac")
				.withDesiredCapacity(workerSize)
				.withMinSize(0)
				.withMaxSize(workerSize)
				.withLaunchConfigurationName("BWAAutoLaunch")
				.withTags(tag)
				.withTargetGroupARNs("arn:aws:elasticloadbalancing:us-east-1:853325255752:targetgroup/BWATargate/7091cff8c6ad6115");
		return autoScalingGroup.createAutoScalingGroup(request);
	}

}
