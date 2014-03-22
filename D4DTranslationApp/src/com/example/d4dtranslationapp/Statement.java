package com.example.d4dtranslationapp;

public class Statement {
	
	// String that separates language and words in toString
	public static final String splitstring = " ";
	
	private String words;
	private int language;
	
	public Statement(String statementstring)
	{
		String[] params = statementstring.split(splitstring, 2);
		words = params[1];
		language = Integer.parseInt(params[0]);
	}
	
	public Statement(String wordsin, int languagein)
	{
		words = wordsin;
		language = languagein;
	}
	
	public String getWords()
	{
		return words;
	}
	
	public int getLanguage()
	{
		return language;
	}
	
	// {language_id} string
	public String toString()
	{
		return language + splitstring + words;
	}
}
