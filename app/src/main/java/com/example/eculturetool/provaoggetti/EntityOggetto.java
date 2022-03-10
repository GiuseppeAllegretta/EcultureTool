package com.example.eculturetool.provaoggetti;

import android.graphics.Bitmap;
import android.media.Image;

import java.io.File;

public class EntityOggetto{
    private Integer id;
    private String nome;
    private String descrizione;
    private String tipologia; // Farlo enum
    private String url;

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
}


