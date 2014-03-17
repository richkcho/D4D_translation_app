package com.example.d4dtranslationapp;

import java.util.*;

public class ConversationTreeNode {
	
	private ArrayList<ConversationTreeNode> children;
	private Statement statement;
	
	public ConversationTreeNode(Statement statementin)
	{
		statement = statementin;
		children = new ArrayList<ConversationTreeNode>();
	}
	
	public ConversationTreeNode(Statement statementin, ArrayList<ConversationTreeNode> childrenin)
	{
		statement = statementin;
		children = childrenin;
	}
	
	/* Sets the children of this node to be a certain ArrayList of children
	 * will only complete operation if language integrity is maintained
	 * returns true if completed, false if not
	 */
	public boolean setChildren(ArrayList<ConversationTreeNode> childrenin)
	{
		for(ConversationTreeNode child : childrenin)
		{
			if(!languagesMatch(statement, child.getStatement()))
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
		if(languagesMatch(statement, nodein.getStatement()))
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
	
	public void setStatement(Statement statementin)
	{
		statement = statementin;
	}
	
	public Statement getStatement()
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
	
	private boolean languagesMatch(Statement s1, Statement s2)
	{
		int[] langs1 = s1.getLanguages();
		int[] langs2 = s2.getLanguages();
		
		if((langs1[0] == langs2[0] && langs1[0] == langs2[0]) || (langs1[0] == langs2[1] && langs1[1] == langs2[0]))
		{
			return true;
		}
		
		return false;
	}
}
