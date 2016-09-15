package org.sonar.plugins.jenkins.checks;

import java.util.Scanner;

import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.check.BelongsToProfile;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.plugins.jenkins.config.JobConfiguration;
import org.sonar.squidbridge.annotations.SqaleConstantRemediation;
import org.sonar.squidbridge.annotations.SqaleSubCharacteristic;

/**
 * Checks that the loc of a pipeline do not overcome a certain threshold. This
 * way the pipeline-script is forced to outsource functionality and always be
 * readable.
 * 
 * @author dhinske
 *
 */
@Rule(key = "PipelineLengthCheck", name = "Keep the pipeline-script short and readable.", priority = Priority.MAJOR, tags = {
		"convention" })
@BelongsToProfile(title = CheckRepository.SONAR_WAY_PROFILE_NAME, priority = Priority.MAJOR)
@SqaleSubCharacteristic(RulesDefinition.SubCharacteristics.READABILITY)
@SqaleConstantRemediation("15min")
public class PipelineLengthCheck extends AbstractConfigXmlCheck {

	public static final int THRESHOLD = 30;

	@Override
	public void validate(JobConfiguration jobConfig) {
		switch (jobConfig.getJobType()) {
		case PIPELINE:
			int linesOfCode = 0;
			// pipeline is defined in the job-config
			String embeddedPipelineScript = jobConfig.getConfigXml().getEmbeddedPipeline();
			if (embeddedPipelineScript != null) {
				try (Scanner scanner = new Scanner(embeddedPipelineScript)){
					while (scanner.hasNextLine()) {
						linesOfCode++;
						scanner.nextLine();
					}
				}
				if (linesOfCode > THRESHOLD) {
					jobConfig.getConfigXml().createViolation(getRuleKey(), 1,
							"Your pipeline-script is too long. Reduce it to " + THRESHOLD + " lines or less.");
				}
			} else { // pipeline is defined externally
				// TODO: implement
			}
			break;
		case MB_PIPELINE:
			// TODO: implement
			break;
		default:
			break;
		}
	}
}
