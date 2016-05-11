package com.bladek.gearwallet.pointcard;

import java.util.EnumMap;
import java.util.Map;

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
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

public class PointCardView extends View {
	// Debug
	private static final	String	TAG	= "PointCardView";
	
	// Paint
	private					Paint	mPaint		= null;
	
	// Bitmap
	private					Bitmap	mBackground	= null;
	
	// Color
	private static final	int		WHITE	= 0xFFFFFFFF;
	private static final	int		BLACK	= 0xFF000000;
	
	// Size
	public static	 		int		WIDTH		= 0;
	public static			int		HEIGHT		= 0;
	private static			int		NAME 		= 100;
	private	static			int		NUMBER 		= 70;
	
	// Number
	private					String	name	= "";
	private					String	code	= "";
	private					String	number 	= "";
	private					int		color	= 0;
	
	/** Constructor **/
	public PointCardView(Context context, String name, String number, int color) {
		super(context);
		
		Log.d(TAG, "Constructor()");
		
		this.name			= name.split(".png")[0];
		this.code			= number;
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
		
		// 카드 색
		mPaint.setColor(this.color);
		canvas.drawRect(0, 0, WIDTH, HEIGHT, mPaint);
		
		// 카드 이름
		mPaint.setColor(Color.BLACK);
		mPaint.setTextSize(NAME);
		mPaint.setTypeface(Typeface.create(Typeface.DEFAULT_BOLD, Typeface.BOLD));
		mPaint.setTextAlign(Paint.Align.CENTER);
		canvas.drawText(name, WIDTH / 2, (HEIGHT / 2) - 160, mPaint);
		
		// Barcode 그리기
		Bitmap bitmap = encodeBitamp(code, BarcodeFormat.CODE_128, WIDTH - 200, (HEIGHT / 2) - 100);
		canvas.drawBitmap(bitmap, 100, (HEIGHT / 2) - 90, mPaint);
		
		// 카드 번호
		mPaint.setTextSize(NUMBER);
		canvas.drawText(number, WIDTH / 2, HEIGHT - 110, mPaint);
		
		// 배경 이미지에 그리기
		canvas.drawBitmap(mBackground, 0, 0, mPaint);
		
		if(mPaint != null) {
			mPaint = null;
		}
	}
	
	/** User Define Methods **/
	private Bitmap encodeBitamp(String contents, BarcodeFormat format, int width, int height) {
    	Log.d(TAG, "encodeBitmap()");
    	
    	String contentsToEncode = contents;
    	
    	if (contentsToEncode == null) {
    		return null;
    	}
    	
    	Map<EncodeHintType, Object> hints = null;
    	String encoding = guessAppropriateEncoding(contentsToEncode);
    	
        if (encoding != null) {
            hints = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
            hints.put(EncodeHintType.CHARACTER_SET, encoding);
        }
        
        MultiFormatWriter writer = new MultiFormatWriter();
        BitMatrix result = null;
        
        try {
            try {
				result = writer.encode(contentsToEncode, format, width, height, hints);
			} catch (WriterException we) {
				we.printStackTrace();
			}
        } catch (IllegalArgumentException iae) {
        	iae.printStackTrace();
            return null;
        }
        
        int img_width = result.getWidth();
        int img_height = result.getHeight();
        int[] pixels = new int[img_width * img_height];
        
        for (int y = 0; y < img_height; y++) {
            int offset = y * img_width;
            
            for (int x = 0; x < img_width; x++) {
            	pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(img_width, img_height, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, img_width, 0, 0, img_width, img_height);
        
        return bitmap;
    }
    
    private String guessAppropriateEncoding(CharSequence contents) {
    	Log.d(TAG, "guessAppropriateEncoding()");
    	
        for (int i = 0; i < contents.length(); i++) {
            if (contents.charAt(i) > 0xFF) {
            	return "UTF-8";
            }
        }
        
        return null;
    }
}

// End of PointCardView