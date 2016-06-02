package org.sonar.plugins.jenkins.checks;

import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.check.BelongsToProfile;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.plugins.jenkins.config.JobConfigSource;
import org.sonar.squidbridge.annotations.SqaleConstantRemediation;
import org.sonar.squidbridge.annotations.SqaleSubCharacteristic;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

@Rule(key = "PollingCheck", name = "Polling must die!", priority = Priority.MINOR, tags = { "convention" })
@BelongsToProfile(title = CheckRepository.SONAR_WAY_PROFILE_NAME, priority = Priority.MINOR)
@SqaleSubCharacteristic(RulesDefinition.SubCharacteristics.CPU_EFFICIENCY)
@SqaleConstantRemediation("5min")
public class PollingCheck extends AbstractConfigXmlCheck {

	@Override
	public void validate(JobConfigSource xmlSourceCode) {
		setJobConfigSource(xmlSourceCode);
		
		Document document = getJobConfigSource().getDocument();
		NodeList nodes = document.getElementsByTagName("hudson.triggers.SCMTrigger");
		if (nodes.getLength() > 0) {
			createViolation(1, "Polling must die! Please replace this with triggering.");
		}
	}
}
