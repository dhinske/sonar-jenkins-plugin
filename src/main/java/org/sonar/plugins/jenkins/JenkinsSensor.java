package org.sonar.plugins.jenkins;

import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.fs.FilePredicate;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.rule.CheckFactory;
import org.sonar.api.batch.rule.Checks;
import org.sonar.api.component.Perspective;
import org.sonar.api.component.ResourcePerspectives;
import org.sonar.api.issue.Issuable;
import org.sonar.api.measures.Measure;
import org.sonar.api.resources.Project;
import org.sonar.plugins.jenkins.checks.AbstractConfigXmlCheck;
import org.sonar.plugins.jenkins.checks.CheckRepository;
import org.sonar.plugins.jenkins.config.ConfigSources;
import org.sonar.plugins.jenkins.config.JobConfig;
import org.sonar.plugins.jenkins.config.JobConfigIssue;
import org.sonar.plugins.jenkins.config.JobType;
import org.sonar.plugins.jenkins.config.types.JobConfigSource;
import org.sonar.plugins.jenkins.config.types.ConfigXml;
import org.sonar.plugins.jenkins.language.Jenkins;
import org.sonar.plugins.jenkins.metrics.JobTypeMetric;

import com.google.common.annotations.VisibleForTesting;

public class JenkinsSensor implements Sensor {

	private final Checks<Object> checks;
	private final FileSystem fileSystem;
	private final ResourcePerspectives resourcePerspectives;
	private final FilePredicate mainFilesPredicate;
	private static final Logger LOG = LoggerFactory.getLogger(JenkinsSensor.class);
	
	private ConfigSources configSources;
	private double freestyleJobs;
	private double pipelineJobs;
	private double mbPipelineJobs;

	public JenkinsSensor(FileSystem fileSystem, ResourcePerspectives resourcePerspectives, CheckFactory checkFactory) {
		this.checks = checkFactory.create(CheckRepository.REPOSITORY_KEY)
				.addAnnotatedChecks(CheckRepository.getCheckClasses());
		this.fileSystem = fileSystem;
		this.resourcePerspectives = resourcePerspectives;
		this.mainFilesPredicate = fileSystem.predicates().and(fileSystem.predicates().hasType(InputFile.Type.MAIN),
				fileSystem.predicates().hasLanguage(Jenkins.KEY));
		
		configSources = new ConfigSources();
		freestyleJobs = 0;
		pipelineJobs = 0;
		mbPipelineJobs = 0;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public void analyse(Project project, SensorContext sensorContext) {
		for (InputFile inputFile : fileSystem.inputFiles(mainFilesPredicate)) {
			configSources.addSource(inputFile);
			System.out.println("analyse " + inputFile.file().getAbsolutePath());
//			JobConfigSources source = new JobConfigSources(inputFile);
//			JobType type = source.getJobType();
//			System.out.println("GOT TYPE: " + type.name());
//			switch (type) {
//			case FREESTYLE:
//				freestyleJobs++;
//				break;
//			case PIPELINE:
//				pipelineJobs++;
//				break;
//			case MB_PIPELINE:
//				mbPipelineJobs++;
//				break;
//			default:
//				break;
//			}
			
			//runChecks(source);
		}
		for (JobConfig config : configSources.getJobs().values()) {
			runChecks(config.getConfigXml());
		}
//		Measure measure;
//		measure = new Measure(JobTypeMetric.AMOUNT_FREESTYLE);
//		measure.setValue(freestyleJobs);
//	    sensorContext.saveMeasure(measure);
//		measure = new Measure(JobTypeMetric.AMOUNT_PIPELINE);
//		measure.setValue(pipelineJobs);
//	    sensorContext.saveMeasure(measure);
//		measure = new Measure(JobTypeMetric.AMOUNT_MB_PIPELINE);
//		measure.setValue(mbPipelineJobs);
//	    sensorContext.saveMeasure(measure);
	}

	private void runChecks(ConfigXml source) {
		try {
			for (Object check : checks.all()) {
				LOG.info(((AbstractConfigXmlCheck) check).getRuleKey() + " - " + source.getInputFile().absolutePath());
				((AbstractConfigXmlCheck) check).setRuleKey(checks.ruleKey(check));
				((AbstractConfigXmlCheck) check).validate(source);
			}
			saveIssue(source);
		} catch (Exception e) {
			throw new IllegalStateException("Could not analyze the file " + source.getInputFile().absolutePath(), e);
		}
	}

	@Nullable
	<P extends Perspective<?>> P perspective(Class<P> clazz, InputFile file) {
		P result = resourcePerspectives.as(clazz, file);
		if (result == null) {
			LOG.warn("Could not get " + clazz.getCanonicalName() + " for " + file);
		}
		return result;
	}

	@VisibleForTesting
	protected void saveIssue(ConfigXml sourceCode) {
		for (JobConfigIssue xmlIssue : sourceCode.getConfigIssues()) {
			Issuable issuable = resourcePerspectives.as(Issuable.class, sourceCode.getInputFile());

			if (issuable != null) {
				issuable.addIssue(issuable.newIssueBuilder().ruleKey(xmlIssue.getRuleKey()).line(xmlIssue.getLine())
						.message(xmlIssue.getMessage()).build());
			}
		}
	}

	@Override
	public boolean shouldExecuteOnProject(Project project) {
		return fileSystem.hasFiles(mainFilesPredicate);
	}

	@Override
	public String toString() {
		return getClass().getSimpleName();
	}

}
