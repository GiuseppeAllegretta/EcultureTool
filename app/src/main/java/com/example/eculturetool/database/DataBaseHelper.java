package com.example.eculturetool.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.eculturetool.entities.Curatore;

public class DataBaseHelper extends SQLiteOpenHelper {

    public static final String CURATORI = "CURATORI";
    public static final String COLONNA_CURATORE_NOME = "CURATORE_NOME";
    public static final String COLONNA_CURATORE_COGNOME = "CURATORE_COGNOME";
    public static final String COLONNA_CURATORE_LUOGO_CORRENTE = "CURATORE_LUOGO_CORRENTE";
    public static final String COLONNA_EMAIL = "EMAIL";
    public static final String COLONNA_CURATORE_PASSWORD = "CURATORE_PASSWORD";

    public DataBaseHelper(@Nullable Context context) {
        super(context, "e-cultureTool.db", null, 1);
    }

    //this is called the first time a database is accessed. There should be code in here to create a new database
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //String che consente di creare la tabella Curatori
        String createTableCuratore= "CREATE TABLE " + CURATORI + " (" + COLONNA_EMAIL + " TEXT PRIMARY KEY, " + COLONNA_CURATORE_PASSWORD + " TEXT, " + COLONNA_CURATORE_NOME + " TEXT, " + COLONNA_CURATORE_COGNOME + " TEXT, " + COLONNA_CURATORE_LUOGO_CORRENTE + " INT)";

        sqLiteDatabase.execSQL(createTableCuratore);
    }

    //this is called if the databse version number changes. It prevents previous users apps from breaking whe you change the database design
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }


    public boolean addCuratore(Curatore curatore){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLONNA_EMAIL, curatore.getEmail());
        contentValues.put(COLONNA_CURATORE_PASSWORD, curatore.getPassword());
        contentValues.put(COLONNA_CURATORE_NOME, curatore.getNome());
        contentValues.put(COLONNA_CURATORE_COGNOME, curatore.getCognome());
        contentValues.put(COLONNA_CURATORE_LUOGO_CORRENTE, curatore.getLuogoCorrente());

        long insert = db.insert(CURATORI, null, contentValues);

        if(insert == -1){
            return false;
        }else {
            return true;
        }


    }
}
