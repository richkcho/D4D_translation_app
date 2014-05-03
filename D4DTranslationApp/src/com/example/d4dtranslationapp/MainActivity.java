package com.example.d4dtranslationapp;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends Activity {
	
	// variables for load and output of conversation data
	ListView listView;
	protected ProgressDialog progress;
	protected ArrayList<String> values = new ArrayList<String>();
	protected ArrayAdapter<String> adapter;
	protected ConversationDatabase db;
	
	// class for getting data
	private class GetDataTask extends AsyncTask<Void, Void, Void> 
	{
		int userlang;
		int[] cparams;
		
		public GetDataTask(String userlangin, String lang2)
		{
			userlang = Integer.parseInt(userlangin);
			int temp = Integer.parseInt(lang2);
			
			/* Want to have them in increasing order i think. 
			 * Might not be necessary as I think checks were used but I want to be sure. 
			 * --few extra lines for safety--
			 */
			if(userlang < temp)
			{
				cparams = new int[]{userlang, temp};
			}
			else
			{
				cparams = new int[]{temp, userlang};
			}
		}
		
		@Override
		protected void onPreExecute()
		{
			progress.show();
		}

		@Override
		protected void onProgressUpdate(Void...params)
		{
			progress.incrementProgressBy(10);
		}

		/* params[0] = user language
		 * params[1] = target language
		*/
		@Override
		protected Void doInBackground(Void... params) {
			
			for(int temp = 0; temp < 9999; temp++)
			{
				publishProgress();
			}

			if(db != null)
			{
				values.add(getResources().getStringArray(R.array.all_translation_array)[userlang]);
				
				System.out.println(db.getConversationData(-1,cparams));
				for(ConversationData temp : db.getConversationData(-1, cparams))
				{
					if(!values.contains(temp))
					{
						values.add(temp.getCategoryString(userlang));
					}
				}
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result)
		{
			if( adapter != null)
			{
				adapter.notifyDataSetChanged();
			}

			progress.hide();
		}

	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// check first run, if so, route them to settings menu
		checkFirstRun();
		
		db = new LocalDatabase(this);
		
		progress = new ProgressDialog(this);
		progress.setMessage("Fetching Stuff");
		progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progress.setIndeterminate(true);
		
		// "fetch" data
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
		System.out.println(pref.getString("user_language", "-1"));
		
		new GetDataTask(pref.getString("user_language", "1"), pref.getString("target_language", "2")).execute(null, null, null);

		System.out.println(values);
		System.out.println(db.getConversation(1,1,2));
		
		// Get ListView object from xml
		listView = (ListView) findViewById(R.id.list);

		// Define a new Adapter
		// First parameter - Context
		// Second parameter - Layout for the row
		// Third parameter - ID of the TextView to which the data is written
		// Forth - the Array of data

		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, android.R.id.text1, values);


		// Assign adapter to ListView
		listView.setAdapter(adapter); 
		
		// R's stuff, testing'
		// System.out.println("Creating HttpClient...");
		// this needs to be encapsulated as a Task
		// ServerDatabase slolol = new ServerDatabase("http://140.247.71.97/D4D/");
		// System.out.println("Stuff gotten from server: " + slolol.getConversation(1, 1, 2));
		// end R's stuff
	}
	
	// handles first run of app
	private void checkFirstRun()
	{	
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
		boolean firstrun = pref.getBoolean("first run", true);
		
		System.out.println("IS FIRST RUN: " + firstrun);
		
		SharedPreferences.Editor editor = pref.edit();
		editor.putBoolean("first run", false);
		editor.commit();
		
		if(firstrun)
		{
			openSettings();
		}
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
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.action_settings:
	            openSettings();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	private void openSettings()
	{
		startActivity(new Intent(MainActivity.this,
				SettingsActivity.class));
	}

}
