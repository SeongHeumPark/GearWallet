package com.bladek.gearwallet.creditcard;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.bladek.gearwallet.R;
import com.bladek.gearwallet.datas.Constants;

public class CreditCardViewActivity extends Activity {
	// Debug
	private static final	String		TAG			= "CreditCardViewActivity";
	
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
	    
	    // �� �ޱ�
	    mCardName = getIntent().getExtras().getString(Constants.NAME);
	    
	    // ImageView �ʱ�ȭ
	    if (mImageView == null) {
	    	mImageView = (ImageView) findViewById(R.id.cardImageView);
	    }
	    
	    // Bitmap �ʱ�ȭ
	    if (mBitmap == null) {
	    	mBitmap = BitmapFactory.decodeFile(Constants.CREDIT_CARD_PATH + "/" + mCardName);
	    	mImageView.setImageBitmap(mBitmap);
	    }
	}
	
	@Override
	protected void onDestroy() {
		Log.d(TAG, "onDestroy()");
		
		super.onDestroy();
		
		// Bitmap ����
		if (mBitmap != null) {
			mBitmap = null;
		}
		
		// ImageView ����
		if (mImageView != null) {
			mImageView.setImageBitmap(null);
			mImageView = null;
		}
	}
}