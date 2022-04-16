package com.example.eculturetool.entities;

import java.io.Serializable;

public class Zona implements Serializable {
    private int id;
    private String nome;
    private String descrizione;
    private int numeroOggetti;
    private int riferimentoLuogo;

    public Zona(){

    }

    public int getRiferimentoLuogo() {
        return riferimentoLuogo;
    }

    public void setRiferimentoLuogo(int riferimentoLuogo) {
        this.riferimentoLuogo = riferimentoLuogo;
    }

    public Zona(int id, String nome, String descrizione, int numeroOggetti, int riferimentoLuogo) {
        this.id=id;
        this.nome = nome;
        this.descrizione = descrizione;
        this.numeroOggetti = numeroOggetti;
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

    public int getNumeroOggetti() {
        return numeroOggetti;
    }

    public void setNumeroOggetti(int numeroOggetti) {
        this.numeroOggetti = numeroOggetti;
    }

    @Override
    public String toString() {
        return "Zona{" +
                "id='" + id + '\'' +
                ", nome='" + nome + '\'' +
                ", descrizione='" + descrizione + '\'' +
                ", numeroOggetti=" + numeroOggetti +
                '}';
    }

    public interface Keys{
        final static String ID = "ID_ZONA";
    }

}
