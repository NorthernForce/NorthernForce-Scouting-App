package com.DataEntry;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.Main.R;

public class NumberDataEntryRow2019 extends DataEntryRow2019 {
    private AppCompatTextView textView;
    private AppCompatEditText editText;

    public NumberDataEntryRow2019(String columnName, int columnNumber, String text, Boolean tableview, String tableHeader, String filters){
        super("Number", columnName, columnNumber, text, tableview, tableHeader, filters);
    }

    /**
     *
     * @param c
     * @param a the activity used to get the layout inflater
     * @return A created view to be added to the data entry screen
     */
    public View getView(Context c, Activity a){
        Log.v("numberDataEntryRow", "getView was called");

        final LayoutInflater factory = a.getLayoutInflater();

        final View rootView = factory.inflate(R.layout.de_number_2019, null);

        textView = rootView.findViewById(R.id.de_number_TextView_2019);
        textView.setText(this.text);
        super.setTextViewAttributes(textView);

        editText = rootView.findViewById(R.id.de_number_EditText_2019);
        //editText.setHint(columnName);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        super.setEditTextAttributes(editText);

        return rootView;
    }

    public String getValue(){
        String value = editText.getText().toString();
        Log.v("numberDataEntryRow", "in getValue() returning " + value);
        return value;
    }
}
