package com.example.d4dtranslationapp;

public class StatementPair {

	// String that prefaces a Statement in toString
	public static final String pre = "[";
	
	// String that postfaces a Statement in toString
	public static final String post = "]";
	
	// String used to separate one Statement from another in toString
	public static final String splitstring = " / ";
	
	private Statement s1,s2;
	
	public StatementPair(String statementstring)
	{
		String[] params = statementstring.substring(1, statementstring.length()-1).split(splitstring);
		s1 = new Statement(params[0]);
		s2 = new Statement(params[1]);
	}
	
	public StatementPair(String s1in, int l1in, String s2in, int l2in)
	{
		this(new Statement(s1in, l1in), new Statement(s2in, l2in));
	}
	
	public StatementPair(Statement s1in, Statement s2in)
	{
		s1 = s1in;
		s2 = s2in;
	}
	
	public String getTranslation(int language)
	{
		return (language == s1.getLanguage()?s1.getWords():(language == s2.getLanguage()?s2.getWords():null));
	}
	
	public int[] getLanguages()
	{
		return new int[]{s1.getLanguage(),s2.getLanguage()};
	}
	
	// assuming language numbers are sorted
	public boolean languageMatch(StatementPair s)
	{
		int[] otherlangs = s.getLanguages();
		
		if(otherlangs[0] == s1.getLanguage() && otherlangs[1] == s2.getLanguage())
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
		return pre + s1 + splitstring + s2 + post;
	}
}
