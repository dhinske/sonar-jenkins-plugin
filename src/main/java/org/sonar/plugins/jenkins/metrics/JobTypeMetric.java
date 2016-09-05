package org.sonar.plugins.jenkins.metrics;

import java.util.Arrays;
import java.util.List;

import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.Metric;
import org.sonar.api.measures.Metrics;
import org.sonar.api.measures.SumChildValuesFormula;

public class JobTypeMetric implements Metrics{

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
