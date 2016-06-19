package org.sonar.plugins.jenkins.config;

import java.util.HashSet;
import java.util.Set;

import org.sonar.api.batch.fs.InputFile;
import org.sonar.plugins.jenkins.config.types.ConfigXml;
import org.sonar.plugins.jenkins.config.types.Pipeline;

/**
 * All different types of sources for a Job-configuration
 * config.xml, pipeline, workflowLib
 */
public class ConfigSources {

	private Set<ConfigXml> configXmls;
	private Set<Pipeline> pipelines;
	private Set<ConfigXml> workflowLib;
	
	public ConfigSources() {
		configXmls = new HashSet<>();
		pipelines = new HashSet<>();
		workflowLib = new HashSet<>();
	}
	
	public void addSource(InputFile file) {
		if (file.file().getName().equals("config.xml")) {
			configXmls.add(new ConfigXml(file));			
		}
	}
	
	public Set<ConfigXml> getConfigXmls() {
		return configXmls;
	}
	
	public Set<Pipeline> getPipelines() {
		return pipelines;
	}
	
	public Set<ConfigXml> getWorkflowLib() {
		return workflowLib;
	}
}
