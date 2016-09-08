package org.sonar.plugins.jenkins.checks;

import org.sonar.api.rule.RuleKey;
import org.sonar.plugins.jenkins.config.JobConfiguration;

/**
 * Abstraction of a check in sonar. Requires a unique @RuleKey. validate() will
 * be called for all jobs from the @JenkinsSensor.
 * 
 * @author dhinske
 *
 */
public abstract class AbstractConfigXmlCheck {

	private RuleKey ruleKey;

	public final void setRuleKey(RuleKey ruleKey) {
		this.ruleKey = ruleKey;
	}

	public RuleKey getRuleKey() {
		return ruleKey;
	}

	public abstract void validate(JobConfiguration JobConfigSource);
}
