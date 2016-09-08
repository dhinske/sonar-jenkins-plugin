package org.sonar.plugins.jenkins.checks;

import org.sonar.api.rule.RuleKey;
import org.sonar.plugins.jenkins.config.JobConfig;

public abstract class AbstractConfigXmlCheck {

	private RuleKey ruleKey;

	public final void setRuleKey(RuleKey ruleKey) {
		this.ruleKey = ruleKey;
	}

	public RuleKey getRuleKey() {
		return ruleKey;
	}

	public abstract void validate(JobConfig JobConfigSource);
}
