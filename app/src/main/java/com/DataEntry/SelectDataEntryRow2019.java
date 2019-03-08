package com.DataEntry;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;

import com.Main.R;

public class SelectDataEntryRow2019 extends DataEntryRow2019 {
    private AppCompatTextView textView;
    private AppCompatSpinner selectBox;
    private String options;

    public SelectDataEntryRow2019(String columnName, int columnNumber, String text, String options, Boolean tableview, String tableHeader, String filters){
        super("String", columnName, columnNumber, text, tableview, tableHeader, filters);
        this.options = options;
    }

    public View getView(Context c, Activity a){
        final LayoutInflater factory = a.getLayoutInflater();

        final View rootView = factory.inflate(R.layout.de_select_2019, null);

        textView = rootView.findViewById(R.id.de_select_TextView_2019);
        textView.setText(this.text);
        super.setTextViewAttributes(textView);

        selectBox = rootView.findViewById(R.id.de_select_Select_2019);

        String[] optionsArray = options.split(",");
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(c, android.R.layout.simple_spinner_item, optionsArray);
        selectBox.setAdapter(spinnerArrayAdapter);
        super.setSpinnerAttributes(selectBox);

        return rootView;
    }

    public String getValue(){
        return selectBox.getSelectedItem().toString();
    }
}
