package com.bladek.gearwallet.creditcard;

import java.io.File;
import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.provider.MediaStore.Images;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bladek.gearwallet.R;
import com.bladek.gearwallet.datas.CardInfo;
import com.bladek.gearwallet.datas.Constants;
import com.bladek.gearwallet.datas.DBAdapter;

public class CreditBaseAdapter extends BaseAdapter {
	// Debug
	private static final	String					TAG			= "CreditBaseAdapter";
	
	// Context
	private					Context					mContext	= null;
	
	// LayoutInflater
	private					LayoutInflater			mInflater 	= null;
	
	// ViewHolder
	private					ViewHolder				mViewHolder	= null;
	
	// Database Adapter
	private					DBAdapter				mDB			= null;
	
	// InfoClass ArrayList
	private 				ArrayList<CardInfo> 	mInfoList 	= null;
	
	/** Constructor **/
	public CreditBaseAdapter(Context context , ArrayList<CardInfo> infoList) {
		Log.d(TAG, "Cunstructor()");
		
		this.mContext = context;
		this.mInflater = LayoutInflater.from(mContext);
		this.mInfoList = infoList;
		this.mDB = DBAdapter.getInstance();
	}
	
	/** Override Methods **/
	@Override
	public int getCount() {
		Log.d(TAG, "getCount()");
		
		return mInfoList.size();
	}

	@Override
	public Object getItem(int position) {
		Log.d(TAG, "getItem()");
		
		return mInfoList.get(position);
	}

	@Override
	public long getItemId(int position) {
		Log.d(TAG, "getItemId()");
		
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Log.d(TAG, "getView()");
		
		View view = convertView;

		if (view == null) {
			view = mInflater.inflate(R.layout.list_row_credit, parent, false);
			
			mViewHolder = new ViewHolder();
			
			mViewHolder.mSlotImageView	= (ImageView) view.findViewById(R.id.credit_image);
			mViewHolder.mSlotTextView	= (TextView)  view.findViewById(R.id.credit_text);
			view.setTag(mViewHolder);
		} else {
			mViewHolder = (ViewHolder) view.getTag();
		}
		
		String name = mInfoList.get(position).getName();
		Bitmap bitmap = BitmapFactory.decodeFile(Constants.CREDIT_SLOT_PATH + "/" + name);
		Drawable image = new BitmapDrawable(mContext.getResources(), bitmap);
				
		mViewHolder.mSlotTextView.setText(name.split(".png")[0]);
		mViewHolder.mSlotTextView.setTextColor(Color.BLACK);

		mViewHolder.mSlotImageView.setTag(position);
		mViewHolder.mSlotImageView.setImageDrawable(image);
		mViewHolder.mSlotImageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				int position = Integer.parseInt(v.getTag().toString());
				String name = mInfoList.get(position).getName();

				Intent intent = new Intent(v.getContext(), CreditCardViewActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
				intent.putExtra(Constants.NAME, name);
				v.getContext().startActivity(intent);
			}
		});	
		mViewHolder.mSlotImageView.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				final int position = Integer.parseInt(v.getTag().toString());
				
				AlertDialog.Builder dlg = new AlertDialog.Builder(mContext);
				dlg.setTitle("Delete Card");
				dlg.setMessage("Do you want Delete?");
				dlg.setCancelable(false);
				dlg.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {						
					@Override
					public void onClick(DialogInterface dialog, int id) {
						dialog.dismiss();
					}
				});
				dlg.setNegativeButton("Delete",  new DialogInterface.OnClickListener() {						
					@Override
					public void onClick(DialogInterface dialog, int id) {
						String name = mInfoList.get(position).getName();
						String[] cardArgs	= {Constants.CREDIT_CARD_PATH + "/" + name};						
						String[] slotArgs 	= {Constants.CREDIT_SLOT_PATH + "/" + name};
						
						// Media에서 삭제
						ContentResolver contentResolver = mContext.getContentResolver();
						File cardImageFile = new File(Constants.CREDIT_CARD_PATH, name);
						File slotImageFile = new File(Constants.CREDIT_SLOT_PATH, name);
						
						contentResolver.delete(Images.Media.EXTERNAL_CONTENT_URI, Images.Media.DATA + " = ?", cardArgs);
						contentResolver.delete(Images.Media.EXTERNAL_CONTENT_URI, Images.Media.DATA + " = ?", slotArgs);
						
						// Database에서 삭제
						mDB.command(Constants.DELETE, Constants.CREDIT, name, null, null, 0);
						mDB.command(Constants.DISPLAY, Constants.CREDIT, null, null, null, 0);

						// List에서 삭제
						mInfoList.remove(position);
				
						// File에서 삭제
						cardImageFile.delete();
						slotImageFile.delete();
						
						Toast.makeText(mContext, "Deletion successful!", Toast.LENGTH_SHORT).show();
						
						notifyDataSetInvalidated();
					}
				});
				dlg.show();
						
				return false;
			}
		});

		return view;
	}

	/** ViewHolder Class **/
	private class ViewHolder{
		public TextView 	mSlotTextView  = null;
		public ImageView 	mSlotImageView = null;
	}
	
	// End Of ViewHolder
}

// End of CreditBaseAdapter
