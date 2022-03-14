package com.example.eculturetool.provaoggetti;

import android.graphics.Bitmap;
import android.media.Image;

import java.io.File;
import java.util.Objects;

public class EntityOggetto{
    private Integer id;
    private String nome;
    private String descrizione;
    private String tipologia; // Farlo enum
    private String url;

    public EntityOggetto(){

    }

    public EntityOggetto(int id, String nome, String descrizione, String tipologia, String url){
        this.id = id;
        this.nome = nome;
        this.descrizione = descrizione;
        this.tipologia = tipologia;
        this.url = url;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public String getTipologia() {
        return tipologia;
    }

    public void setTipologia(String tipologia) {
        this.tipologia = tipologia;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "EntityOggetto{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", descrizione='" + descrizione + '\'' +
                ", tipologia='" + tipologia + '\'' +
                ", url='" + url + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EntityOggetto that = (EntityOggetto) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}


