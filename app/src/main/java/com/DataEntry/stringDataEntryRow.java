package com.DataEntry;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.Main.R;

public class stringDataEntryRow extends DataEntryRow {
    private TextView textView;
    private EditText editText;

    public stringDataEntryRow(String columnName, String text){
        super("String", columnName, text);
    }

    public View getView(Context c, Activity a){
        final LayoutInflater factory = a.getLayoutInflater();

        final View rootView = factory.inflate(R.layout.de_string, null);

        textView = (TextView) rootView.findViewById(R.id.de_string_TextView);
        textView.setText(this.text);

        editText = (EditText) rootView.findViewById(R.id.de_string_EditText);
        //editText.setHint(columnName);

        return rootView;
    }

    public String getValue(){
        return editText.getText().toString();
    }
}
