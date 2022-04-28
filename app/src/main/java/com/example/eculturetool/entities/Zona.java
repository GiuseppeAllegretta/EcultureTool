package com.example.eculturetool.entities;

import java.io.Serializable;
import java.util.ArrayList;

public class Zona implements Serializable {
    private int id;
    private String nome;
    private String descrizione;
    private int riferimentoLuogo;
    private ArrayList<Oggetto> elencoOggetti = new ArrayList<>();

    public Zona(){

    }


    public Zona(String nome, String descrizione, int riferimentoLuogo) {
        this.nome = nome;
        this.descrizione = descrizione;
        this.riferimentoLuogo= riferimentoLuogo;
    }

    public Zona(int id, String nome, String descrizione, int riferimentoLuogo) {
        this.id=id;
        this.nome = nome;
        this.descrizione = descrizione;
        this.riferimentoLuogo= riferimentoLuogo;
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

    public int getRiferimentoLuogo() {
        return riferimentoLuogo;
    }

    public void setRiferimentoLuogo(int riferimentoLuogo) {
        this.riferimentoLuogo = riferimentoLuogo;
    }

    public ArrayList<Oggetto> getElencoOggetti(){
        return elencoOggetti;
    }

    public void addOggetto(Oggetto oggetto){
        elencoOggetti.add(oggetto);
    }


    @Override
    public String toString() {
        return "Zona{" +
                "id='" + id + '\'' +
                ", nome='" + nome + '\'' +
                ", descrizione='" + descrizione + '\'' +
                ", numeroOggetti=" + elencoOggetti.size() +
                '}';
    }

    public interface Keys{
        final static String ID = "ID_ZONA";
    }

}
