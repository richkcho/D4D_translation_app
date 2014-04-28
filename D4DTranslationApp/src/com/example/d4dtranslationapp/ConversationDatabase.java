package com.example.d4dtranslationapp;

import java.util.*;

/* NOTE: whenever a database is accessed, please do so is a thread outside the main thread
 * To do this use Async or some other thread-based method. I may write a async/thread wrapper class for ConversationDatabases. 
 */

public interface ConversationDatabase {
	
	/* Given parameters such as category and a language set, return ArrayList of ConversationData that fits input criteria
	 * -1 for category is wildcard, null for languages is wildcard. 
	 * returns empty ArrayList if no such conversations match data
	 */
	public ArrayList<ConversationData> getConversationData(int category, int[] supported_languages);
	
	/* return Conversation matching specified ID and languages. 
	 * return NULL if no such conversation exists that supports both languages
	 */
	public Conversation getConversation(int conversation_id, int language1, int language2);
}
