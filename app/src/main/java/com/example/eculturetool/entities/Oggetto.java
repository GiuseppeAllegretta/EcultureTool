package com.example.eculturetool.entities;

import java.io.Serializable;

public class Oggetto extends Entita implements Serializable {

    private String urlImmagine;
    private String urlQrcode;
    private TipologiaOggetto tipologiaOggetto;
    private int zonaAppartenenza;

    public Oggetto() {

    }

    public Oggetto(int id, String nome, String descrizione, String urlImmagine) {
        super(id, nome, descrizione);
        this.urlImmagine = urlImmagine;
        //TODO generare il qr
    }

    public Oggetto(String nome, String descrizione, String urlImmagine, TipologiaOggetto tipologiaOggetto, int zonaAppartenenza){
        super(nome, descrizione);
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
        return super.getId();
    }

    public void setId(int id) {
        super.setId(id);
    }

    public String getNome() {
        return super.getNome();
    }

    public void setNome(String nome) {
        super.setNome(nome);
    }

    public String getDescrizione() {
        return super.getDescrizione();
    }

    public void setDescrizione(String descrizione) {
        super.setDescrizione(descrizione);
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
        return super.toString() +
                ", url='" + urlImmagine + '\'';
    }

    public int getZonaAppartenenza() {
        return zonaAppartenenza;
    }

    public interface Keys{
        String ID = "ID_OGGETTO";
    }

    public interface KeysTipologiaOggetto{
        String QUADRO = "Quadro";
        String STATUA = "Statua";
        String SCULTURA = "Scultura";
        String ALTRO = "Altro";
    }
}


