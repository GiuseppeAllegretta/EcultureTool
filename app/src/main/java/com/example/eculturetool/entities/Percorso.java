package com.example.eculturetool.entities;


import java.util.ArrayList;

public class Percorso {

    /**
     * Id del percorso univoco
     */
    private int id;

    /**
     * id del luogo a cui fa riferimento il percorso
     */
    private int idLuogo;
    private String nome;
    private ArrayList<Zona> zone = new ArrayList<>();

    public Percorso(int id, String nome) {
        this.id = id;
        this.nome = nome;
        zone = new ArrayList<>();
    }


    public Percorso(String nome, int idLuogo, ArrayList<Zona> zone) {
        this.idLuogo = idLuogo;
        this.nome = nome;
        this.zone = zone;
    }


    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public ArrayList<Zona> getZone() {
        return zone;
    }

    public void setId(int id) {
        this.id = id;
    }
}
