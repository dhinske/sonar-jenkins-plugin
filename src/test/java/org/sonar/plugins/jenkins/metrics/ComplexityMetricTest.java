package org.sonar.plugins.jenkins.metrics;

import java.io.File;

import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.fs.internal.DefaultInputFile;
import org.sonar.api.measures.Measure;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doAnswer;
import org.sonar.plugins.jenkins.config.ConfigSources;
import org.sonar.plugins.jenkins.config.JobConfiguration;
import org.sonar.plugins.jenkins.config.types.ConfigXml;

import static org.junit.Assert.assertEquals;

public class ComplexityMetricTest {

	@Test
	public void testSimpleFreestyle() {
		ConfigSources configSources = new ConfigSources();
		SensorContext sensorContext = Mockito.spy(SensorContext.class);
		doAnswer(new Answer<Void>() {
			public Void answer(InvocationOnMock invocation) {
				Measure callback = (Measure) invocation.getArguments()[0];
				assertEquals("", 3.0, callback.getValue(), 0.0);
				return null;
			}
		}).when(sensorContext).saveMeasure(any(Measure.class));

		DefaultInputFile inputFile = new DefaultInputFile("");
		inputFile.setFile(new File("src/test/resources/metrics/complexity_config.xml"));
		ConfigXml configXml = new ConfigXml(inputFile);

		JobConfiguration jobConfig = new JobConfiguration("complexity-test");
		jobConfig.setConfigXml(configXml);

		configSources.addJob(jobConfig);

		ComplexityMetric.calculateMetric(configSources, sensorContext);
	}
}
