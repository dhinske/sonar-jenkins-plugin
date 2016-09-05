package org.sonar.plugins.jenkins.checks;

import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.check.BelongsToProfile;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.plugins.jenkins.config.JobConfig;
import org.sonar.squidbridge.annotations.SqaleConstantRemediation;
import org.sonar.squidbridge.annotations.SqaleSubCharacteristic;
import org.w3c.dom.NodeList;

@Rule(key = "TemplateShouldBeDeactivatedCheck", name = "TemplateShouldBeDeactivatedCheck!", priority = Priority.MINOR, tags = { "convention" })
@BelongsToProfile(title = CheckRepository.SONAR_WAY_PROFILE_NAME, priority = Priority.MINOR)
@SqaleSubCharacteristic(RulesDefinition.SubCharacteristics.LOGIC_RELIABILITY)
@SqaleConstantRemediation("1min")
public class TemplateShouldBeDeactivatedCheck extends AbstractConfigXmlCheck {

	@Override
	public void validate(JobConfig jobConfig) {

		if (!jobConfig.getName().contains("template")) {
			return;
		}
		
		NodeList nodes = jobConfig.getConfigXml().getDocument().getElementsByTagName("disabled");
		for (int i = 0; i < nodes.getLength(); i++) {
			if ("true".equals(nodes.item(i).getFirstChild().getNodeValue())) {
				return;
			}
		}
		jobConfig.getConfigXml().createViolation(getRuleKey(), 1, "Jobs with 'template' in the name should be deactivated.");
	}
}
