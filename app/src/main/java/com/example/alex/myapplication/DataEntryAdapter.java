package com.example.alex.myapplication;

import android.content.Context;
import android.text.Editable;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Arrays;

/**
 * Created by alex on 4/20/15.
 */
public class DataEntryAdapter extends ArrayAdapter<DataEntryRow> {

    private Context context;
    private DataEntryRow[] values;

    public DataEntryAdapter(Context context, DataEntryRow[] objects) {
        super(context, R.layout.data_entry_layout, objects);
        this.context = context;
        this.values = objects;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = null;

        if(values[position].getType().equals("String")){
            rowView = inflater.inflate(R.layout.string_entry, parent, false);
            TextView textView = (TextView) rowView.findViewById(R.id.string_entry_textView);
            textView.setText(values[position].getColumnName());

            EditText editText = (EditText) rowView.findViewById(R.id.string_entry_editText);
            editText.setHint(values[position].getColumnName());
            editText.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    EditText editText = (EditText) v;
                    Log.v("FOO", "Text was " + editText.getText());
                    Log.v("FOO", "ColumnName is " + editText.getHint());
                    for (int i = 0;i<values.length;i++) {
                        if (values[i].getColumnName().equals(editText.getHint())) {
                            Log.v("FOO", "The position you need in values is " + i);
                            values[i].setValue(editText.getText().toString());
                            return false;
                        }
                    }
                    return false;
                }
            });
        }
        if(values[position].getType().equals("int")){
            rowView = inflater.inflate(R.layout.int_entry, parent, false);
            TextView textView = (TextView) rowView.findViewById(R.id.int_entry_textView);
            textView.setText(values[position].getColumnName());

            EditText editText = (EditText) rowView.findViewById(R.id.int_entry_editText);
            editText.setHint(values[position].getColumnName());
            editText.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    EditText editText = (EditText) v;
                    Log.v("FOO", "Text was " + editText.getText());
                    Log.v("FOO", "ColumnName is " + editText.getHint());
                    for (int i = 0;i<values.length;i++) {
                        if (values[i].getColumnName().equals(editText.getHint())) {
                            Log.v("FOO", "The position you need in values is " + i);
                            values[i].setValue(editText.getText().toString());
                            return false;
                            }
                        }
                    return false;
                }
            });
        }
        if(rowView == null){
            Log.e("DataEntryAdapter", "rowView was null at position: " + position);
        }

        return rowView;
    }
}
