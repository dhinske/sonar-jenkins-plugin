package org.sonar.plugins.jenkins.checks;

import org.sonar.api.rule.RuleKey;
import org.sonar.plugins.jenkins.config.JobConfigIssue;
import org.sonar.plugins.jenkins.config.JobConfigSource;

public abstract class AbstractConfigXmlCheck {

	private RuleKey ruleKey;
	private JobConfigSource JobConfigSource;

	protected final void createViolation(Integer linePosition, String message) {
		getJobConfigSource().addViolation(new JobConfigIssue(ruleKey, linePosition, message));
	}

	protected JobConfigSource getJobConfigSource() {
		return JobConfigSource;
	}

	public final void setRuleKey(RuleKey ruleKey) {
		this.ruleKey = ruleKey;
	}

	public RuleKey getRuleKey() {
		return ruleKey;
	}

	protected void setJobConfigSource(JobConfigSource JobConfigSource) {
		this.JobConfigSource = JobConfigSource;
	}

	public abstract void validate(JobConfigSource JobConfigSource);
}
