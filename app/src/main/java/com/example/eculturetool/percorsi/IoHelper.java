package com.example.eculturetool.percorsi;

import android.content.Context;
import android.widget.Toast;

import com.example.eculturetool.database.DataBaseHelper;
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

}
