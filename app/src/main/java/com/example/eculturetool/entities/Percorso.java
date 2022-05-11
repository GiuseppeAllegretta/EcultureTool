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

    public Percorso(String nome, int idLuogo) {
        this.idLuogo = idLuogo;
        this.nome = nome;
        zone = new ArrayList<>();
    }

    public Percorso(int id, String nome, int idLuogo){
        this(nome, idLuogo);
        this.id = id;
    }

    public int getIdLuogo() {
        return idLuogo;
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
