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
 * Checks if the job only archives a maximum of one artifact. The can help to
 * keep the disk space on the master clean.
 * 
 * @author dhinske
 *
 */
@Rule(key = "MaxArchiveLatestBuildCheck", name = "Dont archive more than one build.", priority = Priority.MINOR, tags = {
		"diskspace" })
@BelongsToProfile(title = CheckRepository.SONAR_WAY_PROFILE_NAME, priority = Priority.MINOR)
@SqaleSubCharacteristic(RulesDefinition.SubCharacteristics.RESOURCE_RELIABILITY)
@SqaleConstantRemediation("60min")
public class MaxArchiveLatestBuildCheck extends AbstractConfigXmlCheck {

	@Override
	public void validate(JobConfiguration jobConfig) {
		NodeList nodes = jobConfig.getConfigXml().getDocument().getElementsByTagName("artifactNumToKeep");
		if (nodes.getLength() > 0) {
			int size = Integer.parseInt(nodes.item(0).getFirstChild().getNodeValue());
			if (size > 1) {
				jobConfig.getConfigXml().createViolation(getRuleKey(), 1, "Dont archive more than one build.");
			}
		}
	}
}