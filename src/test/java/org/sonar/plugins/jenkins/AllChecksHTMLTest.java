package org.sonar.plugins.jenkins;

import java.util.List;

import org.junit.Test;
import org.sonar.plugins.jenkins.checks.AbstractConfigXmlCheck;
import org.sonar.plugins.jenkins.checks.CheckRepository;

public class AllChecksHTMLTest {

	@Test
	public void test() {
		List<AbstractConfigXmlCheck> checks = CheckRepository.getChecks();
		for (AbstractConfigXmlCheck check : checks) {
			System.out.println(check.getClass().getPackage());
		}
	}

}
