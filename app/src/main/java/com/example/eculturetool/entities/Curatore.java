package com.example.eculturetool.entities;

public class Curatore {


    private String uid, nome, cognome, email, img;

    public Curatore() {
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getUid(){
        return uid;
    }


    public Curatore(String uid, String nome, String cognome, String email) {
        this.uid = uid;
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
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

}
