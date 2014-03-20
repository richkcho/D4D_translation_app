package com.example.d4dtranslationapp;

public class Statement {
	private String words;
	private int language;
	
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
		return language + " " + words;
	}
}
