package com.zohaib.test;
import java.io.File;



import java.util.ArrayList;
import java.util.logging.Level;


import com.almworks.sqlite4java.SQLiteConnection;
import com.almworks.sqlite4java.SQLiteException;
import com.almworks.sqlite4java.SQLiteStatement;

public class DatabaseFunctions {

	//
	// SQL statements
	//

	public String sep = "-";
	
	private final static String CLASS_NAME = DatabaseFunctions.class.getName();
	
	/** Database table for offers of sale */
	private final String createOfferTable = "CREATE TABLE offer ( id INTEGER PRIMARY KEY AUTOINCREMENT, user_number TEXT, year INTEGER, month INTEGER, day INTEGER, hour INTEGER, minute INTEGER, source TEXT, destination TEXT, flag INTEGER );";
	private final String insertOffer = "INSERT INTO offer (user_number, year, month, day, hour, minute, source, destination, flag) VALUES (?, ?, ?, ?, ?, ?, ?, ?, 0);";
	private final String invalidateOffer = "UPDATE offer SET flag = 1 WHERE id = ?;";
	private final String selectOffer = "SELECT * FROM offer where flag = 0 order by id" ;

	private SQLiteConnection dbConn;

	/** Prepare the database connection */
	private void initDatabase() throws Exception {
		
		String methodName = CLASS_NAME + ".initDatabase";
		
		File databaseFile = new File("C:\\Users\\zj9\\workspace\\TestSocketServer\\database_rides");


		if (!databaseFile.exists()){
			// If the database hasn't been created yet, initialize it and prepare the schema.
			

			System.out.println("db does not exist");
			SQLiteStatement st = null;

			try{
				dbConn = new SQLiteConnection(databaseFile);
				dbConn.open(true); // Open and recreate the database if it isn't yet ready

				st = dbConn.prepare(createOfferTable);     
				st.stepThrough();

			}
			catch ( SQLiteException e ){
				System.err.println(e.toString());
				throw e;
			}
			finally{
				if ( st != null ) st.dispose();
			} 
		}

		else { // Just open the database
			try{
				System.out.println("db does exist");
				dbConn = new SQLiteConnection(databaseFile);
				dbConn.open();
			}
			catch ( SQLiteException e ) {
				throw e;
			}
		}
	}

	public long insertOffer(String [] tokens) throws Exception{
		
		String methodName = CLASS_NAME + ".insertOffer()";
		
		SQLiteStatement st = null;
		
		try {
			st = dbConn.prepare(insertOffer);

			st.bind(1, tokens[1]);
			st.bind(2, tokens[2]);
			st.bind(3, tokens[3]);
			st.bind(4, tokens[4]);
			st.bind(5, tokens[5]);
			st.bind(6, tokens[6]);
			st.bind(7, tokens[7]);
			st.bind(8, tokens[8]);

			st.stepThrough();
			return dbConn.getLastInsertId();
			
		}
		catch ( SQLiteException e ) {
			throw e;
		}
		finally {
			if ( st != null ) st.dispose();
		}
	}
	
	public boolean invalidateOffer(int offerId) throws Exception {
		
		String methodName = CLASS_NAME + ".invalidateOffer()";
		System.out.println("id to invalidate is:"+offerId);
		
		SQLiteStatement st = null;

		try {
			st = dbConn.prepare(invalidateOffer);
			st.bind(1, offerId);
			st.stepThrough();
			return true;
		}
		catch ( SQLiteException e ) {
			throw e;
		}
		finally {
			if ( st != null ) st.dispose();
		}
	}
	
	public String getOfferResult()  {
		
		String methodName = CLASS_NAME + ".getOfferResult()";
		
		SQLiteStatement st = null;

		try {
			st = dbConn.prepare(selectOffer);

			String s = "";

			while ( st.step() ) {
				
				int id = st.columnInt(0);
				String num = st.columnString(1);
				int year = st.columnInt(2);
				int month = st.columnInt(3);
				int day = st.columnInt(4);
				int hour = st.columnInt(5);
				int minute = st.columnInt(6);
				String src = st.columnString(7);
				String dst = st.columnString(8);
				
				s = s + id + sep + num +sep+ year +sep + month +sep+ day +sep+ hour +sep+ minute +sep+ src +sep+ dst + ";";
			}
			return s;
		}
		catch ( SQLiteException e ) {
			e.printStackTrace();
			return null;
		}
	}

	public DatabaseFunctions() throws Exception {
		initDatabase();
	}

	public void dispose(){
		dbConn.dispose();
	}

}
