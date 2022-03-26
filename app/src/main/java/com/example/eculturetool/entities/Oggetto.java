package com.example.eculturetool.entities;

import java.util.Objects;

public class Oggetto {
    private String id, idCuratore, idLuogo;
    private String nome, descrizione;
    private String urlImmagine;
    private String urlQrcode;
    private TipologiaOggetto tipologiaOggetto;
    private String zonaAppartenenza;

    public Oggetto() {

    }

    public Oggetto(String id, String nome, String descrizione, String urlImmagine) {
        this.id = id;
        this.nome = nome;
        this.descrizione = descrizione;
        this.urlImmagine = urlImmagine;
        //TODO generare il qr
    }

    public Oggetto(String id, String nome, String descrizione, String urlImmagine, TipologiaOggetto tipologiaOggetto){
        this(id, nome, descrizione, urlImmagine);
        this.tipologiaOggetto = tipologiaOggetto;
    }

    public Oggetto (String id, String nome, String descrizione, String urlImmagine, TipologiaOggetto tipologiaOggetto, String zonaAppartenenza){
        this(id, nome, descrizione, urlImmagine, tipologiaOggetto);
        this.zonaAppartenenza = zonaAppartenenza;
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

    public TipologiaOggetto getTipologiaOggetto() {
        return tipologiaOggetto;
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

    public String getZonaAppartenenza() {
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


