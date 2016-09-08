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
 * Checks if a job with 'template' in its name is deactivated. Enforces that
 * template-jobs are only being used for inheritance.
 * 
 * @author dhinske
 *
 */
@Rule(key = "TemplateShouldBeDeactivatedCheck", name = "Jobs with 'template' in the name should be deactivated.", priority = Priority.MINOR, tags = {
		"convention" })
@BelongsToProfile(title = CheckRepository.SONAR_WAY_PROFILE_NAME, priority = Priority.MINOR)
@SqaleSubCharacteristic(RulesDefinition.SubCharacteristics.LOGIC_RELIABILITY)
@SqaleConstantRemediation("1min")
public class TemplateShouldBeDeactivatedCheck extends AbstractConfigXmlCheck {

	@Override
	public void validate(JobConfiguration jobConfig) {
		if (!jobConfig.getName().contains("template")) {
			return;
		}

		NodeList nodes = jobConfig.getConfigXml().getDocument().getElementsByTagName("disabled");
		for (int i = 0; i < nodes.getLength(); i++) {
			if ("true".equals(nodes.item(i).getFirstChild().getNodeValue())) {
				return;
			}
		}
		jobConfig.getConfigXml().createViolation(getRuleKey(), 1,
				"Jobs with 'template' in the name should be deactivated.");
	}
}