package com.DataView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.DataEntry.DataEntryRow2019;
import com.DataView.holder.CellViewHolder;
import com.DataView.holder.ColumnHeaderViewHolder;
import com.DataView.holder.RowHeaderViewHolder;
import com.DataView.model.CellModel;
import com.DataView.model.ColumnHeaderModel;
import com.DataView.model.RowHeaderModel;
import com.DataView.model.TableViewModel;
import com.Main.R;
import com.evrencoskun.tableview.adapter.AbstractTableAdapter;
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractSorterViewHolder;
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder;

import java.util.List;

public class TableAdapter extends AbstractTableAdapter<ColumnHeaderModel, RowHeaderModel, CellModel> {

    private TableViewModel myTableViewModel;
    private DataEntryRow2019[] allEntries;

    public TableAdapter(Context p_jContext, DataEntryRow2019[] entries) {
        super(p_jContext);
        this.allEntries = entries;
        this.myTableViewModel = new TableViewModel(entries);
    }


    @Override
    public AbstractViewHolder onCreateCellViewHolder(ViewGroup parent, int viewType) {
        View layout;

        switch (viewType) {
            /*
            case TableViewModel.GENDER_TYPE:
                // Get gender cell xml Layout
                layout = LayoutInflater.from(mContext).inflate(R.layout
                        .tableview_gender_cell_layout, parent, false);

                return new GenderCellViewHolder(layout);


            case MyTableViewModel.MONEY_TYPE:
                // Get money cell xml Layout
                layout = LayoutInflater.from(mContext).inflate(R.layout
                        .tableview_money_cell_layout, parent, false);

                // Create the relevant view holder
                return new MoneyCellViewHolder(layout);
                */
            default:
                // Get default Cell xml Layout
                layout = LayoutInflater.from(mContext).inflate(R.layout.tableview_cell_layout,
                        parent, false);

                // Create a Cell ViewHolder
                return new CellViewHolder(layout);
        }
    }

    @Override
    public void onBindCellViewHolder(AbstractViewHolder holder, Object p_jValue, int
            p_nXPosition, int p_nYPosition) {
        CellModel cell = (CellModel) p_jValue;

        if (holder instanceof CellViewHolder) {
            // Get the holder to update cell item text
            ((CellViewHolder) holder).setCellModel(cell, p_nXPosition);
        }

        // If your TableView should have auto resize for cells & columns.
        // Then you should consider the below lines. Otherwise, you can ignore them.

        // It is necessary to remeasure itself.
        holder.itemView.getLayoutParams().width = LinearLayout.LayoutParams.WRAP_CONTENT;
        holder.itemView.requestLayout();
    }

    @Override
    public AbstractSorterViewHolder onCreateColumnHeaderViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(mContext).inflate(R.layout
                .tableview_column_header_layout, parent, false);

        return new ColumnHeaderViewHolder(layout, getTableView());
    }

    @Override
    public void onBindColumnHeaderViewHolder(AbstractViewHolder holder, Object p_jValue, int
            p_nXPosition) {
        ColumnHeaderModel columnHeader = (ColumnHeaderModel) p_jValue;

        // Get the holder to update cell item text
        ColumnHeaderViewHolder columnHeaderViewHolder = (ColumnHeaderViewHolder) holder;

        columnHeaderViewHolder.setColumnHeaderModel(columnHeader, p_nXPosition);
    }

    @Override
    public AbstractViewHolder onCreateRowHeaderViewHolder(ViewGroup parent, int viewType) {

        // Get Row Header xml Layout
        View layout = LayoutInflater.from(mContext).inflate(R.layout.tableview_row_header_layout,
                parent, false);

        // Create a Row Header ViewHolder
        return new RowHeaderViewHolder(layout);
    }

    @Override
    public void onBindRowHeaderViewHolder(AbstractViewHolder holder, Object p_jValue, int
            p_nYPosition) {

        RowHeaderModel rowHeaderModel = (RowHeaderModel) p_jValue;

        RowHeaderViewHolder rowHeaderViewHolder = (RowHeaderViewHolder) holder;
        rowHeaderViewHolder.row_header_textview.setText(rowHeaderModel.getData());

    }

    @Override
    public View onCreateCornerView() {
        return LayoutInflater.from(mContext).inflate(R.layout.tableview_corner_layout, null, false);
    }

    @Override
    public int getColumnHeaderItemViewType(int position) {
        return 0;
    }

    @Override
    public int getRowHeaderItemViewType(int position) {
        return 0;
    }

    @Override
    public int getCellItemViewType(int position) {
        return myTableViewModel.getCellItemViewType(position);
    }


    /**
     * Sets the data on the table
     */
    public void setData( List<List<Object>> data) {
        // Generate the lists that are used to TableViewAdapter
        myTableViewModel.generateListForTableView(data);

        // Now we got what we need to show on TableView.
        setAllItems(myTableViewModel.getColumHeaderModeList(), myTableViewModel
                .getRowHeaderModelList(), myTableViewModel.getCellModelList());
    }

}
