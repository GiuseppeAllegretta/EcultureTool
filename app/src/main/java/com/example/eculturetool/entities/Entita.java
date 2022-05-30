package com.example.eculturetool.entities;

import java.io.Serializable;

/**
 * Classe che rappresenta il concetto di Entità e che fattorizza metodi comuni a Oggetto, Luogo, Zona
 */
public abstract class Entita implements Serializable {

    private int id;
    private String nome, descrizione;

    public Entita(){}

    public Entita(String nome, String descrizione){
        this.nome = nome;
        this.descrizione = descrizione;
    }

    public Entita(int id, String nome, String descrizione){
        this(nome, descrizione);
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    @Override
    public String toString() {
        return "id=" + id +
                ", nome='" + nome + '\'' +
                ", descrizione='" + descrizione + '\'';
    }
}
