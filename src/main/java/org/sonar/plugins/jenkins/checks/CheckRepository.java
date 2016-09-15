package org.sonar.plugins.jenkins.checks;

import java.util.List;

import com.google.common.collect.ImmutableList;

/**
 * Keeps the default quality profile and defines the default checks.
 * 
 * @author dhinske
 *
 */
public class CheckRepository {

	public static final String REPOSITORY_KEY = "jenkins";
	public static final String REPOSITORY_NAME = "SonarQube Jenkins";
	public static final String SONAR_WAY_PROFILE_NAME = "Sonar way Jenkins";

	private CheckRepository() {
	}

	public static List<AbstractConfigXmlCheck> getChecks() {
		return ImmutableList.of(new PollingCheck(), new UseLogRotatorCheck(), new MaxArchiveLatestBuildCheck(),
				new TemplateShouldBeDeactivatedCheck(), new AssignJobToNodeCheck(), new UseTimestampPluginCheck(),
				new JobNameSanityCheck(), new PipelineLengthCheck());
	}

	@SuppressWarnings("rawtypes")
	public static List<Class> getCheckClasses() {
		ImmutableList.Builder<Class> builder = ImmutableList.builder();

		for (AbstractConfigXmlCheck check : getChecks()) {
			builder.add(check.getClass());
		}
		return builder.build();
	}
}
