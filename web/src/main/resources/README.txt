To create/update an eclipse project.Including deployment to the tomcat:

mvn eclipse:clean eclipse:eclipse
Or web application:
mvn eclipse:clean eclipse:eclipse -Dwtpversion=2.0

To make a change in the core influence the web:

mvn install

Otherwise the web will use the previously installed core.

To make the test cases work:
-javaagent:${user_home}/.m2/repository/org/aspectj/aspectjweaver/1.6.6/aspectjweaver-1.6.6.jar
-DCONFIG_PATH=classpath:environment/test.properties
${user_home} needs to be set up as a variable.

