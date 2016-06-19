package org.sonar.plugins.jenkins.checks;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Test;
import org.sonar.api.batch.fs.internal.DefaultInputFile;
import org.sonar.plugins.jenkins.config.JobConfigSources;

public class PollingCheckTest {

	@Test
	public void test() {
		DefaultInputFile inputFile = new DefaultInputFile("");
		inputFile.setFile(new File("src/test/resources/polling_config.xml"));
		JobConfigSources source = new JobConfigSources(inputFile);

		PollingCheck check = new PollingCheck();
		check.validate(source);
		assertEquals(1, source.getConfigIssues().size());

		inputFile = new DefaultInputFile("");
		inputFile.setFile(new File("src/test/resources/noPolling_config.xml"));
		source = new JobConfigSources(inputFile);

		check.validate(source);
		assertEquals(0, source.getConfigIssues().size());
	}
}
