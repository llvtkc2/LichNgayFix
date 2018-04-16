package com.hdpsolutions.lichngay;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hdpsolutions.lichngay.ui.ScrollableDayView;
import com.hdpsolutions.lichngay.ui.ScrollableDayView.OnDateChangedListener;
import com.hdpsolutions.lichngay.widget.HorizontalScrollView;
import com.hdpsolutions.lichngay.widget.HorizontalScrollView.OnScreenSelectedListener;

import java.util.Calendar;
import java.util.Date;

public class VNMDayActivity extends AppCompatActivity {
	private static final int MENU_SELECT_DATE = 1;
	private static final int MENU_SELECT_TODAY = 2;
	
	private HorizontalScrollView scrollView;
	private Date selectedDate;
	private TextView monthText;
	protected TextView vnmHourText;
	protected TextView vnmHourInText;
	protected TextView vnmDayOfMonthText;
	protected TextView vnmDayOfMonthInText;
	protected TextView vnmMonthText;
	protected TextView vnmMonthInText;
	protected TextView tvToday;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);

		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.scrollView = new HorizontalScrollView(this);
        this.setContentView(R.layout.main);
        LinearLayout main = findViewById(R.id.main);
		monthText = findViewById(R.id.monthText);
		vnmHourText =  findViewById(R.id.vnmHourText);
		vnmHourInText =  findViewById(R.id.vnmHourInText);
		vnmDayOfMonthText =  findViewById(R.id.vnmDayOfMonthText);
		vnmDayOfMonthInText =  findViewById(R.id.vnmDayOfMonthInText);
		vnmMonthText =  findViewById(R.id.vnmMonthText);
		vnmMonthInText =  findViewById(R.id.vnmMonthInText);
		tvToday = findViewById(R.id.txttoday);
        
        LayoutParams layoutParams1 = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT, 1.0f);
        main.addView(this.scrollView, layoutParams1);

		
        this.scrollView.setOnScreenSelectedListener( new OnScreenSelectedListener() {
			public void onSelected(int selectedIndex) {
				prepareOtherViews(selectedIndex);				
			}
        });        
        //Log.d("DEBUG", StreamUtils.readAllText(getResources().openRawResource(R.raw.test)));
		selectedDate =  new Date();
        showDate(selectedDate);
		init(selectedDate);
		Calendar calendar =  Calendar.getInstance();
		calendar.setTime(selectedDate);
		tvToday.setText(calendar.get(Calendar.DAY_OF_MONTH)+"");
		tvToday.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showDate(selectedDate);
			}
		});
    }
    
    private void showDate(Date date) {
    	this.selectedDate = date;
    	int childCount = this.scrollView.getChildCount(); 
		for (int i = 0; i < 3 - childCount; i++) {
			ScrollableDayView view = new ScrollableDayView(this);
	    	view.setOnDateChangedListener(onDateChangedListener);
	    	view.setOnClickListener(onClickListener);
			this.scrollView.addView(view);		
		}
    	    	
		ScrollableDayView previousView = (ScrollableDayView)this.scrollView.getChildAt(0);	    	
		previousView.setDate(addDays(date, -1));
		 
		ScrollableDayView currentView = (ScrollableDayView)this.scrollView.getChildAt(1);			
		currentView.setDate(date);
		 
		ScrollableDayView nextView = (ScrollableDayView)this.scrollView.getChildAt(2);
		nextView.setDate(addDays(date, 1));
    	
		this.scrollView.showScreen(1);
    }
    
    protected void prepareOtherViews(int selectedIndex) {
    	ScrollableDayView currentView = (ScrollableDayView)this.scrollView.getChildAt(selectedIndex);
    	Date currentDate = currentView.getDate();
    	if (selectedIndex == 0) {    		
    		// remove last view, add new view at the beginning
    		ScrollableDayView previousView = (ScrollableDayView)this.scrollView.getChildAt(2);
    		previousView.setDate(addDays(currentDate, -1));    		    	
    		this.scrollView.rotateLastView();
    	} else if (selectedIndex == 2) {
    		// remove first view, append new view at the end
    		ScrollableDayView nextView = (ScrollableDayView)this.scrollView.getChildAt(0);
    		nextView.setDate(addDays(currentDate, +1));    					    		
    		this.scrollView.rotateFirstView();
    	}
    	init(currentDate);
	}
    
    private Date addDays(Date date, int days) {
    	Calendar cal = Calendar.getInstance();
    	cal.setTime(date);
    	cal.add(Calendar.DATE, days);
    	return cal.getTime();
    }

	private Date getCurrentDate() {
		int selectedIndex = this.scrollView.getDisplayedChild();
		ScrollableDayView currentView = (ScrollableDayView)this.scrollView.getChildAt(selectedIndex);
    	return currentView.getDate();
	}
	
//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		if (requestCode == SELECT_DATE && resultCode == RESULT_OK) {
//			long result = data.getLongExtra(VNMMonthActivity.SELECTED_DATE_RETURN, 0);
//			Calendar cal = Calendar.getInstance();
//			cal.setTimeInMillis(result);
//			showDate(cal.getTime());
//		}
//	}
	
	private OnClickListener onClickListener = new OnClickListener() {
		public void onClick(View v) {
			Toast.makeText(VNMDayActivity.this, "vào chi tiết", Toast.LENGTH_SHORT).show();
		}		
	};
	
	private OnDateChangedListener onDateChangedListener = new OnDateChangedListener() {
		public void onDateChanged(Date date) {
			ScrollableDayView previousView = (ScrollableDayView)scrollView.getChildAt(0);
			previousView.setDate(addDays(date, -1));

    		ScrollableDayView nextView = (ScrollableDayView)scrollView.getChildAt(2);
    		nextView.setDate(addDays(date, +1));

			init(date);


		}		
	};

	private void init(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		int month = calendar.get(Calendar.MONTH) + 1;
		int year = calendar.get(Calendar.YEAR);

		VNMDate vnmDate = VietCalendar.convertSolar2LunarInVietnamese(date);

		monthText.setText(month + "-" + year);
		vnmHourText.setText(hour + ":" + minute);
		vnmDayOfMonthText.setText(vnmDate.getDayOfMonth() + "");
		vnmMonthText.setText(vnmDate.getMonth() + "");

		String[] vnmCalendarTexts = VietCalendar.getCanChiInfo(vnmDate.getDayOfMonth(), vnmDate.getMonth(), vnmDate.getYear(), dayOfMonth, month, year);

		vnmHourInText.setText(vnmCalendarTexts[VietCalendar.HOUR]);
		vnmDayOfMonthInText.setText(vnmCalendarTexts[VietCalendar.DAY]);
		vnmMonthInText.setText(vnmCalendarTexts[VietCalendar.MONTH]+"\n"+vnmCalendarTexts[VietCalendar.YEAR]);
	}

}