# jboss-logging
This version does not create a subclass of <a href="http://docs.oracle.com/javase/7/docs/api/java/util/logging/Level.html">java.util.logging.Level</a> 
instances. It may create a webapp memory leak on hotdeployments in servlet engines. 
This will happen if jboss-logging.jar is provided by mywebapp/WEB-INF/lib folder.

https://issues.jboss.org/browse/JBLOGGING-66<br/>
http://bugs.java.com/view_bug.do?bug_id=6543126<br/>
http://java.jiderhamn.se/2012/01/01/classloader-leaks-ii-find-and-work-around-unwanted-references/#beanvalidation<br/>

I have witnessed this in OpenJPA+javaxvalidation+Tomcat environment where mywebapp.war provides jar libraries.
Modified two classes to fix memory leak.<br/>
org.jboss.logging.JDKLevel<br/>
org.jboss.logging.JDKLogger<br/>
