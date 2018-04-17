package com.hdpsolutions.lichngay.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PorterDuff.Mode;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hdpsolutions.lichngay.calender.EventManager;
import com.hdpsolutions.lichngay.R;
import com.hdpsolutions.lichngay.calender.VietCalendar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DayView extends FrameLayout {
	private TextView dayOfMonthText;
	private TextView dayOfWeekText;
	private TextView noteText;
	private ImageView bground;

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
		View.inflate(getContext(), R.layout.layout_ngay, this);
		this.setFocusable(false);
		this.setFocusableInTouchMode(false);
		this.setDrawingCacheEnabled(true);
		this.dayOfMonthText = findViewById(R.id.dayOfMonthText);
		this.dayOfWeekText = findViewById(R.id.dayOfWeekText);
		this.noteText = findViewById(R.id.noteText);

		this.bground = findViewById(R.id.backgroundnen);
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

		VietCalendar.Holiday holiday = VietCalendar.getHoliday(date);
		if (dayOfWeek == 1) {
			this.dayOfMonthText.setTextColor(this.dayOfWeekColor);
			this.dayOfWeekText.setTextColor(this.weekendColor);
			this.noteText.setTextColor(this.dayOfWeekColor);

		} else if (holiday != null && holiday.isOffDay()) {
			this.dayOfMonthText.setTextColor(this.dayOfWeekColor);
			this.dayOfWeekText.setTextColor(this.holidayColor);
			this.noteText.setTextColor(this.holidayColor);


		} else {
			this.dayOfMonthText.setTextColor(this.dayOfWeekColor);			
			this.dayOfWeekText.setTextColor(this.dayOfWeekColor);
			this.noteText.setTextColor(this.dayOfWeekColor);

		}
		this.dayOfMonthText.setText(dayOfMonth + "");
		this.dayOfWeekText.setText(VietCalendar.getDayOfWeekText(dayOfWeek));
		if (holiday != null) {
			this.noteText.setText(holiday.getDescription());
		} else {
			this.noteText.setText(famousSaying);
		}

//		if (eventSumarize != null && eventSumarize.length() > 0) {
//			this.noteText.setTextColor(this.eventColor);
//			this.noteText.setText(eventSumarize);
//		}
		
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
	

}
