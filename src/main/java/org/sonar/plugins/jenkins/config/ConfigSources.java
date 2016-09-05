package org.sonar.plugins.jenkins.config;

import java.util.HashMap;
import java.util.Map;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.plugins.jenkins.config.types.ConfigXml;

/**
 * Manages all different types of sources for a Job-configuration of a single Jenkins
 * config.xml, pipeline, workflowLib
 */
public class ConfigSources {

	private Map<String, JobConfig> jobs;
	private WorkflowLibs workFlowLibs;
	
	public Map<String, JobConfig> getJobs() {
		return jobs;
	}

	public WorkflowLibs getWorkFlowLibs() {
		return workFlowLibs;
	}

	public ConfigSources() {
		jobs = new HashMap<>();
		workFlowLibs = new WorkflowLibs();
	}
	
	/**
	 * Identify source and add it to internal datastructure
	 */
	public void addSource(InputFile file) {
		String fileName = file.file().getName();
		
		// config.xml
		if (fileName.equals("config.xml")) {
			JobConfig config = jobs.get(file.file().getParentFile().getName());
			
			if (config == null) {
				config = new JobConfig(file.file().getParentFile().getName());
				config.setConfigXml(new ConfigXml(file));			
				jobs.put(file.file().getParentFile().getName(), config);
			} else {
				config.setConfigXml(new ConfigXml(file));							
			}
		}
		
		// Pipeline-Script, currently assuming that there is no file-ending
		if (!fileName.contains(".")) {
			
		}
		// TODO: workflow-libs
		
		// TODO: additional groovy-scripts
	}
	
}
