package org.sonar.plugins.jenkins.checks;

import org.sonar.api.rule.RuleKey;
import org.sonar.plugins.jenkins.config.ConfigXmlIssue;
import org.sonar.plugins.jenkins.config.ConfigXmlSource;

public abstract class AbstractConfigXmlCheck {

  private RuleKey ruleKey;
  private ConfigXmlSource configXmlSource;

  protected final void createViolation(Integer linePosition, String message) {
    getConfigXMLSource().addViolation(new ConfigXmlIssue(ruleKey, linePosition, message));
  }

  protected ConfigXmlSource getConfigXMLSource() {
    return configXmlSource;
  }

  public final void setRuleKey(RuleKey ruleKey) {
    this.ruleKey = ruleKey;
  }

  public RuleKey getRuleKey() {
    return ruleKey;
  }

  protected void setConfigXmlSource(ConfigXmlSource configXmlSource) {
    this.configXmlSource = configXmlSource;
  }

  public abstract void validate(ConfigXmlSource configXmlSource);
}
