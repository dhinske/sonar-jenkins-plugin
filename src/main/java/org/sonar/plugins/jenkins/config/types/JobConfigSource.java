package org.sonar.plugins.jenkins.config.types;

import java.util.ArrayList;
import java.util.List;

import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.rule.RuleKey;
import org.sonar.plugins.jenkins.config.JobConfigIssue;

public abstract class JobConfigSource {
	protected InputFile inputFile;
	protected final List<JobConfigIssue> configXmlIssues = new ArrayList<>();
	
	public JobConfigSource(InputFile file) {
		this.inputFile = file;
	}
	
	public void addViolation(JobConfigIssue issue) {
		this.configXmlIssues.add(issue);
	}
	
	public List<JobConfigIssue> getConfigIssues() {
		return configXmlIssues;
	}
	
	public final void createViolation(RuleKey ruleKey, Integer linePosition, String message) {
		addViolation(new JobConfigIssue(ruleKey, linePosition, message));
	}
}
