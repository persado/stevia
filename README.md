stevia <img src="https://raw.github.com/persado/stevia/master/doc/stevia-logo.png" align="right" width="80">
======

Build Status of 0.9.6-SNAPSHOT [![Status](https://travis-ci.org/persado/stevia.png?branch=master)](https://travis-ci.org/persado/stevia) 

## Stevia Quick Start (10 minutes) guide

Our 10-minutes guide for using Stevia is in our [wiki](https://github.com/persado/stevia/wiki/Stevia-10-minute-Quick-Start). Read on and start coding!

## Features

The following features are supported in the current Stevia build (new features have a version next to them):
* Works with latest Selenium libraries (2.41+) and Spring 3.2.x (4.0 coming soon)
* Works with stable TestNG tested for parallel running
* Supports both Webdriver and Selenium RC, standalone or Grid via easy configuration
* Supports TestNG with parallel test execution (each thread has its own browser/session)
* Versatile extension mechanism allows users of Stevia to extend it by:
    * [Controllers via Factory Pattern](https://github.com/persado/stevia/wiki/Extending-web-controller-support) (we load `META-INF/spring/stevia-extensions-drivers-*.xml` from classpath)
    * Navigation Beans, PageObjects, Spring beans (we load `META-INF/spring/test-beans-*.xml` from classpath)
    * Connectors for Rally, JIRA, Testlink (we load `META-INF/spring/stevia-extensions-connectors-*.xml` from classpath)
* Full logging support using ReportNG, with 
    * screenshots of browser for tests that failed
    * highlighting of locators, (accessed = yellow, success = green, failure = red)
    * actions reporting on test report log and HTML report
* [Extended "By" mechanism to support SizzleCSS](http://seleniumtestingworld.blogspot.gr/2013/01/adding-sizzle-css-selector-library-and.html) on Webdriver
* Detailed "Verify" class with lots of assertions pre-coded
* Supports for SSH/SFTP via utility classes
* Supports for HTTP GET,POST with Jetty high-performance, multi-threaded helper and cookies support
* Supports thread-level common user configuration and state across Tests (within Stevia thread context)
* Supports Annotations (Java 5+)
    * RunsWithController - allows a different controller (different browser or session) to run a @Test method or class
    * [Preconditions](http://seleniumtestingworld.blogspot.gr/2014/04/concurrency-testing-made-easy.html) - allows methods to be called (optionally with different controller) before @Test method
    * Postconditions - similar to @Precondition but after the @Test method.
* lots of other minor features

## Stevia Help and Javadoc

You can browse our javadoc via this [link](http://persado.github.io/stevia/).
Our [wiki](https://github.com/persado/stevia/wiki) contains topics of interest, let us know (via an issue) if you need clarifications. We're here to help!

### Latest in maven repositories
#### Release
```
<dependency>
  <groupId>com.persado.oss.quality.stevia</groupId>
  <artifactId>stevia-core</artifactId>
  <version>0.9.5</version>
</dependency>
```
#### Cutting edge
```
<dependency>
  <groupId>com.persado.oss.quality.stevia</groupId>
  <artifactId>stevia-core</artifactId>
  <version>0.9.6-SNAPSHOT</version>
</dependency>
```
To work with the cutting-edge version, don't forget to add the following to your pom.xml:
```
<repositories>
  <repository>
    <id>sonatype-nexus-snapshots</id>
    <name>OSS Sonatype Snapshot Repository</name>
    <url>https://oss.sonatype.org/content/repositories/snapshots</url>
  </repository>
</repositories>	
```

* * *

### Who is Persado <img alt="Persado" width="75" align="right" src="http://www.persado.com/templates/youandigraphics/images/logo.png">
Persado programmatically uncovers the language and emotions that make people buy. Its unique technology is powered by Natural Language Processing and advanced statistical algorithms. Working with leading global brands such as American Express, Esurance, McAfee, SurveyMonkey, Verizon Wireless and leading global Mobile Operators, Persado systematically delivers better marketing messages across digital channels. 
