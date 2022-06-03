package com.example.eculturetool.entities;

import java.util.ArrayList;

public class DataHolder {

    private ArrayList<Zona> arrayList = new ArrayList<>();
    private ArrayList<Zona> elencoZone = new ArrayList<>();
    private static DataHolder holder = new DataHolder();
    private String pathName;
    private int idPath = 0;

    public void setData(ArrayList<Zona> arrayList){
        this.arrayList = arrayList;
    }

    public void setElencoZone(ArrayList<Zona> arrayList) { this.elencoZone = arrayList; }

    public void setPathName(String string) { this.pathName = string; }

    public void setIdPath(int id) { this.idPath = id; }

    public ArrayList<Zona> getData() { return arrayList; }

    public ArrayList<Zona> getElencoZone() { return elencoZone; }

    public static DataHolder getInstance() { return holder; }

    public String getPathName() {
        return pathName;
    }

    public int getIdPath(){ return idPath; }

    public Zona searchZonaByNome(String nome){
        for(Zona item : elencoZone){
            if(item.getNome().equalsIgnoreCase(nome))
                return item;
        }
        return null;
    }

}
