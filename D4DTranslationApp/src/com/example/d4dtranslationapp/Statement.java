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
	
	public String toString()
	{
		return "[" + s1 + "/" + s2 + "]";
	}
}
