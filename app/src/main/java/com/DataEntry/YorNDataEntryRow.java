package com.DataEntry;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.DataEntry.DataEntryRow;

public class YorNDataEntryRow extends DataEntryRow {
    private TextView textView;
    private RadioGroup radioGroup;
    private RadioButton yesButton;
    private RadioButton noButton;

    public YorNDataEntryRow(String columnName, String text){
        super("YorN", columnName, text);
    }

    public View getView(Context c){
        textView = new TextView(c);
        textView.setText(columnName);

        radioGroup = new RadioGroup(c);

        yesButton = new RadioButton(c);
        noButton = new RadioButton(c);

        radioGroup.addView(yesButton);
        radioGroup.addView(noButton);


        LinearLayout view = new LinearLayout(c);
        view.addView(this.textView);
        view.addView(this.radioGroup);

        return view;
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
