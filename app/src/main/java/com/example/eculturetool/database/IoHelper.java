package com.example.eculturetool.database;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.example.eculturetool.database.DataBaseHelper;
import com.example.eculturetool.entities.Oggetto;
import com.example.eculturetool.entities.Percorso;
import com.example.eculturetool.entities.Zona;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.nio.json.JSONExporter;
import org.jgrapht.nio.json.JSONImporter;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class IoHelper {

    private Context context;
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

        try {

            outFile = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
            outStream = new ObjectOutputStream(outFile);
            outStream.writeObject(graph);
            Toast.makeText(context, "Salvato in " + context.getFilesDir() + "/" + FILE_NAME, Toast.LENGTH_LONG).show();

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
        Graph<Zona, DefaultEdge> graph = new SimpleGraph<>(DefaultEdge.class);
        Object risultato;

        FileInputStream inFile = null;							//stream di input da file
        ObjectInputStream inStream = null;

        try {
            inFile = context.openFileInput(FILE_NAME);	//istanziazione dello stream di input riferito a un certo file

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

        File dir = context.getFilesDir();
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
        final String FILE_NAME = id + "_TXT" + ".txt";
        List<Zona> zone = new ArrayList<>();
        Iterator<Zona> iterator = graph.vertexSet().iterator();

        zone = fromIteratorToArrayZone(iterator);

        File fileOutputFolder = new File(context.getFilesDir(), "fileOutput"); //cartella in cui salvare i file da condividere

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
     * Metodo che riceve in input l'identificativo di un percorso e condivide il file riepilogativo con un'altra app (Gmail, What's app...)
     * @param id identificativo del percorso
     */
    public void shareFileTxt(int id){
        String fileName = id + "_TXT.txt";
        String stringFile = context.getFilesDir() +  "/fileOutput" +  File.separator + fileName;

        File file = new File(stringFile);
        contentUri = FileProvider.getUriForFile(context, "com.example.eculturetool.fileprovider", file);

        if(!file.exists()){
            Toast.makeText(context, "Il file non esiste!", Toast.LENGTH_LONG).show();
            return;
        }

        Intent intentShare = new Intent(Intent.ACTION_SEND);
        intentShare.setType("text/*");
        intentShare.putExtra(Intent.EXTRA_SUBJECT, "Subject Here"); //per condividere con email app
        intentShare.putExtra(Intent.EXTRA_STREAM, contentUri);
        intentShare.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(Intent.createChooser(intentShare, "Condividi file"));
    }


    private List<Zona> fromIteratorToArrayZone(Iterator<Zona> iterator) {
        List<Zona> returnList = new ArrayList<>();

        while (iterator.hasNext()){
            returnList.add(iterator.next());
        }

        return returnList;
    }


    public Graph<Zona, DefaultEdge> fromListToGraph(List<Zona> list){
        Graph<Zona, DefaultEdge> returnGraph = new SimpleGraph<>(DefaultEdge.class);

        //Aggiunge al grafo tutte le zone che appartengono al percorso principale
        for(Zona zona: list){
            returnGraph.addVertex(zona);
        }

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


}
