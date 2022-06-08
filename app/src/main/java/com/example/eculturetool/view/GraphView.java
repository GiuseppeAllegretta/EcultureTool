package com.example.eculturetool.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.eculturetool.R;
import com.example.eculturetool.entities.Vertice;
import com.example.eculturetool.entities.Zona;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;

import java.util.ArrayList;


public class GraphView extends View {

    private ArrayList<Vertice> vertices = new ArrayList<>();
    private Graph<Zona, DefaultEdge> grafo;

    public GraphView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.grafo = new SimpleDirectedGraph<>(DefaultEdge.class);

    }

    //creo una struttura dati che contenga
    // oltre che il nome del vertice,
    // le cordinate e le calcoli automaticamente
    private void costruzioneGrafo(Graph<Zona, DefaultEdge> grafo) {
        for (Zona v1 : grafo.vertexSet()) {
            vertices.add(new Vertice(v1.getNome(), v1.isFinal()));

            // inizializzo i vertici e assegno le coordinate automaticamente
        }
        int i = 1;
        int x, y;
        String postV;
        String afterV;
        for (Zona v1 : grafo.vertexSet()) {
            for (DefaultEdge e : grafo.outgoingEdgesOf(v1)) {

                Zona source = grafo.getEdgeSource(e);

                x = vertices.get(returnIndice(source.getNome())).getX();
                y = vertices.get(returnIndice(source.getNome())).getY();

                Zona target = grafo.getEdgeTarget(e);
                if (this.verticeNonAncoraModificato(target.getNome())) {

                    while (this.esisteUnVerticeConQuelleCoordinate(x * i, y + 200)) {
                        x = x + 200;
                    }
                    vertices.get(returnIndice(target.getNome())).setX(x * i);
                    vertices.get(returnIndice(target.getNome())).setY(y + 200);
                }
            }
            i = 1;

        }
        normalizzaPunti();

    }

    //metodo che calcola la grandezza della view in base alla lunghezza del grafo
    public int maxY() {
        int i = 0;

        for (Vertice v : vertices) {
            i = Math.max(i, v.getY());

        }
        return i + 200;
    }


    public void setGrafo(Graph<Zona, DefaultEdge> grafo) {
        this.grafo = grafo;
        costruzioneGrafo(grafo);
    }

    // assegno una dimensione per la view in base a come viene ruotato il dispositivo
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int height = maxY();
        int width = Math.min(getScreenHeight(), getScreenWidth());
        setMeasuredDimension(width, height);
    }


    //posiziono i vertici in modo che siano centrati nella view
    public void normalizzaPunti() {
        int numeroVertici = 0;
        int larghezzaDisplay = Math.min(getScreenHeight(), getScreenWidth());
        ArrayList<Integer> indici = new ArrayList<>();

        //conto numero vertici per y fisso

        for (int j = 200; j < maxY(); j += 200) {

            for (int i = 0; i < vertices.size(); i++) {
                if (vertices.get(i).getY() == j) {
                    numeroVertici += 1;
                    indici.add(i);
                }
            }
            int c = 1;
            for (int i : indici) {
                vertices.get(i).setX((larghezzaDisplay / (numeroVertici + 1)) * c);
                c++;
            }
            c = 1;

            numeroVertici = 0;

            indici = new ArrayList<>();

        }

    }

    //controlla se l'algoritmo ha già calcolato le coordinate per quel vertice
    public boolean verticeNonAncoraModificato(String target) {
        boolean flag = false;
        if (vertices.get(returnIndice(target)).getX() == 200 && vertices.get(returnIndice(target)).getY() == 200) {
            flag = true;
        }
        return flag;
    }

    //restituisce l'ampiezza dello schermo
    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    //restituisce la lunghezza dello schermo
    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    //controlla se ci sono due vertici che hanno le stesse coordinate
