package org.sonar.plugins.jenkins.config.types;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.plugins.jenkins.config.JobType;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * config.xml of a single job
 */

public class ConfigXml extends JobConfigSource {

	private static final Logger LOG = LoggerFactory.getLogger(ConfigXml.class);

	Document xmlDocument;

	/**
	 * Represents a config.xml of a job.
	 * 
	 * @param file
	 *            Inputfile of the config.xml. Will be transformed into a
	 *            w3c-Document, which is accessible by getDocument()
	 */
	public ConfigXml(InputFile file) {
		super(file);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = null;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			xmlDocument = dBuilder.parse(inputFile.file());
		} catch (Exception e) {
			LOG.error("Could not convert " + inputFile.absolutePath() + "into a w3c-document: " + e.getMessage());
			xmlDocument = null;
		}
	}

	public Document getDocument() {
		return xmlDocument;
	}

	@Override
	public InputFile getInputFile() {
		return inputFile;
	}

	public String getJobName() {
		return inputFile.file().getParentFile().getName();
	}

	@Override
	public String toString() {
		return inputFile.absolutePath();
	}

	public JobType getJobType() {
		switch (xmlDocument.getFirstChild().getNodeName()) {
		case "project":
			return JobType.FREESTYLE;
		case "flow-definition":
			return JobType.PIPELINE;
		case "org.jenkinsci.plugins.workflow.multibranch.WorkflowMultiBranchProject":
			return JobType.MB_PIPELINE;
		default:
			return null;
		}
	}
}
