package com.example.d4dtranslationapp;

import java.util.*;

public class ConversationTree implements Conversation {

	private ConversationTreeNode root;
	private int[] languages;
	
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
		return "LanguageIDs: " + languages[0] + "," + languages[1] + "\nConversation: " + root.toString();
	}
	// end interface methods
	
	// helper functions
	private boolean validLanguage(int language)
	{
		return language == languages[0] || language == languages[1];
	}
}

