package org.sonar.plugins.jenkins.config;

import org.sonar.plugins.jenkins.config.types.ConfigXml;
import org.sonar.plugins.jenkins.config.types.Pipeline;

/**
 * Represents all known configuration for a single job.
 */
public class JobConfiguration {
	private String name;
	private ConfigXml configXml;
	private Pipeline pipeline;

	public JobConfiguration(String name) {
		this.name = name;
		configXml = null;
		pipeline = null;
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
	
	public Pipeline getPipeline() {
		return pipeline;
	}
	
	public void setPipeline(Pipeline pipeline) {
		this.pipeline = pipeline;
	}

	/**
	 * Checks the config.xml to get the JobType.
	 * Returns null if unknown.
	 */
	public JobType getJobType() {
		if (configXml != null) {
			return configXml.getJobType();			
		}
		return null;
	}
}
