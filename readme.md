#D4D Translation App Code

**Overview of our goals, designs, and expectations:**

General:
* This is an app designed to improve communication between tourists and people of the place that is being toured. Most of the time, people need to ask simple things like where is the restroom or does this food have peanuts in it or something of the like, but for some reason cannot rely on Google translate or do not have internet. In this case, our app holds conversation trees of frequently used conversations in multiple languages. 
* This is intended to connect to a database to get conversation tree information, whether the database be offsite (server) or local (on the android device)
* Although not optimal for testing or for large databases/database management, will start out with local databases
* THe interfaces describe how the objects should work

Database: 
* Conversations are trees, each node when handed a language parameter will return a comment in specified language
* Assume that we are using a few predefined conversation paths that will be translated into different languages
* Hold the conversations in multiple languages, translations of of conversation will be associated and identified via id
* We will be using adjacency list for the database, as we are only going down a path one step at a time and want DB to be easily modifiable
* Each node of the tree will reference a table with translations of comment in supported languages
* Assume entries dealing with languages are sorted by language number

Backend Java/Android:
* Java is chosen as it has to support the (server and the) android (I am hoping for easy transition process)
* Will pass a Conversation object to the frontend to use. 
* Will construct a Conversation object with the information passed from the frontend and the DB's information
	* For now, Conversation has been implemented in a ConversationTree object
* May have to talk to a server to access the database for information which it uses to construct the Conversation. 

Frontend Android: 
* Users will choose a conversation tree and the other supported language to translate into
	* Must ask backend for list of available conversations, will pass a list of supported conversations and languages supported by it
	* These may be grouped into categories, depending on what topic they are about (not immediate goal)
	* These will probably end up being ConversationData types, which consist of a brief summary/description of conversation, its ID, and a list of supported languages. Then the id and two languages are sent to the backend to receive a Conversation object.
* Output responses and interact with the user in receiving his input and traversing the Conversation
* Layout:
	* For initial phase, consider displaying both sides of conversation on the screen at the same time i.e. for En-Es have half of the screen traversing the Conversation in English, half in Spanish, this is a good first step and may help in debugging
	* Later we can do one at a time and add the rotating/switching graphical effects. 
	* Screen should start at a menu displaying a list of ConversationData with part of description(and possibly supported languages, title, group). When a ConversationData is opened, description is showed as well as supported languages, from here select languages and get the conversation

**These are always subject to change**