package com.example.eculturetool;

public class Curatore {

     public String nome, cognome, email;

    public Curatore() {
    }

    public Curatore(String nome, String cognome, String email) {
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
