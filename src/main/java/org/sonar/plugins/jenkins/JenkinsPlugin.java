package org.sonar.plugins.jenkins;

import com.google.common.collect.ImmutableList;

import org.sonar.api.SonarPlugin;
import org.sonar.plugins.jenkins.language.Jenkins;
import org.sonar.plugins.jenkins.metrics.JobTypeMetric;
import org.sonar.plugins.jenkins.rules.ConfigXmlRulesDefinition;
import org.sonar.plugins.jenkins.rules.ConfigXmlSonarWayProfile;

import java.util.List;

public final class JenkinsPlugin extends SonarPlugin {

	public static final String FILE_SUFFIXES_KEY = "sonar.jenkins.file.suffixes";

	@Override
	public List getExtensions() {
		return ImmutableList.of(Jenkins.class, ConfigXmlRulesDefinition.class, ConfigXmlSonarWayProfile.class,
				ConfigXmlSensor.class, JobTypeMetric.class);
	}
}
