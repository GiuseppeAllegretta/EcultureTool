package com.example.eculturetool.entities;

import java.util.ArrayList;

public class DataHolder {

    private ArrayList<Zona> arrayList = new ArrayList<>();
    private static final DataHolder holder = new DataHolder();

    public ArrayList<Zona> getData() { return arrayList; }

    public void setData(ArrayList<Zona> arrayList){
        this.arrayList = arrayList;
    }

    public static DataHolder getInstance() { return holder; }
}
