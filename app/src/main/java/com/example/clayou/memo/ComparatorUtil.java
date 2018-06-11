package com.example.clayou.memo;


import java.util.Comparator;


public class ComparatorUtil implements Comparator<Memo> {

    @Override
    public int compare(Memo o1, Memo o2) {
        return  o1.compareTo(o2);
    }

}
