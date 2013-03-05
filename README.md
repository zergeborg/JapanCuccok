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

2.a Clone the repository to your local computer

2.b Run Maven install, type:
mvn install

2.c Download and install Google App Engine plugin to your IDE

2.d Make sure that the following files were not overwritten by the IDE:
- ${JAPANCUCCOK_HOME}\web\WEB-INF\appengine-web.xml
- ${JAPANCUCCOK_HOME}\web\WEB-INF\web.xml

2.d Make sure that you add the following libraries to your project's (module in Intellij IDEA) classpath:
- ${GAE_HOME}\lib\user
- ${GAE_HOME}\lib\testing
- ${GAE_HOME}\lib\impl\appengine-api-stubs.jar
- ${JAPANCUCCOK_HOME}\web\WEB-INF\lib

2.e Also make sure that the following is available on your webapp's classpath (runtime jars):
- ${JAPANCUCCOK_HOME}\web\WEB-INF\lib


What is this project about?
---------------------------

It's really good question! Since I promised myself to develop my skills further, I thought it would be a nice idea to dive in the clouds! :D Yup, I'm a daydreamer. So anything you find here, is just for my own pleasure. Like an online playground.

Since the fact that HTML5 and CSS3 is here with us is so evident, I thought that I should focus on these technologies the most, so the users of the application might stop for a minute and take their breath for a second... Just to click over the page without any purpose. :) So, the goal is: fluent experiment, with lots of eyecandy.

Please note that the code length and the application release date might go unintentionally into infintiy. :) But this only means that I'll take extra care to craft a smoothly handled and well performing application.

NOTE: This application is not comprehensive at all. Use it on your own risk. Since the application works in limited context, extra care is needed during usage and development. If you're not familiar with Google App Engine or Wicket than I would propose to check these links below first, and come back a little later to decide if you would try it:

Wicket: ~ is a wonderful framework full of easily manageable structure elements. If you ever wondered why you have to learn for months to start using an MVC framework I would recommend this framework in the first place to you! Simple, articulated and easily understandable abstractions, useful services and fine grained tuning possibilities. With a super helpful community! It's all under the same hood!

Wicket sample library: http://www.wicket-library.com/wicket-examples/index.html

Wicket Wiki with articles on specific areas: https://cwiki.apache.org/WICKET/

(MUST HAVE) Wicket book: http://wicketinaction.com/

This latest site is also the repository of valuable knowledge! Check it out! ^_^

Google App Engine: ~ is in the cloud. Sounds familiar, right?! Yup, it does. Today nobody can stay clear of this term. It's everywhere! You'll find it in Gmail, in your iPhone and iPad, in your Android and also in your Media Players or your home equipment! Why not to try it! Yup, it all sounds good. It sounded good for me as well. But then you start coding and you just hit the first door with your head! :) Take it easy, man! :D Why don't you read about the API first! It's impressive, I can guarantee! Then you'll never ever want to miss it out again. So let's do some reading first:

Google App Engine: The official docs. This is the very first starting point! You'll never grasp it if you miss it!
https://developers.google.com/appengine/docs/

Objectify: because we like strongly-typed systems, don't we? ;)
http://code.google.com/p/objectify-appengine/


What is this project NOT about?
--------------------------------

OK, so you read it and you arrived here finally. Then you must be simply crazy or really motivated. So I'll try to loosen your interest since I have bad news. This system is:
- NOT performance tested
- NOT highly scalable
- NOT a simple Joomla template
- NOT a final implementation
- NOT portable (actually it is, but it needs major changes then)

But it:
- IS FREE
- IS carefully designed
- IS responsive
- IS performing well on client side on weaker architectures
- IS lightweight (with small memory footprint)
- IS loosely coupled under the hood


So, have fun! I bet you will... And feel free to contact me anytime!

Cheers!
