package com.example.eculturetool.entities;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Classe che rappresenta il concetto di Zona all'iinterno dell'applicazione
 */
public class Zona extends Entita implements Serializable {

    /**
     * riferimentoLuogo, l'id del luogo a cui la zona appartiene
     * listaOggetti, l'elenco degli oggetti contenuti nella zona
     * diramazione, la lista delle zone in cui la zona può diramarsi
     * isFinal, parametro che indica se la zona è finale in un percorso o meno
     */
    private int riferimentoLuogo;
    private ArrayList<Oggetto> listaOggetti = new ArrayList<>();
    private ArrayList<Zona> diramazione = new ArrayList<>();
    private boolean isFinal = false;

    /**
     * Permette di recuperare la finalità della zona
     * @return boolean, true se la zona è finale, false altrimenti
     */
    public boolean isFinal() {
        return isFinal;
    }

    /**
     * Permette di impostare la finalità della zona
     * @param flag, finalità da impostare
     */
    public void setFinal(boolean flag) {
        isFinal = flag;
    }

    /**
     * Costruttore a-parametrico vuoto
     */
    public Zona(){
    }

    /**
     * Costruttore di Zona
     * @param nome, il nome della zona
     * @param descrizione, la descrizione della zona
     * @param riferimentoLuogo, l'id del luogo in cui la zona è contenuta
     */
    public Zona(String nome, String descrizione, int riferimentoLuogo) {
        super(nome, descrizione);
        this.riferimentoLuogo= riferimentoLuogo;
    }

    /**
     * Costruttore di Zona
     * @param id, l'id univoco della zona
     * @param nome, il nome della zona
     * @param descrizione, la descrizione della zona
     * @param riferimentoLuogo, l'id del luogo in cui la zona è contenuta
     */
    public Zona(int id, String nome, String descrizione, int riferimentoLuogo) {
        super(id, nome, descrizione);
        this.riferimentoLuogo= riferimentoLuogo;
    }

    /**
     * Permette di recuperare l'id della zona
     * @return int, l'id della zona
     */
    public int getId() {
        return super.getId();
    }

    /**
     * Permette di impostare l'id della zona
     * @param id, l'id da impostare
     */
    public void setId(int id) {
        super.setId(id);
    }

    /**
     * Permette di recuperare il nome di una zona
     * @return String, il nome della zona
     */
    public String getNome() {
        return super.getNome();
    }

    /**
     * Permette di impostare il nome di una zona
     * @param nome, il nome da impostare
     */
    public void setNome(String nome) {
        super.setNome(nome);
    }

    /**
     * Permette di recuperare la descrizione di una zona
     * @return String, la descrizione
     */
    public String getDescrizione() {
        return super.getDescrizione();
    }

    /**
     * Permette di impostare la descrizione di una zona
     * @param descrizione, la descrizione da impostare
     */
    public void setDescrizione(String descrizione) {
        super.setDescrizione(descrizione);
    }

    /**
     * Permette di recuperare il luogo in cui la zona è situata
     * @return int, l'id del luogo
     */
    public int getRiferimentoLuogo() {
        return riferimentoLuogo;
    }

    /**
     * Permette di impostare il luogo in cui la zona è sistuata
     * @param riferimentoLuogo, il luogo
     */
    public void setRiferimentoLuogo(int riferimentoLuogo) {
        this.riferimentoLuogo = riferimentoLuogo;
    }

    /**
     * Permette di recuperare la lista di oggetti contenuta in una zona
     * @return ArrayList, la lista di oggetti
     */
    public ArrayList<Oggetto> getListaOggetti(){
        return listaOggetti;
    }

    /**
     * Permette di aggiungere un elenco di oggetti alla lista di oggetti che compongono una zona
     * @param list, l'elenco da aggiungere
     */
    public void addListaOggetti(List<Oggetto> list){
        listaOggetti.addAll(list);
    }

    /**
     * Permette di recuperare la lista di zone che compongono la diramazione della zona
     * @return ArrayList, la lista delle zone
     */
    public ArrayList<Zona> getDiramazione(){
        return diramazione;
    }

    /**
     * Permette di impostare la diramazione di una zona
     * @param diramazione, la lista di zone che compongono la diramazione
     */
    public void setDiramazione(ArrayList<Zona> diramazione){
        this.diramazione = diramazione;
    }


    /**
     * Metodo di print di un'istanza di Zona custom
     * @return String, output formattato
     */
    @NonNull
    @Override
    public String toString() {
        return super.toString();
    }

    /**
     * Interfaccia per identificare univocamente la zona
     */
    public interface Keys{
        String ID = "ID_ZONA";
    }


    /**
     * Metodo di confronto di due zone
     * @param o, l'oggetto da confrontare
     * @return true, se gli oggetti sono identici, false altrimenti
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Zona zona = (Zona) o;
        return super.getId() == zona.getId() && super.getNome().equals(zona.getNome());
    }

    /**
     * Metodo che consente di recuperare l'hash code di un oggetto
     * @return int, l'hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.getId(), super.getNome());
    }
}
