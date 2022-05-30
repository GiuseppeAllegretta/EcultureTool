package com.example.eculturetool.entities;


import java.io.Serializable;
import java.util.ArrayList;
//Classe che rappresenta il concetto di percorso
public class Percorso extends Entita implements Serializable {

    private int idLuogo;
    private ArrayList<Zona> zone = new ArrayList<>();

    public Percorso(String nome, int idLuogo) {
        super(nome,"");
        this.idLuogo=idLuogo;
        zone = new ArrayList<>();
    }

    public Percorso(int id, String nome, int idLuogo){
        super(id, nome, "");
        this.idLuogo = idLuogo;
        zone = new ArrayList<>();
    }

    public int getIdLuogo() {
        return idLuogo;
    }

    public int getId() {
        return super.getId();
    }

    public String getNome() {
        return super.getNome();
    }

    public ArrayList<Zona> getZone() {
        return zone;
    }

    public void setId(int id) {
        super.setId(id);
    }
}
