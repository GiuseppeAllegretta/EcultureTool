package com.example.eculturetool.entities;

/**
 * Classe di support alla creazione del grafo
 */
public class Vertice {

    /**
     * x: coordinata spaziale x del vertice
     * y: coordinata spaziale y del vertice
     * nomeVertice: nome del vertice
     * isFinal: attributo che consente di capire se il vertice è un vertice finale o meno
     */
    private int x;
    private int y;
    private String nomeVertice;
    private boolean isFinal = false;

    /**
     * Permette di recuperare la finalità del vertice
     * @return boolean, true se il vertice è finale, false altrimenti
     */
    public boolean isFinal() {
        return isFinal;
    }

    /**
     * Permette di impostare la finalità del vertice
     * @param aFinal, finalità da impostare
     */
    public void setFinal(boolean aFinal) {
        isFinal = aFinal;
    }

    /**
     * Costruttore di Vertice
     * @param nomeVertice, nome del vertice
     * @param isFinal, finalità del vertice
     */
    public Vertice(String nomeVertice, boolean isFinal){
        this.x=200;
        this.y=200;
        this.nomeVertice= nomeVertice;
        this.isFinal = isFinal;
    }

    /**
     * Costruttore di Vertice
     * @param x, coordinata x del vertice
     * @param y, coordinata y del vertice
     * @param nomeVertice, nome del vertice
     */
    public Vertice(int x, int y, String nomeVertice) {
        this.x = x;
        this.y = y;
        this.nomeVertice = nomeVertice;
    }

    /**
     * Permette di recuperare la coordinata spaziale x di un vertice
     * @return int, coordinata x
     */
    public int getX() {
        return x;
    }

    /**
     * Permette di impostare la coordinata spaziale x di un vertice
     * @param x, la coordinata da impostare
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Permette di recuperare la coordinata spaziale y di un vertice
     * @return int, coordinata y
     */
    public int getY() {
        return y;
    }

    /**
     * Permette di impostare la coordinata spaziale y di un vertice
     * @param y, la coordinata da impostare
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Permette di recuperare il nome di un vertice
     * @return String, nome del vertice
     */
    public String getNomeVertice() {
        return nomeVertice;
    }

    /**
     * Permette di impostare il nome di un vertice
     * @param nomeVertice, nome del vertice da impostare
     */
    public void setNomeVertice(String nomeVertice) {
        this.nomeVertice = nomeVertice;
    }

}