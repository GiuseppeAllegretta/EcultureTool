package com.example.eculturetool.entities;

import java.util.Objects;

public class Vertice {
    private int x;
    private int y;
    private String nomeVertice;

    public Vertice(String nomeVertice){
        this.x=200;
        this.y=200;
        this.nomeVertice= nomeVertice;
    }



    public Vertice(int x, int y, String nomeVertice) {
        this.x = x;
        this.y = y;
        this.nomeVertice = nomeVertice;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String getNomeVertice() {
        return nomeVertice;
    }

    public void setNomeVertice(String nomeVertice) {
        this.nomeVertice = nomeVertice;
    }

}