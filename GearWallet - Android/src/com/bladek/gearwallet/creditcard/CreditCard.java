package com.bladek.gearwallet.creditcard;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.Log;
import android.view.View;

import com.bladek.gearwallet.datas.Constants;
import com.bladek.gearwallet.services.MediaScanner;

public class CreditCard {
	// Debug
	private static final	String	TAG				= "CreditCard";
	
	// Files
	private					File	mCardImageFile	= null;
	private					File	mSlotImageFile	= null;
	
	/** Constructor **/	
	public CreditCard(Context context, String name, String number, String year, String month, String cvc, int color) {
		Log.d(TAG, "Constructor()");		
		
		mCardImageFile = new File(Constants.CREDIT_CARD_PATH + "/" + name);
		mSlotImageFile = new File(Constants.CREDIT_SLOT_PATH + "/" + name);
		
		CreditCardView cardView = new CreditCardView(context, name, number, year, month, cvc, color);
		Bitmap cardImage = rotateImage(cardView, 270);
		saveCardImage(cardImage);
		 
		CreditCardSlotView slotView = new CreditCardSlotView(context, color);
		saveSlotImage(slotView);
		
		// ¹Ìµð¾î ½ºÄµ
		new MediaScanner(context, mCardImageFile);
		new MediaScanner(context, mSlotImageFile);
	}
	
	/** User Define Method **/
    private Bitmap rotateImage(View view, int degrees) {
    	Log.d(TAG, "rotateImage()");
    	
    	Bitmap bitmap = Bitmap.createBitmap(CreditCardView.WIDTH, CreditCardView.HEIGHT, Config.ARGB_8888);
    	Canvas canvas = new Canvas(bitmap);
    	
    	view.draw(canvas);
    	
 		if (degrees != 0 && bitmap != null) {
 			Matrix m = new Matrix();
 			m.setRotate(degrees, (float) bitmap.getWidth() / 2, (float) bitmap.getHeight() / 2);
     
 			try {
 				Bitmap converted = Bitmap.createBitmap(bitmap, 0, 0,  bitmap.getWidth(), bitmap.getHeight(), m, true);
 				
 				if (bitmap != converted) {
 					bitmap = null;
 					bitmap = converted;
 				}
 				
 				Log.d(TAG, "Rotate Success!");	
 			} catch(OutOfMemoryError oome) {}
 		}
 		
 		return bitmap;
 	}
    
    private void saveCardImage(Bitmap bitmap) {
    	Log.d(TAG, "saveCardImage()");
		
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(mCardImageFile);
			bitmap.compress(CompressFormat.PNG, 100, fileOutputStream);
			fileOutputStream.close();
			
			Log.d(TAG, "Success Credit Card Image Saved!");
		} catch (FileNotFoundException fofe) {
			fofe.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
    
    private void saveSlotImage(View view) {
    	Log.d(TAG, "saveSlotImage()");
    	
    	Bitmap bitmap = Bitmap.createBitmap(CreditCardSlotView.WIDTH, CreditCardSlotView.HEIGHT, Config.ARGB_8888);
    	Canvas canvas = new Canvas(bitmap);
    	
    	view.draw(canvas);
		
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(mSlotImageFile);
			bitmap.compress(CompressFormat.PNG, 100, fileOutputStream);
			fileOutputStream.close();
			
			Log.d(TAG, "Success Credit Card Slot Image Saved!");
		} catch (FileNotFoundException fofe) {
			fofe.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
    }
}
