package com.hdpsolutions.lichngay;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.hdpsolutions.lichngay.calender.VietCalendar;
import com.hdpsolutions.lichngay.objects.Global;
import com.hdpsolutions.lichngay.widget.OnWheelScrollListener;
import com.hdpsolutions.lichngay.widget.WheelView;
import com.hdpsolutions.lichngay.widget.adapters.ArrayWheelAdapter;
import com.hdpsolutions.lichngay.widget.adapters.NumericWheelAdapter;

import java.util.Calendar;
import java.util.Date;

import javax.microedition.khronos.opengles.GL;

public class DoiNgayActivity extends AppCompatActivity implements View.OnClickListener{

    WheelView monthSolar,yearSolar,daySolar,monthLunar,yearLunar,dayLunar;
    String months[];
    int lunar[];
    private TextView mLichNgay,mLichThang,mDoiNgay,mNhieuHon;
    private Button mXemNgay;

    Calendar calendar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doingay);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        monthSolar = findViewById(R.id.monthSolar);
        yearSolar = findViewById(R.id.yearSolar);
        daySolar = findViewById(R.id.daySolar);

        monthLunar =  findViewById(R.id.monthLunar);
        yearLunar =  findViewById(R.id.yearLunar);
        dayLunar = findViewById(R.id.dayLunar);
        mLichNgay =  findViewById(R.id.lichngay);
        mLichThang = findViewById(R.id.lichthang);
        mDoiNgay = findViewById(R.id.doingay);
        mNhieuHon = findViewById(R.id.nhieuhon);
        mXemNgay = findViewById(R.id.xemngay);
        mXemNgay.setOnClickListener(this);
        mLichNgay.setOnClickListener(this);


        // Hiển thị ngày hiện tại
        calendar = Calendar.getInstance();
        int curDaySolar = calendar.get(Calendar.DAY_OF_MONTH);
        int curMonthSolar = calendar.get(Calendar.MONTH);
        int curYearSolar = calendar.get(Calendar.YEAR);



        OnWheelScrollListener listenerSolar = new OnWheelScrollListener() {
            @Override
            public void onScrollingStarted(WheelView wheel) {

            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                int day = daySolar.getCurrentItem();
                int month = monthSolar.getCurrentItem();
                int year = yearSolar.getCurrentItem();
                int lunar[] = VietCalendar.convertSolar2Lunar(day+1,month+1,year+1900, Global.timeZone);
                dayLunar.setCurrentItem(lunar[0]-1);
                monthLunar.setCurrentItem(lunar[1]-1);
                yearLunar.setCurrentItem(lunar[2]-1900);
            }
        };


        OnWheelScrollListener listenerLunar = new OnWheelScrollListener() {
            @Override
            public void onScrollingStarted(WheelView wheel) {

            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                updateDaysnMonthsLunar(yearLunar, monthLunar);
                int day = dayLunar.getCurrentItem();
                int month = monthLunar.getCurrentItem();
                int year = yearLunar.getCurrentItem();
                int solar[] = VietCalendar.convertLunar2Solar(day+1,month+1,year+1900, Global.timeZone);
                daySolar.setCurrentItem(solar[0]-1);
                monthSolar.setCurrentItem(solar[1]-1);
                yearSolar.setCurrentItem(solar[2]-1900);
            }
        };



        //Hiển thị nên ngày dương
        // monthSolar
        months = new String[12];
        for(int i=0;i<months.length;i++){
            months[i] = "Tháng " + (i+1);
        }
        monthSolar.setViewAdapter(new DateArrayAdapter(this, months, curMonthSolar));
        monthSolar.setCurrentItem(curMonthSolar);
        monthSolar.addScrollingListener(listenerSolar);

        // yearSolar
        yearSolar.setViewAdapter(new DateNumericAdapter(this, 1900, 2030, curYearSolar-1900));
        yearSolar.setCurrentItem(curYearSolar-1900);
        yearSolar.addScrollingListener(listenerSolar);

        //daySolar
        updateDays(yearSolar, monthSolar, daySolar);
        daySolar.setCurrentItem(curDaySolar-1);
        daySolar.addScrollingListener(listenerSolar);

        //Chuyển đổi dương sang âm
        lunar = VietCalendar.convertSolar2Lunar(curDaySolar,curMonthSolar+1, curYearSolar, Global.timeZone);


        //Hiển thị nên ngày âm
        // yearLunar
        int curYearLunar = lunar[2];
        yearLunar.setViewAdapter(new DateNumericAdapter(this, 1900, 2030,curYearLunar-1900));
        yearLunar.setCurrentItem(curYearLunar-1900);
        yearLunar.addScrollingListener(listenerLunar);

        //dayLunar
        int curDayLunar = lunar[0];
        dayLunar.setViewAdapter(new DateNumericAdapter(this,1,30,curDayLunar-1));
        dayLunar.setCurrentItem(curDayLunar-1);
        dayLunar.addScrollingListener(listenerLunar);



        // monthLunar
        int curMonthLunar = lunar[1];
        updateDaysnMonthsLunar(yearLunar, monthLunar);
        monthLunar.setCurrentItem(curMonthLunar-1);
        monthLunar.addScrollingListener(listenerLunar);




    }


    // Thay đổi ngày 30-31-29 khi chọn tháng năm
    void updateDays(WheelView year, WheelView month, WheelView day) {
        calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + year.getCurrentItem());
        calendar.set(Calendar.MONTH, month.getCurrentItem());

        int maxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        day.setViewAdapter(new DateNumericAdapter(this, 1, maxDays, calendar.get(Calendar.DAY_OF_MONTH) - 1));
        int curDay = Math.min(maxDays, day.getCurrentItem() + 1);
        day.setCurrentItem(curDay - 1, true);


    }

    void updateDaysnMonthsLunar(WheelView year, WheelView month) {
        int iyear = year.getCurrentItem()+1900;
        int leapMonth = VietCalendar.getLeapMonth(iyear, Global.timeZone);
        if(leapMonth != -1){
            //change month lunar
            months = new String[13];
            for(int i=0;i<months.length;i++){
                months[i] = "Tháng " + (i+1);
                if(i == leapMonth){
                    months[i] = "Tháng " + (leapMonth) + "+";
                } else if(i > leapMonth){
                    months[i] = "Tháng " + i;
                }
            }
        }else{
            months = new String[12];
            for(int i=0;i<months.length;i++){
                months[i] = "Tháng " + (i+1);
            }
        }
        month.setViewAdapter(new DateArrayAdapter(this, months, lunar[1]-1));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.lichngay: {
                finish();
                break;

            }
            case R.id.xemngay: {
                int day = dayLunar.getCurrentItem();
                int month = monthLunar.getCurrentItem();
                int year = yearLunar.getCurrentItem();
                int solar[] = VietCalendar.convertLunar2Solar(day+1,month+1,year+1900, Global.timeZone);
                Intent intent =  new Intent();
                intent.putExtra("date",solar);
                setResult(RESULT_OK, intent);
                finish();
                break;

            }
        }
    }

    public class DateNumericAdapter extends NumericWheelAdapter {
        // Index of current item
        int currentItem;
        // Index of item to be highlighted
        int currentValue;

        /**
         * Constructor
         */
        private DateNumericAdapter(Context context, int minValue, int maxValue, int current) {
            super(context, minValue, maxValue);
            this.currentValue = current;
            setTextSize(21);
        }

        @Override
        protected void configureTextView(TextView view) {
            super.configureTextView(view);
            view.setTextColor(0xFFffffff);
            if (currentItem == currentValue) {
                view.setTextColor(0xFFcc0000);
            }
            view.setTypeface(Typeface.SANS_SERIF);
        }

        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            currentItem = index;
            return super.getItem(index, cachedView, parent);
        }
    }

    /**
     * Adapter for string based wheel. Highlights the current value.
     */
    public class DateArrayAdapter extends ArrayWheelAdapter<String> {
        // Index of current item
        int currentItem;
        // Index of item to be highlighted
        int currentValue;

        /**
         * Constructor
         */
        private DateArrayAdapter(Context context, String[] items, int current) {
            super(context, items);
            this.currentValue = current;
            setTextSize(21);
        }

        @Override
        protected void configureTextView(TextView view) {
            super.configureTextView(view);
            view.setTextColor(0xFFffffff);
            if (currentItem == currentValue) {
                view.setTextColor(0xFFcc0000);
            }
            view.setTypeface(Typeface.SANS_SERIF);
        }

        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            currentItem = index;
            return super.getItem(index, cachedView, parent);
        }
    }

}
