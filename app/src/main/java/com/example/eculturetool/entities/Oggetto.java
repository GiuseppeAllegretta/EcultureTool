package com.example.eculturetool.entities;

import androidx.annotation.NonNull;

import java.io.Serializable;

/**
 * Classe che rappresenta il concetto di Oggetto all'interno dell'applicazione
 */
public class Oggetto extends Entita implements Serializable {

    /**
     * urlImmagine: l'indirizzo dell'immagine propria di un oggetto (placeholder nel caso in cui non sia impostata)
     * urlQrcode: l'indirizzo del qr code che permette di compiere operazioni sull'oggetto
     * tipologiaOggetto: la tipologia di oggetto
     * zonaAppartenenza: l'ide della zona a cui l'oggetto appartiene
     */
    private String urlImmagine;
    private String urlQrcode;
    private TipologiaOggetto tipologiaOggetto;
    private int zonaAppartenenza;

    /**
     * Costruttore a-parametrico vuoto
     */
    public Oggetto() {
    }

    /**
     * Costruttore di Oggetto
     * @param id, l'id univoco dell'oggetto
     * @param nome, il nome dell'oggetto
     * @param descrizione, la descrizione dell'oggetto
     * @param urlImmagine, l'url dell'immagine dell'ggetto
     */
    public Oggetto(int id, String nome, String descrizione, String urlImmagine) {
        super(id, nome, descrizione);
        this.urlImmagine = urlImmagine;
    }

    /**
     * Costruttore di Oggetto
     * @param nome, l'id univoco dell'oggetto
     * @param descrizione, il nome dell'oggetto
     * @param urlImmagine, l'url dell'immagine dell'oggetto
     * @param tipologiaOggetto, la tipologia dell'oggetto
     * @param zonaAppartenenza, l'id della zona di appartenenza dell'oggetto
     */
    public Oggetto(String nome, String descrizione, String urlImmagine, TipologiaOggetto tipologiaOggetto, int zonaAppartenenza){
        super(nome, descrizione);
        this.urlImmagine = urlImmagine;
        this.tipologiaOggetto = tipologiaOggetto;
        this.zonaAppartenenza = zonaAppartenenza;
    }

    /**
     * Costruttore di oggetto
     * @param id, l'id univoco dell'oggetto
     * @param nome, il nome dell'oggetto
     * @param descrizione, la descrizione dell'oggeto
     * @param urlImmagine, l'url dell'immagine dell'oggetto
     * @param tipologiaOggetto, la tipologia dell'oggetto
     */
    public Oggetto(int id, String nome, String descrizione, String urlImmagine, TipologiaOggetto tipologiaOggetto){
        this(id, nome, descrizione, urlImmagine);
        this.tipologiaOggetto = tipologiaOggetto;
    }

    /**
     * Costruttore di Oggetto
     * @param id, l'id univoco dell'oggetto
     * @param nome, il nome dell'oggetto
     * @param descrizione, la descrizione dell'oggeto
     * @param urlImmagine, l'url dell'immagine dell'oggetto
     * @param tipologiaOggetto, la tipologia dell'oggetto
     * @param zonaAppartenenza, l'id della zona di appartenenza
     */
    public Oggetto (int id, String nome, String descrizione, String urlImmagine, TipologiaOggetto tipologiaOggetto, int zonaAppartenenza){
        this(id, nome, descrizione, urlImmagine, tipologiaOggetto);
        this.zonaAppartenenza = zonaAppartenenza;
    }

    /**
     * Permette di recuperare l'id dell'oggetto
     * @return int, l'id
     */
    public int getId() {
        return super.getId();
    }

    /**
     * Permette di impostare l'id dell'oggetto
     * @param id, l'id da impostare
     */
    public void setId(int id) {
        super.setId(id);
    }

    /**
     * Permette di recuperare il nome di un oggetto
     * @return Strin, il nome
     */
    public String getNome() {
        return super.getNome();
    }

    /**
     * Permette di impostare il nome di un oggetto
     * @param nome, il nome da impostare
     */
    public void setNome(String nome) {
        super.setNome(nome);
    }

    /**
     * Permette di recuperare la descrizione di un oggetto
     * @return String, la descrizione
     */
    public String getDescrizione() {
        return super.getDescrizione();
    }

    /**
     * Permette di impostare la descrizione di un oggetto
     * @param descrizione, la descrizione da impostare
     */
    public void setDescrizione(String descrizione) {
        super.setDescrizione(descrizione);
    }

    /**
     * Permette di recuperare la tipologia di un oggetto
     * @return TipologiaOggetto, la tipologia
     */
    public TipologiaOggetto getTipologiaOggetto() {
        return tipologiaOggetto;
    }

    /**
     * Permette di recuperare l'url immagine di un oggetto
     * @return String, l'url
     */
    public String getUrl() {
        return urlImmagine;
    }

    /**
     * Permette di impostare l'url immagine di un oggetto
     * @param url, l'url da impostare
     */
    public void setUrl(String url) {
        this.urlImmagine = url;
    }

    /**
     * Permette di recuperare l'url del qr code di un oggetto
     * @return String, l'url
     */
    public String getUrlQrcode() {
        return urlQrcode;
    }

    /**
     * Permette di impostare l'url del qr code di un oggetto
     * @param urlQrcode, l'url da impostare
     */
    public void setUrlQrcode(String urlQrcode) {
        this.urlQrcode = urlQrcode;
    }

    /**
     * Metodo di print di un'istanza di Oggetto custom
     * @return String, output formattato
     */
    @NonNull
    @Override
    public String toString() {
        return super.toString() +
                ", url='" + urlImmagine + '\'';
    }

    /**
     * Permette di recuperare la zona di appartenenza di un oggetto
     * @return int, l'id della zona di appartenenza
     */
    public int getZonaAppartenenza() {
        return zonaAppartenenza;
    }

    /**
     * Interfaccia per identificare univocamente l'oggetto
     */
    public interface Keys{
        String ID = "ID_OGGETTO";
    }

    /**
     * Interfaccia che consente di differenzaire la varie tipologie di oggetto
     */
    public interface KeysTipologiaOggetto{
        String QUADRO = "Quadro";
        String STATUA = "Statua";
        String SCULTURA = "Scultura";
        String ALTRO = "Altro";
    }
}


