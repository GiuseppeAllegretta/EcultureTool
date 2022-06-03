package com.example.eculturetool.activities;

import android.app.Dialog;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.eculturetool.R;
import com.example.eculturetool.entities.DataHolder;
import com.example.eculturetool.entities.Oggetto;
import com.example.eculturetool.entities.Zona;
import com.example.eculturetool.utilities.ImageAdapter;

import java.util.Objects;

public class InfoZonaActivity extends AppCompatActivity {

    DataHolder data = DataHolder.getInstance();

    ImageView closeX;
    TextView titolo;
    TextView descrizione;
    Zona zonaSelezionata;

    GridView gridView;

    public InfoZonaActivity() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_zona);
        Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.descrizione_zona));
        titolo = findViewById(R.id.titolo);
        descrizione = findViewById(R.id.descrizione);
        gridView = findViewById(R.id.grid_oggetti);

        //Ritrovamento della zona aperta
        String title = getIntent().getStringExtra("TITLE");
        zonaSelezionata = data.searchZonaByNome(title);

        //Setting degli elementi dell'interfaccia
        titolo.setText(zonaSelezionata.getNome());
        descrizione.setText(zonaSelezionata.getDescrizione());

        //Setting griglia immagini
        gridView = findViewById(R.id.grid_oggetti);
        gridView.setAdapter(new ImageAdapter(zonaSelezionata.getListaOggetti(), this));
        gridView.setOnItemClickListener((parent, view, position, id) -> showDialogBox(zonaSelezionata.getListaOggetti().get(position)));

        //Tasto x per chiudere
        closeX = findViewById(R.id.ic_close);
        closeX.setOnClickListener(v -> finish());
    }


    public void showDialogBox(Oggetto oggetto){
        Dialog dialog = new Dialog(this);

        dialog.setContentView(R.layout.dialog_anteprima_oggetto);
        TextView imageName = dialog.findViewById(R.id.nome_immagine);
        ImageView image = dialog.findViewById(R.id.immagine);

        //Setting dell'immagine nel dialog
        imageName.setText(oggetto.getNome());
        Glide.with(getApplicationContext()).load(oggetto.getUrl()).into(image);
        dialog.show();
    }

}
