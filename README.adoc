= JBoss Logging

JBoss Logging is a logging facade which can bind to different log managers allowing your applications to be log manager
agnostic.

== Usage

JBoss Logging is similar to other logging facades in the way you get a logger and log messages. One thing to note is
the format style log methods will only format the message if the log level is enabled. This helps with performance of
objects which may have complex `toString()` methods.

[source,java]
----
private static final Logger LOGGER = Logger.getLogger(Customer.class);

public Customer getCustomer(final int id) {
    LOGGER.debugf("Looking up customer %d", id);
    try {
        final Customer customer = findCustomer(id);
        LOGGER.tracef("Found customer: %s", customer);
        return customer;
    } catch (Exception e) {
        LOGGER.errorf(e, "Error looking up customer %d", id);
    }
    return null;
}
----

=== Supported Log Managers

The following are the supported log managers and listed in the order the attempt to discover the provider is done.

1. JBoss Log Manager
2. https://logging.apache.org/log4j/2.x/[Log4j 2]
3. https://logback.qos.ch/[SLF4J and Logback]
4. https://logging.apache.org/log4j/1.2/[log4j] (note this log manager is EOL'd)
5. Java Util Logging

You can define the specific log manager you want to use by specifying the `org.jboss.logging.provider` system property.
The following is the mapping of the property value to the log manager.

|===
|Property Value |Log Manager

|jboss
|JBoss Log Manager

|jdk
|Java Util Logging

|log4j2
|Log4j 2

|log4j
|log4j

|slf4j
|SLF4J and Logback
|===

=== Custom Provider

You can also implement your own `org.jboss.logging.LoggerProvider` which would be loaded from a `ServiceLoader`. Simply
implement the API and add a `META-INF/services/org.jboss.logging.LoggerProvider` file with the fully qualified class
name of your implementation to your library. If the system property is not defined, your implementation should be
discovered.

=== Maven Dependency

[source,xml]
----
<dependency>
    <groupId>org.jboss.logging</groupId>
    <artifactId>jboss-logging</artifactId>
    <version>${version.org.jboss.logging}</version>
</dependency>
----