package org.sonar.plugins.jenkins.checks;

import org.sonar.api.rule.RuleKey;
import org.sonar.plugins.jenkins.config.JobConfigIssue;
import org.sonar.plugins.jenkins.config.types.ConfigXml;

public abstract class AbstractConfigXmlCheck {

	private RuleKey ruleKey;
	private ConfigXml jobConfigSource;

	protected final void createViolation(Integer linePosition, String message) {
		jobConfigSource.addViolation(new JobConfigIssue(ruleKey, linePosition, message));
	}

	protected ConfigXml getJobConfigSource() {
		return jobConfigSource;
	}

	public final void setRuleKey(RuleKey ruleKey) {
		this.ruleKey = ruleKey;
	}

	public RuleKey getRuleKey() {
		return ruleKey;
	}

	protected void setJobConfigSource(ConfigXml JobConfigSource) {
		this.jobConfigSource = JobConfigSource;
	}

	public abstract void validate(ConfigXml JobConfigSource);
}
