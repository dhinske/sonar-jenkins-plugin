#Sonar Jenkins Plugin#
This sonar-plugin analysis all job-configurations of your Jenkins-instance. It offers
- a set of rules which you can use to see if jobs inside a Jenkins contain bad practices
- metrics to measure the nature and complexity of your Jenkins-instance

#How to run#
- Build/Download the latest jar of this plugin
- Put the jar-file in your Sonar-instance under extensions/plugins
- Restart the Sonarqube-Server
- create pom.xml in the jenkins-installation-folder (see src/main/resources/example_pom.xml)
- adapt the pom.xml with your system
- run mvn sonar:sonar

#How to add custom rules#
coming soon