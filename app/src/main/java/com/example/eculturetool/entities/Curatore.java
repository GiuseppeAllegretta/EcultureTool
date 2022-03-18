package com.example.eculturetool.entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.net.URI;
import java.sql.Array;
import java.util.ArrayList;

public class Curatore implements Serializable {

    // TODO capire se serve ancora email o va eliminata
    // TODO capire se uid è un campo o non è necessario
    private String nome, cognome, email;
    //TODO vedere se fare string o URI
    private String img;
    private ArrayList<Luogo> luoghi = new ArrayList<>();
    private String luogoCorrente;

    public Curatore() {
    }

    public Curatore(String nome, String cognome, String email) {
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
    }

    public Curatore(String nome, String cognome, String email, String luogoCorrente) {
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.luogoCorrente = luogoCorrente;
    }

    public void addLuogo(Luogo luogo){
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

    public String getImg() {
        return img;
    }

    public String getLuogoCorrente() {
        return luogoCorrente;
    }

    public void setLuogoCorrente(String luogoCorrente) {
        this.luogoCorrente = luogoCorrente;
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
                '}';
    }

    public interface Keys{
        final static String CURATORE_KEY = "CURATORE_KEY";
        final static String PASSWORD_KEY = "PASSWORD_KEY";
    }




}
