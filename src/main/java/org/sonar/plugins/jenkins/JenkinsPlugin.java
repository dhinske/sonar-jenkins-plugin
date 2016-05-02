package org.sonar.plugins.jenkins;

import com.google.common.collect.ImmutableList;
import org.sonar.api.SonarPlugin;
import org.sonar.plugins.jenkins.language.Jenkins;
import org.sonar.plugins.jenkins.rules.XmlRulesDefinition;
import org.sonar.plugins.jenkins.rules.XmlSonarWayProfile;

import java.util.List;

public final class JenkinsPlugin extends SonarPlugin {

	public static final String FILE_SUFFIXES_KEY = "sonar.jenkins.file.suffixes";

	@Override
	public List getExtensions() {
		return ImmutableList.of(Jenkins.class, XmlRulesDefinition.class, XmlSonarWayProfile.class,
				ConfigXMLSensor.class);
	}
}
