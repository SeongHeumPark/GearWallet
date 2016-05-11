package com.bladek.gearwallet.services;

import java.io.File;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;
import android.util.Log;

public class MediaScanner implements MediaScannerConnectionClient {
	// Debug
	private static final	String					TAG		= "Media Scanner";
	
	// Media Scanner Connection
	private 				MediaScannerConnection 	mMSC	= null;
	
	// File
	private 				File 					mFile	= null;

	/** Constructor **/
	public MediaScanner(Context context, File f) {
		Log.d(TAG, "Constructor()");
		
		mFile = f;
		mMSC = new MediaScannerConnection(context, this);
		mMSC.connect();
	}
	
	/** Override Method **/
	@Override
	public void onMediaScannerConnected() {
		Log.d(TAG, "onMediaScannerConnected()");
		Log.e(TAG, mFile.getAbsolutePath());
		
		if (mMSC.isConnected()) {
			mMSC.scanFile(mFile.getAbsolutePath(), null);
		}
	}

	@Override
	public void onScanCompleted(String path, Uri uri) {
		Log.d(TAG, "onScanCompleted()");
		
		mMSC.disconnect();
	}
}

// End of MediaSanner