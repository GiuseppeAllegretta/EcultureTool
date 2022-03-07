package com.example.eculturetool;

public class Curatore {


    private String uid, nome, cognome, email;

    public Curatore() {
    }

    public String getUid(){
        return uid;
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

    public Curatore(String uid, String nome, String cognome, String email) {
        this.uid = uid;
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;

    }

}
