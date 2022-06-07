package com.example.eculturetool.entities;

import androidx.annotation.NonNull;

import java.io.Serializable;

/**
 * Classe che rappresenta il concetto di curatore.
 */
public class Curatore implements Serializable {

    /**
     * nome: nome del curatore
     * cognome: cognome del curatore
     * email: email del curatore
     * password:password del curatore
     * img: l'url dell'immagine di profilo del curatore
     */
    private String nome, cognome, email, password, img;

    /**
     * l'id del luogo correntemente selezionato
     */
    private int luogoCorrente;

    /**
     * costruttore vuoto
     */
    public Curatore() {
    }

    /**
     * Costruttore parametrico di Curatore
     * @param nome, il nome del curatore
     * @param cognome, il cognome del curatore
     * @param email, l'email del curatore
     * @param password, la password del curatore
     * @param img, l'url dell'immagine di profilo del curatore
     */
    public Curatore(String nome, String cognome, String email, String password, String img) {
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.password = password;
        this.img = img;
    }

    /**
     * Costruttore parametrico di Curatore
     * @param nome, il nome del curatore
     * @param cognome, il cognome del curatore
     * @param email, l'email del curatore
     * @param password, la password del curatore
     * @param img, l'url dell'immagine di profilo del curatore
     * @param luogoCorrente, l'id del luogo correntemente selezionato
     */
    public Curatore(String nome, String cognome, String email, String password, String img, int luogoCorrente) {
        this(nome, cognome, email, password, img);
        this.luogoCorrente = luogoCorrente;
    }


    /**
     * Restituisce il nome di un'istanza di Curatore
     * @return String, nome curatore
     */
    public String getNome() {
        return nome;
    }

    /**
     * Restituisce il cognome di un'istanza di Curatore
     * @return String, cognome curatore
     */
    public String getCognome() {
        return cognome;
    }

    /**
     * Restituisce l'email di un'istanza di Curatore
     * @return String, email curatore
     */
    public String getEmail() {
        return email;
    }

    /**
     * Restituisce la password di un'istanza di Curatore
     * @return String, password curatore
     */
    public String getPassword() {
        return password;
    }

    /**
     * Restituisce l'url immagine di un'istanza di Curatore
     * @return String, url immagine curatore
     */
    public String getImg() {
        return img;
    }

    /**
     * Restituisce l'id del luogo corrente
     * @return int, id luogo corrente
     */
    public int getLuogoCorrente() {
        return luogoCorrente;
    }

    /**
     * Setta l'attributo luogo corrente
     * @param luogoCorrente, id luogo corrente
     */
    public void setLuogoCorrente(int luogoCorrente) {
        this.luogoCorrente = luogoCorrente;
    }

    /**
     * Setta l'attributo password
     * @param password, password curatore
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Setta l'attributo img
     * @param img, url immagine del curatore
     */
    public void setImg(String img) {
        this.img = img;
    }


    /**
     * Metodo di print di un'istanza di Curatore custom
     * @return String, output formattato
     */
    @NonNull
    @Override
    public String toString() {
        return "Curatore{" +
                "nome='" + nome + '\'' +
                ", cognome='" + cognome + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", luogoCorrente=" + luogoCorrente +
                '}';
    }

    /**
     * Interfaccia contenente valori chiave
     */
    public interface Keys {
        String CURATORE_KEY = "CURATORE_KEY";
        String PASSWORD_KEY = "PASSWORD_KEY";
    }

}
