package org.sonar.plugins.jenkins.config;

import org.sonar.api.batch.fs.InputFile;
import org.w3c.dom.Document;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class ConfigXmlSource {

	private final List<ConfigXmlIssue> configXmlIssues = new ArrayList<>();

	private InputFile xmlFile;

	Document xmlFileDocument;

	public ConfigXmlSource(InputFile xmlFile) {
		this.xmlFile = xmlFile;
		parseSource();
	}

	public void addViolation(ConfigXmlIssue xmlIssue) {
		this.configXmlIssues.add(xmlIssue);
	}

	public Document getDocument() {
		return xmlFileDocument;
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
			xmlFileDocument = dBuilder.parse(xmlFile.file());
		} catch (Exception e) {
			e.printStackTrace();
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

	public List<ConfigXmlIssue> getConfigIssues() {
		return configXmlIssues;
	}

	@Override
	public String toString() {
		return xmlFile.absolutePath();
	}
}
