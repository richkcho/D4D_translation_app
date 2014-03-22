package com.example.d4dtranslationapp;

import java.util.*;

public class ConversationTreeNode {
	
	// Char that prefaces a ConversationTreeNode in toString
	public static final char pre = '(';
	
	// Char that postfaces a ConversationTreeNode in toString
	public static final char post = ')';
	
	// string that separates the statement and children in toString
	public static final String splitstring = ",";
	
	private ArrayList<ConversationTreeNode> children;
	private ConversationTreeNode parent;
	private StatementPair statement;
	
	public ConversationTreeNode(String conversationstring)
	{
		// parse info from string
		conversationstring = conversationstring.substring(1, conversationstring.length()-1);
		String[] params = conversationstring.split(splitstring, 2);
		
		// initialize children and set statement
		statement = new StatementPair(params[0]);
		children = new ArrayList<ConversationTreeNode>();
		
		// add children
		if(params.length > 1)
		{
			while(params[1].length() != 0)
			{
				int[] bounds = findNextConversationTreeNode(params[1], 0);
				this.addChild(new ConversationTreeNode(params[1].substring(bounds[0], bounds[1]+1)));
				params[1] = params[1].substring(bounds[1]+1);
			}
		}
	}
	
	public ConversationTreeNode(StatementPair statementpairin)
	{
		parent = null;
		statement = statementpairin;
		children = new ArrayList<ConversationTreeNode>();
	}
	
	public ConversationTreeNode(StatementPair statementpairin, ArrayList<ConversationTreeNode> childrenin)
	{
		this(statementpairin);
		children = childrenin;
	}
	
	public ConversationTreeNode(StatementPair statementpairin, ConversationTreeNode parentin)
	{
		this(statementpairin);
		parent = parentin;
	}
	
	public ConversationTreeNode(StatementPair statementpairin, ConversationTreeNode parentin, ArrayList<ConversationTreeNode> childrenin)
	{
		this(statementpairin, childrenin);
		parent = parentin;
	}
	
	public void setParent(ConversationTreeNode parentin)
	{
		parent = parentin;
	}
	
	public ConversationTreeNode getParent()
	{
		return parent;
	}
	
	/* Sets the children of this node to be a certain ArrayList of children
	 * will only complete operation if language integrity is maintained
	 * returns true if completed, false if not
	 */
	public boolean setChildren(ArrayList<ConversationTreeNode> childrenin)
	{
		for(ConversationTreeNode child : childrenin)
		{
			if(!statement.languageMatch(child.getStatementPair()))
			{
				return false;
			}
		}
		
		children = childrenin;
		return true;
	}
	
	/* Adds a child node to the children of this node, and sets this node as its parent
	 * will only complete if language integrity is maintained
	 * returns true if completed, false if not
	 */
	public boolean addChild(ConversationTreeNode nodein)
	{
		if(statement.languageMatch(nodein.getStatementPair()))
		{
			nodein.setParent(this);
			children.add(nodein);
			return true;
		}
		
		return false;
	}
	
	public ArrayList<ConversationTreeNode> getChildren()
	{
		return children;
	}
	
	public void setStatementPair(StatementPair statementpairin)
	{
		statement = statementpairin;
	}
	
	public StatementPair getStatementPair()
	{
		return statement;
	}
	
	/* Returns String representation of this object
	 * anything in () is a TreeNode, anything in [] is a statement
	 * Format is ([statement], (child 1), (child 2), ..., (child n))
	 */
	public String toString()
	{
		String res = pre + statement.toString();
		
		for(ConversationTreeNode temp : children)
		{
			res += splitstring + temp.toString();
		}
		
		return res + post;
	}
	
	// helper functions
	
	/* given a string and a starting index, returns an integer pair with the locations of the next set of balanced parenthesis 
	 * returns null if no such balanced parenthesis can be found
	 */
	private int[] findNextConversationTreeNode(String str, int startindex)
	{
		int nesting = 0;
		int[] res = new int[2];
		for(int temp = startindex; temp < str.length(); temp++)
		{
			char c = str.charAt(temp);
			
			if(c == pre)
			{
				if(nesting == 0)
				{
					res[0] = temp;
				}
				
				nesting++;
			}
			else if(c == post)
			{
				nesting--;
				
				if(nesting == 0)
				{
					res[1] = temp;
					return res;
				}
			}
		}
		
		return null;
	}
}
