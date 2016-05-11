package com.bladek.gearwallet.main;

import java.io.File;
import java.util.ArrayList;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.bladek.gearwallet.R;
import com.bladek.gearwallet.creditcard.CreditBarChartActivity;
import com.bladek.gearwallet.creditcard.CreditBaseAdapter;
import com.bladek.gearwallet.creditcard.CreditCardRegisteActivity;
import com.bladek.gearwallet.datas.CardInfo;
import com.bladek.gearwallet.datas.Constants;
import com.bladek.gearwallet.datas.DBAdapter;
import com.bladek.gearwallet.pointcard.PointBarChartActivity;
import com.bladek.gearwallet.pointcard.PointBaseAdapter;
import com.bladek.gearwallet.pointcard.PointCardRegisteActivity;

public class GearWalletActivity extends FragmentActivity implements View.OnClickListener {
	// Debug
	private static final	String					TAG					= "GearWalletActivity";
	
	// ViewPager
	private					ViewPager				mViewPager			= null;
	
	// Button
	private					Button 					mPointPageButton	= null;
	private					Button					mCreditPageButton	= null;
	
	/** Constructor **/
	public GearWalletActivity() {}
	
	/** Override Methods **/
	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		Log.d( TAG, "onCreate()" );
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gear_wallet);
		
		createFolder();
		
		initButton();
				
		initViewPager();
	}
	
	/** User Define Methods **/
	private void createFolder() {
		Log.d(TAG, "createFolder()");
		
		File root = new File(Constants.ROOT_PATH);
		
		if (!root.exists()) {
			root.mkdirs();

			new File(Constants.POINT_SLOT_PATH).mkdirs();
			new File(Constants.CREDIT_SLOT_PATH).mkdirs();

			Toast.makeText(getApplicationContext(), "Folder Create", Toast.LENGTH_SHORT).show();
		}
	}
	
	private void initButton() {
		Log.d(TAG, "initButton()");
		
		if (mPointPageButton == null) {
			mPointPageButton = (Button) findViewById(R.id.Page1Btn);
			mPointPageButton.setOnClickListener(this);
			mPointPageButton.setSelected(true);
		}
		
		if (mCreditPageButton == null) {
			mCreditPageButton = (Button) findViewById(R.id.Page2Btn);
			mCreditPageButton.setOnClickListener(this);
			mCreditPageButton.setSelected(false);
		}
	}
	
	private void initViewPager() {
		Log.d(TAG, "initViewPager()");
		
		if (mViewPager == null) {
			mViewPager = (ViewPager) findViewById(R.id.pager);
			mViewPager.setAdapter(new PagerAdapter(getSupportFragmentManager()));
			mViewPager.setCurrentItem(Constants.POINT);
			mViewPager.setOnPageChangeListener(new OnPageChangeListener() {			
				@Override
				public void onPageSelected(int position) {
					mPointPageButton.setSelected(false);
					mCreditPageButton.setSelected(false);
					
					switch (position) {
						case Constants.POINT  : mPointPageButton.setSelected(true);  break;
						case Constants.CREDIT : mCreditPageButton.setSelected(true); break;
						default : break;
					}
				}
				
				@Override
				public void onPageScrolled(int arg0, float arg1, int arg2) {}

				@Override
				public void onPageScrollStateChanged(int arg0) {}
			});
		}
	}
	
	public void onClick(View v) {
		Log.d(TAG, "onClick()");
		
		switch (v.getId()){
			case R.id.Page1Btn : 
				mViewPager.setCurrentItem(Constants.POINT);
				break;
			case R.id.Page2Btn :
				mViewPager.setCurrentItem(Constants.CREDIT);
				break;
			default : break;
		}
	}

	/** pagerAdapter Class **/
	private class PagerAdapter extends FragmentPagerAdapter{
		// Debug
		private static final	String	TAG	 = "PagerAdapter";
		
		/** Constructor **/
		public PagerAdapter(FragmentManager fragmentManager) {
			super(fragmentManager);
			
			Log.d(TAG, "Constructor()");
		}

		/** Override Methods **/
		@Override
		public Fragment getItem(int position) {
			Log.d(TAG, "getItem() " + position);
			
			return ArrayFragment.newInstance(position);
		}

		@Override
		public int getCount() {
			return Constants.PAGE_COUNT;
		}
	}
	
	// End of pagerAdapter
	
	/** ArrayFragment Class **/
	private static class ArrayFragment extends Fragment {
		// Debug
		private static final	String					TAG					= "ArrayFragment";
		
		// ListView
		private					ListView 				mPointListView 		= null;
		private					ListView				mCreditListView		= null;
				
		// ImageView
		private					ImageView				mPointCardAdd		= null;
		private					ImageView				mCreditCardAdd		= null;
		private					ImageView				mPointDisplayFreq	= null;
		private					ImageView				mCreditDisplayFreq	= null;
				
		// InfoClass ArrayList
		private					ArrayList<CardInfo>		mPointInfoList		= null;
		private					ArrayList<CardInfo>		mCreditInfoList		= null;

		// Database Adapter
		private					DBAdapter				mDB					= null;
		
		// Position
		private	static			int						mPosition			= 0;

		private ArrayFragment() {}
		public static ArrayFragment newInstance(int position) {
			Log.d(TAG, "newInstance()");
			
			ArrayFragment instance = new ArrayFragment();
			
			Bundle bundle = new Bundle();
			ArrayFragment.mPosition = position;
			bundle.putInt(Constants.POSITION,  ArrayFragment.mPosition);
			instance.setArguments(bundle);
				
			return instance;
		}
				
		/** Override Methods **/
		@Override
		public void onCreate(Bundle savedInstanceState) {
			Log.d(TAG, "onCreate()");
			
			super.onCreate(savedInstanceState);
			
			mPosition = (getArguments() != null) ? getArguments().getInt(Constants.POSITION) : 0;
			Log.e(TAG, "onCreate() " + mPosition);
			
			mPointInfoList = new ArrayList<CardInfo>();
			mCreditInfoList = new ArrayList<CardInfo>();
			
			mDB = DBAdapter.getInstance();
			mDB.connect(getActivity());
			
			drawCursor();
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			Log.d(TAG, "onCreateView()");
			
			View view = null;
			
			Log.e(TAG, "obCreateView() " + mPosition);
			
			switch (mPosition) {
				case Constants.POINT :
					view = inflater.inflate(R.layout.activity_pointcard_main, container, false);
					PointBaseAdapter pointAdapter = new PointBaseAdapter(this.getActivity(), mPointInfoList);
					
					mPointListView 		= (ListView)  view.findViewById(R.id.point_list);
					mPointCardAdd		= (ImageView) view.findViewById(R.id.point_add);
					mPointDisplayFreq	= (ImageView) view.findViewById(R.id.point_display_freq);
					
					mPointListView.setAdapter(pointAdapter);
					mPointListView.setDivider(null);
					
					mPointCardAdd.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent intent = new Intent(v.getContext(), PointCardRegisteActivity.class);
							intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
							startActivityForResult(intent, Constants.REQUEST_CODE);
						}
					});
					
					mPointDisplayFreq.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent intent = new Intent(v.getContext(), PointBarChartActivity.class);
							intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
							startActivity(intent);
						}
					});
					
					break;
				case Constants.CREDIT :
					view = inflater.inflate(R.layout.activity_creditcard_main, container, false);
					CreditBaseAdapter creditAdapter = new CreditBaseAdapter(this.getActivity(), mCreditInfoList);
					
					mCreditListView		= (ListView)  view.findViewById(R.id.credit_list);
					mCreditCardAdd		= (ImageView) view.findViewById(R.id.credit_add);
					mCreditDisplayFreq	= (ImageView) view.findViewById(R.id.credit_display_freq);
					
					mCreditListView.setAdapter(creditAdapter);
					mCreditListView.setDivider(null);
					
					mCreditCardAdd.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent intent = new Intent(v.getContext(), CreditCardRegisteActivity.class);
							intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
							startActivityForResult(intent, Constants.REQUEST_CODE);
						}
					});
					
					mCreditDisplayFreq.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent intent = new Intent(v.getContext(), CreditBarChartActivity.class);
							intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
							startActivity(intent);
						}
					});
					
					break;
				default : break;
			}
			
			return view;
		}
		
		@Override
		public void onActivityResult(int requestCode, int resultCode, Intent data) {
			Log.d(TAG, "onActivityResult()");
			
			super.onActivityResult(requestCode, resultCode, data);			
			
			switch (requestCode) {
				case Constants.REQUEST_CODE :
					if (resultCode == Constants.RESULT_CODE_POINT) {
						mPointInfoList.add((CardInfo) data.getSerializableExtra(Constants.CARD_INFO));						
						PointBaseAdapter pointAdapter = new PointBaseAdapter(this.getActivity(), mPointInfoList);
						mPointListView.setAdapter(pointAdapter);
					} else if (resultCode == Constants.RESULT_CODE_CREDIT) {
						mCreditInfoList.add((CardInfo) data.getSerializableExtra(Constants.CARD_INFO));
						CreditBaseAdapter creditAdapter = new CreditBaseAdapter(this.getActivity(), mCreditInfoList);
						mCreditListView.setAdapter(creditAdapter);
					}
					
					break;
				default : break;
			}
		}
		
		/** User Define Method **/
		private void drawCursor() {
			Log.d(TAG, "drawCursor()");
			
			Cursor pointCursor = mDB.getConnection().query(Constants.TABLE_NAMES[Constants.POINT], 
														   Constants.COLUMS, 
														   null, null, null, null, null, null);
			Cursor creditCursor = mDB.getConnection().query(Constants.TABLE_NAMES[Constants.CREDIT], 
					   										Constants.COLUMS, 
					   										null, null, null, null, null, null);
			
			if (pointCursor != null && pointCursor.moveToFirst()) {
				do {
					try {					
						final String 	name	= pointCursor.getString(pointCursor.getColumnIndexOrThrow(Constants.COL_NAME));
						final String 	number 	= pointCursor.getString(pointCursor.getColumnIndexOrThrow(Constants.COL_NUMBER));
						final String 	color 	= pointCursor.getString(pointCursor.getColumnIndexOrThrow(Constants.COL_COLOR));
						final int 		freq 	= pointCursor.getInt(pointCursor.getColumnIndexOrThrow(Constants.COL_FREQ));
						
						mPointInfoList.add(new CardInfo(name, number, color, freq));
					} catch (Exception e) {}
				} while (pointCursor.moveToNext());
			}
			
			if (creditCursor != null && creditCursor.moveToFirst()) {
				do {
					try {					
						final String 	name	= creditCursor.getString(creditCursor.getColumnIndexOrThrow(Constants.COL_NAME));
						final String 	number 	= creditCursor.getString(creditCursor.getColumnIndexOrThrow(Constants.COL_NUMBER));
						final String 	color 	= creditCursor.getString(creditCursor.getColumnIndexOrThrow(Constants.COL_COLOR));
						final int 		freq 	= creditCursor.getInt(creditCursor.getColumnIndexOrThrow(Constants.COL_FREQ));
						
						mCreditInfoList.add(new CardInfo(name, number, color, freq));
					} catch (Exception e) {}
				} while (creditCursor.moveToNext());
			}
						
			pointCursor.close();			
			creditCursor.close();
		}
	}
	
	// End of ArrayFragment
}

// End of GearWalletActivity