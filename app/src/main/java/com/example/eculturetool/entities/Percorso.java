package com.example.eculturetool.entities;

import java.util.ArrayList;

public class Percorso {

    private String id;
    private String nome;
    private ArrayList<Zona> zone;

    public Percorso(String id, String nome) {
        this.id = id;
        this.nome = nome;
        zone = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public ArrayList<Zona> getZone() {
        return zone;
    }


}
