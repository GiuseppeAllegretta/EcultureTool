package com.example.eculturetool.utilities;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eculturetool.R;
import com.example.eculturetool.database.DataBaseHelper;
import com.example.eculturetool.entities.Entita;

import java.util.ArrayList;
import java.util.List;


/**
 * Permette di ricercare elementi di tipo Entita (luoghi, zone, oggetti), passando il nome della classe da cercare
 * @param <T>
 */
public class Searcher<T extends Entita> extends AppCompatActivity {

    private String type;
    private ArrayList<T> array = new ArrayList<>();
    private DataBaseHelper dataBaseHelper;


    public Searcher() {
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searcher);
        dataBaseHelper = new DataBaseHelper(getApplicationContext());

        type = getIntent().getStringExtra("CONTENT");

        switch (type) {
            case "Luogo":
                array = (ArrayList<T>) dataBaseHelper.getLuoghi();
                break;

            case "Zona":
                array = (ArrayList<T>) dataBaseHelper.getZone();
                break;

            case "Oggetto":
                array = (ArrayList<T>) dataBaseHelper.getAllOggetti();
                break;

            default: System.err.println("Class non valida");
                break;
        }

    }

}
