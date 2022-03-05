package com.example.eculturetool;

public class Curatore {

     public String uid, nome, cognome, email;

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

    public Curatore(String uid, String nome, String cognome, String email) {
        this.uid = uid;
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;

    }

    public void setNome(String nome) {
        this.nome = nome;
    }

}
