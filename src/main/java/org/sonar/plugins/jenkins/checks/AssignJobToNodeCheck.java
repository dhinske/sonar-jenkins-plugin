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

@Rule(key = "AssignJobToNodeCheck", name = "Every job should be assigned to a node or label", priority = Priority.MINOR, tags = { "convention" })
@BelongsToProfile(title = CheckRepository.SONAR_WAY_PROFILE_NAME, priority = Priority.MAJOR)
@SqaleSubCharacteristic(RulesDefinition.SubCharacteristics.MAINTAINABILITY_COMPLIANCE)
@SqaleConstantRemediation("5min")
public class AssignJobToNodeCheck extends AbstractConfigXmlCheck {
	
	@Override
	public void validate(ConfigXmlSource xmlSourceCode) {
		setConfigXmlSource(xmlSourceCode);
		
		Document document = getConfigXMLSource().getDocument();
		NodeList nodes = document.getElementsByTagName("assignedNode");
		if (nodes.getLength() > 0) {
			createViolation(1, "Every job should be assigned to a node or label.");
		}
	}
}
