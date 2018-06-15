package com.bwa.worker.executor;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bwa.worker.properties.WorkerProperties;

@Component
public class CommandExecutor {

	protected static final Logger logger = LoggerFactory.getLogger(CommandExecutor.class);

	@Autowired
	WorkerProperties properties;
	
	public boolean executeCommand(String command) {
		boolean executed = false;
		try {
			logger.info("Executing command {}", command);
			ProcessBuilder builder = new ProcessBuilder();
			builder.command("sh", "-c", command);
			builder.directory(new File(properties.getHomeDirectory()));
			Process process = builder.start();
			StringBuilder out = new StringBuilder();
			BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line = null;
			String previous = null;
			while ((line = br.readLine()) != null)
				if (!line.equals(previous)) {
					previous = line;
					out.append(line).append('\n');
					logger.info(line);
				}
			// Check result
			if (process.waitFor() == 0) {
				logger.info("Success {} ", command);
				executed = true;
			} else {
				// Abnormal termination: Log command parameters and output and throw
				// ExecutionException
				logger.info("failure {} ", command);
				logger.info("Reason {} ", out.toString());
			}
		} catch (IOException | InterruptedException e) {
			logger.info("Command execution failed {}", e);
		}
		return executed;
	}

}
