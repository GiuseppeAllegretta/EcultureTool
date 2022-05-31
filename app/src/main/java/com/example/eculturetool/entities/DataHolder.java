package com.example.eculturetool.entities;

import java.util.ArrayList;

public class DataHolder {

    private ArrayList<Zona> arrayList = new ArrayList<>();
    private static DataHolder holder = new DataHolder();
    private String pathName;
    private int idPath;

    public void setData(ArrayList<Zona> arrayList){
        this.arrayList = arrayList;
    }

    public void setPathName(String string) { this.pathName = string; }

    public void setIdPath(int id) { this.idPath = id; }

    public ArrayList<Zona> getData() { return arrayList; }

    public static DataHolder getInstance() { return holder; }

    public String getPathName() {
        return pathName;
    }

    public int getIdPath(){ return idPath; }
}
