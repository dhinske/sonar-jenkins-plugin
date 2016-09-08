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
import org.sonar.api.resources.Project;
import org.sonar.plugins.jenkins.checks.AbstractConfigXmlCheck;
import org.sonar.plugins.jenkins.checks.CheckRepository;
import org.sonar.plugins.jenkins.config.ConfigSources;
import org.sonar.plugins.jenkins.config.JobConfig;
import org.sonar.plugins.jenkins.config.JobConfigIssue;
import org.sonar.plugins.jenkins.language.Jenkins;
import org.sonar.plugins.jenkins.metrics.ComplexityMetric;
import org.sonar.plugins.jenkins.metrics.JobTypeMetric;

import com.google.common.annotations.VisibleForTesting;

public class JenkinsSensor implements Sensor {

	private final Checks<Object> checks;
	private final FileSystem fileSystem;
	private final ResourcePerspectives resourcePerspectives;
	private final FilePredicate mainFilesPredicate;
	private static final Logger LOG = LoggerFactory.getLogger(JenkinsSensor.class);
	
	private ConfigSources configSources;

	public JenkinsSensor(FileSystem fileSystem, ResourcePerspectives resourcePerspectives, CheckFactory checkFactory) {
		this.checks = checkFactory.create(CheckRepository.REPOSITORY_KEY)
				.addAnnotatedChecks(CheckRepository.getCheckClasses());
		this.fileSystem = fileSystem;
		this.resourcePerspectives = resourcePerspectives;
		this.mainFilesPredicate = fileSystem.predicates().and(fileSystem.predicates().hasType(InputFile.Type.MAIN),
				fileSystem.predicates().hasLanguage(Jenkins.KEY));
		
		configSources = new ConfigSources();
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public void analyse(Project project, SensorContext sensorContext) {
		for (InputFile inputFile : fileSystem.inputFiles(mainFilesPredicate)) {
			// organize every config-file the Jenkins-instance has
			configSources.addSource(inputFile);
			LOG.debug("analyse " + inputFile.file().getAbsolutePath());
		}
		
		// run metrics
		JobTypeMetric.calculateMetric(configSources, sensorContext);
		ComplexityMetric.calculateMetric(configSources, sensorContext);
		
		// run checks
		LOG.debug("running " + checks.all().size()+ " checks for " + configSources.getJobs().size() + " jobs");
		for (JobConfig config : configSources.getJobs().values()) {
			runChecks(config);
		}
	}

	private void runChecks(JobConfig jobConfig) {
		try {
			for (Object check : checks.all()) {
				LOG.debug(((AbstractConfigXmlCheck) check).getRuleKey() + " - " + jobConfig.getName());
				((AbstractConfigXmlCheck) check).setRuleKey(checks.ruleKey(check));
				((AbstractConfigXmlCheck) check).validate(jobConfig);
			}
			saveIssue(jobConfig);
		} catch (Exception e) {
			throw new IllegalStateException("Could not analyze the file " + jobConfig.getName(), e);
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
	protected void saveIssue(JobConfig jobConfig) {
		for (JobConfigIssue xmlIssue : jobConfig.getConfigXml().getConfigIssues()) {
			Issuable issuable = resourcePerspectives.as(Issuable.class, jobConfig.getConfigXml().getInputFile());

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
