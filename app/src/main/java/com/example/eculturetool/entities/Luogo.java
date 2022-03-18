package com.example.eculturetool.entities;

import com.example.eculturetool.database.Connection;

public class Luogo {

    private String nome, descrizione;
    private Tipologia tipologia;
    private String id;

    public Luogo(){

    }

    public Luogo(String nome, String descrizione, Tipologia tipologia, String id){
        this.nome = nome;
        this.descrizione = descrizione;
        this.tipologia = tipologia;
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public Tipologia getTipologia() {
        return tipologia;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Luogo{" +
                "nome='" + nome + '\'' +
                ", descrizione='" + descrizione + '\'' +
                ", tipologia=" + tipologia +
                ", id='" + id + '\'' +
                '}';
    }
}
