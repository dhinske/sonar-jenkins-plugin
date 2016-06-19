package org.sonar.plugins.jenkins.language;

import org.sonar.api.resources.AbstractLanguage;

public class Jenkins extends AbstractLanguage {

  public static final String KEY = "jenkins";

  private static final String JENKINS_LANGUAGE_NAME = "jenkins";

  public Jenkins() {
    super(KEY, JENKINS_LANGUAGE_NAME);
  }

  @Override
  public String[] getFileSuffixes() {
    return new String[] {};
  }
}
