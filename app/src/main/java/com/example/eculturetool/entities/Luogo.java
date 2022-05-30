package com.example.eculturetool.entities;

import java.io.Serializable;

public class Luogo extends Entita implements Serializable {

    private Tipologia tipologia;
    private String emailCuratore;

    public Luogo() {

    }

    public Luogo(String nome, String descrizione, Tipologia tipologia, String emailCuratore) {
        super(nome, descrizione);
        this.tipologia = tipologia;
        this.emailCuratore = emailCuratore;
    }

    public int getId() {
        return super.getId();
    }

    public String getNome() {
        return super.getNome();
    }

    public String getDescrizione() {
        return super.getDescrizione();
    }

    public Tipologia getTipologia() {
        return tipologia;
    }

    public String getEmailCuratore() { return emailCuratore; }


    public void setId(int id) {
        super.setId(id);
    }

    @Override
    public String toString() {
        return super.toString() +
                ", tipologia=" + tipologia +
                ", emailCuratore=" + emailCuratore;
    }

    public interface tipologiaLuoghi {
        String MUSEO = "Museo";
        String AREA_ARCHEOLOGICA = "Area Archeologica";
        String SITO_CULTURALE = "Sito Culturale";
        String MOSTRA_ITINERANTE = "Mostra Itinerante";
    }

    public interface Keys{
        String ID = "ID_LUOGO";
    }
}
