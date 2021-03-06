package com.example.d4dtranslationapp;

import java.util.*;

public interface Conversation {
	
	/* returns an array of two integers, one for each language used in this conversation
	 * returns null if this conversation has no instantiated languages 
	 */
	public int[] languages();
	
	/* returns an array of the possible responses to the current statement
	 * returns null if invalid language
	 */
	public ArrayList<String> responses(int language);
	
	/* Tells the conversation what choice was made, return true if was successful
	 * choice is the index of the response in responses
	 * returns false if invalid choice
	 */
	public boolean choose(int choice);
	
	/* Undoes a choice. Steps back in the Conversation
	 * returns true if successful (there was a comment before), false otherwise
	 */
	public boolean stepBack();
	
	/* returns the current statement in conversation
	 * returns null if invalid language
	 */
	public String currentStatementString(int language);
	
	/* returns remaining* conversation tree in string form for specified language
	 * *remaining tree in that whenever a choice is made the Conversation moves to the subtree defined by the choice
	 * returns null if invalid language
	 */
	public String toString();
}
