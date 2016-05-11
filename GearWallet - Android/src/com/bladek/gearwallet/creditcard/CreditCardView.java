package com.bladek.gearwallet.creditcard;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;

import com.bladek.gearwallet.R;

public class CreditCardView extends View {
	// Debug
	private static final	String	TAG	= "CreditCardView";
	
	// Paint
	private					Paint	mPaint		= null;
	
	// Bitmap
	private					Bitmap	mBackground	= null;
	
	// Size
	public static	 		int		WIDTH		= 0;
	public static			int		HEIGHT		= 0;
	private static			int		NAME 		= 100;
	private	static			int		NUMBER 		= 80;
	private static			int		VAILDITY	= 70;
	private static			int		CVC			= 70;
	
	// Number
	private					String	name	= "";
	private					String	number 	= "";
	private					String	year	= "";
	private					String	month	= "";
	private					String	cvc		= "";
	private					int		color	= 0;
	
	/** Constructor **/
	public CreditCardView(Context context, String name, String number, String year, String month, String cvc, int color) {
		super(context);
		
		Log.d(TAG, "Constructor()");
		
		this.name			= name.split(".png")[0];
		this.year			= year;
		this.month			= month;
		this.cvc			= cvc;
		this.color			= color;
		this.mPaint			= new Paint(Paint.ANTI_ALIAS_FLAG);
		this.mBackground	= BitmapFactory.decodeResource(context.getResources(), R.drawable.background);
		
		WIDTH = mBackground.getWidth();
		HEIGHT = mBackground.getHeight();
		
		for (int i = 0; i < 4; i++) {
			this.number += number.substring(i * 4, (i * 4) + 4 ) + ( i < 3 ? " " : "");
		}
	}
	
	/** Override Methods **/
	@Override
	protected void onDraw(Canvas canvas) {
		Log.d(TAG, "onDraw()");
		
		super.onDraw(canvas);
		
		// ī�� ��
		mPaint.setColor(this.color);
		canvas.drawRect(0, 0, WIDTH, HEIGHT, mPaint);
		
		// ī�� CVC ���
		mPaint.setColor(Color.WHITE);
		canvas.drawRect(0, HEIGHT - 250, WIDTH, HEIGHT - 170, mPaint);
		
		// ī�� �̸�
		mPaint.setColor(Color.BLACK);
		mPaint.setTextSize(NAME);
		mPaint.setTypeface(Typeface.create(Typeface.DEFAULT_BOLD, Typeface.BOLD));
		mPaint.setTextAlign(Paint.Align.CENTER);
		canvas.drawText(name, WIDTH / 2, (HEIGHT / 2) - 160, mPaint);
		
		// ī�� ��ȣ
		mPaint.setTextSize(NUMBER);
		canvas.drawText(number, WIDTH / 2, HEIGHT - 350, mPaint);
		
		// ī�� ��ȿ�Ⱓ
		mPaint.setTextSize(VAILDITY);
		String vaildity = month + " / " + year;
		canvas.drawText(vaildity, (WIDTH / 2) + 240, HEIGHT - 265, mPaint);
		
		// ī�� CVC : Test
		mPaint.setTextSize(CVC);
		canvas.drawText(cvc, (WIDTH / 2) + 285, HEIGHT - 185, mPaint);
		
		// ��� �̹����� �׸���
		canvas.drawBitmap(mBackground, 0, 0, mPaint);
		
		if(mPaint != null) {
			mPaint = null;
		}
	}
}

// End of CreditCardView