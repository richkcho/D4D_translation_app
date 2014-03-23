package com.example.d4dtranslationapp;

import java.util.*;

public class ConversationTree implements Conversation {

	// String that separates languages
	public static final String languagesplit = ",";
	
	// String that prefaces the languages in toString
	public static final String languagelabel = "LanguageIDs: ";
	
	// String that separates languages from conversations in toString
	public static final String splitstring = "\n";
	
	// String that prefaces the conversation in toString
	public static final String conversationlabel = "Conversation: ";
	
	private ConversationTreeNode root;
	private int[] languages;
	
	public ConversationTree(String conversationstring)
	{
		String[] params = conversationstring.split(splitstring);
		languages = strArrToIntArr(params[0].substring(params[0].indexOf(languagelabel)+languagelabel.length()).split(","));
		root = new ConversationTreeNode(params[1].substring(params[1].indexOf(conversationlabel)+conversationlabel.length()));
	}
	
	public ConversationTree(ConversationTreeNode rootin)
	{
		root = rootin;
		languages = rootin.getStatementPair().getLanguages();
	}
	
	// begin interface methods
	@Override
	public int[] languages() {
		return languages;
	}

	@Override
	public ArrayList<String> responses(int language) {
		if(validLanguage(language))
		{
			ArrayList<ConversationTreeNode> childnodes = root.getChildren();
			ArrayList<String> resp = new ArrayList<String>();
			for(ConversationTreeNode node : childnodes)
			{
				resp.add(node.getStatementPair().getTranslation(language));
			}
			
			return resp;
		}
		else
		{
			return null;
		}
	}

	@Override
	public boolean choose(int choice) {
		ArrayList<ConversationTreeNode> children = root.getChildren();
		if(choice >= 0 && choice < children.size())
		{
			root = children.get(choice);
			return true;
		}
		return false;
	}
	
	@Override
	public boolean stepBack()
	{
		if(root.getParent() != null)
		{
			root = root.getParent();
			return true;
		}
		
		return false;
	}

	@Override
	public String currentStatementString(int language) {
		return root.getStatementPair().getTranslation(language);
	}

	@Override
	public String toString() {
		return languagelabel + languages[0] + languagesplit + languages[1] + splitstring + conversationlabel + root.toString();
	}
	// end interface methods
	
	// helper functions
	private boolean validLanguage(int language)
	{
		return language == languages[0] || language == languages[1];
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
}

