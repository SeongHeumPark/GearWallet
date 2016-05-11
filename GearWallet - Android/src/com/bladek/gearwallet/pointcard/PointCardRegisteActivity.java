package com.bladek.gearwallet.pointcard;

import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bladek.gearwallet.R;
import com.bladek.gearwallet.colorpicker.ColorPickerDialog;
import com.bladek.gearwallet.colorpicker.ColorPickerDialog.OnColorSelectedListener;
import com.bladek.gearwallet.datas.CardInfo;
import com.bladek.gearwallet.datas.Constants;
import com.bladek.gearwallet.datas.DBAdapter;

public class PointCardRegisteActivity extends Activity {
	// Debug
	private static final	String			TAG				= "Register Point Card";
	
	// Edit Text
	private					EditText		mCardName		= null;
	private					EditText		mCardNumber		= null;
	
	// Button
	private					Button			mRegisterButton	= null;
	
	// Image View
	private					ImageView		mImageView		= null;
	
	// Database Adapter
	private					DBAdapter		mDB				= null;
	
	// Input Filter
	private					InputFilter		mInputFilter	= new InputFilter() {
		@Override
		public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
			Pattern pattern = Pattern.compile(getResources().getString(R.string.limited_character));
			
			if (source.length() > 0 && !pattern.matcher(source).matches()) {
				// Only English Toast
				Toast.makeText(getApplicationContext(), R.string.special_character_error, Toast.LENGTH_LONG).show();
				
				return "";
			}
			
			return null;
		}
	};
	
	// Point 카드 속성
	private					String			name			= "";
	private					String			number			= "";
	private					int				color			= Color.RED;
	private					String			hexColor		= "";
	
	/** Override Method **/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "onCreate()");
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pointcard_registe);
		
		name = "";
		number = "";
		color = Color.RED;
		hexColor = String.format("#%06X", (0xFFFFFF & this.color));
		
		mDB = DBAdapter.getInstance();
		
		mImageView 		= (ImageView) 	findViewById(R.id.preview);
		mRegisterButton = (Button) 		findViewById(R.id.registerButton);
		mCardName 		= (EditText) 	findViewById(R.id.inputCardNameEditText);
		mCardNumber 	= (EditText) 	findViewById(R.id.inputCardNumberEditText);
		
		mImageView.setImageBitmap(setPreview());
		
		mRegisterButton.setEnabled(false);
		
		mCardName.setFilters(new InputFilter[] {mInputFilter});
		
		mCardName.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			
			@Override
			public void afterTextChanged(Editable s) {
				name = s.toString();
				
				mImageView.setImageBitmap(setPreview());
				
				if (number.length() > 0 && name.length() >= 5) {
					mRegisterButton.setEnabled(true);
				} else {
					mRegisterButton.setEnabled(false);
				}
			}
		});
		
		mCardNumber.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}

			@Override
			public void afterTextChanged(Editable s) {
				if( s.toString().length() == 16 ) {
					number = s.toString().substring( 0,  4) + "  " +
							 s.toString().substring( 4,  8) + "  " +
							 s.toString().substring( 8, 12) + "  " +
							 s.toString().substring(12, 16) + "  ";
					
					mImageView.setImageBitmap(setPreview());
				}
				
				if (number.length() > 0 && name.length() >= 5) {
					mRegisterButton.setEnabled(true);
				} else {
					mRegisterButton.setEnabled(false);
				}
				
				mImageView.setImageBitmap(setPreview());
			}
		});
	}
	
	/** User Define Methods **/
	public void onClick(View v) {
		Log.d(TAG, "onClick()");
		
		switch (v.getId()) {
			case R.id.registerButton :
				String name 	= mCardName.getText().toString() + ".png";
				String number	= mCardNumber.getText().toString();
				
				// 중복 체크
				if (name.length() >= 5) {
					Cursor pointCuror = DBAdapter.getInstance().getConnection().query(Constants.TABLE_NAMES[Constants.POINT], 
																	  				  Constants.COLUMS,
																	  				  Constants.COL_NAME + " LIKE '" + name + "'", 
																	  				  null, null, null, null);

					// 똑같은 이름의 카드가 존재함
					if (pointCuror.moveToFirst() == true) {
						mCardName.setText("");
						mRegisterButton.setEnabled(false);
						
						Toast.makeText(getApplicationContext(), "Duplicate Card Names" , Toast.LENGTH_LONG).show();
					} else {
						// Point 카드 생성
						new PointCard(getApplicationContext(), name, number, color);
												
						mDB.command(Constants.INSERT, Constants.POINT, name, number, hexColor, 0);  // Database 저장 
						mDB.command(Constants.DISPLAY, Constants.POINT, null, null, null, 0);		// 출력
												
						// 생성 완료 Toast 띄우기
						Toast.makeText(getApplicationContext(), name.split(".png")[0] + " " + 
									   getResources().getString(R.string.create_complete), Toast.LENGTH_SHORT).show();
						
						// Activity 종료시 onActivityResult() 로 값 전달
						Intent intent = new Intent();
						intent.putExtra(Constants.CARD_INFO, new CardInfo(name, number, hexColor, 0));
						setResult(Constants.RESULT_CODE_POINT, intent);
						
						// 해당 Activity 끝내기
						finish();
					}
				}
				
				break;
			case R.id.setColorButton :
				// 컬러 지정 open source 띄우기
				showColorPickerDialogDemo();
				
				break;
		}
	}
	
	private void setColor(int color) {
		this.color = color;
		this.hexColor = String.format("#%06X", (0xFFFFFF & this.color));
	}
	
	private void showColorPickerDialogDemo() {
        int initialColor = Color.RED;

        ColorPickerDialog colorPickerDialog = new ColorPickerDialog(this, initialColor, new OnColorSelectedListener() {
            @Override
            public void onColorSelected(int color) {
            	Log.d(TAG, "onColorSeletcted()");
            	
            	setColor(color);
            	mImageView.setImageBitmap(setPreview());
            }

        });
        
        colorPickerDialog.show();
    }
	
	private Bitmap setPreview() {
		Bitmap bitmap = Bitmap.createBitmap(200, 200, Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		Paint paint = new Paint();
		
		// 배경 색 지정
		paint.setColor(color);
		canvas.drawRoundRect(new RectF(0, 25, 200, 150), 10, 10, paint);
		
		// 빈 칸
		paint.setColor(Color.WHITE);
		canvas.drawRect(0, 55, 200, 130, paint);
		
		// 카드 이름
		paint.setColor(Color.BLACK);
		paint.setTextSize(20);
		paint.setTypeface(Typeface.create(Typeface.DEFAULT_BOLD, Typeface.BOLD));
		paint.setTextAlign(Paint.Align.CENTER);
		canvas.drawText(name, 100, 48, paint);
		
		// 카드 번호
		paint.setTextSize(15);
		canvas.drawText(number, 100, 145, paint);
		
		return bitmap;
	}
}

// End of PointCardRegister