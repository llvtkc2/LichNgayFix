package com.hdpsolutions.lichngay;


import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.hdpsolutions.lichngay.Adapter.DateArrayAdapter;
import com.hdpsolutions.lichngay.Adapter.DateNumericAdapter;
import com.hdpsolutions.lichngay.calender.VNMDate;
import com.hdpsolutions.lichngay.calender.VietCalendar;
import com.hdpsolutions.lichngay.ui.ScrollableDayView;
import com.hdpsolutions.lichngay.ui.ScrollableDayView.OnDateChangedListener;
import com.hdpsolutions.lichngay.widget.HorizontalScrollView;
import com.hdpsolutions.lichngay.widget.HorizontalScrollView.OnScreenSelectedListener;
import com.hdpsolutions.lichngay.widget.WheelView;

import java.util.Calendar;
import java.util.Date;

public class DayActivity extends AppCompatActivity implements OnClickListener{

	WheelView monthSolar,yearSolar,daySolar,monthLunar,yearLunar,dayLunar;
	String months[];
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
	private TextView mLichNgay,mLichThang,mDoiNgay,mNhieuHon;
	
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


		mLichNgay =  findViewById(R.id.lichngay);
		mLichThang = findViewById(R.id.lichthang);
		mDoiNgay = findViewById(R.id.doingay);
		mNhieuHon = findViewById(R.id.nhieuhon);
		mDoiNgay.setOnClickListener(this);
		monthText.setOnClickListener(this);
        
        LayoutParams layoutParams1 = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT, 1.0f);
        main.addView(this.scrollView, layoutParams1);

		
        this.scrollView.setOnScreenSelectedListener( new OnScreenSelectedListener() {
			public void onSelected(int selectedIndex) {
				prepareOtherViews(selectedIndex);				
			}
        });
		selectedDate =  new Date();
        showDate(selectedDate);
		init(selectedDate);
		Calendar calendar =  Calendar.getInstance();
		calendar.setTime(selectedDate);
		tvToday.setText(calendar.get(Calendar.DAY_OF_MONTH)+"");
		tvToday.setOnClickListener(this);
    }

	private void HienThiNgayDuong() {
		Calendar calendar =  Calendar.getInstance();
		int curDaySolar = calendar.get(Calendar.DAY_OF_MONTH);
		int curMonthSolar = calendar.get(Calendar.MONTH);
		int curYearSolar = calendar.get(Calendar.YEAR);
		//Hiển thị nên ngày dương
		// monthSolar
		months = new String[12];
		for(int i=0;i<months.length;i++){
			months[i] = "Tháng " + (i+1);
		}
		monthSolar.setViewAdapter(new DateArrayAdapter(this, months, curMonthSolar));
		monthSolar.setCurrentItem(curMonthSolar);
//		monthSolar.addScrollingListener(listenerSolar);

		// yearSolar
		yearSolar.setViewAdapter(new DateNumericAdapter(this, 1900, 2030, curYearSolar-1900));
		yearSolar.setCurrentItem(curYearSolar-1900);
//		yearSolar.addScrollingListener(listenerSolar);

		//daySolar
		updateDays(yearSolar, monthSolar, daySolar);
		daySolar.setCurrentItem(curDaySolar-1);
//		daySolar.addScrollingListener(listenerSolar);
	}

	// Thay đổi ngày 30-31-29 khi chọn tháng năm
	void updateDays(WheelView year, WheelView month, WheelView day) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + year.getCurrentItem());
		calendar.set(Calendar.MONTH, month.getCurrentItem());
		int maxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		day.setViewAdapter(new DateNumericAdapter(this, 1, maxDays, calendar.get(Calendar.DAY_OF_MONTH) - 1));
		int curDay = Math.min(maxDays, day.getCurrentItem() + 1);
		day.setCurrentItem(curDay - 1, true);


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
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 100 && resultCode == RESULT_OK) {
			int solar[] = data.getIntArrayExtra("date");
			Calendar calendar =  Calendar.getInstance();
			calendar.set(solar[2],solar[1]-1,solar[0]);
			showDate(calendar.getTime());
		}
	}
	
	private OnClickListener onClickListener = new OnClickListener() {
		public void onClick(View v) {
			Toast.makeText(DayActivity.this, "vào chi tiết", Toast.LENGTH_SHORT).show();
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

	@Override
	public void onClick(View view) {
		switch (view.getId()){
			case R.id.txttoday: showDate(new Date());
				break;
			case R.id.doingay: {
				startActivityForResult(new Intent(DayActivity.this,DoiNgayActivity.class),100);
				break;
			}
			case R.id.monthText: {
				showdialogSelectDay();
				break;
			}
		}
	}

	private void showdialogSelectDay() {
		Dialog dialog =  new Dialog(this);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialog.setContentView(R.layout.dialog_chonngay);
		monthSolar = dialog.findViewById(R.id.monthSolard);
		yearSolar = dialog.findViewById(R.id.yearSolard);
		daySolar = dialog.findViewById(R.id.daySolard);
		HienThiNgayDuong();
		dialog.show();
	}
}