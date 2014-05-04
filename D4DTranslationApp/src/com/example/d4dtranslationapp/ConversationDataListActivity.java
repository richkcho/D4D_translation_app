package com.example.d4dtranslationapp;

import java.util.ArrayList;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class ConversationDataListActivity extends Activity {
	
	public final static String CONVERSATION = "com.example.d4dtranslationapp.CONV";

	// variables for load and output of conversation data
	ListView listView;
	protected ProgressDialog progress;
	protected ArrayList<String> values = new ArrayList<String>();
	protected ArrayList<Conversation> conversations = new ArrayList<Conversation>();
	protected ArrayAdapter<String> adapter;
	protected ConversationDatabase db;

	// class for getting data
	private class GetDataTask extends AsyncTask<Void, Void, Void> 
	{
		int userlang;
		int targetlang;
		int[] cparams;
		int catid;

		public GetDataTask(String userlangin, String lang2, int catidin)
		{
			userlang = Integer.parseInt(userlangin);
			targetlang = Integer.parseInt(lang2);

			/* Want to have them in increasing order i think. 
			 * Might not be necessary as I think checks were used but I want to be sure. 
			 * --few extra lines for safety--
			 */
			if(userlang < targetlang)
			{
				cparams = new int[]{userlang, targetlang};
			}
			else
			{
				cparams = new int[]{targetlang, userlang};
			}
			
			catid = catidin;
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

			if(db != null)
			{
				for(ConversationData temp : db.getConversationData(catid, cparams))
				{
					String stemp = temp.toString();
					stemp = stemp.substring(stemp.indexOf("Description: ") + 13);
					String[] descriptions = stemp.split(" / ");
					
					for(String s : descriptions)
					{
						if(Integer.parseInt(s.substring(0,1)) == userlang)
						{
							values.add(s.substring(2));
						}
					}
					
					conversations.add(db.getConversation(temp.getConversationID(), cparams[0], cparams[1]));
					
					publishProgress();
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

			progress.dismiss();
		}

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
		
		if(pref.getBoolean("use_local_db", true))
		{
			System.out.println("Using local database");
			db = new LocalDatabase(this);
		}
		else
		{
			System.out.println("Using Server Database");
			db = new ServerDatabase(MainActivity.SERVER_URL);
		}

		progress = new ProgressDialog(this);
		progress.setMessage("Fetching Stuff");
		progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progress.setIndeterminate(true);

		// "fetch" data
		Intent intent = getIntent();
	    int catid = intent.getIntExtra(MainActivity.CATEGORY_ID, -1);
	    System.out.println("Recieved Category ID: " + catid);

		new GetDataTask(pref.getString("user_language", "1"), pref.getString("target_language", "2"), catid).execute(null, null, null);

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
		
		// Assign an onClick listener
		 listView.setOnItemClickListener(new OnItemClickListener() {
			 
             @Override
             public void onItemClick(AdapterView<?> parent, View view,
                int position, long id) {

               Intent intent = new Intent(ConversationDataListActivity.this, ConversationDisplayActivity.class); // provide intent context and class to deliver intent to
               intent.putExtra(CONVERSATION, conversations.get(position).toString());
               
               System.out.println("Conversation Sent: " + conversations.get(position).toString());
               
               startActivity(intent);
            
             }

        }); 
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
		startActivity(new Intent(ConversationDataListActivity.this,
				SettingsActivity.class));
	}

}