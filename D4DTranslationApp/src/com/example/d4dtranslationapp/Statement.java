package com.example.d4dtranslationapp;

//holds statements in s and the language of the statement in l
public class Statement {
	private String s1, s2;
	private int l1, l2;
	
	public Statement(String s1in, int l1in, String s2in, int l2in)
	{
		s1 = s1in;
		l1 = l1in;
		s2 = s2in;
		l2 = l2in;
	}
	
	public String getTranslation(int language)
	{
		return (language == l1?s1:(language == l2?s2:null));
	}
	
	public int[] getLanguages()
	{
		return new int[]{l1,l2};
	}
	
	// assuming language numbers are sorted
	public boolean languageMatch(Statement s)
	{
		int[] otherlangs = s.getLanguages();
		
		if(otherlangs[0] == l1 && otherlangs[1] == l2)
		{
			return true;
		}
		
		return false;
	}
	
	/* [{language_id} string / {language_id} string]
	 * this is the way the data will be stored in the database, minus the surrounding brackets and if applicable other language translations
	 */
	public String toString()
	{
		return "[" + l1 + " " + s1 + " / " + l2 + " " + s2 + "]";
	}
}
