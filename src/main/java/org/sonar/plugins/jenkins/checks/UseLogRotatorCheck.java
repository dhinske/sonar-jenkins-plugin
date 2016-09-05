package org.sonar.plugins.jenkins.checks;

import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.check.BelongsToProfile;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.plugins.jenkins.config.JobConfig;
import org.sonar.squidbridge.annotations.SqaleConstantRemediation;
import org.sonar.squidbridge.annotations.SqaleSubCharacteristic;
import org.w3c.dom.NodeList;

@Rule(key = "UseLogRotatorCheck", name = "You should use the LogRotator-Plugin", priority = Priority.MINOR, tags = {
		"convention" })
@BelongsToProfile(title = CheckRepository.SONAR_WAY_PROFILE_NAME, priority = Priority.MINOR)
@SqaleSubCharacteristic(RulesDefinition.SubCharacteristics.RESOURCE_RELIABILITY)
@SqaleConstantRemediation("1min")
public class UseLogRotatorCheck extends AbstractConfigXmlCheck {

	@Override
	public void validate(JobConfig jobConfig) {

		NodeList nodes = jobConfig.getConfigXml().getDocument().getElementsByTagName("logRotator");
		if (nodes.getLength() == 0) {
			jobConfig.getConfigXml().createViolation(getRuleKey(), 1, "Please use the Logrotator to limit logs and/or artifacts!");
		}
	}
}