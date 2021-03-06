package org.sonar.plugins.jenkins.config;

import org.sonar.api.rule.RuleKey;

public class JobConfigurationIssue {

	private final RuleKey ruleKey;
	private final Integer line;
	private final String message;

	public JobConfigurationIssue(RuleKey ruleKey, Integer line, String message) {
		this.ruleKey = ruleKey;
		this.line = line;
		this.message = message;
	}

	public RuleKey getRuleKey() {
		return ruleKey;
	}

	public Integer getLine() {
		return line;
	}

	public String getMessage() {
		return message;
	}
}
