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

public class ConversationDataListActivity extends Activity {

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
		int catid;

		public GetDataTask(String userlangin, String lang2, int catidin)
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

			for(int temp = 0; temp < 999; temp++)
			{
				publishProgress();
			}

			if(db != null)
			{
				System.out.println(db.getConversationData(catid,cparams));
				for(ConversationData temp : db.getConversationData(catid, cparams))
				{
					if(!values.contains(temp))
					{
						values.add(temp.toString());
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

			progress.dismiss();
		}

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		db = new LocalDatabase(this);

		progress = new ProgressDialog(this);
		progress.setMessage("Fetching Stuff");
		progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progress.setIndeterminate(true);

		// "fetch" data
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
		
		Intent intent = getIntent();
	    int catid = intent.getIntExtra(MainActivity.CATEGORY_ID, -1);
	    System.out.println("Recieved Category ID: " + catid);

		new GetDataTask(pref.getString("user_language", "1"), pref.getString("target_language", "2"), catid).execute(null, null, null);

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