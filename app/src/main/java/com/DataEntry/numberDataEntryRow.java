package com.DataEntry;

import android.content.Context;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.DataEntry.DataEntryRow;

public class numberDataEntryRow extends DataEntryRow {
    private TextView textView;
    private EditText editText;

    public numberDataEntryRow(String columnName, String text){
        super("Number", columnName, text);
    }

    public View getView(Context c){
        Log.v("numberDataEntryRow", "getVIew was called");
        textView = new TextView(c);
        textView.setText(columnName);

        editText = new EditText(c);
        editText.setHint(columnName);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);

        LinearLayout view = new LinearLayout(c);

        view.addView(this.textView);
        view.addView(this.editText);

        return view;
    }

    public String getValue(){
        String value = editText.getText().toString();
        Log.v("numberDataEntryRow", "in getValue() returning " + value);
        return value;
    }
}
