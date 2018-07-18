package com.bwa.manager.controller;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bwa.manager.service.BWAManager;

@RestController
@CrossOrigin
@RequestMapping(value = "/api/v1/")
public class BWAController {
	

	@Autowired
	BWAManager manager;
	
	@GetMapping(value = "/healthcheck")
	String healthCheck() {
		return "Health Check Passed";
	}
	
	@GetMapping(value = "/bwa/start")
	String start(@RequestAttribute(required = false, value="ftpLocation") String ftpLocation) {
		if(Objects.isNull(ftpLocation)) {
			ftpLocation = "ftp://ftp.1000genomes.ebi.ac.uk/vol1/ftp/phase3/data/HG00513/sequence_read/";
		}
		return manager.start(ftpLocation);
	}

}