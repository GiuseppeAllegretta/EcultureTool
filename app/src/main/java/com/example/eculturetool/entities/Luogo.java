package com.example.eculturetool.entities;

import java.io.Serializable;

public class Luogo implements Serializable {

    private String nome, descrizione;
    private TipologiaLuogo tipologia;
    private String id;


    public Luogo() {

    }

    public Luogo(String nome, String descrizione, TipologiaLuogo tipologiaLuogo, String id) {
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

    public TipologiaLuogo getTipologia() {
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

    public interface tipologiaLuoghi {
        public final static String MUSEO = "Museo";
        public final static String AREA_ARCHEOLOGICA = "Area Archeologica";
        public final static String SITO_CULTURALE = "Sito Culturale";
        public final static String MOSTRA_ITINERANTE = "Mostra Itinerante";
    }
}
