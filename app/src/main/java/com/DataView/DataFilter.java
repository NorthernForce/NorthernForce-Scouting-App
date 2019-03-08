package com.DataView;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;

import com.Main.R;

public class DataFilter {
    private SwitchCompat switchControl;
    private String text;

    public DataFilter(String text){
        super();
        this.text = text;
    }

    public View getView(Context c, Activity a){
        final LayoutInflater factory = a.getLayoutInflater();

        final View rootView = factory.inflate(R.layout.data_view_filter_layout, null);

        switchControl = rootView.findViewById(R.id.sampleFilterSwitch);
        System.out.println("Creating new view with name: " + this.text + " on object: " + switchControl);
        switchControl.setText(this.text);

        return rootView;
    }

}
