package com.example.eculturetool.database;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.example.eculturetool.R;
import com.example.eculturetool.entities.Oggetto;
import com.example.eculturetool.entities.Zona;
import com.google.gson.Gson;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;
import org.jgrapht.graph.SimpleGraph;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Classe di supporto che contiene metodi utili di INPUT e OUTPUT su File e di conversione da array a Graph
 */
public class IoHelper {

    private static final String SHARE = "_SHARE";
    private static final String SHARE_FOLDER = "fileShare";
    private static final String GRAPH_FOLDER = "graphSerializzazione";
    private static final String LIST_FOLDER = "listSerializzazione";

    //Contesto che viene avvalorato attraverso il costruttore
    private final Context context;
    private Uri contentUri;

    /**
     * Costruttore della classe IoHelper
     * @param context Il contex dell'activity
     */
    public IoHelper(Context context){
        this.context = context;
    }


    /**
     * Metodo che consente di scrivere su file un grafo che rappresenta un percorso (nodi e archi). Il metodo crea un file di tipo TXT nella memoria interna del device.
     * @param graph grafo che rappresenta il percorso
     * @param id identificativo del percorso con cui si andrà a chiamare il file in modo da dare un identificativo univoco
     */
    public void serializzaPercorso(Graph graph, int id){
        final String FILE_NAME = id + ".txt";
        FileOutputStream outFile = null;
        ObjectOutputStream outStream = null;

        //Ottiene il percorso e specifica la cartella in cui saranno salvati i dati
        File fileOutputFolder = new File(context.getFilesDir(), GRAPH_FOLDER); //cartella in cui salvare i file da serializzare
        File file;

        try {

            fileOutputFolder.mkdirs(); //crea la cartella se non esiste
            //File da salvare
            file = new File(fileOutputFolder, FILE_NAME);
            outFile = new FileOutputStream(file);

            //outFile = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
            outStream = new ObjectOutputStream(outFile);
            outStream.writeObject(graph);

        }catch(FileNotFoundException e) {
            System.err.println("Errore in scrittura: file non trovato");
        }catch(IOException e) {
            e.printStackTrace();
        }catch(Exception e) {
            e.printStackTrace();
        }finally {
            if(outFile != null && outStream != null){
                try {
                    outFile.close();
                    outStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * Metodo che consente di deserializzare un grafo.
     * @param id identificativo del percorso. Sulla base di questo valore sarà possibile recuperare correttamente i dati relativo al percorso.
     * @return  Grafo che rappresenta il percorso
     */
    public Graph<Zona, DefaultEdge> deserializzaPercorso(int id){
        final String FILE_NAME = id + ".txt";
        Graph<Zona, DefaultEdge> graph = new SimpleDirectedGraph<>(DefaultEdge.class);
        Object risultato;

        FileInputStream inFile = null;							//stream di input da file
        ObjectInputStream inStream = null;

        File fileInputFolder = new File(context.getFilesDir(), GRAPH_FOLDER); //cartella in cui leggere i file da deserializzare
        File file;

        try {
            file = new File(fileInputFolder, FILE_NAME);
            inFile = new FileInputStream(file);

            //inFile = context.openFileInput(FILE_NAME);	//istanziazione dello stream di input riferito a un certo file

            if(inFile.available() != 0)
                inStream = new ObjectInputStream(inFile);

            risultato = inStream.readObject();		//legge l'oggetto dal file
            graph = (Graph<Zona, DefaultEdge>) risultato;

        }catch(FileNotFoundException e) {
            System.err.println("Errore in lettura: file non trovato");
        }catch(IOException e) {
            e.printStackTrace();
        }catch(Exception e) {
            e.printStackTrace();
        }finally {
            if(inFile != null && inStream != null){
                try {
                    inStream.close();
                    inFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return graph;
    }


    /**
     * Questo metodo consente di eliminare il file contenente i dai di un percorso sottoforma di grafo una volta che lo steso percorso è stato eliminato dal db SQLite
     * @param id identificativo del percorso fornito da SQLite
     * @return valore booleano: true se il file è stato cancellato correttamente, false altrimenti
     * @throws IllegalArgumentException eccezione che comprande diversi tipi di situazioni da gestire
     */
    public boolean cancellaPercorso(int id) throws IllegalArgumentException {
        final String FILE_NAME = id + ".txt";
        boolean risultato = false;

        File dir = new File(context.getFilesDir(), GRAPH_FOLDER);
        File file = new File(dir, FILE_NAME);

        if (!file.exists()) {
            throw new IllegalArgumentException("Il file o la Directory non esiste: " + FILE_NAME);
        }

        if (!file.canWrite()) {
            throw new IllegalArgumentException("Non ho il permesso di scrittura: " + FILE_NAME);
        }

        risultato = file.delete();

        if (!risultato) {
            throw new IllegalArgumentException("Cancellazione Fallita!");
        }

        return risultato;
    }


    /**
     * Questo metodo consente di eliminare il file contenente i dai di un percorso sottoforma di ARRAY DI ARRAY una volta che lo steso percorso è stato eliminato dal db SQLite
     * @param id identificativo del percorso fornito da SQLite
     * @return valore booleano: true se il file è stato cancellato correttamente, false altrimenti
     * @throws IllegalArgumentException eccezione che comprande diversi tipi di situazioni da gestire
     */
    public boolean cancellaPercorsoArray(int id) throws IllegalArgumentException {
        final String FILE_NAME = id + ".txt";
        boolean risultato = false;

        File dir = new File(context.getFilesDir(), LIST_FOLDER); //cartella in cui salvare i file da serializzare
        File file = new File(dir, FILE_NAME);

        if (!file.exists()) {
            throw new IllegalArgumentException("Il file o la Directory non esiste: " + FILE_NAME);
        }

        if (!file.canWrite()) {
            throw new IllegalArgumentException("Non ho il permesso di scrittura: " + FILE_NAME);
        }

        risultato = file.delete();

        if (!risultato) {
            throw new IllegalArgumentException("Cancellazione Fallita!");
        }

        return risultato;
    }

    /**
     * Questo metodo consente di eliminare il file contenente i dai di un percorso sottoforma di JSON una volta che lo steso percorso è stato eliminato dal db SQLite
     * @param id identificativo del percorso fornito da SQLite
     * @return valore booleano: true se il file è stato cancellato correttamente, false altrimenti
     * @throws IllegalArgumentException eccezione che comprande diversi tipi di situazioni da gestire
     */
    public boolean cancellaPercorsoJson(int id) throws IllegalArgumentException {
        final String FILE_NAME = id + SHARE + ".txt";
        boolean risultato = false;

        File dir = new File(context.getFilesDir(), SHARE_FOLDER); //cartella in cui salvare i file da condividere
        File file = new File(dir, FILE_NAME);

        if (!file.exists()) {
            throw new IllegalArgumentException("Il file o la Directory non esiste: " + FILE_NAME);
        }

        if (!file.canWrite()) {
            throw new IllegalArgumentException("Non ho il permesso di scrittura: " + FILE_NAME);
        }

        risultato = file.delete();

        if (!risultato) {
            throw new IllegalArgumentException("Cancellazione Fallita!");
        }

        return risultato;
    }


    public void esportaTxt(Graph graph, int id){
        final String FILE_NAME = id + SHARE + ".txt";
        List<Zona> zone = new ArrayList<>();
        Iterator<Zona> iterator = graph.vertexSet().iterator();

        zone = fromIteratorToArrayZone(iterator);

        File fileOutputFolder = new File(context.getFilesDir(), SHARE_FOLDER); //cartella in cui salvare i file da condividere

        FileOutputStream fileOutputStream = null;
        try {
            fileOutputFolder.mkdirs(); //crea la cartella se non esiste
            File file = new File(fileOutputFolder, FILE_NAME); //il file da salvare

            fileOutputStream = new FileOutputStream(file);

            for(int i = 0; i < zone.size(); i++){
                fileOutputStream.write((i + 1 + ") " + zone.get(i).getNome() + "\n").getBytes());

                Iterator<Oggetto> iteratoreOggetti = zone.get(i).getListaOggetti().iterator();
                while (iteratoreOggetti.hasNext()){
                    fileOutputStream.write(("   - " + iteratoreOggetti.next().getNome() + "\n").getBytes());
                }
            }

            contentUri = FileProvider.getUriForFile(context, "com.example.eculturetool.fileprovider", file);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(fileOutputStream != null){
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     *Metodo che contiene la logica di salvataggio su file un file TXT contenente dati in formato JSON. Si appoggia a un metodo che consente di prendere un array e di scriverlo in formato JSon su File
     * @param graph grafo da scrivere su file
     * @param id id del percorso a cui fa riferimento quel grafo
     */
    public void esportaTxtinJsonFormat(Graph graph, int id){
        final String FILE_NAME = id + SHARE + ".txt";
        List<Zona> zone = new ArrayList<>();
        Iterator<Zona> iterator = graph.vertexSet().iterator();

        zone = fromIteratorToArrayZone(iterator);

        File fileOutputFolder = new File(context.getFilesDir(), SHARE_FOLDER); //cartella in cui salvare i file da condividere

        FileOutputStream fileOutputStream = null;
        try {
            fileOutputFolder.mkdirs(); //crea la cartella se non esiste
            File file = new File(fileOutputFolder, FILE_NAME); //il file da salvare

            fileOutputStream = new FileOutputStream(file);

            //Scrive su file i byte che restituiti dal metodo arrayToJsonString
            fileOutputStream.write(arrayToJsonString(zone));

            contentUri = FileProvider.getUriForFile(context, "com.example.eculturetool.fileprovider", file);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(fileOutputStream != null){
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Metodo che converte un array in una stringa scritta in formato Json
     * @param list lista da convertire
     * @return i bytes della stringa in formato Json
     */
    private byte[] arrayToJsonString(List<Zona> list){
        String jsonString;
        Gson gson = new Gson().newBuilder().setPrettyPrinting().create();
        jsonString = gson.toJson(list);
        return jsonString.getBytes();
    }

    /**
     * Metodo che riceve in input l'identificativo di un percorso e condivide il file riepilogativo con un'altra app (Gmail, What's app...)
     * @param id identificativo del percorso
     */
    public void shareFileTxt(int id){
        String fileName = id + SHARE + ".txt";
        String stringFile = context.getFilesDir() +  "/" + SHARE_FOLDER +  File.separator + fileName;

        File file = new File(stringFile);
        contentUri = FileProvider.getUriForFile(context, "com.example.eculturetool.fileprovider", file);

        //Controlla se il file esiste
        if(!file.exists()){
            Toast.makeText(context, context.getString(R.string.file_non_esiste), Toast.LENGTH_LONG).show();
            return;
        }

        //Attributi che l'intent deve avere per poter effettuare lo share e il richiamo di applicazioni che consentono di ricevere file
        Intent intentShare = new Intent(Intent.ACTION_SEND);
        intentShare.setType("text/*");
        intentShare.putExtra(Intent.EXTRA_SUBJECT, "Subject Here"); //per condividere con email app
        intentShare.putExtra(Intent.EXTRA_STREAM, contentUri);
        intentShare.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(Intent.createChooser(intentShare, "Condividi file"));
    }


    /**
     * metoto che inserisce elementi in una lista provenienti da un iteratore
     * @param iterator
     * @return la lista degli oggetti che siamo andati ad iterare
     */
    private List<Zona> fromIteratorToArrayZone(Iterator<Zona> iterator) {
        List<Zona> returnList = new ArrayList<>();

        while (iterator.hasNext()){
            returnList.add(iterator.next());
        }

        return returnList;
    }


    /**
     * Metoto che converte una lista in un grafo facendo uso di cicli
     * @param list lista da convertire
     * @return grafo ottenuto dalla conversione
     */
    public Graph<Zona, DefaultEdge> fromListToGraph(List<Zona> list){
        Graph<Zona, DefaultEdge> returnGraph = new SimpleDirectedGraph<>(DefaultEdge.class);

        //Aggiunge al grafo tutte le zone che appartengono al percorso principale
        for(Zona zona : list){
            returnGraph.addVertex(zona);
        }
        //indico l'elemento finale della lista
        list.get(0).setFinal(true);
        list.get(list.size()-1).setFinal(true);
        //Collega tutte le zone contenute nel percorso principale sequenzialmente
        for(int i = 0; i < list.size()-1; i++){
            returnGraph.addEdge(list.get(i), list.get(i+1));
        }

        //Aggiunge tutte le zone delle diramazioni al grafo. Siccome sono implementati equals() e hashCode()
        //aggiunge solo nel caso in cui ci siano zone diverse da quell già inserite
        for(int i = 0; i < list.size(); i++){
            for(int j = 0; j < list.get(i).getDiramazione().size(); j++){
                returnGraph.addVertex(list.get(i).getDiramazione().get(j));
            }
        }

        //Collega tutte le zone all'interno della diramazione
        for(int i = 0; i < list.size(); i++){
            System.out.println(list.get(i).getNome() + "\n");
            if(list.get(i).getDiramazione().size() != 0){
                returnGraph.addEdge(list.get(i), list.get(i).getDiramazione().get(0));

                for(int j = 0; j < list.get(i).getDiramazione().size()-1; j++){
                    returnGraph.addEdge(list.get(i).getDiramazione().get(j), list.get(i).getDiramazione().get(j+1));
                }
            }

        }

        return returnGraph;
    }


    /**
     * Metoto che effettua una serializzazione su file di una lista di zone
     * @param list lista da serializzare
     * @param id id del percorso da serializzare
     */
    public void listZoneSerializzazione(List<Zona> list, int id){
        final String FILE_NAME = id + ".txt";
        FileOutputStream outFile = null;
        ObjectOutputStream outStream = null;

        File fileOutputFolder = new File(context.getFilesDir(), LIST_FOLDER); //cartella in cui salvare i file da serializzare
        File file;

        try {

            fileOutputFolder.mkdirs(); //crea la cartella se non esiste
            file = new File(fileOutputFolder, FILE_NAME);
            outFile = new FileOutputStream(file);

            outStream = new ObjectOutputStream(outFile);
            outStream.writeObject(list);

        }catch(FileNotFoundException e) {
            System.err.println("Errore in scrittura: file non trovato");
        }catch(IOException e) {
            e.printStackTrace();
        }catch(Exception e) {
            e.printStackTrace();
        }finally {
            if(outFile != null && outStream != null){
                try {
                    outFile.close();
                    outStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * Metoto che deserializza una lisya memoriazzati in un file
     * @param id della percorso da deserializzare e scritto su file
     * @return
     */
    public List<Zona> listZoneDeserializzazione(int id){
        List<Zona> returnList = null;
        final String FILE_NAME = id + ".txt";
        Object risultato;

        FileInputStream inFile = null;							//stream di input da file
        ObjectInputStream inStream = null;

        File fileInputFolder = new File(context.getFilesDir(), LIST_FOLDER); //cartella in cui leggere i file da deserializzare
        File file;

        try {
            file = new File(fileInputFolder, FILE_NAME);
            inFile = new FileInputStream(file);

            //inFile = context.openFileInput(FILE_NAME);	//istanziazione dello stream di input riferito a un certo file

            if(inFile.available() != 0)
                inStream = new ObjectInputStream(inFile);

            risultato = inStream.readObject();		//legge l'oggetto dal file
            returnList = (List<Zona>) risultato;

        }catch(FileNotFoundException e) {
            System.err.println("Errore in lettura: file non trovato");
        }catch(IOException e) {
            e.printStackTrace();
        }catch(Exception e) {
            e.printStackTrace();
        }finally {
            if(inFile != null && inStream != null){
                try {
                    inStream.close();
                    inFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return returnList;
    }

}
