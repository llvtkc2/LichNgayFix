package com.hdpsolutions.lichngay.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hdpsolutions.lichngay.EventManager;
import com.hdpsolutions.lichngay.R;
import com.hdpsolutions.lichngay.VNMDate;
import com.hdpsolutions.lichngay.VietCalendar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class DayView extends FrameLayout {
	private TextView dayOfMonthText;
	private TextView dayOfWeekText;
	private TextView noteText;
	private ImageView bground;
	
	protected TextView vnmHourText;
	protected TextView vnmHourInText;
	protected TextView vnmDayOfMonthText;
	protected TextView vnmDayOfMonthInText;
	protected TextView vnmMonthText;
	protected TextView vnmMonthInText;
	protected TextView vnmYearText;
	protected TextView vnmYearInText;
	
	private Date displayDate;
			
	private int dayOfWeekColor;
	private int weekendColor;
	private int holidayColor;
	private int eventColor;


	private int[] bg = {R.drawable.body,R.drawable.body1,
						R.drawable.body2,R.drawable.body3,R.drawable.body4,R.drawable.body5,R.drawable.body6,R.drawable.body7,R.drawable.body8};
	
	public DayView(Context context) {
		super(context);
		init(context);
	}
	
	public DayView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	
	private void init(Context context) {
		// Inflate the view from the layout resource.
		View.inflate(getContext(), R.layout.vnm_day_viewer, this);
		this.setFocusable(false);
		this.setFocusableInTouchMode(false);
		this.setDrawingCacheEnabled(true);
		this.dayOfMonthText = (TextView)findViewById(R.id.dayOfMonthText);
		this.dayOfWeekText = (TextView)findViewById(R.id.dayOfWeekText);
		this.noteText = (TextView)findViewById(R.id.noteText);
		
		this.vnmHourText = (TextView) findViewById(R.id.vnmHourText);
		this.vnmHourInText = (TextView) findViewById(R.id.vnmHourInText);
		this.vnmDayOfMonthText = (TextView) findViewById(R.id.vnmDayOfMonthText);
		this.vnmDayOfMonthInText = (TextView) findViewById(R.id.vnmDayOfMonthInText);
		this.vnmMonthText = (TextView) findViewById(R.id.vnmMonthText);
		this.vnmMonthInText = (TextView) findViewById(R.id.vnmMonthInText);
		this.bground = findViewById(R.id.backgroundnen);
//		this.vnmYearText = (TextView) findViewById(R.id.vnmYearText);
//		this.vnmYearInText = (TextView) findViewById(R.id.vnmYearInText);
		this.dayOfWeekColor = getResources().getColor(R.color.dayOfWeekColor);
		this.weekendColor = getResources().getColor(R.color.weekendColor);
		this.holidayColor = getResources().getColor(R.color.holidayColor);
		this.eventColor = getResources().getColor(R.color.eventColor);

		
		this.displayDate = new Date();
		this.setDate(this.displayDate);		
	}
	
	private String getQuoteOfDay() {
		String[] quotes = getResources().getStringArray(R.array.quotes);
		int index = Calendar.getInstance().get(Calendar.MILLISECOND) % quotes.length;
		return quotes[index];
	}
	
	public void setDate(Date date) {
		Glide.with(this).load(bg[(int)(Math.random()*5+1)]).into(bground);

		EventManager eventManager = new EventManager(getContext().getContentResolver());
		String eventSumarize = eventManager.getSumarize(date);
		this.displayDate = date;
		String famousSaying = getQuoteOfDay();
		this.noteText.setText(famousSaying);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
		int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		int month = calendar.get(Calendar.MONTH) + 1;
		int year = calendar.get(Calendar.YEAR);
		
		VNMDate vnmDate = VietCalendar.convertSolar2LunarInVietnamese(date);
		VietCalendar.Holiday holiday = VietCalendar.getHoliday(date);
		if (dayOfWeek == 1) {
			this.dayOfMonthText.setTextColor(this.dayOfWeekColor);
			this.dayOfWeekText.setTextColor(this.weekendColor);
			this.noteText.setTextColor(this.dayOfWeekColor);
//			this.monthText.setTextColor(this.dayOfWeekColor);
		} else if (holiday != null && holiday.isOffDay()) {
			this.dayOfMonthText.setTextColor(this.dayOfWeekColor);
			this.dayOfWeekText.setTextColor(this.holidayColor);
			this.noteText.setTextColor(this.holidayColor);
//			this.monthText.setTextColor(this.dayOfWeekColor);
			if (!holiday.isSolar()) {
				this.vnmDayOfMonthText.setTextColor(this.holidayColor);
				this.vnmMonthText.setTextColor(this.dayOfWeekColor);
//				this.vnmYearText.setTextColor(this.holidayColor);
			}
		} else {
			this.dayOfMonthText.setTextColor(this.dayOfWeekColor);			
			this.dayOfWeekText.setTextColor(this.dayOfWeekColor);
			this.noteText.setTextColor(this.dayOfWeekColor);
//			this.monthText.setTextColor(this.dayOfWeekColor);
		}
		this.dayOfMonthText.setText(dayOfMonth + "");
		this.dayOfWeekText.setText(VietCalendar.getDayOfWeekText(dayOfWeek));
		if (holiday != null) {
			this.noteText.setText(holiday.getDescription());
		} else {
			this.noteText.setText(famousSaying);
		}
		
		this.vnmHourText.setText(hour + ":" + minute);
		this.vnmDayOfMonthText.setText(vnmDate.getDayOfMonth() + "");
		this.vnmMonthText.setText(vnmDate.getMonth() + "");
//		this.vnmYearText.setText(vnmDate.getYear() + "");
		
		String[] vnmCalendarTexts = VietCalendar.getCanChiInfo(vnmDate.getDayOfMonth(), vnmDate.getMonth(), vnmDate.getYear(), dayOfMonth, month, year);
		
		this.vnmHourInText.setText(vnmCalendarTexts[VietCalendar.HOUR]);
		this.vnmDayOfMonthInText.setText(vnmCalendarTexts[VietCalendar.DAY]);
		this.vnmMonthInText.setText(vnmCalendarTexts[VietCalendar.MONTH]);
//		this.vnmYearInText.setText(vnmCalendarTexts[VietCalendar.YEAR]);
		if (eventSumarize != null && eventSumarize.length() > 0) {
			this.noteText.setTextColor(this.eventColor);
			this.noteText.setText(eventSumarize);
		}
		
		this.dayOfMonthText.setShadowLayer(1.2f, 1.0f, 1.0f, getResources().getColor(R.color.shadowColor));		
		this.invalidate();
	}		
	
	public void setNote(String note) {
		this.noteText.setText(note);
	}
	
	public Date getDisplayDate() {
		return displayDate;
	}

	public String getDisplayDateInText() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
		return simpleDateFormat.format(this.displayDate);
	}	

	@Override
	protected void onDraw(Canvas canvas) {
		Bitmap cacheBitmap = getDrawingCache();
		if (cacheBitmap != null) {			
			//canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), clearPaint);
			canvas.drawColor(0, Mode.CLEAR);
			canvas.drawBitmap(cacheBitmap, 0, 0, null); 
		} else {
			super.onDraw(canvas);
		}
	}
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		Log.d("DEBUG", "Called Onlayout...");
		this.destroyDrawingCache();
		super.onLayout(changed, l, t, r, b);
		this.buildDrawingCache();
	}
	
//	@Override
//	public boolean onTouchEvent(MotionEvent event) {
//		return true;
//	}
}
