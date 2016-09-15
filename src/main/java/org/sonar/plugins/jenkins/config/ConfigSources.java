package org.sonar.plugins.jenkins.config;

import java.util.HashMap;
import java.util.Map;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.plugins.jenkins.config.types.ConfigXml;
import org.sonar.plugins.jenkins.config.types.Pipeline;

/**
 * Manages all different types of sources for a Job-configuration of a single
 * Jenkins config.xml, pipeline, workflowLib
 */
public class ConfigSources {

	private Map<String, JobConfiguration> jobs;
	private WorkflowLibs workFlowLibs;

	public Map<String, JobConfiguration> getJobs() {
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
		if ("config.xml".equals(fileName)) {
			JobConfiguration config = jobs.get(file.file().getParentFile().getName());

			if (config == null) {
				config = new JobConfiguration(file.file().getParentFile().getName());
				config.setConfigXml(new ConfigXml(file));
				if (config.getConfigXml().getJobType() != null) {
					jobs.put(file.file().getParentFile().getName(), config);
				}
			} else {
				config.setConfigXml(new ConfigXml(file));
			}
		}

		// Pipeline-Script, currently assuming that there is no file-ending and
		// that the pipeline is in the root of the project
		if (!fileName.contains(".")) {
			JobConfiguration config = jobs.get(file.file().getParentFile().getName());
			if (config == null) {
				config = new JobConfiguration(file.file().getParentFile().getName());
				config.setPipeline(new Pipeline(file));
			} else {
				config.setPipeline(new Pipeline(file));
			}
		}
		// TODO: workflow-libs

		// TODO: additional groovy-scripts
	}

	/**
	 * Currently for unit-testing purposes
	 * @param jobConfig
	 */
	public void addJob(JobConfiguration jobConfig) {
		jobs.put(jobConfig.getName(), jobConfig);
	}
}
