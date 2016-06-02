package org.sonar.plugins.jenkins.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.fs.InputFile;
import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class JobConfigSource {

	private static final Logger LOG = LoggerFactory.getLogger(JobConfigSource.class);
	private final List<JobConfigIssue> configXmlIssues = new ArrayList<>();

	private InputFile xmlFile;

	Document xmlDocument;
	//TODO: get if possible
	String jenkinsFile;

	public JobConfigSource(InputFile xmlFile) {
		this.xmlFile = xmlFile;
		parseSource();
	}

	public void addViolation(JobConfigIssue issue) {
		this.configXmlIssues.add(issue);
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
			xmlDocument = dBuilder.parse(xmlFile.file());
		} catch (Exception e) {
			LOG.error(e.getMessage());
			return false;
		}
		return true;
	}

	public InputFile getInputFile() {
		return xmlFile;
	}

	public String getJobName() {
		return xmlFile.file().getParentFile().getName();
	}

	public List<JobConfigIssue> getConfigIssues() {
		return configXmlIssues;
	}

	@Override
	public String toString() {
		return xmlFile.absolutePath();
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
