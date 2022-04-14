package com.example.eculturetool.entities;

import java.io.Serializable;

public class Luogo implements Serializable {

    private String nome, descrizione;
    private Tipologia tipologia;
    private String id;
    private String emailCuratore;


    public Luogo() {

    }

    public Luogo(String nome, String descrizione, Tipologia tipologia, String emailCuratore) {
        this.nome = nome;
        this.descrizione = descrizione;
        this.tipologia = tipologia;
        this.emailCuratore = emailCuratore;
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

    public String getEmailCuratore() {
        return emailCuratore;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Luogo{" +
                "nome='" + nome + '\'' +
                ", descrizione='" + descrizione + '\'' +
                ", tipologia=" + tipologia +
                ", emailCuratore='" + emailCuratore + '\'' +
                '}';
    }

    public interface tipologiaLuoghi {
        public final static String MUSEO = "Museo";
        public final static String AREA_ARCHEOLOGICA = "Area Archeologica";
        public final static String SITO_CULTURALE = "Sito Culturale";
        public final static String MOSTRA_ITINERANTE = "Mostra Itinerante";
    }

    public interface Keys{
        final static String ID = "ID_LUOGO";
    }
}
