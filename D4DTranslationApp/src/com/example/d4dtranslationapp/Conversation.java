package com.example.d4dtranslationapp;

import java.util.*;

public interface Conversation {
	
	/* returns an array of two strings, one for each language used in this conversation
	 * returns null if this conversation has no instantiated languages 
	 */
	public int[] languages();
	
	/* returns an array of the possible responses to the current statement
	 * returns null if invalid language
	 */
	public ArrayList<String> responses(int language);
	
	/* tells the conversation what choice was made, return true if was successful
	 * returns false and does nothing if invalid choice
	 */
	public boolean choose(int choice);
	
	/* returns the current statement in conversation
	 * returns null if invalid language
	 */
	public String currentStatementString(int language);
	
	/* returns conversation tree is string form for specified language
	 * returns null if invalid language
	 */
	public String toString();
}
