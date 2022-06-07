package com.example.eculturetool.entities;


import java.io.Serializable;
import java.util.ArrayList;

/**
 * Classe che rappresenta il concetto di Percorso all'interno dell'applicazione
 */
public class Percorso extends Entita implements Serializable {

    /**
     * idLuogo: l'id del luogo a cui il percorso fa riferimento
     * zone: arraylist di tutte le zone di cui il percorso si compone
     */
    private int idLuogo;
    private ArrayList<Zona> zone;

    /**
     * Costruttore di Percorso
     * @param nome, nome del percorso
     * @param idLuogo, id del luogo a cui il percorso fa riferimento
     */
    public Percorso(String nome, int idLuogo) {
        super(nome,"");
        this.idLuogo=idLuogo;
        zone = new ArrayList<>();
    }

    /**
     * Costruttore di Percorso
     * @param id, id del percorso
     * @param nome, nome del percorso
     * @param idLuogo, id del luogo a cui il percorso fa riferimento
     */
    public Percorso(int id, String nome, int idLuogo){
        super(id, nome, "");
        this.idLuogo = idLuogo;
        zone = new ArrayList<>();
    }

    /**
     * Permette di recuperare l'id del luogo a cui il percorso fa riferimento
     * @return int, l'id del luogo;
     */
    public int getIdLuogo() {
        return idLuogo;
    }

    /**
     * Permette di recuperare l'id del percorso
     * @return int, l'id percorso
     */
    public int getId() {
        return super.getId();
    }

    /**
     * Permette di recuperare il nome del percorso
     * @return String, nome percorso
     */
    public String getNome() {
        return super.getNome();
    }

    /**
     * Permette di recuperare la lista delle zone che compongono il percorso
     * @return ArrayList, la lista delle zone
     */
    public ArrayList<Zona> getZone() {
        return zone;
    }

    /**
     * Permette di impostare l'id del percorso (univoco)
     * @param id, l'id da impostare
     */
    public void setId(int id) {
        super.setId(id);
    }
}
