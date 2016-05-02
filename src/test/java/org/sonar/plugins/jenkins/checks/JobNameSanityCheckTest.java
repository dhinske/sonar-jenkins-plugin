package org.sonar.plugins.jenkins.checks;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class JobNameSanityCheckTest {

	@Test
	public void test() {
		JobNameSanityCheck check = new JobNameSanityCheck();

		assertEquals(true, check.isJobNameValid("server_martec_integration_deploy"));
		assertEquals(false, check.isJobNameValid("TEST.deploy"));
		assertEquals(false, check.isJobNameValid("feature_übelkeit"));
		assertEquals(false, check.isJobNameValid("project_feature(a)"));
	}
}
