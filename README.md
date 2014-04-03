stevia
======
Build Status of 0.8.7-SNAPSHOT [![Status](https://travis-ci.org/persado/stevia.png?branch=master)](https://travis-ci.org/persado/stevia) 

Stevia is an Open Source QA Automation Testing Framework by [Persado](http://www.persado.com). In Persado, we took the pain out of having to care about Selenium or Webdriver (or both) and unified them under a common API, with a sane and clear-cut design, ability to extend and expand (courtesy of Spring!) and a bit of sweetness. Stevia is what we got out of it: 

<p align="center"><img src="https://raw.github.com/persado/stevia/master/doc/stevia-logo.png" width="120"> </p>

## Stevia API Javadoc

You can browse our javadoc via this [link](http://persado.github.io/stevia/).

## Stevia Quick Start (10 minutes) guide

Our 10-minutes guide for using Stevia is in our [wiki](https://github.com/persado/stevia/wiki/Stevia-10-minute-Quick-Start). Read on and start coding!

## News

### 04-Apr-2014 Quick Start guide

Hello! 

We have just included a quick-start guide. Lots of stuff explained and a fully running project, from start to end in 10 minutes. Of course, there are still many things unexplained. Feel free to ping us or raise an issue in our [issues](https://github.com/persado/stevia/issues) tool and we'll try to answer as soon as possible.


### 13-Mar-2014 Update for Selenium libraries 2.40

Hello!

Our latest SNAPSHOT 0.8.7 has an important update that fixes our SizzleCSS integration with Selenium 2.40 (commit [6b857c6](https://github.com/persado/stevia/commit/6b857c650b684a60f483c9caf7b106b359284f0b)). Pick up the latest with this fragment:
```
<dependency>
  <groupId>com.persado.oss.quality.stevia</groupId>
  <artifactId>stevia-core</artifactId>
  <version>0.8.7-SNAPSHOT</version>
</dependency>
```


### 07-Mar-2014 Latest developments

It has come to our attention that some of our friends have difficulty downloading stevia from our Open Source repository, Sonatype. The best (and easiest) way to do this is via Maven dependencies; adding the repo to your pom.xml is fairly trivial:

```
<repositories>
  <repository>
    <id>sonatype-nexus-snapshots</id>
    <name>OSS Sonatype Snapshot Repository</name>
    <url>https://oss.sonatype.org/content/repositories/snapshots</url>
  </repository>
</repositories>	
```

Our latest SNAPSHOT (you can see this by observing the pom.xml) can be added in your dependencies as follows:
```
<dependency>
  <groupId>com.persado.oss.quality.stevia</groupId>
  <artifactId>stevia-core</artifactId>
  <version>0.8.6-SNAPSHOT</version>
</dependency>
```

Enjoy!


### 20-Jan-2014 Upcoming Stevia 0.8.0!
Dear friends, 

We are now getting ready with a 0.8.0 release, with many ideas still in progress and waiting to be merged. For the moment we've changed the way we're instantiating controllers so now you can add your own without the need to recompile. Check our wiki page [here](https://github.com/persado/stevia/wiki/Extending-web-controller-support) for the way to do this. Contact us for more ideas - play with this in our snapshot 0.8.2 version (snapshot repo location in pom.xml):

```
<dependency>
  <groupId>com.persado.oss.quality.stevia</groupId>
  <artifactId>stevia-core</artifactId>
  <version>0.8.2-SNAPSHOT</version>
</dependency>
```


* * *

### Who is Persado <p align="right"><img alt="Persado" width="75" src="http://www.persado.com/templates/youandigraphics/images/logo.png"></p>
Persado is the leader in Marketing Language Engineering: the use of math and big data to unlock the DNA of selling online. 

Persado uses semantic and statistical algorithms to map marketing language and engineer the absolute best online marketing messages. It's proven. With our technology, you sell more. 

Over the past 7 years, we've analyzed billions of consumers' online interactions to identify and map the key components of marketing language. Emotions, product features, calls-to-action, formatting, amongst others, are semantically mapped in our database. For any marketing message, we first model and then produce all possible variations (up to 16 million). Using our statistical algorithms, we identify the absolute best one.




