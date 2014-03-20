package com.example.d4dtranslationapp;

import java.util.ArrayList;

public class ConversationTreeNode {
	
	private ArrayList<ConversationTreeNode> children;
	private ConversationTreeNode parent;
	private StatementPair statement;
	
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
	
	/* Adds a child node to the children of this node
	 * will only complete if language integrity is maintained
	 * returns true if completed, false if not
	 */
	public boolean addChild(ConversationTreeNode nodein)
	{
		if(statement.languageMatch(nodein.getStatementPair()))
		{
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
		String res = "(" + statement.toString();
		
		for(ConversationTreeNode temp : children)
		{
			res += "," + temp.toString();
		}
		
		return res + ")";
	}
}
