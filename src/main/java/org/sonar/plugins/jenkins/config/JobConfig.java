package org.sonar.plugins.jenkins.config;

import org.sonar.plugins.jenkins.config.types.ConfigXml;

public class JobConfig {
	private String name;
	private ConfigXml configXml;
	
	public JobConfig() {
		name = null;
		configXml = null;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ConfigXml getConfigXml() {
		return configXml;
	}

	public void setConfigXml(ConfigXml configXml) {
		this.configXml = configXml;
	}
	
}
