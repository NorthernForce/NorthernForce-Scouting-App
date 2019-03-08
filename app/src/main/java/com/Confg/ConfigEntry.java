package com.Confg;

import java.io.Serializable;

/**
 * Created by alex on 4/18/15.
 */
public class ConfigEntry implements Serializable{

    private String type;
    private String colName;
    private String text;
    private String options;
    private String selectOptions;
    private Boolean tableview;
    private String tableHeader;
    private String filters;

    public ConfigEntry(){

    }

    public ConfigEntry(String type, String colName, String text, String selectOptions, Boolean tableview, String tableHeader, int table){
        this.type = type;
        this.colName = colName;
        this.text = text;
        this.selectOptions = selectOptions;
        this.tableview = tableview;
        this.tableHeader = tableHeader;
    }

    public ConfigEntry(String type, String colName, String text, String selectOptions, Boolean tableview, String tableHeader, String options, String filters){
        this.type = type;
        this.colName = colName;
        this.text = text;
        this.options = options;
        this.selectOptions = selectOptions;
        this.tableview = tableview;
        this.tableHeader = tableHeader;
        this.filters = filters;
    }

    public String getType(){
        return this.type;
    }

    public void setType(String type){
        this.type = type;
    }

    public String getText(){
        return this.text;
    }

    public void setText(String text){
        this.text = text;
    }

    public String getColName() {
        return colName;
    }

    public void setColName(String colName) {
        this.colName = colName;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public String getSelectOptions() {
        return selectOptions;
    }

    public void setSelectOptions(String selectOptions) {
        this.selectOptions = selectOptions;
    }

    public Boolean getTableview() {
        return tableview;
    }

    public void setTableview(Boolean tableview) {
        this.tableview = tableview;
    }

    public String getTableHeader() {
        return tableHeader;
    }

    public void setTableHeader(String tableHeader) {
        this.tableHeader = tableHeader;
    }

    public String getFilters() {
        return filters;
    }

    public void setFilters(String filters) {
        this.filters = filters;
    }
}
