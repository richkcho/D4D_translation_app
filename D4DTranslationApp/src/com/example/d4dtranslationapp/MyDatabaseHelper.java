package com.example.d4dtranslationapp;

import android.database.sqlite.*;
import android.content.*;
import android.util.*;

public class MyDatabaseHelper extends SQLiteOpenHelper {

	private static String DB_NAME = "conversationdatabase";
	private static int DB_VERSION = 1;
	
	// the tables in the database
	private static String CREATE_TABLES = 
			"CREATE TABLE statements(statement_id INTEGER PRIMARY KEY,parent_statement_id INTEGER,conversation_id INTEGER,translation_id INTEGER);" + 
			"CREATE TABLE translations(translation_id INTEGER PRIMARY KEY,translation TEXT);" +
			"CREATE TABLE conversation_data(conversation_id INTEGER PRIMARY KEY,supported_languages TEXT,category INTEGER,description INTEGER);";
	
	private static String DELETE_TABLES = 
			"DROP TABLE IF EXISTS statements, translations, conversation_data;";
	
	// test information for the database
	private static String make_test_info = 
		"INSERT INTO statements VALUES(1,-1,1,1);" +
		"INSERT INTO statements VALUES(2,1,1,2);" +
		"INSERT INTO statements VALUES(3,1,1,3);" +
		"INSERT INTO statements VALUES(4,1,1,4);" +
		"INSERT INTO statements VALUES(5,2,1,2);" +
		"INSERT INTO statements VALUES(6,2,1,5);" +
		"INSERT INTO statements VALUES(7,3,1,6);" +
		"INSERT INTO statements VALUES(8,3,1,2);" +
		"INSERT INTO translations VALUES(1,'1 Hi / 2 Hola / 3 salut');" +
		"INSERT INTO translations VALUES(2,'1 Bye / 2 Adios');" +
		"INSERT INTO translations VALUES(3,'1 Hello / 2 Hola');" +
		"INSERT INTO translations VALUES(4,'1 <no comment> / 2 <no comment>');" +
		"INSERT INTO translations VALUES(5,'1 How are you doing? / 2 ¿Cómo está');" +
		"INSERT INTO translations VALUES(6,'1 Nice to meet you / 2 Encantada de conocerte');" +
		"INSERT INTO conversation_data VALUES(1,'1,2',1,3);";
		
		
	
	public MyDatabaseHelper(Context context)
	{
		super (context, DB_NAME, null, DB_VERSION);
	}
	
	/* Runs iff database does not already exist
	 * (non-Javadoc)
	 * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
	 */
	@Override
	public void onCreate(SQLiteDatabase database) {
		// trim out make_test_info for production release
		executeBatchSQL(database, CREATE_TABLES);
		executeBatchSQL(database, make_test_info);
	}

	@Override
	public void onUpgrade(SQLiteDatabase database, int oldversion, int newversion) {
		Log.w(MyDatabaseHelper.class.getName(), "Upgrading from version" + oldversion + " to " + newversion + ". This will wipe all old data.");
		database.execSQL(DELETE_TABLES);
		executeBatchSQL(database, CREATE_TABLES);
	}
	/* Executes SQL statements in bulk. (buy one get n-1 free)
	 * Separates the queries by semicolons
	 */
	private void executeBatchSQL(SQLiteDatabase database, String sql)
	{
		String[] queries = sql.split(";");
		
		for(String query : queries)
		{
			database.execSQL(query);
		}
	}

}
