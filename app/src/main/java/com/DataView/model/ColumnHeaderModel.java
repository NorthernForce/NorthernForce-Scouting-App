package com.DataView.model;

public class ColumnHeaderModel {

    private String mData;

    public ColumnHeaderModel(String mData) {
        this.mData = mData;
    }

    public String getData() {
        return "  " + mData + "        ";
    }
}
