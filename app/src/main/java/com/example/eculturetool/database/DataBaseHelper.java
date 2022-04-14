package com.example.eculturetool.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.eculturetool.entities.Curatore;
import com.example.eculturetool.entities.Luogo;

public class DataBaseHelper extends SQLiteOpenHelper {

    //Email del curatore che si è loggato
    private static String emailCuratore;

    //VARIABILI INERENTI I CURATORI
    public static final String TABLE_CURATORI = "CURATORI";
    public static final String COLONNA_CURATORE_NOME = "CURATORE_NOME";
    public static final String COLONNA_CURATORE_COGNOME = "CURATORE_COGNOME";
    public static final String COLONNA_CURATORE_LUOGO_CORRENTE = "CURATORE_LUOGO_CORRENTE";
    public static final String COLONNA_EMAIL = "EMAIL";
    public static final String COLONNA_CURATORE_PASSWORD = "CURATORE_PASSWORD";

    //VARIABILI INERENTI I LUOGHI
    public static final String TABLE_LUOGHI = "LUOGHI";
    public static final String COLONNA_LUOGHI_ID = "LUOGHI_ID";
    public static final String COLONNA_LUOGHI_NOME = "LUOGHI_NOME";
    public static final String COLONNA_LUOGHI_DESCRIZIONE = "LUOGHI_DESCRIZIONE";
    public static final String COLONNA_LUOGHI_TIPOLOGIA = "LUOGHI_TIPOLOGIA";
    public static final String COLONNA_LUOGHI_EMAIL_CURATORE = "LUOGHI_EMAIL_CURATORE";

    public DataBaseHelper(@Nullable Context context) {
        super(context, "e-cultureTool.db", null, 1);
    }

    public static String getEmailCuratore() {
        return emailCuratore;
    }

    //this is called the first time a database is accessed. There should be code in here to create a new database
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //String che consente di creare la tabella Curatori
        String createTableCuratore= "CREATE TABLE " + TABLE_CURATORI +
                " (" + COLONNA_EMAIL + " TEXT PRIMARY KEY," +
                " " + COLONNA_CURATORE_PASSWORD + " TEXT," +
                " " + COLONNA_CURATORE_NOME + " TEXT," +
                " " + COLONNA_CURATORE_COGNOME + " TEXT," +
                " " + COLONNA_CURATORE_LUOGO_CORRENTE + " INT)";

        String createTableLuogo = "CREATE TABLE " + TABLE_LUOGHI +
                " (" + COLONNA_LUOGHI_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                " " + COLONNA_LUOGHI_NOME + " TEXT," +
                " " + COLONNA_LUOGHI_DESCRIZIONE + " TEXT," +
                " " + COLONNA_LUOGHI_TIPOLOGIA + " TEXT," +
                " " + COLONNA_LUOGHI_EMAIL_CURATORE + " TEXT," +
                " FOREIGN KEY (" + COLONNA_LUOGHI_EMAIL_CURATORE + ") REFERENCES " + TABLE_CURATORI + " ( " + COLONNA_EMAIL + "))";


        sqLiteDatabase.execSQL(createTableCuratore);
        sqLiteDatabase.execSQL(createTableLuogo);
    }

    //this is called if the databse version number changes. It prevents previous users apps from breaking whe you change the database design
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public boolean login(String email, String password){
        boolean risultato = false;

        String stringQuery = "SELECT " + COLONNA_EMAIL + ", " + COLONNA_CURATORE_PASSWORD + " FROM " + TABLE_CURATORI + " WHERE " + COLONNA_EMAIL + " = ? and " + COLONNA_CURATORE_PASSWORD + " = ?";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(stringQuery, new String[] {email, password});

        if(cursor.getCount() == 1){
            risultato = true;
            emailCuratore = email;
        }

        cursor.close();
        db.close();
        return risultato;
    }


    public boolean addCuratore(Curatore curatore){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLONNA_EMAIL, curatore.getEmail());
        contentValues.put(COLONNA_CURATORE_PASSWORD, curatore.getPassword());
        contentValues.put(COLONNA_CURATORE_NOME, curatore.getNome());
        contentValues.put(COLONNA_CURATORE_COGNOME, curatore.getCognome());
        contentValues.put(COLONNA_CURATORE_LUOGO_CORRENTE, curatore.getLuogoCorrente());

        long insert = db.insert(TABLE_CURATORI, null, contentValues);

        if(insert == -1){
            db.close();
            return false;
        }else {
            db.close();
            return true;
        }
    }

    public boolean checkEmailExist(String email){
        boolean risultato = false;

        String stringQuery = "SELECT * FROM " + TABLE_CURATORI + " WHERE " + COLONNA_EMAIL + " = " + "?";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(stringQuery, new String[] {email});

        if(cursor.getCount() > 0){
            risultato = true;
        }

        cursor.close();
        db.close();
        return risultato;
    }


    public boolean addLuogo(Luogo luogo){
        boolean risultato = false;

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLONNA_LUOGHI_NOME, luogo.getNome());
        contentValues.put(COLONNA_LUOGHI_DESCRIZIONE, luogo.getDescrizione());
        contentValues.put(COLONNA_LUOGHI_TIPOLOGIA, luogo.getTipologia().name());
        contentValues.put(COLONNA_LUOGHI_EMAIL_CURATORE, luogo.getEmailCuratore());

        long insert = db.insert(TABLE_LUOGHI, null, contentValues);

        if(insert == -1){
            risultato = false;
        }else {
            risultato = true;
        }

        db.close();
        return risultato;
    }


    /**
     * Metoco che consente di recuperare il luogo corrente solamente durante la fase di registrazione. Questo metodo viene utilizzto solo una volta nel codice, cioè nella fase di registrazione.
     * @param email email del curatore che sta effettuando l'accesso
     * @return luogoCorrente: ritorna l'identificativo univoco del luogo corrente
     */
    public int getFirstLuogoCorrente(String email){
        int luogoCorrente = 0;

        SQLiteDatabase db = this.getReadableDatabase();
        String stringQuery = "SELECT * FROM " + TABLE_LUOGHI + " WHERE " + COLONNA_LUOGHI_EMAIL_CURATORE + " = ?";

        Cursor cursor = db.rawQuery(stringQuery, new String[] {email});

        if(cursor.moveToFirst()){
            luogoCorrente = cursor.getInt(0);
        }

        cursor.close();
        db.close();
        return luogoCorrente;
    }
}
