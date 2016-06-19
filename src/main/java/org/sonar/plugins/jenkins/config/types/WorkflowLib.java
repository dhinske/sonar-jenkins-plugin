package org.sonar.plugins.jenkins.config.types;

import org.sonar.api.batch.fs.InputFile;
import org.sonar.plugins.jenkins.config.JobConfigIssue;

public class WorkflowLib {
	private InputFile xmlFile;
	
	public WorkflowLib(InputFile xmlFile) {
		this.xmlFile = xmlFile;

	}

	public void addViolation(JobConfigIssue issue) {
		//
	}
}
