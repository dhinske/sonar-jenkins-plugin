package org.sonar.plugins.jenkins.config.types;

import java.util.ArrayList;
import java.util.List;

import org.sonar.api.batch.fs.InputFile;
import org.sonar.plugins.jenkins.config.JobConfigIssue;

public abstract class Config {
	protected InputFile inputFile;
	protected final List<JobConfigIssue> configXmlIssues = new ArrayList<>();
	
	public Config(InputFile file) {
		this.inputFile = file;
	}
	
	public void addViolation(JobConfigIssue issue) {
		this.configXmlIssues.add(issue);
	}
}
