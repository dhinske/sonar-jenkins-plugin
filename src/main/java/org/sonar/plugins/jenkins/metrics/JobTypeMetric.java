package org.sonar.plugins.jenkins.metrics;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.Measure;
import org.sonar.api.measures.Metric;
import org.sonar.api.measures.Metrics;
import org.sonar.api.measures.SumChildValuesFormula;
import org.sonar.plugins.jenkins.config.ConfigSources;
import org.sonar.plugins.jenkins.config.JobConfig;
import org.sonar.plugins.jenkins.config.JobType;

public class JobTypeMetric implements Metrics{
	
	private static final Logger LOG = LoggerFactory.getLogger(JobTypeMetric.class);

	public static void calculateMetric(ConfigSources configSources, SensorContext sensorContext) {
		LOG.info("Calculating metrics for job-types");
		double freestyleJobs = 0;
		double pipelineJobs = 0;
		double mbPipelineJobs = 0;
		
		for (JobConfig jobConfig : configSources.getJobs().values()) {
			JobType type = jobConfig.getConfigXml().getJobType();
			if (type == null) {
				LOG.info("Could not get JobType for " + jobConfig.getName());
			} else {
				switch (type) {
				case FREESTYLE:
					freestyleJobs++;
					break;
				case PIPELINE:
					pipelineJobs++;
					break;
				case MB_PIPELINE:
					mbPipelineJobs++;
					break;
				default:
					break;
				}	
			}
		}
		
		Measure<Integer> measure;
		measure = new Measure<Integer>(JobTypeMetric.AMOUNT_FREESTYLE);
		measure.setValue(freestyleJobs);
	    sensorContext.saveMeasure(measure);
		measure = new Measure<Integer>(JobTypeMetric.AMOUNT_PIPELINE);
		measure.setValue(pipelineJobs);
	    sensorContext.saveMeasure(measure);
		measure = new Measure<Integer>(JobTypeMetric.AMOUNT_MB_PIPELINE);
		measure.setValue(mbPipelineJobs);
	    sensorContext.saveMeasure(measure);
	}
	
    public static final Metric AMOUNT_FREESTYLE =
            new Metric.Builder(
                "amount_freestyle",
                "Amount of Freestyle-Jobs",
                Metric.ValueType.INT)
                .setDescription("Number of Freestyle-Jobs found")
                .setQualitative(false)
                .setDomain(CoreMetrics.DOMAIN_GENERAL)
                .setFormula(new SumChildValuesFormula(false))
                .create();
    
    public static final Metric AMOUNT_PIPELINE =
            new Metric.Builder(
                "amount_pipeline",
                "Amount of Pipeline-Jobs",
                Metric.ValueType.INT)
                .setDescription("Number of Pipeline-Jobs found")
                .setQualitative(false)
                .setDomain(CoreMetrics.DOMAIN_GENERAL)
                .setFormula(new SumChildValuesFormula(false))
                .create();
    
    public static final Metric AMOUNT_MB_PIPELINE =
            new Metric.Builder(
                "amount_mb_pipeline",
                "Amount of MB-Pipeline-Jobs",
                Metric.ValueType.INT)
                .setDescription("Number of MB-Pipeline-Jobs found")
                .setQualitative(false)
                .setDomain(CoreMetrics.DOMAIN_GENERAL)
                .setFormula(new SumChildValuesFormula(false))
                .create();
	
	@Override
	public List<Metric> getMetrics() {
		return Arrays.asList(AMOUNT_FREESTYLE, AMOUNT_PIPELINE, AMOUNT_MB_PIPELINE);
	}

}
