package com.example.d4dtranslationapp;

import java.util.*;

public interface Conversation {
	
	/* returns an array of two integers, one for each language used in this conversation. This should be sorted
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
	
	/* returns conversation tree in string form 
	 */
	public String toString();
}
