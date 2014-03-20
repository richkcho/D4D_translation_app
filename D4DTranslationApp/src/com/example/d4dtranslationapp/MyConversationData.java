package com.example.d4dtranslationapp;

public class MyConversationData implements ConversationData{

	private int conversation_id;
	private int[] supported_languages;
	private Statement[] category;
	private Statement[] description;
	
	public MyConversationData(int conversation_idin, String supported_languagesin, String categoryin, String descriptionin)
	{
		conversation_id = conversation_idin;
		supported_languages = strToIntArray(supported_languagesin);
		category = strToStatementArray(categoryin);
		description = strToStatementArray(descriptionin);
		
	}
	
	public int getConversationID()
	{
		return conversation_id;
	}
	
	public int[] getSupportedLanguages()
	{
		return supported_languages;
	}
	
	public String getCategoryString(int language)
	{
		for(Statement temp : category)
		{
			if(temp.getLanguage() == language)
			{
				return temp.getWords();
			}
		}
		
		return null;
	}
	
	public String getDescriptionString(int language)
	{
		for(Statement temp : description)
		{
			if(temp.getLanguage() == language)
			{
				return temp.getWords();
			}
		}
		
		return null;
	}
	
	public String toString()
	{
		return "conversation_id: " + conversation_id + "\n" + category[0] + "\n" + description[0];
	}
	
	private Statement[] strToStatementArray(String str)
	{
		String[] trans = str.split(" / ");
		Statement[] res = new Statement[trans.length];
		
		for(int temp = 0; temp < res.length; temp++)
		{
			String stemp = trans[temp];
			res[temp] = new Statement(stemp.substring(stemp.indexOf(" ")+1), Integer.parseInt(stemp.substring(0, stemp.indexOf(" "))));
		}
		
		return res;
	}
	
	private int[] strToIntArray(String str)
	{
		String[] temparr = str.split(",");
		int[] res = new int[temparr.length];
		
		for(int temp = 0; temp < res.length; temp++)
		{
			res[temp] = Integer.parseInt(temparr[temp]);
		}
		
		return res;
	}
}
