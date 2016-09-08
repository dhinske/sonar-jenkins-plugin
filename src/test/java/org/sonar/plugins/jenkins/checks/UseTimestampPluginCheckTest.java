package org.sonar.plugins.jenkins.checks;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Test;
import org.sonar.api.batch.fs.internal.DefaultInputFile;
import org.sonar.plugins.jenkins.config.JobConfiguration;
import org.sonar.plugins.jenkins.config.types.ConfigXml;

public class UseTimestampPluginCheckTest {

	@Test
	public void test() {
		DefaultInputFile inputFile = new DefaultInputFile("");
		inputFile.setFile(new File("src/test/resources/timestamp_config.xml"));
		JobConfiguration config = new JobConfiguration("Timestamp");
		config.setConfigXml(new ConfigXml(inputFile));

		UseTimestampPluginCheck check = new UseTimestampPluginCheck();
		check.validate(config);
		assertEquals(0, config.getConfigXml().getConfigIssues().size());

		inputFile = new DefaultInputFile("");
		inputFile.setFile(new File("src/test/resources/no_timestamp_config.xml"));
		config = new JobConfiguration("NoTimestamp");
		config.setConfigXml(new ConfigXml(inputFile));

		check.validate(config);
		assertEquals(1, config.getConfigXml().getConfigIssues().size());
	}
}
