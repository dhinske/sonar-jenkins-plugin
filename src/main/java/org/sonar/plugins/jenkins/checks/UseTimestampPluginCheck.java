package org.sonar.plugins.jenkins.checks;

import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.check.BelongsToProfile;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.plugins.jenkins.config.JobConfig;
import org.sonar.squidbridge.annotations.SqaleConstantRemediation;
import org.sonar.squidbridge.annotations.SqaleSubCharacteristic;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

/**
 * Checks if the timestamp-plugin is being used. Enforcing this plugin leads to
 * timestamps in each console output.
 * 
 * @author dhinske
 *
 */
@Rule(key = "UseTimestampPluginCheck", name = "You should use the Timestamp-Plugin", priority = Priority.INFO, tags = {
		"convention" })
@BelongsToProfile(title = CheckRepository.SONAR_WAY_PROFILE_NAME, priority = Priority.INFO)
@SqaleSubCharacteristic(RulesDefinition.SubCharacteristics.READABILITY)
@SqaleConstantRemediation("1min")
public class UseTimestampPluginCheck extends AbstractConfigXmlCheck {

	@Override
	public void validate(JobConfig jobConfig) {

		switch (jobConfig.getJobType()) {
		case FREESTYLE:
			Document document = jobConfig.getConfigXml().getDocument();
			NodeList nodes = document.getElementsByTagName("hudson.plugins.timestamper.TimestamperBuildWrapper");
			if (nodes.getLength() == 0) {
				jobConfig.getConfigXml().createViolation(getRuleKey(), 1,
						"Every job should use the Timestamper-Plugin.");
			}
			break;
		case PIPELINE:

			break;
		case MB_PIPELINE:

			break;
		default:
			break;
		}
	}
}