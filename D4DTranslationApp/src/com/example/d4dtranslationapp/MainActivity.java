package com.example.d4dtranslationapp;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// R's stuff, testing
		ConversationDatabase db = new MyDatabase(this); 
		Conversation c = db.getConversation(1, 1, 2);
		System.out.println(c);
		c.choose(1);
		System.out.println(c);
		c.stepBack();
		System.out.println(c);
		
		System.out.println(db.getConversationData(-1, null));
		// end R's stuff
	}

	// test Conversation for debugging purposes
	public Conversation makeTestConversation()
	{
		ConversationTreeNode s1 = new ConversationTreeNode(new StatementPair("Hi",1,"Hola",2));
		ConversationTreeNode s1c1 = new ConversationTreeNode(new StatementPair("Bye",1,"Adios",2), s1);
		ConversationTreeNode s1c2 = new ConversationTreeNode(new StatementPair("Hello",1,"Hola",2), s1);
		ConversationTreeNode s1c3 = new ConversationTreeNode(new StatementPair("<no comment>",1,"<no comment>",2), s1);
		
		ConversationTreeNode s1c1c1 = new ConversationTreeNode(new StatementPair("Bye",1,"Adios",2), s1c1);
		ConversationTreeNode s1c1c2 = new ConversationTreeNode(new StatementPair("How are you doing?",1,"Vete a chingarte",2), s1c1);
		
		ConversationTreeNode s1c2c1 = new ConversationTreeNode(new StatementPair("Nice to meet you",1,"Encantada de conocerte",2), s1c2);
		ConversationTreeNode s1c2c2 = new ConversationTreeNode(new StatementPair("Bye",1,"Adios",2), s1c2);
		
		s1.addChild(s1c1);
		s1.addChild(s1c2);
		s1.addChild(s1c3);
		
		s1c1.addChild(s1c1c1);
		s1c1.addChild(s1c1c2);
		
		s1c2.addChild(s1c2c1);
		s1c2.addChild(s1c2c2);
		
		ConversationTree tree = new ConversationTree(s1);
		
		return tree;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
