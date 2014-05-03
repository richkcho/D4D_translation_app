package com.example.d4dtranslationapp;

import android.database.sqlite.*;
import android.content.*;
import android.util.*;

public class LocalDatabaseHelper extends SQLiteOpenHelper {

	private static String DB_NAME = "conversationdatabase";
	private static int DB_VERSION = 1;
	
	// the tables in the database
	private static String CREATE_TABLES = 
		"CREATE TABLE statements(statement_id INTEGER PRIMARY KEY,parent_statement_id INTEGER,conversation_id INTEGER,translation_id INTEGER);" + 
		"CREATE TABLE translations(translation_id INTEGER PRIMARY KEY,translation TEXT);" +
		"CREATE TABLE conversation_data(conversation_id INTEGER PRIMARY KEY,supported_languages TEXT,category INTEGER,description INTEGER);";
	
	private static String DELETE_TABLES = 
		"DROP TABLE IF EXISTS statements, translations, conversation_data, languages;";
	
	// test information for the database
	private static String make_test_info = 
		"INSERT INTO statements VALUES(1,-1,1,1);" +
		"INSERT INTO statements VALUES(2,-1,2,2);" +
		"INSERT INTO statements VALUES(3,-1,3,3);" +
		"INSERT INTO statements VALUES(4,1,1,1);" +
		"INSERT INTO statements VALUES(5,1,1,2);" +
		"INSERT INTO statements VALUES(6,1,1,3);" +
		"INSERT INTO statements VALUES(7,1,2,1);" +
		"INSERT INTO statements VALUES(8,1,2,2);" +
		"INSERT INTO statements VALUES(9,1,2,3);" +
		"INSERT INTO statements VALUES(10,1,3,1);" +
		"INSERT INTO statements VALUES(11,1,3,2);" +
		"INSERT INTO statements VALUES(12,1,3,3);" +
		"INSERT INTO statements VALUES(13,4,1,4);" +
		"INSERT INTO statements VALUES(14,4,1,5);" +
		"INSERT INTO statements VALUES(15,4,1,6);" +
		"INSERT INTO statements VALUES(16,13,1,7);" +
		"INSERT INTO statements VALUES(17,13,1,8);" +
		"INSERT INTO statements VALUES(18,13,1,9);" +
		"INSERT INTO statements VALUES(19,16,1,10);" +
		"INSERT INTO statements VALUES(20,16,1,11);" +
		"INSERT INTO statements VALUES(21,16,1,12);" +
		"INSERT INTO statements VALUES(22,17,1,10);" +
		"INSERT INTO statements VALUES(23,17,1,12);" +
		"INSERT INTO statements VALUES(24,18,1,11);" +
		"INSERT INTO statements VALUES(25,14,1,7);" +
		"INSERT INTO statements VALUES(26,14,1,8);" +
		"INSERT INTO statements VALUES(27,14,1,9);" +
		"INSERT INTO statements VALUES(28,15,1,8);" +
		"INSERT INTO statements VALUES(29,15,1,8);" +
		"INSERT INTO statements VALUES(30,5,1,4);" +
		"INSERT INTO statements VALUES(31,5,1,5);" +
		"INSERT INTO statements VALUES(32,5,1,7);" +
		"INSERT INTO statements VALUES(33,6,1,6);" +
		"INSERT INTO statements VALUES(34,7,2,6);" +
		"INSERT INTO statements VALUES(35,7,2,4);" +
		"INSERT INTO statements VALUES(36,34,2,7);" +
		"INSERT INTO statements VALUES(37,34,2,7);" +
		"INSERT INTO statements VALUES(38,34,2,7);" +
		"INSERT INTO statements VALUES(39,35,2,9);" +
		"INSERT INTO statements VALUES(40,38,2,11);" +
		"INSERT INTO statements VALUES(41,38,2,12);" +
		"INSERT INTO statements VALUES(42,8,2,5);" +
		"INSERT INTO statements VALUES(43,42,2,7);" +
		"INSERT INTO statements VALUES(44,42,2,9);" +
		"INSERT INTO statements VALUES(45,444,2,11);" +
		"INSERT INTO statements VALUES(46,44,2,10);" +
		"INSERT INTO statements VALUES(47,44,2,12);" +
		"INSERT INTO statements VALUES(48,9,2,6);" +
		"INSERT INTO statements VALUES(49,9,2,6);" +
		"INSERT INTO statements VALUES(50,49,2,8);" +
		"INSERT INTO statements VALUES(51,10,3,4);" +
		"INSERT INTO statements VALUES(52,10,3,5);" +
		"INSERT INTO statements VALUES(53,10,3,6);" +
		"INSERT INTO statements VALUES(54,10,3,9);" +
		"INSERT INTO statements VALUES(55,11,3,4);" +
		"INSERT INTO statements VALUES(56,12,3,4);" +
		"INSERT INTO statements VALUES(57,12,3,4);" +
		"INSERT INTO statements VALUES(58,56,3,9);" +
		"INSERT INTO statements VALUES(59,57,3,8);" +
		"INSERT INTO statements VALUES(60,57,3,7);" +
		"INSERT INTO statements VALUES(61,60,3,11);" +
		"INSERT INTO statements VALUES(62,60,3,11);" +
		"INSERT INTO statements VALUES(63,60,3,11);" +
		"INSERT INTO translations VALUES(1,'1 Hello / 2 Hola / 3 Bonjour');" +
		"INSERT INTO translations VALUES(2,'1 Good morning / 2 Buenos dias / 3 Bonjour');" +
		"INSERT INTO translations VALUES(3,'1 What''s up? / 2 ¿Que pasa? / 3 Ça va?');" +
		"INSERT INTO translations VALUES(4,'1 How are you? / 2 ¿Cómo estás? / 3  Comment allez-vous?');" +
		"INSERT INTO translations VALUES(5,'1 How''s it going? / 2 Como te va? / 3  Comment ça va?');" +
		"INSERT INTO translations VALUES(6,'1 Are you doing well? / 2 ¿Estás bien? / 3 Ça va bien?');" +
		"INSERT INTO translations VALUES(7,'1 I''m good! You? / 2 Bien! ¿Y tú? / 3  Bien, et vous?');" +
		"INSERT INTO translations VALUES(8,'1 Life sucks. You? / 2 Mi vida está muy mal. ¿Y tu? / 3 Je fais mal, et vous?');" +
		"INSERT INTO translations VALUES(9,'1 I don''t want to talk about it. You? / 2 No me preguntas. ¿Y tú? / 3 Je ne veux pas en parler, et vous?');" +
		"INSERT INTO translations VALUES(10,'1 Same / 2 A mi también / 3 Moi aussi');" +
		"INSERT INTO translations VALUES(11,'1 I''m fine / 2 Asi asi / 3 Pas mal');" +
		"INSERT INTO translations VALUES(12,'1 I''m doing well / 2 Estoy bien / 3 Ça va bien');" +
		"INSERT INTO translations VALUES(13,'1 Ask people how they are doing / 2 No bueno / 3 Haaaaalp');" +
		"INSERT INTO translations VALUES(14,'1 THIS IS A TEST THE CAKE A LIE / 2 Tengo pastel / 3 Frenchmen make cake');" + 
		"INSERT INTO conversation_data VALUES(1,'1,2,3',1,13);" +
		"INSERT INTO conversation_data VALUES(2,'1,2,3',2,13);" +
		"INSERT INTO conversation_data VALUES(3,'1,2,3',3,14);";
		
		
	
	public LocalDatabaseHelper(Context context)
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
		Log.w(LocalDatabaseHelper.class.getName(), "Upgrading from version" + oldversion + " to " + newversion + ". This will wipe all old data.");
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
