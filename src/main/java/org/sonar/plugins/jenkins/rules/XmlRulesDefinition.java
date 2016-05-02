package org.sonar.plugins.jenkins.rules;

import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.plugins.jenkins.checks.CheckRepository;
import org.sonar.plugins.jenkins.language.Jenkins;
import org.sonar.squidbridge.annotations.AnnotationBasedRulesDefinition;

public final class XmlRulesDefinition implements RulesDefinition {

	@Override
	public void define(Context context) {
		NewRepository repository = context.createRepository(CheckRepository.REPOSITORY_KEY, Jenkins.KEY)
				.setName(CheckRepository.REPOSITORY_NAME);

		new AnnotationBasedRulesDefinition(repository, Jenkins.KEY).addRuleClasses(false,
				CheckRepository.getCheckClasses());

		repository.done();
	}
}
