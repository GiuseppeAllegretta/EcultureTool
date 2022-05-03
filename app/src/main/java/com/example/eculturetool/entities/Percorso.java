package com.example.eculturetool.entities;

import org.jgrapht.nio.json.JSONExporter;

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
    private String descrizione;
    private ArrayList<ArrayList<Oggetto>> zone;

    public Percorso(int id, String nome) {
        this.id = id;
        this.nome = nome;
        zone = new ArrayList<>();
    }

    public Percorso(String nome, String descrizione, int idLuogo) {
        this.idLuogo = idLuogo;
        this.nome = nome;
        this.descrizione = descrizione;
    }


    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public ArrayList<ArrayList<Oggetto>> getZone() {
        return zone;
    }

    public void setId(int id) {
        this.id = id;
    }
}
