package org.sonar.plugins.jenkins.config.types;

import java.util.ArrayList;
import java.util.List;

import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.rule.RuleKey;
import org.sonar.plugins.jenkins.config.JobConfigurationIssue;

public abstract class JobConfigSource {
	protected InputFile inputFile;
	protected final List<JobConfigurationIssue> configXmlIssues = new ArrayList<>();
	
	public JobConfigSource(InputFile file) {
		this.inputFile = file;
	}
	
	public void addViolation(JobConfigurationIssue issue) {
		this.configXmlIssues.add(issue);
	}
	
	public InputFile getInputFile() {
		return inputFile;
	}

	public void setInputFile(InputFile inputFile) {
		this.inputFile = inputFile;
	}

	public List<JobConfigurationIssue> getConfigIssues() {
		return configXmlIssues;
	}
	
	public final void createViolation(RuleKey ruleKey, Integer linePosition, String message) {
		addViolation(new JobConfigurationIssue(ruleKey, linePosition, message));
	}
}
