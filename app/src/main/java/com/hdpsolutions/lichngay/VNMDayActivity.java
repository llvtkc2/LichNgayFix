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
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
//        BackgroundManager.init(this);

		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.scrollView = new HorizontalScrollView(this);
        this.setContentView(R.layout.main);
        LinearLayout main = (LinearLayout)findViewById(R.id.main);
		monthText = (TextView)findViewById(R.id.monthText);
        
        LayoutParams layoutParams1 = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT, 1.0f);
        main.addView(this.scrollView, layoutParams1);
		;
		
        this.scrollView.setOnScreenSelectedListener( new OnScreenSelectedListener() {
			public void onSelected(int selectedIndex) {
				prepareOtherViews(selectedIndex);				
			}
        });        
        //Log.d("DEBUG", StreamUtils.readAllText(getResources().openRawResource(R.raw.test)));
		selectedDate =  new Date();
        showDate(new Date());
    }
    
    private void showDate(Date date) {
    	this.selectedDate = date;
    	int childCount = this.scrollView.getChildCount(); 
		for (int i = 0; i < 3 - childCount; i++) {
			ScrollableDayView view = new ScrollableDayView(this);
	    	view.setOnDateChangedListener(onDateChangedListener);
	    	view.setOnClickListener(onClickListener);
//			view.setBackgroundDrawable(BackgroundManager.getRandomBackground());
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
	}
    
    private Date addDays(Date date, int days) {
    	Calendar cal = Calendar.getInstance();
    	cal.setTime(date);
    	cal.add(Calendar.DATE, days);
    	return cal.getTime();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	menu.add(0, MENU_SELECT_DATE, 0, "Chọn ngày").setIcon(android.R.drawable.ic_menu_day);
    	menu.add(0, MENU_SELECT_TODAY, 0, "Hôm nay").setIcon(android.R.drawable.ic_menu_today);
    	//menu.add(0, MENU_SETTINGS, 0, "Tùy chọn").setIcon(android.R.drawable.ic_menu_preferences);
    	return true;
    }

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == MENU_SELECT_DATE) {
			selectDate();
		} else if (item.getItemId() == MENU_SELECT_TODAY) {
			showDate(new Date());
		}
		return true;
	}
	public void selectDate() {
		Toast.makeText(this, "ok đang làm", Toast.LENGTH_SHORT).show();
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

			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			int month = calendar.get(Calendar.MONTH) + 1;
			int year = calendar.get(Calendar.YEAR);
			monthText.setText(month + "-" + year);


		}		
	};

}