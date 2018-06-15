package com.bwa.manager.aws;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.bwa.manager.properties.AWSProperties;

@Component
public class AWSHelper {

	@Autowired
	AWSProperties awsProperties;
	
	private AWSCredentials credentials;
	
	public AWSCredentials getCredentials() {
		if(Objects.isNull(credentials)) {
			credentials = new BasicAWSCredentials(awsProperties.getAccessKey(), awsProperties.getSecretKey());
		}
		return credentials;
	}

}
