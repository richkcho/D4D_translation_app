package com.example.d4dtranslationapp;

import java.util.*;
import android.database.*;
import android.database.sqlite.*;
import android.content.*;

public class MyDatabase implements ConversationDatabase {

	private MyDatabaseHelper dbhelper;
	private SQLiteDatabase database;
	
	public MyDatabase(Context context)
	{
		dbhelper = new MyDatabaseHelper(context);
		database = dbhelper.getReadableDatabase();
	}
	
	@Override
	public ArrayList<ConversationData> getConversationData(int category, int[] languages) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Conversation getConversation(int conversation_id, int language1, int language2) {
		if(validConversationParamters(conversation_id, language1, language2))
		{
			Cursor root = database.query("statements", null, "conversation_id = ? AND parent_statement_id=-1", new String[]{""+conversation_id}, null, null, null);
			root.moveToFirst();
			return new ConversationTree(build(root, language1, language2));
		}
		
		return null;
	}
	
	/* Traverses down the tree down the database and builds a ConversationTree object
	 * the cursor passed in will be used to make the root of the tree
	 */
	private ConversationTreeNode build(Cursor node, int language1, int language2)
	{
		// prepare current node's Statement and create node
		int translation_id = node.getInt(node.getColumnIndex("conversation_id"));
		
		ConversationTreeNode ctnode = new ConversationTreeNode(getStatement(translation_id, language1, language2)); // TODO
		
		// prepare cursor with children
		String[] params = new String[]{""+node.getInt(node.getColumnIndex("conversation_id")), ""+node.getInt(node.getColumnIndex("statement_id"))};
		Cursor children = database.query("statements", null, "conversation_id=? AND parent_statement_id=?", params, null, null, null);
		
		// add all of the children to the node
		if(children.moveToFirst())
		{
			for(int temp = 0, max = children.getCount(); temp < max; temp++)
			{
				ctnode.addChild(build(children, language1, language2));
				children.moveToNext();
			}
		}
		
		return ctnode;
	}
	
	/* Gets info from the translations table in the database
	 * returns the proper Statement, given languages and translation_id parameters
	 */
	private Statement getStatement(int translation_id, int language1, int language2)
	{
		// get all translations associated with translation_id
		Cursor c = database.query("translations", new String[]{"translation"}, "translation_id="+translation_id, null, null, null, null, "1");
		
		// if such translation_id exists, then parse
		if(c.moveToFirst())
		{
			// database entries are structured as: lang_id_1 string_1 / lang_id_2 string_2 / ...
			String[] trans = c.getString(0).split(" / ");
			String s1 = "", s2 = "";
			for(String stemp : trans)
			{
				if(Integer.parseInt(stemp.substring(0, stemp.indexOf(" "))) == language1)
				{
					s1 = stemp.substring(stemp.indexOf(" ")+1);
				}
				else if(Integer.parseInt(stemp.substring(0, stemp.indexOf(" "))) == language2)
				{
					s2 = stemp.substring(stemp.indexOf(" ")+1);
				}
			}
			
			return new Statement(s1, language1, s2, language2);
		}
		
		return null;
	}
	
	/* Given conversation_id and language parameters, checks if it exists in database
	 * return true if valid combination, false otherwise
	 */
	private boolean validConversationParamters(int conversation_id, int language1, int language2)
	{
		String[] params = new String[]{"" + conversation_id, Math.min(language1, language2)+","+Math.max(language1, language2)};
		Cursor root = database.query("conversation_data", new String[]{"conversation_id"}, "conversation_id = ? AND supported_languages=?", params, null, null, null, "1");
		return root.moveToFirst();
	}
}
