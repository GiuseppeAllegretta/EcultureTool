package com.example.eculturetool.entities;

import org.jgrapht.nio.json.JSONExporter;

import java.util.ArrayList;

public class Percorso {

    private int id;
    private String nome;
    private String jsonPercorso;
    private ArrayList<Zona> zone;

    public Percorso(int id, String nome) {
        this.id = id;
        this.nome = nome;
        zone = new ArrayList<>();
    }

    public Percorso(String nome, String jsonPercorso) {
        this.nome = nome;
        this.jsonPercorso = jsonPercorso;
    }

    public String getJsonPercorso() {
        return jsonPercorso;
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

    public void setJsonPercorso(String jsonPercorso) {
        this.jsonPercorso = jsonPercorso;
    }

    public void setId(int id) {
        this.id = id;
    }
}
