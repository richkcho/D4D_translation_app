package com.example.d4dtranslationapp;

public class MyConversationData implements ConversationData{
	
	public static final String pre = "(";
	
	public static final String post = ")";
	
	// String that prefaces the conversation_id in toString
	public static final String conversationidlabel = "ConversationID: ";
	
	// String that separates languages
	public static final String languagesplit = ",";
	
	// String that separates statements
	public static final String statementsplit = " / ";
	
	// String that prefaces the languages in toString
	public static final String languagelabel = "LanguageIDs: ";
	
	// String that separates different sections of the toString
		public static final String splitstring = "\n";
	
	// String that prefaces category
	public static final String categorylabel = "Category: ";
	
	// String that prefaces description
	public static final String descriptionlabel = "Description: ";

	private int conversation_id;
	private int[] supported_languages;
	private Statement[] category;
	private Statement[] description;
	
	public MyConversationData(int conversation_idin, int[] supported_languagesin, String categoryin, String descriptionin)
	{
		conversation_id = conversation_idin;
		supported_languages = supported_languagesin;
		category = strToStatementArray(categoryin);
		description = strToStatementArray(descriptionin);
		
	}
	
	public MyConversationData(String conversationdata)
	{
		String[] params = conversationdata.substring(1, conversationdata.length()-1).split(splitstring);
		
		conversation_id  = Integer.parseInt(trimLabel(params[0], conversationidlabel));
		supported_languages = strArrToIntArr(trimLabel(params[1], languagelabel).split(languagesplit));
		category = strToStatementArray(trimLabel(params[2], categorylabel));
		description = strToStatementArray(trimLabel(params[3], descriptionlabel));
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
		return pre + conversationidlabel + conversation_id + splitstring + 
				languagelabel + intArrToString(supported_languages) + splitstring +
				categorylabel + statementArrToString(category) + splitstring + 
				descriptionlabel + statementArrToString(description) + post;
	}
	
	private String trimLabel(String str, String label)
	{
		return str.substring(str.indexOf(label)+label.length());
	}
	
	private String intArrToString(int[] arr)
	{
		String res = ""+arr[0];
		
		for(int temp = 1; temp < arr.length; temp++)
		{
			res += languagesplit + arr[temp];
		}
		
		return res;
	}
	
	private String statementArrToString(Statement[] arr)
	{
		String res = ""+arr[0];
		
		for(int temp = 1; temp < arr.length; temp++)
		{
			res += statementsplit + arr[temp].toString();
		}
		
		return res;
	}
	
	private int[] strArrToIntArr(String[] arr)
	{
		int[] res = new int[arr.length];
		for(int temp = 0; temp < res.length; temp++)
		{
			res[temp] = Integer.parseInt(arr[temp]);
		}
		
		return res;
	}
	
	private Statement[] strToStatementArray(String str)
	{
		String[] trans = str.split(statementsplit);
		Statement[] res = new Statement[trans.length];
		
		for(int temp = 0; temp < res.length; temp++)
		{
			String stemp = trans[temp];
			res[temp] = new Statement(stemp.substring(stemp.indexOf(Statement.splitstring)+1), 
						Integer.parseInt(stemp.substring(0, stemp.indexOf(Statement.splitstring))));
		}
		
		return res;
	}
}
