package com.bwa.manager.aws;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.services.autoscaling.AmazonAutoScaling;
import com.amazonaws.services.autoscaling.AmazonAutoScalingClientBuilder;
import com.amazonaws.services.autoscaling.model.CreateAutoScalingGroupRequest;
import com.amazonaws.services.autoscaling.model.CreateAutoScalingGroupResult;
import com.amazonaws.services.autoscaling.model.DeleteAutoScalingGroupRequest;
import com.amazonaws.services.autoscaling.model.DeleteAutoScalingGroupResult;
import com.amazonaws.services.autoscaling.model.Tag;

@Service
public class AWSServiceImpl implements AWSService {
	
	protected static final Logger logger = LoggerFactory.getLogger(AWSServiceImpl.class);
	
	@Autowired
	AWSHelper awsHelper;
	
	
	
	@Override
	public CreateAutoScalingGroupResult createWorkerGroup(String groupName, int workerSize) {
		AmazonAutoScaling autoScalingGroup = getAutoScaling();
		logger.info("Creating auto scaling group : {} with Size : {}", groupName, workerSize);
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

	@Override
	public CreateAutoScalingGroupResult createSAMWorker(String groupName) {
		AmazonAutoScaling autoScalingGroup = getAutoScaling();
		int workerSize = 1;
		logger.info("Creating SAM worker group : {} with Size : {}", groupName, workerSize);

		Tag tag = new Tag();
		tag.setKey("Name");
		tag.setValue("BWASAMWorker");
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

	@Override
	public DeleteAutoScalingGroupResult deleteWorkerGroup(String groupName) {
		AmazonAutoScaling autoScalingGroup = getAutoScaling();
		DeleteAutoScalingGroupRequest deleteRequest = new DeleteAutoScalingGroupRequest().withAutoScalingGroupName(groupName).withForceDelete(true);
		return autoScalingGroup.deleteAutoScalingGroup(deleteRequest);
	}
	
	AmazonAutoScaling getAutoScaling() {
		return AmazonAutoScalingClientBuilder.standard()
				.withCredentials(new AWSStaticCredentialsProvider(awsHelper.getCredentials())).withRegion("us-east-1").build();
	}
	
}
