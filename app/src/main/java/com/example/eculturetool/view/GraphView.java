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
import android.widget.TextView;

import org.jgrapht.graph.DefaultEdge;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.eculturetool.R;
import com.example.eculturetool.entities.Vertice;
import com.example.eculturetool.entities.Zona;

import org.jgrapht.Graph;
import org.jgrapht.graph.SimpleDirectedGraph;
import org.jgrapht.graph.SimpleGraph;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class GraphView extends View{

    ArrayList<Vertice> vertices = new ArrayList<>();
    Graph<Zona,DefaultEdge>grafo;



    public GraphView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.grafo = new SimpleDirectedGraph<>(DefaultEdge.class);


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

        for (int j = 200; j< 3000; j+=200) {

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

    private void drawArrow(Point startPoint, Point endPoint, Paint paint, Canvas mCanvas) {
        Path mPath = new Path();
        float deltaX = endPoint.x - startPoint.x;
        float deltaY = endPoint.y - startPoint.y;
        //float frac = (float) 0.1;
        int ARROWHEAD_LENGTH = 20;
        float sideZ = (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY);
        float frac = ARROWHEAD_LENGTH < sideZ ? ARROWHEAD_LENGTH / sideZ : 1.0f;
        float point_x_1 = startPoint.x + (float) ((1 - frac) * deltaX + frac * deltaY);
        float point_y_1 = startPoint.y + (float) ((1 - frac) * deltaY - frac * deltaX);
        float point_x_2 = endPoint.x;
        float point_y_2 = endPoint.y;
        float point_x_3 = startPoint.x + (float) ((1 - frac) * deltaX - frac * deltaY);
        float point_y_3 = startPoint.y + (float) ((1 - frac) * deltaY + frac * deltaX);
        mPath.moveTo(point_x_1, point_y_1);
        mPath.lineTo(point_x_2, point_y_2);
        mPath.lineTo(point_x_3, point_y_3);
        //mPath.lineTo(point_x_1, point_y_1);
        //mPath.lineTo(point_x_1, point_y_1);
        mCanvas.drawPath(mPath, paint);
        invalidate();
    }



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int colorVertice = ContextCompat.getColor(getContext(), R.color.verdePrimario);

        Paint paintVertex = new Paint();
        paintVertex.setTextSize(60);
        paintVertex.setColor(colorVertice);


        for (Vertice v : vertices) {
            canvas.drawCircle(v.getX(), v.getY(), 30, paintVertex);

            String nomeZona = v.getNomeVertice();
            String nomeZonaRidotto = "";

            if (nomeZona.length() > 10) {

                String[] split = nomeZona.split(" ",2);


                for (String s : split) {
                        s = s.substring(0, 3);

                    if (s.length()>2) {
                        nomeZonaRidotto = nomeZonaRidotto + s + ". ";
                    }



                }



                canvas.drawText(nomeZonaRidotto, v.getX() - 110, v.getY() - 60, paintVertex);
            }
            else {
                canvas.drawText(nomeZona, v.getX() - 110, v.getY() - 60, paintVertex);

            }

            int colorArrow = ContextCompat.getColor(getContext(), R.color.orangeAction);
            int colorArco = ContextCompat.getColor(getContext(), R.color.gialloPrimario);

            Paint paintEdge = new Paint();
            paintEdge.setColor(colorArco);
            paintEdge.setStrokeWidth(7);
            Paint paintArrow = new Paint();
            paintArrow.setColor(colorArrow);
            paintArrow.setStrokeWidth(20);

//disegno tutti gli archi
            for (DefaultEdge e : grafo.edgeSet()) {


                Zona v1 = grafo.getEdgeSource(e);
                Zona v2 = grafo.getEdgeTarget(e);

                //se a sta sopra a b


                if( returnY(v1.getNome()) < returnY(v2.getNome()) ){


                    canvas.drawLine(returnX(v1.getNome()), returnY(v1.getNome())+30, returnX(v2.getNome()), returnY(v2.getNome())-30, paintEdge);


                }else

                //se a sta sotto b

                if( returnY(v1.getNome()) > returnY(v2.getNome())){


                    canvas.drawLine(returnX(v1.getNome()), returnY(v1.getNome())-30, returnX(v2.getNome()), returnY(v2.getNome())+30, paintEdge);


                }else

                // se a sta a destra di b

                if(returnX(v1.getNome()) < returnX(v2.getNome())){

                    canvas.drawLine(returnX(v1.getNome())+30, returnY(v1.getNome()), returnX(v2.getNome())-30, returnY(v2.getNome()), paintEdge);


                }else

                // se a sta a sinistra di b
                if(returnX(v1.getNome()) > returnX(v2.getNome())){

                    canvas.drawLine(returnX(v1.getNome())-30, returnY(v1.getNome()), returnX(v2.getNome())+30, returnY(v2.getNome()), paintEdge);


                }





            }

            //disegno tutte le frecce

            for (DefaultEdge e : grafo.edgeSet()) {


                Zona v1 = grafo.getEdgeSource(e);
                Zona v2 = grafo.getEdgeTarget(e);


                //se a sta sopra a b


                if( returnY(v1.getNome()) < returnY(v2.getNome()) ){

                    Point startPoint= new Point((int) returnX(v1.getNome()), (int)returnY(v1.getNome())+30);
                    Point endPoint = new Point((int)returnX(v2.getNome()), (int)returnY(v2.getNome())-30);
                    this.drawArrow(startPoint,endPoint,paintArrow,canvas);

                }else

                    //se a sta sotto b

                    if( returnY(v1.getNome()) > returnY(v2.getNome())){
                        Point startPoint= new Point((int) returnX(v1.getNome()), (int)returnY(v1.getNome())-30);
                        Point endPoint = new Point((int)returnX(v2.getNome()), (int)returnY(v2.getNome())+30);
                        this.drawArrow(startPoint,endPoint,paintArrow,canvas);

                    }else

                        // se a sta a destra di b

                        if(returnX(v1.getNome()) < returnX(v2.getNome())){
                            Point startPoint= new Point((int) returnX(v1.getNome())+30, (int)returnY(v1.getNome()));
                            Point endPoint = new Point((int)returnX(v2.getNome())-30, (int)returnY(v2.getNome()));
                            this.drawArrow(startPoint,endPoint,paintArrow,canvas);

                        }else

                            // se a sta a sinistra di b
                            if(returnX(v1.getNome()) > returnX(v2.getNome())){
                                Point startPoint= new Point((int) returnX(v1.getNome())-30, (int)returnY(v1.getNome()));
                                Point endPoint = new Point((int)returnX(v2.getNome())+30, (int)returnY(v2.getNome()));
                                this.drawArrow(startPoint,endPoint,paintArrow,canvas);

                            }
            }




            }

    }
}









