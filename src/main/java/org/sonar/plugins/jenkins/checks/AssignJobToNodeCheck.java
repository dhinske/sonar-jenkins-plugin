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
 * Checks if a job is assigned to a node or label.
 * @author dhinske
 *
 */
@Rule(key = "AssignJobToNodeCheck", name = "Every job should be assigned to a node or label", priority = Priority.MINOR, tags = {
		"convention" })
@BelongsToProfile(title = CheckRepository.SONAR_WAY_PROFILE_NAME, priority = Priority.MAJOR)
@SqaleSubCharacteristic(RulesDefinition.SubCharacteristics.MAINTAINABILITY_COMPLIANCE)
@SqaleConstantRemediation("5min")
public class AssignJobToNodeCheck extends AbstractConfigXmlCheck {

	@Override
	public void validate(JobConfig jobConfig) {

		switch (jobConfig.getJobType()) {
		case FREESTYLE:
			Document document = jobConfig.getConfigXml().getDocument();
			NodeList nodes = document.getElementsByTagName("assignedNode");
			if (nodes.getLength() > 0) {
				jobConfig.getConfigXml().createViolation(getRuleKey(), 1, "Every job should be assigned to a node or label.");
			}
			break;
		case PIPELINE:
			//TODO: implement
			break;
		case MB_PIPELINE:
			//TODO: implement
			break;
		default:
			break;
		}
	}
}
