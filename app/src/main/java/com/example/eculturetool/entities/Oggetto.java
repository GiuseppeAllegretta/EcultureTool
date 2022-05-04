package com.example.eculturetool.entities;

import java.io.Serializable;
import java.util.Objects;

public class Oggetto implements Serializable {
    private int id;
    private String nome, descrizione;
    private String urlImmagine;
    private String urlQrcode;
    private TipologiaOggetto tipologiaOggetto;
    private int zonaAppartenenza;

    public Oggetto() {

    }

    public Oggetto(int id, String nome, String descrizione, String urlImmagine) {
        this.id = id;
        this.nome = nome;
        this.descrizione = descrizione;
        this.urlImmagine = urlImmagine;
        //TODO generare il qr
    }

    public Oggetto(String nome, String descrizione, String urlImmagine, TipologiaOggetto tipologiaOggetto, int zonaAppartenenza){
        this.nome = nome;
        this.descrizione = descrizione;
        this.urlImmagine = urlImmagine;
        this.tipologiaOggetto = tipologiaOggetto;
        this.zonaAppartenenza = zonaAppartenenza;
    }


    public Oggetto(int id, String nome, String descrizione, String urlImmagine, TipologiaOggetto tipologiaOggetto){
        this(id, nome, descrizione, urlImmagine);
        this.tipologiaOggetto = tipologiaOggetto;
    }

    public Oggetto (int id, String nome, String descrizione, String urlImmagine, TipologiaOggetto tipologiaOggetto, int zonaAppartenenza){
        this(id, nome, descrizione, urlImmagine, tipologiaOggetto);
        this.zonaAppartenenza = zonaAppartenenza;
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

    public TipologiaOggetto getTipologiaOggetto() {
        return tipologiaOggetto;
    }

    public String getUrl() {
        return urlImmagine;
    }

    public void setUrl(String url) {
        this.urlImmagine = url;
    }

    public String getUrlQrcode() {
        return urlQrcode;
    }

    public void setUrlQrcode(String urlQrcode) {
        this.urlQrcode = urlQrcode;
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


    public int getZonaAppartenenza() {
        return zonaAppartenenza;
    }

    public interface Keys{
        final static String ID = "ID_OGGETTO";
    }

    public interface KeysTipologiaOggetto{
        public final static String QUADRO = "Quadro";
        public final static String STATUA = "Statua";
        public final static String SCULTURA = "Scultura";
        public final static String ALTRO = "Altro";
    }
}


