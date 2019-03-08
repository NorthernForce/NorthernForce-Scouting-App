package com.DataView.model;

import android.database.Cursor;
import android.view.Gravity;

import com.DataEntry.DataEntryRow2019;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TableViewModel {
    // View Types
    public static final int NUMBER_TYPE = 1;
    public static final int STRING_TYPE = 2;
    public static final int YES_NO_TYPE = 3;

    private List<ColumnHeaderModel> mColumnHeaderModelList;
    private List<RowHeaderModel> mRowHeaderModelList;
    private List<List<CellModel>> mCellModelList;

    // Holds a map of column index to the descriptor
    private Map<Integer, DataEntryRow2019> visibleEntryRowMap = new HashMap<>();

    /**
     * Create a model based on the description of the entries
      */
    public TableViewModel(DataEntryRow2019[] entries) {

        // Create a map of only the table view data entry rows
        int columnNum = 0;
        for (DataEntryRow2019 entry : entries) {
            if (entry.isTableview()) {
                visibleEntryRowMap.put(columnNum++, entry);
            }
        }
    }
    public int getCellItemViewType(int column) {
        DataEntryRow2019 entry = visibleEntryRowMap.get(column);
        switch (entry.getType()) {
            case "Number":
                return NUMBER_TYPE;
            case "YorN":
                return YES_NO_TYPE;
            case "String":
            default:
                return STRING_TYPE;
        }
    }


    public int getColumnTextAlign(int column) {
        DataEntryRow2019 entry = visibleEntryRowMap.get(column);
        switch (entry.getType()) {
            case "Number":
                return Gravity.RIGHT;
            case "YorN":
            case "String":
            default:
                return Gravity.CENTER;
        }
    }

    private List<ColumnHeaderModel> createColumnHeaderModelList() {
        List<ColumnHeaderModel> list = new ArrayList<>();

        // Create Column Headers
        for (int i = 0; i < visibleEntryRowMap.size(); i++) {
            list.add(new ColumnHeaderModel(visibleEntryRowMap.get(i).getTableHeader()));
        }
        return list;
    }

    private List<List<CellModel>> createCellModelList(List<List<Object>> data) {

        List<List<CellModel>> lists = new ArrayList<>(data.size());
        int rowNum = 0;
        for (List<Object> dataRow : data) {
            List<CellModel> tableRow = new ArrayList<>(dataRow.size());
            int colNum = 0;
            for (Object dataObj : dataRow) {
                tableRow.add(new CellModel(rowNum++ + "-" + colNum++, dataObj == null ? "" : dataObj));
            }
            lists.add(tableRow);
        }
        return lists;
    }


    private List<List<CellModel>> createCellModelList(Cursor cursor) {

        int numRows = cursor.getCount();
        int columnCount = visibleEntryRowMap.size();
        List<List<CellModel>> lists = new ArrayList<>(numRows);


        cursor.moveToFirst();
        for (int rowNum = 0; rowNum < numRows; rowNum++) {

            List<CellModel> list = new ArrayList<>(columnCount);
            for (int colNum = 0; colNum < columnCount; colNum++) {
                DataEntryRow2019 visibleEntry = visibleEntryRowMap.get(colNum);
                String value = cursor.getString(visibleEntry.getColumnNumber() + 1); // add 1 to skip over id column
                Object valObj = value;
                if (visibleEntry.getType().equals("Number")) {
                    if ((value == null) || (value.isEmpty())) valObj = 0;
                    else valObj = Integer.valueOf(value);
                }
                list.add(new CellModel(rowNum + "-" + colNum, valObj));
            }

            // Add
            lists.add(list);
            cursor.moveToNext();
        }
        return lists;
    }

    private List<RowHeaderModel> createRowHeaderList(int size) {
        List<RowHeaderModel> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            // In this example, Row headers just shows the index of the TableView List.
            list.add(new RowHeaderModel(String.valueOf(i + 1)));
        }
        return list;
    }


    public List<ColumnHeaderModel> getColumHeaderModeList() {
        return mColumnHeaderModelList;
    }

    public List<RowHeaderModel> getRowHeaderModelList() {
        return mRowHeaderModelList;
    }

    public List<List<CellModel>> getCellModelList() {
        return mCellModelList;
    }


    public void generateListForTableView( List<List<Object>> data) {
        mColumnHeaderModelList = createColumnHeaderModelList();
        mCellModelList = createCellModelList(data);
        mRowHeaderModelList = createRowHeaderList(data.size());
    }

}
