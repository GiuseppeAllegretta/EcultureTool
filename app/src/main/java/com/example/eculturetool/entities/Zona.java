package com.example.eculturetool.entities;

public class Zona {
    private String id;
    private String nome;
    private String descrizione;
    private int numeroOggetti;
    private static final int MAX_OGGETTI = 10;

    public Zona(String nome, String descrizione, int numeroOggetti) {
        this.nome = nome;
        this.descrizione = descrizione;
        this.numeroOggetti = numeroOggetti;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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


}
