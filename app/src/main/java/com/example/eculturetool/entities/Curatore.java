package com.example.eculturetool.entities;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Classe che rappresenta il concetto di curatore.
 */
public class Curatore implements Serializable {

    /**
     * nome: nome del curatore
     * cognome: cognome del curatore
     * email: email del curatore
     * password:password del curatore
     */
    private String nome, cognome, email, password;


    private String img;
    private ArrayList<Luogo> luoghi = new ArrayList<>();
    private int luogoCorrente;

    public Curatore() {
    }

    public Curatore(String nome, String cognome, String email, String password, String img) {
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.password = password;
        this.img = img;
    }

    public Curatore(String nome, String cognome, String email, String password, String img, int luogoCorrente) {
        this(nome, cognome, email, password, img);
        this.luogoCorrente = luogoCorrente;
    }

    public void addLuogo(Luogo luogo) {
        luoghi.add(luogo);
    }


    public String getNome() {
        return nome;
    }

    public String getCognome() {
        return cognome;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getImg() {
        return img;
    }

    public int getLuogoCorrente() {
        return luogoCorrente;
    }

    public void setLuogoCorrente(int luogoCorrente) {
        this.luogoCorrente = luogoCorrente;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public ArrayList<Luogo> getLuoghi() {
        return luoghi;
    }


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

    public interface Keys {
        final static String CURATORE_KEY = "CURATORE_KEY";
        final static String PASSWORD_KEY = "PASSWORD_KEY";
    }


}
