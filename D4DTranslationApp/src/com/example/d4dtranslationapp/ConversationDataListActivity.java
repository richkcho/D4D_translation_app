package com.example.d4dtranslationapp;

import java.util.ArrayList;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.support.v4.app.NavUtils;

public class ConversationDataListActivity extends Activity {

	
	// variables for load and output of conversation data
		ListView listView;
		protected ProgressDialog progress;
		protected ArrayList<ConversationData> values = new ArrayList<ConversationData>();
		protected ArrayAdapter<ConversationData> adapter;
		protected ConversationDatabase db;
		
		// class for getting data
		private class GetDataTask extends AsyncTask<Void, Void, Void> 
		{
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

			@Override
			protected Void doInBackground(Void... params) {

				for(int temp = 0; temp < 9999; temp++)
				{
					publishProgress();
				}

				if(db != null)
				{
					System.out.println(db.getConversationData(-1,null));
					for(ConversationData temp : db.getConversationData(-1, null))
					{
						values.add(temp);
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
			
			db = new LocalDatabase(this);
			
			progress = new ProgressDialog(this);
			progress.setMessage("Fetching Stuff");
			progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progress.setIndeterminate(true);

			// "fetch" data
			new GetDataTask().execute(null, null, null);

			System.out.println(values);
			System.out.println(db.getConversation(1,1,2));
			
			// Get ListView object from xml
			listView = (ListView) findViewById(R.id.list);

			// Define a new Adapter
			// First parameter - Context
			// Second parameter - Layout for the row
			// Third parameter - ID of the TextView to which the data is written
			// Forth - the Array of data

			adapter = new ArrayAdapter<ConversationData>(this,
					android.R.layout.simple_list_item_1, android.R.id.text1, values);
		}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.conversation_data_list, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
