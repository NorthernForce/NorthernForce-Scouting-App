package com.DataView;

import android.database.Cursor;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.DataEntry.DataEntryRow;
import com.Main.MySQLiteHelper;
import com.Main.UIDatabaseInterface;

/**
 * Created by alex on 8/11/15.
 */
public class ViewDataAdapter extends BaseAdapter {

    private MySQLiteHelper mySQLiteHelper;
    private int count;
    private ViewDataActivity viewDataActivity;
    private String currentTable;

    public ViewDataAdapter(MySQLiteHelper mySQLiteHelper, ViewDataActivity viewDataActivity){
        this.currentTable = UIDatabaseInterface.getCurrentDataViewTable();
        this.mySQLiteHelper = mySQLiteHelper;

        Cursor c = mySQLiteHelper.selectFromTable(currentTable, "*");

        this.count = c.getCount();

        this.viewDataActivity = viewDataActivity;
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView view = new TextView(viewDataActivity);
        view.setText("");

        DataEntryRow[] rows = UIDatabaseInterface.getDataEntryRows();

        Cursor cursor = mySQLiteHelper.selectFromTable(currentTable, "*");
        cursor.moveToPosition(position);
        //i = 1 to skip _id column
        for (int i = 1; i < cursor.getColumnCount(); i++) {
            Log.i("ViewDataAdapter", "adding following to text view " + rows[i - 1].getText() + ": " + cursor.getString(i));
            view.setText(view.getText() + "\n" +/* cursor.getColumnName(i)*/ rows[i - 1].getText() + ": " + cursor.getString(i));
        }

        return view;
    }

    public void setCurrentViewTable(String table){
        this.currentTable = table;
    }
}
