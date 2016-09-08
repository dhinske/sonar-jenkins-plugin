package org.sonar.plugins.jenkins.checks;

import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.check.BelongsToProfile;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.plugins.jenkins.config.JobConfiguration;
import org.sonar.squidbridge.annotations.SqaleConstantRemediation;
import org.sonar.squidbridge.annotations.SqaleSubCharacteristic;

/**
 * Checks the name of the job for special characters. Some plugins have trouble with special characters in the job-name.
 * @author dhinske
 *
 */
@Rule(key = "JobNameSanityCheck", name = "Sanitize your job-names", priority = Priority.MAJOR, tags = { "convention" })
@BelongsToProfile(title = CheckRepository.SONAR_WAY_PROFILE_NAME, priority = Priority.MAJOR)
@SqaleSubCharacteristic(RulesDefinition.SubCharacteristics.ARCHITECTURE_RELIABILITY)
@SqaleConstantRemediation("5min")
public class JobNameSanityCheck extends AbstractConfigXmlCheck {

	@Override
	public void validate(JobConfiguration jobConfig) {

		if (!isJobNameValid(jobConfig.getName())) {
			jobConfig.getConfigXml().createViolation(getRuleKey(),
					1,
					"Jenkins uses project names for folders related to the project. Many poorly written tools cannot handle spaces, dollar signs, or similar characters in file paths. So it's easiest to limit yourself to e.g. [a-zA-Z0-9_-]+ in project names, and use the Display Name feature to make them look nice. You can define a pattern for allowed project names in Configure Jenkins to enforce this restriction on all your users.");
		}
	}
	
	protected boolean isJobNameValid(String jobName) {
		return jobName.matches("[a-zA-Z0-9_-]+");
	}
}
