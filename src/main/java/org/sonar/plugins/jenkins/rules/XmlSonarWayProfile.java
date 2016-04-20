package org.sonar.plugins.jenkins.rules;

import org.sonar.api.profiles.AnnotationProfileParser;
import org.sonar.api.profiles.ProfileDefinition;
import org.sonar.api.profiles.RulesProfile;
import org.sonar.api.utils.ValidationMessages;
import org.sonar.plugins.jenkins.checks.CheckRepository;
import org.sonar.plugins.jenkins.language.Jenkins;

public final class XmlSonarWayProfile extends ProfileDefinition {

  private final AnnotationProfileParser annotationProfileParser;

  public XmlSonarWayProfile(AnnotationProfileParser annotationProfileParser) {
    this.annotationProfileParser = annotationProfileParser;
  }

  @Override
  public RulesProfile createProfile(ValidationMessages validation) {
    return annotationProfileParser.parse(
        CheckRepository.REPOSITORY_KEY,
        CheckRepository.SONAR_WAY_PROFILE_NAME,
        Jenkins.KEY,
        CheckRepository.getCheckClasses(),
        validation);
  }
}