// in modo che non ci siano vertici che si sovrappongono
    public boolean esisteUnVerticeConQuelleCoordinate(int x, int y) {
        boolean flag = false;
        for (Vertice v : vertices) {
            if (v.getX() == x && v.getY() == y) {
                flag = true;
                return flag;
            }
        }
        return flag;

    }

    //ritorna l'ascissa del vertice
    public float returnX(String v) {
        int x = 0;

        for (Vertice ver : vertices) {
            if (ver.getNomeVertice().equals(v)) {
                x = ver.getX();
            }
        }
        return x;
    }

    //ritorna l'ordinata del vertice
    public float returnY(String v) {
        int y = 0;

        for (Vertice ver : vertices) {
            if (ver.getNomeVertice().equals(v)) {
                y = ver.getY();
            }
        }
        return y;
    }

    //ritorna l'indice dell'arraylist dei vertici
    // in base al nome del vertice
    public int returnIndice(String v) {
        int indice = 0;
        for (int i = 0; i < vertices.size(); i++) {
            if (vertices.get(i).getNomeVertice().equals(v)) {
                indice = i;
            }
        }
        return indice;
    }

    // metodo per disegnare le frecce
    private void drawArrow(Point startPoint, Point endPoint, Paint paint, Canvas mCanvas) {
        Path mPath = new Path();
        float deltaX = endPoint.x - startPoint.x;
        float deltaY = endPoint.y - startPoint.y;
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
        mCanvas.drawPath(mPath, paint);
        invalidate();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //paint dei vertici
        int colorVertice = ContextCompat.getColor(getContext(), R.color.verdePrimario);
        Paint paintVertex = new Paint();
        paintVertex.setTextSize(60);
        paintVertex.setColor(colorVertice);

        //paint degli archi
        int colorArco = ContextCompat.getColor(getContext(), R.color.gialloPrimario);
        Paint paintEdge = new Paint();
        paintEdge.setColor(colorArco);
        paintEdge.setStrokeWidth(7);

        //paint delle frecce
        int colorArrow = ContextCompat.getColor(getContext(), R.color.orangeAction);
        Paint paintArrow = new Paint();
        paintArrow.setColor(colorArrow);
        paintArrow.setStrokeWidth(20);

        //per ogni vertice stampa prima gli archi e le frecce
        for (Vertice v : vertices) {
            //disegno tutti gli archi
            for (DefaultEdge e : grafo.edgeSet()) {

                Zona v1 = grafo.getEdgeSource(e);
                Zona v2 = grafo.getEdgeTarget(e);

                //se l'arco sorgente sta sopra quello di destinazione

                if (returnY(v1.getNome()) < returnY(v2.getNome())) {
                    canvas.drawLine(returnX(v1.getNome()), returnY(v1.getNome()) + 30, returnX(v2.getNome()), returnY(v2.getNome()) - 30, paintEdge);
                } else

                    //se l'arco sorgente sta sotto quello di destinazione

                    if (returnY(v1.getNome()) > returnY(v2.getNome())) {
                        canvas.drawLine(returnX(v1.getNome()), returnY(v1.getNome()) - 30, returnX(v2.getNome()), returnY(v2.getNome()) + 30, paintEdge);
                    } else

                        //se l'arco sorgente sta a destra di quello di destinazione

                        if (returnX(v1.getNome()) < returnX(v2.getNome())) {
                            canvas.drawLine(returnX(v1.getNome()) + 30, returnY(v1.getNome()), returnX(v2.getNome()) - 30, returnY(v2.getNome()), paintEdge);
                        } else

                            //se l'arco sorgente sta a sinistra di quello di destinazione
                            if (returnX(v1.getNome()) > returnX(v2.getNome())) {
                                canvas.drawLine(returnX(v1.getNome()) - 30, returnY(v1.getNome()), returnX(v2.getNome()) + 30, returnY(v2.getNome()), paintEdge);
                            }
            }

            //disegno tutte le frecce

            for (DefaultEdge e : grafo.edgeSet()) {

                Zona v1 = grafo.getEdgeSource(e);
                Zona v2 = grafo.getEdgeTarget(e);

                //se il vertice sorgente sta sopra quello di destinazione

                if (returnY(v1.getNome()) < returnY(v2.getNome())) {
                    Point startPoint = new Point((int) returnX(v1.getNome()), (int) returnY(v1.getNome()) + 30);
                    Point endPoint = new Point((int) returnX(v2.getNome()), (int) returnY(v2.getNome()) - 30);
                    this.drawArrow(startPoint, endPoint, paintArrow, canvas);
                } else

                    //se il vertice sorgente sta sotto quello di destinazione

                    if (returnY(v1.getNome()) > returnY(v2.getNome())) {
                        Point startPoint = new Point((int) returnX(v1.getNome()), (int) returnY(v1.getNome()) - 30);
                        Point endPoint = new Point((int) returnX(v2.getNome()), (int) returnY(v2.getNome()) + 30);
                        this.drawArrow(startPoint, endPoint, paintArrow, canvas);
                    } else

                        //se il vertice sorgente sta a destra di quello di destinazione

                        if (returnX(v1.getNome()) < returnX(v2.getNome())) {
                            Point startPoint = new Point((int) returnX(v1.getNome()) + 30, (int) returnY(v1.getNome()));
                            Point endPoint = new Point((int) returnX(v2.getNome()) - 30, (int) returnY(v2.getNome()));
                            this.drawArrow(startPoint, endPoint, paintArrow, canvas);
                        } else

                            //se il vertice sorgente sta a sinistra di quello di destinazione

                            if (returnX(v1.getNome()) > returnX(v2.getNome())) {
                                Point startPoint = new Point((int) returnX(v1.getNome()) - 30, (int) returnY(v1.getNome()));
                                Point endPoint = new Point((int) returnX(v2.getNome()) + 30, (int) returnY(v2.getNome()));
                                this.drawArrow(startPoint, endPoint, paintArrow, canvas);
                            }
            }
        }

        //disegno tutti i vertici e i nomi dei vertici
        for (Vertice v : vertices) {
            if (!v.isFinal()) {
                canvas.drawCircle(v.getX(), v.getY(), 30, paintVertex);
            } else {
                canvas.drawCircle(v.getX(), v.getY(), 30, paintArrow);
            }

            //riduco i nomi dei vertici troppo lunghi
            String nomeZona = v.getNomeVertice();
            String nomeZonaRidotto = "";

            if (nomeZona.length() > 10) {
                String[] split = nomeZona.split(" ", 2);
                for (String s : split) {
                    if (s.length() > 4) {
                        s = s.substring(0, 3);
                        nomeZonaRidotto = nomeZonaRidotto + s + ". ";
                    } else {
                        nomeZonaRidotto = nomeZonaRidotto + s + " ";
                    }
                }
                canvas.drawText(nomeZonaRidotto, v.getX() - 110, v.getY() - 60, paintVertex);
            } else {
                canvas.drawText(nomeZona, v.getX() - 110, v.getY() - 60, paintVertex);
            }
        }

        //coloro i vertici di inizio e tutti quelli che fanno terminare il percorso di un colore diverso
        for (Zona z : grafo.vertexSet()) {
            if (grafo.outgoingEdgesOf(z).size() == 0) {
                canvas.drawCircle(vertices.get(returnIndice(z.getNome())).getX(), vertices.get(returnIndice(z.getNome())).getY(), 30, paintArrow);
            }
        }
    }
}