package com.example.eculturetool.entities;

import java.util.Objects;

public class Oggetto {
    private String id, idCuratore, idLuogo;
    private String nome, descrizione;
    private String urlImmagine;
    private String urlQrcode;

    public Oggetto(){

    }

    public Oggetto(String id, String nome, String descrizione, String urlImmagine){
        this.id = id;
        this.nome = nome;
        this.descrizione = descrizione;
        this.urlImmagine = urlImmagine;
        //TODO generare il qr
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




    public String getUrl() {
        return urlImmagine;
    }

    public void setUrl(String url) {
        this.urlImmagine = url;
    }

    @Override
    public String toString() {
        return "EntityOggetto{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", descrizione='" + descrizione + '\'' +
                ", url='" + urlImmagine + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Oggetto that = (Oggetto) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}


