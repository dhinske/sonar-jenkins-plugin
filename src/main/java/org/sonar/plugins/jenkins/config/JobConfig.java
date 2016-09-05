package org.sonar.plugins.jenkins.config;

import org.sonar.plugins.jenkins.config.types.ConfigXml;

public class JobConfig {
	private String name;
	private ConfigXml configXml;

	public JobConfig(String name) {
		this.name = name;
		configXml = null;
	}

	public String getName() {
		return name;
	}

	public ConfigXml getConfigXml() {
		return configXml;
	}

	public void setConfigXml(ConfigXml configXml) {
		this.configXml = configXml;
	}
}
