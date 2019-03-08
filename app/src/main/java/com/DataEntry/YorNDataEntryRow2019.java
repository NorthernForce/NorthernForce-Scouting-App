package com.DataEntry;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.Main.R;

public class YorNDataEntryRow2019 extends DataEntryRow2019 {
    private TextView textView;
    private SwitchCompat switchbox;

    public YorNDataEntryRow2019(String columnName, int columnNumber, String text, Boolean tableview, String tableHeader, String filters){
        super("YorN", columnName, columnNumber, text, tableview, tableHeader, filters);
    }

    public View getView(Context c, Activity a){

        final LayoutInflater factory = a.getLayoutInflater();

        final View rootView = factory.inflate(R.layout.de_yes_or_no_2019, null);
/*
        textView = rootView.findViewById(R.id.de_YorN_TextView_2019);
        textView.setText(this.text);

*/
        switchbox = rootView.findViewById(R.id.de_YorN_checkbox_2019);
        switchbox.setText(this.text);
        super.setSwitchAttributes(switchbox);

        return rootView;
    }

    public String getValue(){
        return switchbox.isChecked() ? "yes" : "no";
    }
}
