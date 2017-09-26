package com.DataEntry;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.alex.Main.R;

public class numberDataEntryRow extends DataEntryRow {
    private TextView textView;
    private EditText editText;

    public numberDataEntryRow(String columnName, String text){
        super("Number", columnName, text);
    }

    public View getView(Context c, Activity a){
        Log.v("numberDataEntryRow", "getView was called");

        final LayoutInflater factory = a.getLayoutInflater();

        final View rootView = factory.inflate(R.layout.de_number, null);

        LinearLayout linearLayout = (LinearLayout) rootView.findViewById(R.id.de_number_LinearLayout);
        int count = linearLayout.getChildCount();
        for(int i = 0; i < count; i++){
            Log.v("numberDataEntryRow", "i is: " + i);
            View childView = linearLayout.getChildAt(i);
            Log.v("numberDataEntryRow", "childView is " + childView.toString());
            //Log.v("numberDataEntryRow", "linear layout child number " + count + " is " + linearLayout.getChildAt(count).getId());
        }
        textView = (TextView) linearLayout.getChildAt(0);
        textView.setText(columnName);
        //textView.setTextColor(Color.BLACK);

        editText = (EditText) linearLayout.getChildAt(1);
        editText.setHint(columnName);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);

        //LinearLayout view = new LinearLayout(c);

        //view.addView(this.textView);
        //view.addView(this.editText);

        return linearLayout;
    }

    public String getValue(){
        String value = editText.getText().toString();
        Log.v("numberDataEntryRow", "in getValue() returning " + value);
        return value;
    }
}
