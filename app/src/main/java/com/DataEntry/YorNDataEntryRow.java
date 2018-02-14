package com.DataEntry;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.alex.Main.R;

import java.util.ArrayList;

public class YorNDataEntryRow extends DataEntryRow {
    private TextView textView;
    private RadioGroup radioGroup;
    private RadioButton yesButton;
    private RadioButton noButton;

    public YorNDataEntryRow(String columnName, String text){
        super("YorN", columnName, text);
    }

    public View getView(Context c, Activity a){

        final LayoutInflater factory = a.getLayoutInflater();

        final View rootView = factory.inflate(R.layout.de_yes_or_no, null);

        textView = (TextView) rootView.findViewById(R.id.de_YorN_TextView);
        yesButton = (RadioButton) rootView.findViewById(R.id.de_YorN_yesButton);
        noButton = (RadioButton) rootView.findViewById(R.id.de_YorN_noButton);

        textView.setText(this.text);

        return rootView;
    }

    public String getValue(){
        if(yesButton.isChecked()){
            return "yes";
        }
        if(noButton.isChecked()){
            return "no";
        }
        else{
            return "";
        }
    }
}
