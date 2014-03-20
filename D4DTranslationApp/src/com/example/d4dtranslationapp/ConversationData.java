package com.example.d4dtranslationapp;

public interface ConversationData {
	
	/* returns the id of the conversation in the database
	 * this is later used to fetch conversation from database
	 */
	public int getConversationID();
	
	/* returns an array of supported languages for the conversation
	 * two of these are passed with an id to retrieve a conversation
	 */
	public int[] getSupportedLanguages();
	
	/* Gets the string form of category translated into specified language
	 * returns null if invalid language
	 */
	public String getCategoryString(int language);
	
	/* Gets the string form of the description translated into specified language
	 * returns null if invalid language
	 */
	public String getDescriptionString(int language);
}
