package com.hdpsolutions.lichngay.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hdpsolutions.lichngay.widget.adapters.NumericWheelAdapter;

/**
 * Created by HP 6300 Pro on 4/17/2018.
 */

public class DateNumericAdapter extends NumericWheelAdapter {
    // Index of current item
    int currentItem;
    // Index of item to be highlighted
    int currentValue;

    /**
     * Constructor
     */
    public DateNumericAdapter(Context context, int minValue, int maxValue, int current) {
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
