package com.bladek.gearwallet.datas;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

public class DBAdapter {
	private static final String DATABASE_NAME = "gearwalletDB.db";
	private static final int DATABASE_VERSION = 1;
	
	private Context mContext = null;
	private SQLiteOpenHelper mDbHelper = null;
	private SQLiteDatabase mConnection = null;
	
	private static DBAdapter mInstance = null;
	
	private DBAdapter() {}
	public synchronized static DBAdapter getInstance() {
		if (mInstance == null) {
			mInstance = new DBAdapter();
		}
		
		return mInstance;
	}
	
	public SQLiteDatabase getConnection() {
		return mConnection;
	}
	
	public void connect(Context context) {
		this.mContext = context;
		this.mDbHelper = new GearWalletDBHelper(mContext);
		this.mConnection = this.mDbHelper.getWritableDatabase();
	}
	
	public void close() {
		try {
			mConnection.close();
			mDbHelper.close();
		} catch (Exception e) {}
	}
	
	public boolean command(int command, int table, String name, String number, String color, int freq) {
		ContentValues values = new ContentValues();
		Cursor cursor = null;
		
		switch (command) {
			case Constants.INSERT:
				values.put(Constants.COL_NAME, name);
				values.put(Constants.COL_NUMBER, stringToMD5(number));
				values.put(Constants.COL_COLOR, color);
				values.put(Constants.COL_FREQ, freq);
				
				return mConnection.insertOrThrow(Constants.TABLE_NAMES[table], null, values) > 0;
			case Constants.UPDATE :
				int updateFreq = 0;
				cursor = mConnection.query(Constants.TABLE_NAMES[table], 
										   Constants.COLUMS, 
										   Constants.COL_NAME + "='" + name + "'", null, null, null, null);
								
				if (cursor != null && cursor.moveToFirst()) {
					updateFreq = cursor.getInt(cursor.getColumnIndex(Constants.COL_FREQ)) + freq;
					Log.d("update freqency : ", "" + updateFreq);
					Toast.makeText(mContext, "update freqency : " + name + ", " + updateFreq, Toast.LENGTH_SHORT).show();
				
					if (!cursor.isClosed()) {
						cursor.close();
					}
				}
				
				values.put(Constants.COL_FREQ, updateFreq);
				String whereClause = Constants.COL_NAME + "='" + name +"'";
				
				return mConnection.update(Constants.TABLE_NAMES[table], values, whereClause, null) > 0;
			case Constants.DELETE :
				return mConnection.delete(Constants.TABLE_NAMES[table], 
						  				  Constants.COL_NAME + "='" + name + "'", null) > 0;
			case Constants.DISPLAY :
				cursor = mConnection.query(Constants.TABLE_NAMES[table], Constants.COLUMS, null, null, null, null, null);
				
				if (cursor.getCount() == 0) {
					Log.e("Cursor : ", "count is 0");
					return false;
				}
				
				cursor.moveToFirst();
				int length = cursor.getCount();
				
				for (int i = 0; i < length; i++) {
					Log.d(Constants.TABLE_NAMES[table] + " : ", cursor.getString(cursor.getColumnIndex(Constants.COL_NAME)) + ", " +
													  			cursor.getString(cursor.getColumnIndex(Constants.COL_NUMBER)) + ", " +
													  			cursor.getString(cursor.getColumnIndex(Constants.COL_COLOR)) + ", " +
													  			cursor.getInt(cursor.getColumnIndex(Constants.COL_FREQ)));
					
					if (!cursor.moveToNext()) {
						break;
					}
				}
				
				cursor.close();
				
				return true;	
			default : break;
		}
		
		return false;
	}
	
	public String stringToMD5(String str) {
		String MD5 = ""; 
		
		try{
			MessageDigest md = MessageDigest.getInstance("MD5"); 
			md.update(str.getBytes());
			
			byte byteData[] = md.digest();
			
			StringBuffer sb = new StringBuffer(); 
			for(int i = 0 ; i < byteData.length ; i++) {
				sb.append(Integer.toString((byteData[i]&0xff) + 0x100, 16).substring(1));
			}
			
			MD5 = sb.toString();
			
		} catch(NoSuchAlgorithmException nsae) {
			nsae.printStackTrace();
			MD5 = null; 
		}
		
		return MD5;
	}
	
	private class GearWalletDBHelper extends SQLiteOpenHelper {
		public GearWalletDBHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}
		
		@Override
		public void onCreate(SQLiteDatabase db) {
			try {
				for (int i = 0; i < 2; i++) {
					db.execSQL("CREATE TABLE " + Constants.TABLE_NAMES[i] + " (" + Constants.COL_NAME + " CHAR(10) PRIMARY KEY, " + 
																				   Constants.COL_NUMBER + " CHAR(20), " +
																				   Constants.COL_COLOR + " CHAR(15), " +
																				   Constants.COL_FREQ + " INTEGER)");
				}
			} catch (Exception e) {}
		}
		
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			try {
				for (int i = 0; i < 2; i++) {
					db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_NAMES[i]);
				}
				
				onCreate(db);
			} catch (Exception e) {}
		}
	}
}
