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
import org.sonar.plugins.jenkins.config.ConfigXmlIssue;
import org.sonar.plugins.jenkins.config.ConfigXmlSource;
import org.sonar.plugins.jenkins.language.Jenkins;

import com.google.common.annotations.VisibleForTesting;

public class ConfigXmlSensor implements Sensor {

	private final Checks<Object> checks;
	private final FileSystem fileSystem;
	private final ResourcePerspectives resourcePerspectives;
	private final FilePredicate mainFilesPredicate;
	private static final Logger LOG = LoggerFactory.getLogger(ConfigXmlSensor.class);

	public ConfigXmlSensor(FileSystem fileSystem, ResourcePerspectives resourcePerspectives, CheckFactory checkFactory) {
		this.checks = checkFactory.create(CheckRepository.REPOSITORY_KEY)
				.addAnnotatedChecks(CheckRepository.getCheckClasses());
		this.fileSystem = fileSystem;
		this.resourcePerspectives = resourcePerspectives;
		this.mainFilesPredicate = fileSystem.predicates().and(fileSystem.predicates().hasType(InputFile.Type.MAIN),
				fileSystem.predicates().hasLanguage(Jenkins.KEY));
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public void analyse(Project project, SensorContext sensorContext) {
		for (InputFile inputFile : fileSystem.inputFiles(mainFilesPredicate)) {
			runChecks(inputFile);
		}
	}

	private void runChecks(InputFile inputFile) {
		try {
			ConfigXmlSource sourceCode = new ConfigXmlSource(inputFile);

			for (Object check : checks.all()) {
				LOG.info(((AbstractConfigXmlCheck) check).getRuleKey() + " - " + inputFile.absolutePath());
				((AbstractConfigXmlCheck) check).setRuleKey(checks.ruleKey(check));
				((AbstractConfigXmlCheck) check).validate(sourceCode);
			}
			saveIssue(sourceCode);
		} catch (Exception e) {
			throw new IllegalStateException("Could not analyze the file " + inputFile.absolutePath(), e);
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
	protected void saveIssue(ConfigXmlSource sourceCode) {
		for (ConfigXmlIssue xmlIssue : sourceCode.getConfigIssues()) {
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
