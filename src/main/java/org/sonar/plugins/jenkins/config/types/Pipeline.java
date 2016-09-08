package org.sonar.plugins.jenkins.config.types;

import org.sonar.api.batch.fs.InputFile;
import org.sonar.plugins.jenkins.config.JobConfigurationIssue;

public class Pipeline {
	private InputFile xmlFile;
	
	public Pipeline(InputFile xmlFile) {
		this.xmlFile = xmlFile;

	}

	public void addViolation(JobConfigurationIssue issue) {
		//
	}
}
