package com.example.d4dtranslationapp;

import java.util.*;

public interface ConversationDatabase {
	
	/* Given parameters such as category and a language set, return ArrayList of ConversationData that fits input criteria
	 * -1 for category is wildcard, null for languages is wildcard. Needs a valid user_language though. 
	 * returns empty ArrayList if no such conversations match data
	 */
	public ArrayList<ConversationData> getConversationData(int category, int[] languages);
	
	/* return Conversation matching specified ID and languages
	 * return NULL if no such id exists
	 */
	public Conversation getConversation(int conversation_id, int language1, int language2);
}
