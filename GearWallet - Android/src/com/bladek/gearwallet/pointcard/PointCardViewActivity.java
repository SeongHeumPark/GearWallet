package com.bladek.gearwallet.pointcard;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.bladek.gearwallet.R;
import com.bladek.gearwallet.datas.Constants;

public class PointCardViewActivity extends Activity {
	// Debug
	private static final	String		TAG			= "PointCardViewActivity";
	
	// ImageView
	private					ImageView 	mImageView	= null;
	
	// Bitmap
	private					Bitmap		mBitmap		= null;
	
	// Card Name
	private					String		mCardName	= "";

	/** Override Methods **/
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "onCreate()");
		
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_card_view);
	    
	    // 값 받기
	    mCardName = getIntent().getExtras().getString(Constants.NAME);
	    
	    // ImageView 초기화
	    if (mImageView == null) {
	    	mImageView = (ImageView) findViewById(R.id.cardImageView);
	    }
	    
	    // Bitmap 초기화
	    if (mBitmap == null) {
	    	mBitmap = BitmapFactory.decodeFile(Constants.POINT_CARD_PATH + "/" + mCardName);
	    	mImageView.setImageBitmap(mBitmap);
	    }
	}
	
	@Override
	protected void onDestroy() {
		Log.d(TAG, "onDestroy()");
		
		super.onDestroy();
		
		// Bitmap 해제
		if (mBitmap != null) {
			mBitmap = null;
		}
		
		// ImageView 해제
		if (mImageView != null) {
			mImageView.setImageBitmap(null);
			mImageView = null;
		}
	}
}

// End of PointCardViewActivity