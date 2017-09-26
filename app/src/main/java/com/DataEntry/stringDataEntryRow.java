package com.DataEntry;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class stringDataEntryRow extends DataEntryRow {
    private TextView textView;
    private EditText editText;

    public stringDataEntryRow(String columnName, String text){
        super("String", columnName, text);
    }

    public View getView(Context c, Activity a){
        textView = new TextView(c);
        textView.setText(this.columnName);
        textView.setTextColor(Color.BLACK);

        editText = new EditText(c);
        editText.setHint(columnName);

        LinearLayout view = new LinearLayout(c);
        view.addView(this.textView);
        view.addView(this.editText);

        return view;
    }

    public String getValue(){
        return editText.getText().toString();
    }
}
