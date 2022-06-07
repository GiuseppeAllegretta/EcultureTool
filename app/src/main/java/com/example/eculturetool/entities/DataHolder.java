package com.example.eculturetool.entities;

import java.util.ArrayList;

/**
 * Classe utilizzata per acquisire e gestire dati in fase di creazione del percorso
 */
public class DataHolder {

    /**
     * arrayList: array contenente le zone impiegate nella creazione del percorso
     * elencoZone: array contenente tutte le zone attualmente salvate nel database per il luogo attuale
     * holder: contenitore statico, utilizzato per rendere accessibile l'arrayList in varie sezioni dell'app
     * pathName: nome che l'utente assegna al percorso
     * idPath: id del percorso, utilizzato prevalentemente in fase di modifica
     */
    private ArrayList<Zona> arrayList = new ArrayList<>();
    private ArrayList<Zona> elencoZone = new ArrayList<>();
    private static DataHolder holder = new DataHolder();
    private String pathName;
    private int idPath = 0;

    /**
     * Inizializza arrayList attraverso il passaggio di un array di zone
     * @param arrayList, l'array iniziale
     */
    public void setData(ArrayList<Zona> arrayList){
        this.arrayList = arrayList;
    }

    /**
     * Inizializza elencoZone attraverso il passaggio di un array di zone
     * @param arrayList, l'array iniziale
     */
    public void setElencoZone(ArrayList<Zona> arrayList) { this.elencoZone = arrayList; }

    /**
     * Assegna il nome del percorso inserito dall'utente
     * @param string, il nome del percorso
     */
    public void setPathName(String string) { this.pathName = string; }

    /**
     * Assegna l'id del percorso gestito
     * @param id, id univoco del percorso
     */
    public void setIdPath(int id) { this.idPath = id; }

    /**
     * Metodo che permette di recuperare l'array di zone attualmente coinvolte nella creazione del percorso (arrayList)
     * @return ArrayList, l'array di zone
     */
    public ArrayList<Zona> getData() { return arrayList; }

    /**
     * Metodo che permette di recuperare l'array contente tutte le zone attualmente inserite nel db
     * @return ArrayList, l'array di zone
     */
    public ArrayList<Zona> getElencoZone() { return elencoZone; }

    /**
     * Metodo che permette di utilizzare l'holder, contente tutti i dati relativi al percorso
     * @return DataHolder, l'holder
     */
    public static DataHolder getInstance() { return holder; }

    /**
     * Metodo che permette di recuperare il nome del percorso attualmente gestito
     * @return String, il nome del percorso
     */
    public String getPathName() {
        return pathName;
    }

    /**
     * Metodo che permette di recuperare l'id del percorso attualemtne gestito
     * @return int, l'id del percorso
     */
    public int getIdPath(){ return idPath; }

    /**
     * Metodo che permette di ricercare una zona dall'elenco delle zone, attraverso il suo nome
     * @param nome, il nome della zona da cercare
     * @return Zona, se questa viene trovata, null altrimenti
     */
    public Zona searchZonaByNome(String nome){
        for(Zona item : elencoZone){
            if(item.getNome().equalsIgnoreCase(nome))
                return item;
        }
        return null;
    }

}
