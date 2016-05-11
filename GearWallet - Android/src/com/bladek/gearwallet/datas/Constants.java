package com.bladek.gearwallet.datas;

import android.os.Environment;

public class Constants {
	// Page Count
	public static final int 		PAGE_COUNT 			= 2;
	
	// Page Number
	public static final int 		POINT 				= 0;
	public static final int 		CREDIT 				= 1;
	
	// Transfer Data Field
	public static final String 		NAME 				= "card_name";
	public static final String 		POSITION 			= "position";
	public static final String 		CARD_INFO 			= "card_info";
	public static final String 		TABLE_NUMBER 		= "table_number";
	
	// DataBase
	public static final String[]	TABLE_NAMES 		= {"point_card", "credit_card"};
	public static final String 		COL_NUMBER 			= "number";
	public static final String 		COL_NAME 			= "name";
	public static final String 		COL_COLOR 			= "color";
	public static final String 		COL_FREQ 			= "freq";
	public static final String[] 	COLUMS 				= {COL_NUMBER, COL_NAME, COL_COLOR, COL_FREQ};
	
	// Database Commands
	public static final int 		INSERT 				= 100;
	public static final int 		UPDATE 				= 101;
	public static final int 		DELETE 				= 102;
	public static final int 		DISPLAY 			= 103;
	
	// Intent Key
	public static final int 		REQUEST_CODE 		= 200;
	public static final int 		RESULT_CODE_POINT	= 201;
	public static final int 		RESULT_CODE_CREDIT	= 202;
		
	// File Paths
	public static final String 		ROOT_PATH			= Environment.getExternalStorageDirectory() + "/gearwallet";
	public static final String 		POINT_CARD_PATH 	= Environment.getExternalStorageDirectory() + "/gearwallet/pointcard";
	public static final String 		CREDIT_CARD_PATH 	= Environment.getExternalStorageDirectory() + "/gearwallet/creditcard";
	public static final String 		POINT_SLOT_PATH 	= Environment.getExternalStorageDirectory() + "/gearwallet/pointcard/slot";
	public static final String 		CREDIT_SLOT_PATH 	= Environment.getExternalStorageDirectory() + "/gearwallet/creditcard/slot";
}

// End of Constants