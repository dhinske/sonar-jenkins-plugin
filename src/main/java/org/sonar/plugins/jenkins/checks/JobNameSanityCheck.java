package org.sonar.plugins.jenkins.checks;

import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.check.BelongsToProfile;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.plugins.jenkins.config.ConfigXmlSource;
import org.sonar.squidbridge.annotations.SqaleConstantRemediation;
import org.sonar.squidbridge.annotations.SqaleSubCharacteristic;

@Rule(key = "JobNameSanityCheck", name = "limit yourself to e.g. [a-zA-Z0-9_-]+ in project names, and use the Display Name feature to make them look nice.!", priority = Priority.MAJOR, tags = { "convention" })
@BelongsToProfile(title = CheckRepository.SONAR_WAY_PROFILE_NAME, priority = Priority.MAJOR)
@SqaleSubCharacteristic(RulesDefinition.SubCharacteristics.ARCHITECTURE_RELIABILITY)
@SqaleConstantRemediation("5min")
public class JobNameSanityCheck extends AbstractConfigXmlCheck {

	@Override
	public void validate(ConfigXmlSource xmlSourceCode) {
		setConfigXmlSource(xmlSourceCode);

		String jobName = getConfigXMLSource().getJobName();
		if (!isJobNameValid(jobName)) {
			createViolation(
					1,
					"Jenkins uses project names for folders related to the project. Many poorly written tools cannot handle spaces, dollar signs, or similar characters in file paths. So it's easiest to limit yourself to e.g. [a-zA-Z0-9_-]+ in project names, and use the Display Name feature to make them look nice. You can define a pattern for allowed project names in Configure Jenkins to enforce this restriction on all your users.");
		}
	}
	
	protected boolean isJobNameValid(String jobName) {
		return jobName.matches("[a-zA-Z0-9_-]+");
	}
}
