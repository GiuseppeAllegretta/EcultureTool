package com.example.eculturetool.entities;
// classe di supporto per la creazione del grafo
public class Vertice {
    private int x;
    private int y;
    private String nomeVertice;
    private boolean isFinal= false;

    public boolean isFinal() {
        return isFinal;
    }

    public void setFinal(boolean aFinal) {
        isFinal = aFinal;
    }

    public Vertice(String nomeVertice, boolean isFinal){
        this.x=200;
        this.y=200;
        this.nomeVertice= nomeVertice;
        this.isFinal = isFinal;
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