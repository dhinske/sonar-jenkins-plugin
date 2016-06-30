package org.sonar.plugins.jenkins.config.types;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.plugins.jenkins.config.JobConfigIssue;
import org.sonar.plugins.jenkins.config.JobType;
import org.w3c.dom.Document;

import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * config.xml of a single job
 */

public class ConfigXml extends JobConfigSource{

	private static final Logger LOG = LoggerFactory.getLogger(ConfigXml.class);

	Document xmlDocument;

	public ConfigXml(InputFile file) {
		super(file);
		parseSource();
	}

	public Document getDocument() {
		return xmlDocument;
	}

	/**
	 * Parses the source and returns true if succeeded false if the file is
	 * corrupted.
	 */
	public boolean parseSource() {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = null;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			xmlDocument = dBuilder.parse(inputFile.file());
		} catch (Exception e) {
			LOG.error(e.getMessage());
			return false;
		}
		return true;
	}

	public InputFile getInputFile() {
		return inputFile;
	}

	public String getJobName() {
		return inputFile.file().getParentFile().getName();
	}

	public List<JobConfigIssue> getConfigIssues() {
		return configXmlIssues;
	}

	@Override
	public String toString() {
		return inputFile.absolutePath();
	}

	public JobType getJobType() {
		if (xmlDocument.getFirstChild().getNodeName().equals("project")) {
			return JobType.FREESTYLE;
		}
		if (xmlDocument.getFirstChild().getNodeName().equals("flow-definition")) {
			return JobType.PIPELINE;
		}
		if (xmlDocument.getFirstChild().getNodeName().equals("org.jenkinsci.plugins.workflow.multibranch.WorkflowMultiBranchProject")) {
			return JobType.MB_PIPELINE;
		}
		return null;
	}
}
