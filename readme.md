#D4D Translation App Code

**Overview of our goals, designs, and expectations:**

General:
* This is an app designed to improve communication between tourists and people of the place that is being toured. Most of the time, people need to ask simple things like where is the restroom or does this food have peanuts in it or something of the like, but for some reason cannot rely on Google translate or do not have internet. In this case, our app holds conversation trees of frequently used conversations in multiple languages. 
* This is intended to connect to a database to get conversation tree information, whether the database be offsite (server) or local (on the android device)
* Although not optimal for testing or for large databases/database management, will start out with local databases

Database: 
* Conversations are trees, each node when handed a language parameter will return a comment in specified language
* Assume that we are using a few predefined conversation paths that will be translated into different languages
* Hold the conversations in multiple languages, translations of of conversation will be associated and identified via id
* We will be using adjacency list for the database, as we are only going down a path one step at a time and want DB to be easily modifiable
* Each node of the tree will reference a table with translations of comment in supported languages

Backend Java/Android:
* Java is chosen as it has to support the (server and the) android (I am hoping for easy transition process)
* Will pass a Conversation Tree (CT) object to the frontend to use. 
* Will construct a CT object with the information passed from the frontend and the DB's information
* May have to talk to a server to ask for CT info in some form, which it uses to construct the CT. 

Frontend Android: 
* Users will choose a conversation tree and the other supported language to translate into
* Send language choice and conversation ID to backend and receive a CT. 
* Output responses and interact with the user in receiving his input and traversing the CT