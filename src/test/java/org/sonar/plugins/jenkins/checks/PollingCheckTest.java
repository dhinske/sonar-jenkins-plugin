package org.sonar.plugins.jenkins.checks;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Test;
import org.sonar.api.batch.fs.internal.DefaultInputFile;
import org.sonar.plugins.jenkins.config.JobConfiguration;
import org.sonar.plugins.jenkins.config.types.ConfigXml;

public class PollingCheckTest {

	@Test
	public void test() {
		DefaultInputFile inputFile = new DefaultInputFile("");
		inputFile.setFile(new File("src/test/resources/checks/polling_config.xml"));
		JobConfiguration config = new JobConfiguration("Polling");
		config.setConfigXml(new ConfigXml(inputFile));

		PollingCheck check = new PollingCheck();
		check.validate(config);
		assertEquals(1, config.getConfigXml().getConfigIssues().size());

		inputFile = new DefaultInputFile("");
		inputFile.setFile(new File("src/test/resources/checks/noPolling_config.xml"));
		config = new JobConfiguration("NoPolling");
		config.setConfigXml(new ConfigXml(inputFile));

		check.validate(config);
		assertEquals(0, config.getConfigXml().getConfigIssues().size());
	}
}
