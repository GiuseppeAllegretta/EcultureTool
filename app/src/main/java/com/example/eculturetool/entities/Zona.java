package com.example.eculturetool.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Zona extends Entita implements Serializable {
    private int riferimentoLuogo;
    private ArrayList<Oggetto> listaOggetti = new ArrayList<>();
    private ArrayList<Zona> diramazione = new ArrayList<>();

    public Zona(){

    }


    public Zona(String nome, String descrizione, int riferimentoLuogo) {
        super(nome, descrizione);
        this.riferimentoLuogo= riferimentoLuogo;
    }

    public Zona(int id, String nome, String descrizione, int riferimentoLuogo) {
        super(id, nome, descrizione);
        this.riferimentoLuogo= riferimentoLuogo;
    }

    public int getId() {
        return super.getId();
    }

    public void setId(int id) {
        super.setId(id);
    }

    public String getNome() {
        return super.getNome();
    }

    public void setNome(String nome) {
        super.setNome(nome);
    }

    public String getDescrizione() {
        return super.getDescrizione();
    }

    public void setDescrizione(String descrizione) {
        super.setDescrizione(descrizione);
    }

    public int getRiferimentoLuogo() {
        return riferimentoLuogo;
    }

    public void setRiferimentoLuogo(int riferimentoLuogo) {
        this.riferimentoLuogo = riferimentoLuogo;
    }

    public ArrayList<Oggetto> getListaOggetti(){
        return listaOggetti;
    }

    public void addListaOggetti(List<Oggetto> list){
        listaOggetti.addAll(list);
    }

    public ArrayList<Zona> getDiramazione(){
        return diramazione;
    }

    public void setDiramazione(ArrayList<Zona> diramazione){
        this.diramazione = diramazione;
    }


    @Override
    public String toString() {
        return super.toString();
    }

    public boolean addOggetto(Oggetto oggetto) {
        boolean risultato = false;
        if(oggetto != null){
            listaOggetti.add(oggetto);
            risultato = true;
        }
        return risultato;
    }


    public interface Keys{
        final static String ID = "ID_ZONA";
    }

    public boolean addZonaDiramazione(Zona zona){
        boolean risultato = false;

        if(zona != null){
            if(!diramazione.contains(zona)){
                diramazione.add(zona);
                risultato = true;
            }
        }

        return risultato;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Zona zona = (Zona) o;
        return super.getId() == zona.getId() && super.getNome().equals(zona.getNome());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.getId(), super.getNome());
    }
}
