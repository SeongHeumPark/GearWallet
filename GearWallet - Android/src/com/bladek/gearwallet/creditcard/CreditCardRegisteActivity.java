package com.bladek.gearwallet.creditcard;

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

public class CreditCardRegisteActivity extends Activity {
	// Debug
	private static final	String			TAG 			= "RestigerCreditCard";
	
	// Edit Text
	private					EditText		mCardName		= null;
	private					EditText		mCardNumber		= null;
	private					EditText		mCardYear		= null;
	private					EditText		mCardMonth		= null;
	private					EditText		mCardCVC		= null;
	
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
			
			if(source.length() > 0 && !pattern.matcher(source).matches()) {
				// Only English Toast
				Toast.makeText(getApplicationContext(), R.string.special_character_error, Toast.LENGTH_LONG).show();
				
				return "";
			}
			
			return null;
		}
	};
		
	// Credit 카드 속성
	private					String			name			= "";
	private					String			number			= "";
	private					String			year			= "";
	private 				String			month			= "";
	private					String			cvc				= "";
	private 				int				color			= 0;
	private					String			hexColor		= "";
	
	/** Override Methods **/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "onCreate()");
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_creditcard_registe);
		
		name = "";
		number = "";
		year = "";
		month = "";
		cvc = "";
		color = Color.RED;
		hexColor = String.format("#%06X", (0xFFFFFF & this.color));
		
		mDB = DBAdapter.getInstance();
		
		mImageView		= (ImageView) 	findViewById(R.id.preview);
		mRegisterButton	= (Button) 		findViewById(R.id.registerButton);
		mCardName 		= (EditText) 	findViewById(R.id.inputCreditCardNameEditText);
		mCardNumber		= (EditText) 	findViewById(R.id.inputCreditCardNumberEditText);
		mCardYear		= (EditText) 	findViewById(R.id.inputYearNumberEditText);
		mCardMonth		= (EditText) 	findViewById(R.id.inputMonthNumberEditText);
		mCardCVC		= (EditText) 	findViewById(R.id.inputCVCNumberEditText);
		
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
				
				if(number.length() > 0 && name.length() >= 5 && year.length() == 2 && month.length() == 2 && cvc.length() == 3) {
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
				if (s.toString().length() == 16) {
					number = s.toString().substring( 0,  4) + "  " +
							 s.toString().substring( 4,  8) + "  " +
							 s.toString().substring( 8, 12) + "  " +
							 s.toString().substring(12, 16) + "  ";
					
					mImageView.setImageBitmap( setPreview() );
				}
				
				if(number.length() > 0 && name.length() >= 5 && year.length() == 2 && month.length() == 2 && cvc.length() == 3) {
					mRegisterButton.setEnabled(true);
				} else {
					mRegisterButton.setEnabled(false);
				}
			}
		});
		
		mCardYear.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}

			@Override
			public void afterTextChanged(Editable s) {
				year = s.toString();
				
				mImageView.setImageBitmap( setPreview() );
				
				if(number.length() > 0 && name.length() >= 5 && year.length() == 2 && month.length() == 2 && cvc.length() == 3) {
					mRegisterButton.setEnabled(true);
				} else {
					mRegisterButton.setEnabled(false);
				}
			}
		});
		
		mCardMonth.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}

			@Override
			public void afterTextChanged(Editable s) {
				month = s.toString();
				
				mImageView.setImageBitmap(setPreview());
				
				if(number.length() > 0 && name.length() >= 5 && year.length() == 2 && month.length() == 2 && cvc.length() == 3) {
					mRegisterButton.setEnabled(true);
				} else {
					mRegisterButton.setEnabled(false);
				}
			}
		});
		
		mCardCVC.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			
			@Override
			public void afterTextChanged(Editable s) {
				cvc = s.toString();
				
				mImageView.setImageBitmap(setPreview());
				
				if(number.length() > 0 && name.length() >= 5 && year.length() == 2 && month.length() == 2 && cvc.length() == 3) {
					mRegisterButton.setEnabled(true);
				} else {
					mRegisterButton.setEnabled(false);
				}
			}
		});
	}
	
	/** User Define Methods **/
	public void onClick(View v) {
		Log.d(TAG, "onClick()");
		
		switch (v.getId()) {
			case R.id.registerButton :
				String name		= mCardName.getText().toString() + ".png";
				String number	= mCardNumber.getText().toString();
				String year		= mCardYear.getText().toString();
				String month	= mCardMonth.getText().toString();
				String cvc		= mCardCVC.getText().toString();
				
				// 중복 체크
				if (name.length() >= 5) {
					Cursor creditCuror = DBAdapter.getInstance().getConnection().query(Constants.TABLE_NAMES[Constants.CREDIT], 
			  				  														   Constants.COLUMS,
			  				  														   Constants.COL_NAME + " LIKE '" + name + "'", 
			  				  														   null, null, null, null);
					
					if (creditCuror.moveToFirst() == true) {
						mCardName.setText("");
						mRegisterButton.setEnabled(false);
						
						Toast.makeText(getApplicationContext(), "Duplicate Card Names" , Toast.LENGTH_SHORT).show();
					} else {
						// Credit 카드 생성
						new CreditCard(getApplicationContext(), name, number, year, month, cvc, color);
						
						mDB.command(Constants.INSERT, Constants.CREDIT, name, number, hexColor, 0);  // Database 저장 
						mDB.command(Constants.DISPLAY, Constants.CREDIT, null, null, null, 0);		 // 출력
						
						Toast.makeText(getApplicationContext(), name + " " +
									   getResources().getString(R.string.create_complete), Toast.LENGTH_SHORT).show();
						
						// Activity 종료시 onActivityResult() 로 값 전달
						Intent intent = new Intent();
						intent.putExtra(Constants.CARD_INFO, new CardInfo(name, number, hexColor, 0));
						setResult(Constants.RESULT_CODE_CREDIT, intent);
						
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
		
		// 카드 이름
		paint.setColor(Color.BLACK);
		paint.setTextSize(20);
		paint.setTypeface(Typeface.create(Typeface.DEFAULT_BOLD, Typeface.BOLD));
		paint.setTextAlign(Paint.Align.CENTER);
		canvas.drawText(name, 100, 53, paint);
		
		// 카드 번호
		paint.setTextSize(17);
		canvas.drawText(number, 100, 100, paint);
		
		// 카드 유효기간
		paint.setTextSize(15);
		canvas.drawText(month, 125, 125, paint);
		canvas.drawText("/", 139, 125, paint);
		canvas.drawText(year, 152, 125, paint);
		
		// 카드 CVC
		paint.setTextSize(15);
		canvas.drawText(cvc, 146, 140, paint);
		
		return bitmap;
	}
}
