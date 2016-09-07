package org.sonar.plugins.jenkins.metrics;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.fs.internal.DefaultInputFile;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.Measure;
import org.sonar.api.measures.Metric;
import org.sonar.api.measures.Metrics;
import org.sonar.api.measures.SumChildValuesFormula;
import org.sonar.plugins.jenkins.config.ConfigSources;
import org.sonar.plugins.jenkins.config.JobConfig;
import org.sonar.plugins.jenkins.config.JobType;
import org.sonar.plugins.jenkins.config.types.ConfigXml;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ComplexityMetric implements Metrics{
	
	private static final Logger LOG = LoggerFactory.getLogger(ComplexityMetric.class);

	public static void calculateMetric(ConfigSources configSources, SensorContext sensorContext) {
		LOG.info("Calculating complexity-metrics for jobs");
		double overallComplexity = 0;
		
		for (JobConfig jobConfig : configSources.getJobs().values()) {
			JobType type = jobConfig.getConfigXml().getJobType();
			if (type == null) {
				LOG.info("Could not get JobType for " + jobConfig.getName());
			} else {
				switch (type) {
				case FREESTYLE:
					double complexity = 0;
					
					// each builder-plugin adds 1
					NodeList nodes = jobConfig.getConfigXml().getDocument().getElementsByTagName("builders");
					for(int i = 0 ; i < nodes.item(0).getChildNodes().getLength() ; i++) {
						if(nodes.item(0).getChildNodes().item(i).getNodeType() == 1) {
							complexity++;
						}
					}
					sensorContext.saveMeasure(jobConfig.getConfigXml().getInputFile(), COMPLEXITY_JOBS, complexity);
					overallComplexity += complexity;
					break;
				case PIPELINE:
//					sensorContext.saveMeasure(jobConfig.getConfigXml().getInputFile(), AMOUNT_PIPELINE, 1.0);
//					overallComplexity++;
					break;
				case MB_PIPELINE:
//					sensorContext.saveMeasure(jobConfig.getConfigXml().getInputFile(), AMOUNT_MB_PIPELINE, 1.0);
//					overallComplexity++;
					break;
				default:
					break;
				}	
			}
		}
		
		Measure<Integer> measure;
		measure = new Measure<Integer>(ComplexityMetric.COMPLEXITY_JOBS);
		measure.setValue(overallComplexity);
	    sensorContext.saveMeasure(measure);
	}
	
	static final Metric COMPLEXITY_JOBS =
            new Metric.Builder(
                "complexity_jobs",
                "Complexity of all jobs",
                Metric.ValueType.INT)
                .setDescription("Complexity all jobs")
                .setQualitative(false)
                .setDomain(CoreMetrics.DOMAIN_GENERAL)
                .setFormula(new SumChildValuesFormula(false))
                .create();

	
	@Override
	public List<Metric> getMetrics() {
		return Arrays.asList(COMPLEXITY_JOBS);
	}
}
