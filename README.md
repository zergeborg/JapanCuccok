JapanCuccok
===========

My first Wicket Project running on Google AppEngine. Basically a simple webshop.

Visit the link below if you're interested, please:
http://fogettijapancuccok.appspot.com/

Build steps:
-------------------

1. Prerequisites:
Please make sure that you have the following programs installed. Also make sure that you have the very same(!) version of these programs. While it is possible that the webapp might work with other versions as well, there is no guarantee that it will do so.
Install the following:
    - JRE 1.6
    - Google App Engine Java SDK 1.7.5
    - Apache Maven 3.0.5
    - Your choice of IDE


2. Setup:
For the easier setup process you will find a pom.xml with all the neccessary dependencies included it. Please make sure that you use Maven 3.0.5, otherwise you will get lot(!) of tranistive (and unnecessary dependencies). You have been warned!

    2. Clone the repository to your local computer

    2. Run Maven install, type:
    mvn install

    2. Download and install Google App Engine plugin to your IDE

    2. Make sure that the following files were not overwritten by the IDE:
        - ${JAPANCUCCOK_HOME}\web\WEB-INF\appengine-web.xml
        - ${JAPANCUCCOK_HOME}\web\WEB-INF\web.xml

    2. Make sure that you add the following libraries to your project's (module in Intellij IDEA) classpath:
        - ${GAE_HOME}\lib\user
        - ${GAE_HOME}\lib\testing
        - ${GAE_HOME}\lib\impl\appengine-api-stubs.jar
        - ${JAPANCUCCOK_HOME}\web\WEB-INF\lib

    2. Also make sure that the following is available on your webapp's classpath (runtime jars):
        - ${JAPANCUCCOK_HOME}\web\WEB-INF\lib
