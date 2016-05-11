package com.bladek.gearwallet.pointcard;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;

import com.bladek.gearwallet.R;

public class PointCardSlotView extends View {
	// Debug
	private static final	String	TAG			= "PointCardSlotVeiw";
	
	// Bitmap
	private					Bitmap	mBackground	= null;
	
	// Size
	public static			int		WIDTH		= 0;
	public static			int		HEIGHT		= 0;
	
	// Paint
	private					Paint	mPaint 		= null;
	
	// Number
	private					int		mColor		 = 0;
		
	/** Constructor **/
	public PointCardSlotView(Context context, int color) {
		super(context);
		
		Log.d(TAG, "Constructor()");
		
		this.mColor 		= color;
		this.mPaint 		= new Paint(Paint.ANTI_ALIAS_FLAG);
		this.mBackground 	= BitmapFactory.decodeResource(getResources(), R.drawable.background_slot);
		
		WIDTH	= mBackground.getWidth();
		HEIGHT 	= mBackground.getHeight();
	}
	
	/** Override Method **/
	@Override
	protected void onDraw( Canvas canvas ) {
		Log.d(TAG, "onDraw()");
		
		super.onDraw(canvas);
		
		// 카드 색
		mPaint.setColor(this.mColor);
		canvas.drawRect(0, 0, WIDTH, HEIGHT, mPaint);
		
		// 배경 이미지에 그리기
		canvas.drawBitmap(mBackground, 0, 0, mPaint);
		
		if(mPaint != null) {
			mPaint = null;
		}
	}
}

// End of PointCardSlotView