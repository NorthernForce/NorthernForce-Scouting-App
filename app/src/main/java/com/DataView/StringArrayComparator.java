package edu.falgor.frc.dynamicapp;

import java.util.Comparator;

public class StringArrayComparator implements Comparator<String[]> {
    private int indexToCompare;

    public StringArrayComparator(int indexToCompare) {
        this.indexToCompare = indexToCompare;
    }

    @Override
    public int compare(String[] o1, String[] o2) {
        String s1 = o1[indexToCompare];
        String s2 = o2[indexToCompare];
        if (s1 == null) s1 = "";
        if (s2 == null) s2 = "";
        return s1.compareToIgnoreCase(s2);
    }
}
