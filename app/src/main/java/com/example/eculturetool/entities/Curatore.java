package com.example.eculturetool.entities;

import java.net.URI;
import java.sql.Array;
import java.util.ArrayList;

public class Curatore {

    // TODO capire se serve ancora email o va eliminata
    // TODO capire se uid è un campo o non è necessario
    private String nome, cognome, email;
    //TODO vedere se fare string o URI
    private String img;
    private ArrayList<Luogo> luoghi = new ArrayList<>();

    public Curatore() {
    }

    public Curatore(String nome, String cognome, String email) {
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
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

    public ArrayList<Luogo> getLuoghi() {
        return luoghi;
    }
}
