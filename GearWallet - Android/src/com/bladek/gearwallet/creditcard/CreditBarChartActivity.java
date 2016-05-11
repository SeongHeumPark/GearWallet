package com.bladek.gearwallet.creditcard;

import java.text.DecimalFormat;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import com.bladek.gearwallet.R;
import com.bladek.gearwallet.datas.Constants;
import com.bladek.gearwallet.datas.DBAdapter;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendForm;
import com.github.mikephil.charting.components.Legend.LegendPosition;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.components.YAxis.AxisDependency;
import com.github.mikephil.charting.components.YAxis.YAxisLabelPosition;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.Highlight;
import com.github.mikephil.charting.utils.ValueFormatter;

public class CreditBarChartActivity extends Activity implements OnChartValueSelectedListener {
	// Debug
	private static final	String		TAG 	= "CreditBarChartActivity";
	
	// BarChar
	private					BarChart	mChart	= null;
	
	// Typeface
    private					Typeface	mTf 	= null;
    
    // Database Adapter
    private					DBAdapter	mDB		= null;
	
    /** Override Methods **/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_creditcard_freq);

        mChart = (BarChart) findViewById(R.id.chart1);
        mChart.setOnChartValueSelectedListener(this);
        mChart.setDrawBarShadow(false);
        mChart.setDrawValueAboveBar(true);
        mChart.setDescription("");
        mChart.setMaxVisibleValueCount(60);
        mChart.setPinchZoom(false);
        mChart.setDrawGridBackground(false);
        mChart.animateY(2000);

        mTf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxisPosition.BOTTOM);
        xAxis.setTypeface(mTf);
        xAxis.setDrawGridLines(false);
        xAxis.setSpaceBetweenLabels(2);
        
        ValueFormatter custom = new MyValueFormatter();

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setTypeface(mTf);
        leftAxis.setLabelCount(8);
        leftAxis.setValueFormatter(custom);
        leftAxis.setPosition(YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setTypeface(mTf);
        rightAxis.setLabelCount(8);
        rightAxis.setValueFormatter(custom);
        rightAxis.setSpaceTop(15f);
        
        Legend l = mChart.getLegend();
        l.setPosition(LegendPosition.BELOW_CHART_LEFT);
        l.setForm(LegendForm.SQUARE);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);

        mDB = DBAdapter.getInstance();
        
        setData();
	}

    @SuppressLint("NewApi")
    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
    	Log.d(TAG, "onValueSelected()");
    	
        if (e == null)
            return;

        RectF bounds = mChart.getBarBounds((BarEntry) e);
        PointF position = mChart.getPosition(e, AxisDependency.LEFT);

        Log.i("bounds", bounds.toString());
        Log.i("position", position.toString());
    }
    
    @Override
	public void onNothingSelected() {
    	Log.d(TAG, "onNothingSelected()");
    }
    
    /** User Defined Methods **/
    private void setData() {
    	Log.d(TAG, "setData()");
    	
    	ArrayList<String> xVals = new ArrayList<String>();
    	ArrayList<BarEntry> yVals = new ArrayList<BarEntry>();
    	ArrayList<Integer> colors = new ArrayList<Integer>();
    	Cursor cursor = mDB.getConnection().query(Constants.TABLE_NAMES[Constants.CREDIT],
    											  Constants.COLUMS,
    											  null, null, null, null, null);
    	int count = cursor.getCount();
    	
    	if (count > 0) {
	    	if (cursor != null && cursor.moveToFirst()) {
	    		for (int i = 0; i < count; i++) {
	    			String name = cursor.getString(cursor.getColumnIndex(Constants.COL_NAME));
	    			int freq = cursor.getInt(cursor.getColumnIndex(Constants.COL_FREQ));
	    			int color = Color.parseColor(cursor.getString(cursor.getColumnIndex(Constants.COL_COLOR)));
	    			
	    			xVals.add(name.split(".png")[0]);
	    			yVals.add(new BarEntry((float) freq, i));
	    			colors.add(color);
	    			
	    			cursor.moveToNext();
	    		}
	    		
	    		if (!cursor.isClosed()) {
	    			cursor.close();
	    		}
	    	}
	    	
	        BarDataSet set1 = new BarDataSet(yVals, "Frequency");
	        set1.setBarSpacePercent(35f);
	        set1.setColors(colors);
	
	        ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
	        dataSets.add(set1);
	
	        BarData data = new BarData(xVals, dataSets);
	        data.setValueTextSize(10f);
	        data.setValueTypeface(mTf);
	
	        mChart.setData(data);
    	}
    }
    
    /** MyValueFormatter Class **/
    private class MyValueFormatter implements ValueFormatter {
    	// Debug
    	private static final	String			TAG		= "MyValueFormatter";
    	
    	// DecimalFormat
        private					DecimalFormat	mFormat = null;
        
        /** Constructor **/
        public MyValueFormatter() {
        	Log.d(TAG, "Constructor()");
            mFormat = new DecimalFormat("###,###.0");
        }
        
        /** Override Methods **/
        @Override
        public String getFormattedValue(float value) {
        	Log.d(TAG, "getFormattedValue()");
        	
            return mFormat.format(value);
        }
    }
    
    // End of MyValueFormatter
}

// End of CreditCardBarChartActivity