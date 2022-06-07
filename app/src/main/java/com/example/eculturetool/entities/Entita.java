package com.example.eculturetool.entities;

import androidx.annotation.NonNull;

import java.io.Serializable;

/**
 * Classe astratta che rappresenta il concetto di Entità e che fattorizza metodi comuni a Oggetto, Luogo, Zona
 */
public abstract class Entita implements Serializable {

    /**
     * id: id dell'entità, univoco
     * nome: nome dell'entità
     * descrizione: descrizione dell'entità
     */
    private int id;
    private String nome, descrizione;

    /**
     * Costruttore a-parametrico vuoto
     */
    public Entita(){}

    /**
     * Costruttore di Entita
     * @param nome, nome dell'entità
     * @param descrizione, descrizione dell'entità
     */
    public Entita(String nome, String descrizione){
        this.nome = nome;
        this.descrizione = descrizione;
    }

    /**
     * Costruttore di Entita
     * @param id, id dell'entità
     * @param nome, nome dell'entità
     * @param descrizione, descrizione dell'entità
     */
    public Entita(int id, String nome, String descrizione){
        this(nome, descrizione);
        this.id = id;
    }

    /**
     * Permette di recuperare l'id univoco di un oggetto Entita
     * @return int, l'id
     */
    public int getId() {
        return id;
    }

    /**
     * Permette di impostare l'id di un oggetto Entita
     * @param id, l'id da impostare
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Permette di recuperare il nome di un oggetto Entita
     * @return String, il nome
     */
    public String getNome() {
        return nome;
    }

    /**
     * Permette di impostare il nome di un oggetto Entita
     * @param nome, il nome da impostare
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Permette di recuperare la descrizione di un oggetto Entita
     * @return String, la descrizione dell'oggetto
     */
    public String getDescrizione() {
        return descrizione;
    }

    /**
     * Permette di impostare la descizione di un oggetto Entita
     * @param descrizione, la descrizione da impostare
     */
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    /**
     *  Metodo di print di un'istanza di Entita custom
     *  @return String, output formattato
     */
    @NonNull
    @Override
    public String toString() {
        return "id=" + id +
                ", nome='" + nome + '\'' +
                ", descrizione='" + descrizione + '\'';
    }
}
