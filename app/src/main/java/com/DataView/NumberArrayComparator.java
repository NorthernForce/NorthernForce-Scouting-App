package com.DataView;

import java.util.Comparator;

public class NumberArrayComparator implements Comparator<Integer[]> {
    private int indexToCompare;

    public NumberArrayComparator(int indexToCompare) {
        this.indexToCompare = indexToCompare;
    }

    @Override
    public int compare(Integer[] o1, Integer[] o2) {
        Integer s1 = o1[indexToCompare];
        Integer s2 = o2[indexToCompare];
        if (s1 == null) s1 = 0;
        if (s2 == null) s2 = 0;
        return s1.compareTo(s2);
    }
}
