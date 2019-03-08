package com.Main;

import android.util.Log;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.InputStream;

/**
 * Basic csv parser, can parse csv files
 * @deprecated not used for anything, app no longer takes csv input
 */
@Deprecated
public class DocumentParser {

    private CSVParser parser;
    private CSVFormat csvFileFormat;
    private String csvFile;
    private int length = 0;
    private static final String [] FILE_HEADER_MAPPING = {"Name","Data 1","Data 2"};

    public void consumeDocument(String csvFile) {
        parser = null;
        this.csvFile = csvFile;

            try {
                csvFileFormat = CSVFormat.DEFAULT.withHeader(FILE_HEADER_MAPPING);
                parser = CSVParser.parse(csvFile, csvFileFormat);

                Log.v("document parser", "consumed document");

                for(CSVRecord csvRecord : parser){
                    this.length++;
                    //Iterate through parser using csvRecord object
                    //Each csvRecord is a line
                }
        }
        catch(IOException e) {
            e.printStackTrace();
            Log.e("document parser", "IOException");
        }
    }

    public String getValue(int lineNum, String columnName){
        String value = "";

        int lineCount = 0;

        try {
            parser = CSVParser.parse(csvFile, csvFileFormat);

            for(CSVRecord csvRecord : parser){
                if(lineCount == lineNum -1) {
                    value = csvRecord.get(columnName);
                }
                lineCount++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return value.trim();
    }

    public int getDocLength(){
        return length;
    }

    public static String copyInputStreamToString( InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}