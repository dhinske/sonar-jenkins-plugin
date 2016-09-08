package org.sonar.plugins.jenkins.checks;

import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.check.BelongsToProfile;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.plugins.jenkins.config.JobConfiguration;
import org.sonar.squidbridge.annotations.SqaleConstantRemediation;
import org.sonar.squidbridge.annotations.SqaleSubCharacteristic;
import org.w3c.dom.NodeList;

/**
 * Checks if a job is polling. Polling must die!
 * @author dhinske
 *
 */
@Rule(key = "PollingCheck", name = "Polling must die!", priority = Priority.MINOR, tags = { "convention" })
@BelongsToProfile(title = CheckRepository.SONAR_WAY_PROFILE_NAME, priority = Priority.MINOR)
@SqaleSubCharacteristic(RulesDefinition.SubCharacteristics.CPU_EFFICIENCY)
@SqaleConstantRemediation("5min")
public class PollingCheck extends AbstractConfigXmlCheck {

	@Override
	public void validate(JobConfiguration jobConfig) {

		NodeList nodes = jobConfig.getConfigXml().getDocument().getElementsByTagName("hudson.triggers.SCMTrigger");
		if (nodes.getLength() > 0) {
			jobConfig.getConfigXml().createViolation(getRuleKey(), 1, "Polling must die! Please replace this with triggering.");
		}
	}
}
