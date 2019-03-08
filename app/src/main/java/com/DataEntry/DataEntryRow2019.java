package com.DataEntry;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.SwitchCompat;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 */
public abstract class DataEntryRow2019 {

    protected String type;
    protected String columnName;
    protected int columnNumber;
    protected String text;
    protected boolean tableview = false;
    protected String tableHeader;
    protected Set<String> filters = new HashSet<>();

    protected DataEntryRow2019(String type, String columnName, int columnNumber,
                               String text, Boolean tableview, String tableHeader,
                               String filters){
        this.type = type;
        this.columnName = columnName;
        this.columnNumber = columnNumber;
        this.text = text;
        if (tableview != null) this.tableview = tableview;
        this.tableHeader = tableHeader;

        // filters should be comma-delimited.  Split them into a list
        if ((filters != null) && (filters.length() > 0)) {
            this.filters.addAll(Arrays.asList(filters.split(",")));
        }
    }

    public abstract View getView(Context c, Activity a);

    public abstract String getValue();

    public String getType(){
        return this.type;
    }

    public String getColumnName(){
        return this.columnName;
    }

    public int getColumnNumber() {
        return columnNumber;
    }

    public String getText() {
        return text;
    }

    public boolean isTableview() {
        return tableview;
    }

    public String getTableHeader() {
        return tableHeader;
    }

    public Set<String> getFilters() {
        return filters;
    }

    public boolean isFilterable(String filterName) {
        return filters.contains(filterName);
    }

    protected void setEditTextAttributes(AppCompatEditText editText) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        params.setMargins(convertDpToPixel(16),
                convertDpToPixel(16),
                convertDpToPixel(16),
                0
        );

        editText.setLayoutParams(params);
    }

    protected void setSwitchAttributes(SwitchCompat switchButton) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        params.setMargins(convertDpToPixel(16),
                convertDpToPixel(16),
                convertDpToPixel(16),
                0
        );

        switchButton.setLayoutParams(params);
    }

    protected void setSpinnerAttributes(AppCompatSpinner spinner) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        params.setMargins(convertDpToPixel(16),
                convertDpToPixel(16),
                convertDpToPixel(16),
                0
        );

        spinner.setLayoutParams(params);
    }

    protected void setCheckBoxAttributes(CheckBox checkBox) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        params.setMargins(convertDpToPixel(16),
                convertDpToPixel(16),
                convertDpToPixel(16),
                0
        );

        checkBox.setLayoutParams(params);

        //This is used to place the checkbox on the right side of the textview
        //By default, the checkbox is placed at the left side
        TypedValue typedValue = new TypedValue();
       // getTheme().resolveAttribute(android.R.attr.listChoiceIndicatorMultiple,
        //        typedValue, true);

        checkBox.setButtonDrawable(null);
        checkBox.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                typedValue.resourceId, 0);
    }

    protected void setTextViewAttributes(TextView textView) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        params.setMargins(convertDpToPixel(16),
                convertDpToPixel(16),
                0, 0
        );

        textView.setTextColor(Color.BLACK);
        textView.setLayoutParams(params);
    }

    protected void setRadioButtonAttributes(RadioButton radioButton) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        params.setMargins(convertDpToPixel(16),
                convertDpToPixel(16),
                0, 0
        );

        radioButton.setLayoutParams(params);
    }

    //This function to convert DPs to pixels
    private int convertDpToPixel(float dp) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return Math.round(px);
    }
}
