stevia <img src="https://raw.github.com/persado/stevia/master/doc/stevia-logo.png" align="right" width="80">
======

Build Status of latest SNAPSHOT [![Status](https://travis-ci.org/persado/stevia.png?branch=master)](https://travis-ci.org/persado/stevia) 

# Moving on....

Thank you all for this wonderful trip to the world of Testing Automation with Stevia. We are now looking forward to our next big release, which will be called (we cannot yet tell this!!!). However, we've uploaded a teaser video for you!

[![Check it out...](https://i.imgur.com/Pg3xsrm.png)](https://www.youtube-nocookie.com/embed/HeT7uZXSblM?rel=0 "Testing Automation New Gen")


## Stevia Quick Start (10 minutes) guide

Our 10-minutes guide for using Stevia is in our [wiki](https://github.com/persado/stevia/wiki/Stevia-10-minute-Quick-Start). Read on and start coding!

## Stevia Help and Javadoc

You can browse our javadoc via this [link](http://persado.github.io/stevia/).
Our [wiki](https://github.com/persado/stevia/wiki) contains topics of interest, let us know (via an issue) if you need clarifications. We're here to help!

## Features

The following features are supported in the current Stevia build (new features have a version next to them):
* Works with latest Selenium libraries (3.3+) and Spring 3.2.x (4.0 coming soon)
* Works with stable TestNG tested for parallel running
* Supports both Webdriver ONLY, standalone or Grid via easy configuration
* Supports TestNG with parallel test execution (each thread has its own browser/session)
* Versatile extension mechanism allows users of Stevia to extend it by:
    * [Controllers via Factory Pattern](https://github.com/persado/stevia/wiki/Extending-web-controller-support) (we load `META-INF/spring/stevia-extensions-drivers-*.xml` from classpath)
    * Navigation Beans, PageObjects, Spring beans (we load `META-INF/spring/test-beans-*.xml` from classpath)
    * Connectors for Rally, JIRA, Testlink (we load `META-INF/spring/stevia-extensions-connectors-*.xml` from classpath)
* Full logging support using ReportNG, with 
    * [screenshots of browser for tests that failed](http://seleniumtestingworld.blogspot.gr/2013/03/reportng-enrichment-with-screenshots.html)
    * actions reporting on test report log and HTML report
* Realtime(!) highlighting of locators, (accessed = yellow, success = green, failure = red)
* [Extended "By" mechanism to support SizzleCSS](http://seleniumtestingworld.blogspot.gr/2013/01/adding-sizzle-css-selector-library-and.html) on Webdriver
* Detailed "[Verify](https://github.com/persado/stevia/blob/ba8d0a54ed743c0050356dca7eb769ef57293175/src/main/java/com/persado/oss/quality/stevia/testng/Verify.java#L49)" class with lots of assertions pre-coded
* Supports for SSH/SFTP via utility classes
* Supports for HTTP GET,POST with Jetty high-performance, multi-threaded helper and cookies support
* Supports thread-level common user configuration and state across Tests (within Stevia thread context)
* Supports Annotations (Java 5+)
    * RunsWithController - allows a different controller (different browser or session) to run a @Test method or class
    * [Preconditions](http://seleniumtestingworld.blogspot.gr/2014/04/concurrency-testing-made-easy.html) - allows methods to be called (optionally with different controller) before @Test method
    * Postconditions - similar to @Precondition but after the @Test method.
* lots of other minor features

### Latest in maven repositories
#### Release
```
<dependency>
  <groupId>com.persado.oss.quality.stevia</groupId>
  <artifactId>stevia-core</artifactId>
  <version>0.9.12</version>
</dependency>
```
#### Cutting edge
```
<dependency>
  <groupId>com.persado.oss.quality.stevia</groupId>
  <artifactId>stevia-core</artifactId>
  <version>0.9.20-SNAPSHOT</version>
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

### Who is Persado <img alt="Persado" width="75" style="background-color:black;" align="right" src="https://raw.githubusercontent.com/persado/stevia/master/doc/persado-squarelogo-1449510923430.png">
Persado’s cognitive content platform generates language that inspires action.
Powered by cognitive computing technologies, the platform eliminates the random process behind traditional message development.
Persado arms organizations and individuals with “smart content” that maximizes engagement with any audience, for every touchpoint, at scale, while delivering unique insight into the specific triggers that drive action.
