package com.example.d4dtranslationapp;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;

public class ConversationDisplayActivity extends Activity {
	
	protected ProgressDialog progress;
	
	private class SaveConversationTask extends AsyncTask<Void, Void, Void> 
	{
		private Conversation conv;
		private ConversationData convdata;
		private LocalDatabaseHelper dbhelper;
		private SQLiteDatabase database;
		
		public SaveConversationTask(Context context, Conversation convin, ConversationData convdatain)
		{
			conv = convin;
			convdata = convdatain;
			dbhelper = new LocalDatabaseHelper(context);
			database = dbhelper.getWritableDatabase();
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

			int convdataid = database.query("conversation_data", null, null, null, null, null, null).getCount() + 1;
			int translationcount = database.query("translations", null, null, null, null, null, null).getCount() + 1;
			
			ContentValues convdatacv = new ContentValues();
			convdatacv.put("conversation_id", convdataid);
			convdatacv.put("supported_languages", convdataid);
			convdatacv.put("conversation_id", convdataid);
			convdatacv.put("description", convdataid);
			
			database.insert("conversation_data", null, convdatacv);

			// ABORT ABORT ABORT INCOMPLETE
			
			database.close();
			
			return null;
		}

		@Override
		protected void onPostExecute(Void result)
		{
			progress.dismiss();
		}

	}
	
	private class LanguageSwap {

		int userLanguage;
		int targetLanguage;

		public LanguageSwap(int startUserLang, int startTargetLang) {
			userLanguage = startUserLang;
			targetLanguage = startTargetLang;
		}

		public int swap(int currentLanguage) {
			if (currentLanguage == userLanguage){
				return targetLanguage;
			} else {
				return userLanguage;
			}
		}
	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_conversation_display);

		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
		
		// ... A LOT OF STUFF HERE

		Intent intent = getIntent();
		int userLang = Integer.parseInt(pref.getString("user_language", "1")); 
		int targetLang = Integer.parseInt(pref.getString("target_language", "2")); 
		Conversation conv = new ConversationTree(intent.getStringExtra(ConversationDataListActivity.CONVERSATION));

		System.out.println("Recieved Conversation: " + conv.toString());
		
		// After selecting a convo, display the options in your "target language"
		LanguageSwap startLangSwap = new LanguageSwap(userLang, targetLang);
		populateButtons(conv, targetLang, startLangSwap);

	}


	private void populateButtons(final Conversation currentConversation, final int language, 
			final LanguageSwap langSwap) {

		// Swap languages for each click
		final int otherLanguage = langSwap.swap(language);

		ArrayList<String> current = currentConversation.responses(language);
		int size = current.size();

		// Set up the table for all the buttons
		final TableLayout table = (TableLayout) findViewById(R.id.buttonTable);

		// This displays the current statement as well as the back button
		TableRow tableRowBack = new TableRow(this);
		table.addView(tableRowBack);
		Button buttonBack = new Button(this);
		tableRowBack.addView(buttonBack);
		buttonBack.setText("Current: "+currentConversation.currentStatementString(language));

		buttonBack.setOnClickListener(new View.OnClickListener() {
			public void onClick(final View v) {
				Button pressedButton = (Button)v; 
				String buttonText = pressedButton.getText().toString();
				// Check to see if we're at the head - if not, move back
				boolean isBack = currentConversation.stepBack();
				if (isBack == true){
					table.removeAllViews();
					populateButtons(currentConversation, otherLanguage, langSwap);
				}
			} 
		});

		// Now output the available choices
		for (int row = 0; row < size; row++) {
			TableRow tableRow = new TableRow(this);
			table.addView(tableRow);
			Button button = new Button(this);

			// Below lines are for optional formatting 
			// Stretches buttons to fill up entire screen
			/*   tableRow.setLayoutParams(new TableLayout.LayoutParams(
		     TableLayout.LayoutParams.MATCH_PARENT,
		     TableLayout.LayoutParams.MATCH_PARENT,
		     1.0f
		    ));
		   button.setLayoutParams(new TableRow.LayoutParams(
		     TableRow.LayoutParams.MATCH_PARENT,
		     TableRow.LayoutParams.MATCH_PARENT,
		     1.0f
		    ));
			 */

			// Add buttons with correct text and ID
			tableRow.addView(button);
			button.setText(current.get(row).toString());
			button.setId(row);

			// Gets chosen button index, traverse convo, display results
			button.setOnClickListener(new View.OnClickListener() {
				public void onClick(final View v) {
					Button pressedButton = (Button)v; 
					int buttonID = pressedButton.getId();
					boolean isChoice = currentConversation.choose(buttonID); 
					if (isChoice == true){
						table.removeAllViews();
						populateButtons(currentConversation, otherLanguage, langSwap); 
					}
				} 
			});   
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.conversation_display, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.action_settings:
	            System.out.println("JAJAJAJAJAJAJAJAJAAJAJAJAJAJAJA");
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

}
