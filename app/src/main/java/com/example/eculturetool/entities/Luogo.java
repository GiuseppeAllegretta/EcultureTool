package com.example.eculturetool.entities;

import androidx.annotation.NonNull;

import java.io.Serializable;

/**
 * Classe che rappresenta il concetto di Luogo all'interno dell'applicazione
 */
public class Luogo extends Entita implements Serializable {

    /**
     * tipologia: il tipo di luogo in questione
     * emailCuratore: ogni luogo è legato ad un curatore tramite la sua email; necessario per rendere il luogo univoco
     */
    private Tipologia tipologia;
    private String emailCuratore;

    /**
     * Costruttore a-parametrico vuoto
     */
    public Luogo() {
    }

    /**
     * Costruttore di un oggetto di tipo Luogo
     * @param nome, il nome del luogo
     * @param descrizione, la descrizione del luogo
     * @param tipologia, il tipo di luogo
     * @param emailCuratore, l'email del curatore che ha creato il luogo
     */
    public Luogo(String nome, String descrizione, Tipologia tipologia, String emailCuratore) {
        super(nome, descrizione);
        this.tipologia = tipologia;
        this.emailCuratore = emailCuratore;
    }

    /**
     * Permette di recuperare l'id univoco del luogo
     * @return int, l'id del luogo
     */
    public int getId() {
        return super.getId();
    }

    /**
     * Permette di recuperare il nome del luogo
     * @return String, il nome
     */
    public String getNome() {
        return super.getNome();
    }

    /**
     * Permette di recuperare la descrizione del luogo
     * @return String, la descrizione
     */
    public String getDescrizione() {
        return super.getDescrizione();
    }

    /**
     * Permette di recuperare la tipologia del luogo
     * @return Tipologia, tipologia del luogo
     */
    public Tipologia getTipologia() {
        return tipologia;
    }

    /**
     * Permette di recuperare l'email del curatore
     * @return String, l'email
     */
    public String getEmailCuratore() { return emailCuratore; }

    /**
     * Permette di impostare l'id del Luogo
     * @param id, l'id da impostare
     */
    public void setId(int id) {
        super.setId(id);
    }

    /**
     * Metodo di print di un'istanza di Luogo custom
     * @return String, output formattato
     */
    @NonNull
    @Override
    public String toString() {
        return super.toString() +
                ", tipologia=" + tipologia +
                ", emailCuratore=" + emailCuratore;
    }

    /**
     * Interfaccia che consente la differenziazione delle tipologie di Luogo
     */
    public interface tipologiaLuoghi {
        String MUSEO = "Museo";
        String AREA_ARCHEOLOGICA = "Area Archeologica";
        String SITO_CULTURALE = "Sito Culturale";
        String MOSTRA_ITINERANTE = "Mostra Itinerante";
    }

    /**
     * Interfaccia per identificare univocamente il luogo
     */
    public interface Keys{
        String ID = "ID_LUOGO";
    }
}
