package org.sonar.plugins.jenkins.checks;

import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.check.BelongsToProfile;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.plugins.jenkins.config.ConfigXmlSource;
import org.sonar.squidbridge.annotations.SqaleConstantRemediation;
import org.sonar.squidbridge.annotations.SqaleSubCharacteristic;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

@Rule(key = "MaxArchiveLatestBuildCheck", name = "Dont archive more than one build. It will be stored on the master, which is not made for file-storage. Please outsource the build-artefacts to some fileserver.", priority = Priority.MINOR, tags = { "diskspace" })
@BelongsToProfile(title = CheckRepository.SONAR_WAY_PROFILE_NAME, priority = Priority.MINOR)
@SqaleSubCharacteristic(RulesDefinition.SubCharacteristics.RESOURCE_RELIABILITY)
@SqaleConstantRemediation("60min")
public class MaxArchiveLatestBuildCheck extends AbstractConfigXmlCheck {

	@Override
	public void validate(ConfigXmlSource xmlSourceCode) {
		setConfigXmlSource(xmlSourceCode);

		Document document = getConfigXMLSource().getDocument();
		NodeList nodes = document.getElementsByTagName("artifactNumToKeep");
		if (nodes.getLength() > 0) {
			int size = Integer.parseInt(nodes.item(0).getFirstChild().getNodeValue());
			if (size > 1) {
				createViolation(1, "Dont archive more than one build.");				
			}
		}
	}
}