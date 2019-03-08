package com.DataEntry;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;

import com.Main.R;

public class StringDataEntryRow2019 extends DataEntryRow2019 {
    private AppCompatTextView textView;
    private AppCompatEditText editText;

    public StringDataEntryRow2019(String columnName, int columnNumber, String text, Boolean tableview, String tableHeader, String filters){
        super("String", columnName, columnNumber, text, tableview, tableHeader, filters);
    }

    public View getView(Context c, Activity a){
        final LayoutInflater factory = a.getLayoutInflater();

        final View rootView = factory.inflate(R.layout.de_string_2019, null);

        textView = rootView.findViewById(R.id.de_string_TextView_2019);
        textView.setText(this.text);
        super.setTextViewAttributes(textView);

        editText = rootView.findViewById(R.id.de_string_EditText_2019);
        super.setEditTextAttributes(editText);
        //editText.setHint(columnName);

        return rootView;
    }

    public String getValue(){
        return editText.getText().toString();
    }
}
