----------------------------------------------------------------------------------
                   I. RealTimeElasticsearchTopology
----------------------------------------------------------------------------------



Overview:
---------

This application is used to interface with a twitter application through the Twitter APIs and stores all the tweets
in an Elasticsearch. The elasticsearch is then queried via different types of queries that elasticsearch provides
and the results are passed on to the DRPC object, which is then displayed on the console.



Running Instructions:
---------------------

To run the RealTimeElasticSearchTopology.java, the following environment variables need to be set:
	a. TWITTER_CONSUMER_KEY - contains the consumer key for the twitter application
	b. TWITTER_CONSUMER_SECRET - contains the consumer secret key for the twitter application
	c. TWITTER_ACCESS_TOKEN - contains the access public key for the twitter application
	d. TWITTER_ACCESS_SECRET - contains the access secret key for the twitter application

Go to the $TRIDENT_STARTER directory (the directory with the pom.xml file).

To compile the application, run the following command:
	mvn package

Before running the application, ensure that Elasticsearch process is running. It can be run as:
	./bin/elasticsearch #from the elasticsearch directory

To execute the RealTimeElasticSearchTopology.java file, run the following command:
	storm jar target/storm-starter-0.9.3.jar storm.starter.trident.project.RealTimeElasticSearchTopology \
		$TWITTER_CONSUMER_KEY $TWITTER_CONSUMER_SECRET $TWITTER_ACCESS_TOKEN $TWITTER_ACCESS_SECRET \
		[optional list of keywords to filter the tweets]








----------------------------------------------------------------------------------
                   II. CountMinSketchTopology - Top-K words
----------------------------------------------------------------------------------



Overview:
---------

This application uses a count-min sketch to store and retrieve the Top-K words from a real time stream of fake
tweets. The fake tweets are generated through a Trident Spout, and every word in the tweet is then stored in
the count-min sketch. The priority queue, which contains the Top-K words is also updated for every word that is
added or updated in the count-min sketch.

Since the fake tweets also contain very common words such as adjectives, verbs, pronouns, prepositions, etc.
(like - 'a', 'an', 'the', 'and', etc.), these words should be avoided in computing the Top K words to truly be 
able to find the common words. Therefore, these words, labelled Stop Words ignored. The stop words are mentioned
in the "state/TopList.java" file. More words can be added to the Stop Words by adding words in this file in the
appropriate location within the java file (String[] stopWords).



Running Instructions:
---------------------

Since this application does not interface with Twitter or any other stream, no enviroment variables containing the
application details are required. Also, since elasticsearch is not used, elasticsearch process need not be running.

To compile the application, go to the $TRIDENT_STARTER directory and run the following command:
	mvn package

To execute the application, run the following command:
	storm jar target/storm-starter-0.9.3.jar storm.starter.trident.project.countmin.CountMinSketchTopology








----------------------------------------------------------------------------------
Author: Vijay Sankar Ganesh
EMail: vgsankar@ncsu.edu
----------------------------------------------------------------------------------

