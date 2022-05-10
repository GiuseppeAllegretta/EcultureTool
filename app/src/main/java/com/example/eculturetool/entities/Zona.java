package com.example.eculturetool.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Zona implements Serializable {
    private int id;
    private String nome;
    private String descrizione;
    private int riferimentoLuogo;
    private ArrayList<Oggetto> listaOggetti = new ArrayList<>();
    private ArrayList<Zona> diramazione = new ArrayList<>();

    public Zona(){

    }


    public Zona(String nome, String descrizione, int riferimentoLuogo) {
        this.nome = nome;
        this.descrizione = descrizione;
        this.riferimentoLuogo= riferimentoLuogo;
    }

    public Zona(int id, String nome, String descrizione, int riferimentoLuogo) {
        this.id=id;
        this.nome = nome;
        this.descrizione = descrizione;
        this.riferimentoLuogo= riferimentoLuogo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
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
        return "Zona{" +
                "id='" + id + '\'' +
                ", nome='" + nome + '\'' +
                ", descrizione='" + descrizione + '\'' +
                '}';
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
        return id == zona.id && nome.equals(zona.nome);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome);
    }
}
