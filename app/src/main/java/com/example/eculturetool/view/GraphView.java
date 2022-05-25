package com.example.eculturetool.view;

import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import org.jgrapht.graph.DefaultEdge;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.eculturetool.entities.Vertice;
import com.example.eculturetool.entities.Zona;

import org.jgrapht.Graph;
import org.jgrapht.graph.SimpleGraph;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class GraphView extends View{

    ArrayList<Vertice> vertices = new ArrayList<>();
    Graph<Zona,DefaultEdge>grafo;



    public GraphView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.grafo = new SimpleGraph<Zona,DefaultEdge>(DefaultEdge.class);


//inizializzo tutti i vertici che servono;




    }

    private void costruzioneGrafo(Graph<Zona, DefaultEdge> grafo) {
        for (Zona v1 : grafo.vertexSet()) {
            vertices.add(new Vertice(v1.getNome()));

        }
        int i=1;
        int x,y;
        String postV;
        String afterV;
        for (Zona v1 : grafo.vertexSet()){
            for (DefaultEdge e : grafo.outgoingEdgesOf(v1)){


                Zona source = grafo.getEdgeSource(e);

                x = vertices.get(returnIndice(source.getNome())).getX();;
                y = vertices.get(returnIndice(source.getNome())).getY();;


                Zona target = grafo.getEdgeTarget(e);
                if(this.verticeNonAncoraModificato(target.getNome())){

                    while(this.esisteUnVerticeConQuelleCoordinate(x*i,y+200)){
                        x=x+200;
                    }
                    vertices.get(returnIndice(target.getNome())).setX(x*i);
                    vertices.get(returnIndice(target.getNome())).setY(y+200);
                }

            }
            i=1;

        }

        normalizzaPunti();
    }

    public void setGrafo (Graph<Zona,DefaultEdge> grafo){
        this.grafo = grafo;
        costruzioneGrafo(grafo);
    }

    public void normalizzaPunti(){
        int numeroVertici = 0;
        int larghezzaDisplay= getScreenWidth();
        ArrayList <Integer> indici = new ArrayList<>();

        //conto numero vertici per y fisso

        for (int j = 200; j< 1000; j+=200) {

            for (int i = 0; i < vertices.size(); i++) {
                if (vertices.get(i).getY() == j) {
                    numeroVertici += 1;
                    indici.add(i);
                }
            }
            int c= 1;
            for (int i : indici) {
                vertices.get(i).setX((larghezzaDisplay / (numeroVertici + 1)) * c);
                c++;
            }
            c=1;

            numeroVertici = 0;

            indici = new ArrayList<>();

        }







    }
    public boolean verticeNonAncoraModificato(String target){
        boolean flag = false;
        if(vertices.get(returnIndice(target)).getX()==200&&vertices.get(returnIndice(target)).getY()==200){
            flag = true;
        }
        return flag;


    }

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    public boolean esisteUnVerticeConQuelleCoordinate(int x,int y){
        boolean flag = false;
        for (Vertice v :vertices){
            if(v.getX()==x && v.getY()==y){
                flag = true;
                return flag;
            }
        }
        return flag;

    }




/*
    private static Graph<String, DefaultEdge> graph()
    {
        Graph<String, DefaultEdge> g = new SimpleGraph<>(DefaultEdge.class);

        String v1 = "v1";
        String v2 = "v2";
        String v3 = "v3";
        String v4 = "v4";
        String v5 = "v5";
        String v7 = "v7";
        String v8 = "v8";

        g.addVertex(v1);
        g.addVertex(v2);
        g.addVertex(v3);
        g.addVertex(v4);
        g.addVertex(v5);
        g.addVertex("v6");
        g.addVertex(v7);
        g.addVertex(v8);




        g.addEdge(v1, v2);
        g.addEdge(v1, v3);
        g.addEdge(v2, v4);
        g.addEdge(v2, v5);
        g.addEdge(v4, v5);
        g.addEdge(v3,v4);
        g.addEdge(v5,"v6");
        g.addEdge(v8,v7);
        g.addEdge(v3,v8);

        return g;
    }
*/
    public float returnX (String v){
        int x =0;

        for (Vertice ver : vertices){
            if (ver.getNomeVertice().equals(v)){
                x= ver.getX();
            }
        }
        return x;

    }

    public float returnY (String v){
        int y =0;

        for (Vertice ver : vertices){
            if (ver.getNomeVertice().equals(v)){
                y= ver.getY();
            }
        }
        return y;

    }

    public int returnIndice (String v){
        int indice = 0;
        for(int i=0; i< vertices.size(); i++){
            if(vertices.get(i).getNomeVertice().equals(v)){
                indice=i;
            }
        }
        return indice;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint paintVertex =new Paint();
        paintVertex.setTextSize(100);
        paintVertex.setColor(Color.BLUE);

        for (Vertice v : vertices){
            canvas.drawCircle(v.getX(), v.getY(), 30, paintVertex);
            canvas.drawText(v.getNomeVertice(),v.getX(),v.getY()-30,paintVertex);
        }

        Paint paintEdge =new Paint();
        paintEdge.setColor(Color.GREEN);
        paintEdge.setStrokeWidth(7);

        for (DefaultEdge e: grafo.edgeSet()){

            Zona v1 = grafo.getEdgeSource(e);
            Zona v2 = grafo.getEdgeTarget(e);


            canvas.drawLine(returnX(v1.getNome()),returnY(v1.getNome()),returnX(v2.getNome()),returnY(v2.getNome()),paintEdge);




        }
    }

}









