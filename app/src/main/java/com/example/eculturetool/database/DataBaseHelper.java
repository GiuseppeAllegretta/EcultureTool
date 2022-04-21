package com.example.eculturetool.database;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.eculturetool.entities.Curatore;
import com.example.eculturetool.entities.Luogo;
import com.example.eculturetool.entities.Oggetto;
import com.example.eculturetool.entities.Tipologia;
import com.example.eculturetool.entities.TipologiaOggetto;
import com.example.eculturetool.entities.Zona;

import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {

    //VARIABILI INERENTI AGLI OGGETTI
    public static final String TABLE_OGGETTI = "OGGETTI";
    public static final String COLONNA_OGGETTO_ID = "OGGETTO_ID";
    public static final String COLONNA_OGGETTO_NOME = "OGGETTO_NOME";
    public static final String COLONNA_OGGETTO_DESCRIZIONE = "OGGETTO_DESCRIZIONE";
    public static final String COLONNA_OGGETTO_URL_IMMAGINE = "OGGETTO_URL_IMMAGINE";
    public static final String COLONNA_OGGETTO_URL_QRCODE = "OGGETTO_URL_QRCODE";
    public static final String COLONNA_OGGETTO_TIPOLOGIA = "OGGETTO_TIPOLOGIA";
    public static final String COLONNA_OGGETTO_ZONA_ID = "OGGETTO_ZONA_ID";


    SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "session";
    private static final String SESSION_KEY = "session_user";
    //Email del curatore che si è loggato
    private static String emailCuratore;

    //VARIABILI INERENTI I CURATORI
    public static final String TABLE_CURATORI = "CURATORI";
    public static final String COLONNA_CURATORE_NOME = "CURATORE_NOME";
    public static final String COLONNA_CURATORE_COGNOME = "CURATORE_COGNOME";
    public static final String COLONNA_CURATORE_LUOGO_CORRENTE = "CURATORE_LUOGO_CORRENTE";
    public static final String COLONNA_EMAIL = "EMAIL";
    public static final String COLONNA_CURATORE_PASSWORD = "CURATORE_PASSWORD";
    public static final String COLONNA_CURATORE_IMG = "CURATORE_IMMAGINE";

    //VARIABILI INERENTI I LUOGHI
    public static final String TABLE_LUOGHI = "LUOGHI";
    public static final String COLONNA_LUOGHI_ID = "LUOGHI_ID";
    public static final String COLONNA_LUOGHI_NOME = "LUOGHI_NOME";
    public static final String COLONNA_LUOGHI_DESCRIZIONE = "LUOGHI_DESCRIZIONE";
    public static final String COLONNA_LUOGHI_TIPOLOGIA = "LUOGHI_TIPOLOGIA";
    public static final String COLONNA_LUOGHI_EMAIL_CURATORE = "LUOGHI_EMAIL_CURATORE";

    //VARIABILI INERENTI LE ZONE
    public static final String TABLE_ZONE = "ZONE";
    public static final String COLONNA_ZONE_ID = "ZONE_ID";
    public static final String COLONNA_ZONE_NOME = "ZONE_NOME";
    public static final String COLONNA_ZONE_DESCRIZIONE = "ZONE_DESCRIZIONE";
    public static final String COLONNA_ZONE_NUMERO_OGGETTI = "ZONE_NUMERO_OGGETTI";
    public static final String COLONNA_LUOGO_RIFERIMENTO = "LUOGHI_RIF";

    public DataBaseHelper(@Nullable Context context) {
        super(context, "e-cultureTool.db",null , 3);
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        emailCuratore = sharedPreferences.getString(SESSION_KEY, "null");
    }

    public static String getEmailCuratore() {
        return emailCuratore;
    }

    public void setEmailCuratore(String emailCuratore) {
        DataBaseHelper.emailCuratore = emailCuratore;
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
                " " + COLONNA_CURATORE_LUOGO_CORRENTE + " INT," +
                " " + COLONNA_CURATORE_IMG + " TEXT" + ")";

        String createTableLuogo = "CREATE TABLE " + TABLE_LUOGHI +
                " (" + COLONNA_LUOGHI_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                " " + COLONNA_LUOGHI_NOME + " TEXT," +
                " " + COLONNA_LUOGHI_DESCRIZIONE + " TEXT," +
                " " + COLONNA_LUOGHI_TIPOLOGIA + " TEXT," +
                " " + COLONNA_LUOGHI_EMAIL_CURATORE + " TEXT," +
                " " + "CONSTRAINT fk_curatori " +
                " FOREIGN KEY (" + COLONNA_LUOGHI_EMAIL_CURATORE + ") REFERENCES " + TABLE_CURATORI + " ( " + COLONNA_EMAIL + ")" + " ON DELETE CASCADE " + " )";

        String createTableZona = "CREATE TABLE " + TABLE_ZONE +
                " (" + COLONNA_ZONE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                " " + COLONNA_ZONE_NOME + " TEXT," +
                " " + COLONNA_ZONE_DESCRIZIONE + " TEXT," +
                " " + COLONNA_ZONE_NUMERO_OGGETTI + " INT," +
                " " + COLONNA_LUOGO_RIFERIMENTO + " INT," +
                " " + "CONSTRAINT fk_luoghi " +
                " FOREIGN KEY (" + COLONNA_LUOGO_RIFERIMENTO + ") REFERENCES " + TABLE_LUOGHI + " ( " + COLONNA_LUOGHI_ID + ")" + " ON DELETE CASCADE" + ")";


        String createTableOggetti = "CREATE TABLE " + TABLE_OGGETTI + " " +
                "(" + COLONNA_OGGETTO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLONNA_OGGETTO_NOME + " TEXT," +
                COLONNA_OGGETTO_DESCRIZIONE + " TEXT," +
                COLONNA_OGGETTO_URL_IMMAGINE + " TEXT," +
                COLONNA_OGGETTO_URL_QRCODE + " TEXT, " +
                COLONNA_OGGETTO_TIPOLOGIA + " TEXT," +
                COLONNA_OGGETTO_ZONA_ID + " INT NOT NULL," +
                "CONSTRAINT fk_zone FOREIGN KEY (" + COLONNA_OGGETTO_ZONA_ID + ") REFERENCES " + TABLE_ZONE + " (" + COLONNA_ZONE_ID + ")" + " ON DELETE CASCADE" + ")";

        sqLiteDatabase.execSQL(createTableCuratore);
        sqLiteDatabase.execSQL(createTableLuogo);
        sqLiteDatabase.execSQL(createTableZona);
        sqLiteDatabase.execSQL(createTableOggetti);
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

    public Curatore getCuratore(){
        Curatore curatore = null;
        String email = null;
        String nome = null;
        String cognome = null;
        String password = null;
        String img = null;

        String stringQuery = "SELECT * FROM " + TABLE_CURATORI + " WHERE " + COLONNA_EMAIL + " = ?";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(stringQuery, new String[] {emailCuratore});

        if(cursor.getCount() == 1){
            if(cursor.moveToFirst()){
                email = cursor.getString(cursor.getColumnIndexOrThrow(COLONNA_EMAIL));
                nome = cursor.getString(cursor.getColumnIndexOrThrow(COLONNA_CURATORE_NOME));
                cognome = cursor.getString(cursor.getColumnIndexOrThrow(COLONNA_CURATORE_COGNOME));
                password = cursor.getString(cursor.getColumnIndexOrThrow(COLONNA_CURATORE_PASSWORD));
                img = cursor.getString(cursor.getColumnIndexOrThrow(COLONNA_CURATORE_IMG));
                curatore = new Curatore(nome, cognome, email, password, img);
            }
        }

        cursor.close();
        db.close();
        return curatore;
    }

    public boolean modificaCuratore(String nome, String cognome){
        boolean risultato = false;

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COLONNA_CURATORE_NOME, nome);
        contentValues.put(COLONNA_CURATORE_COGNOME, cognome);
        final int update = db.update(TABLE_CURATORI, contentValues, COLONNA_EMAIL + " = ?", new String[]{emailCuratore});

        if(update == -1){
            risultato = false;
        }else {
            risultato = true;
        }

        db.close();
        return risultato;
    }

    public boolean setImageCuratore(String uri){
        boolean risultato = false;

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLONNA_CURATORE_IMG, uri);

        int update = db.update(TABLE_CURATORI, contentValues, COLONNA_EMAIL + " = ?", new String[]{emailCuratore});

        if(update == -1){
            risultato = false;
        }else {
            risultato = true;
        }

        db.close();
        return risultato;
    }

    public String getImageCuratore(){
        String uri = null;

        SQLiteDatabase db = this.getReadableDatabase();
        String stringQuery = "SELECT * FROM " + TABLE_CURATORI + " WHERE " + COLONNA_EMAIL + " = ?";
        Cursor cursor = db.rawQuery(stringQuery, new String[] {emailCuratore});

        if(cursor.getCount() == 1){
            if(cursor.moveToFirst()){
                uri = cursor.getString(cursor.getColumnIndexOrThrow(COLONNA_CURATORE_IMG));
            }
        }
        cursor.close();
        db.close();

        return uri;
    }

    public Luogo getLuogoCorrente(){
        Luogo luogo = null;

        int id;
        String nome = null;
        String descrizione = null;
        Tipologia tipologia = null;
        String emailCuratore = null;

        int luogoCorrente = getIdLuogoCorrente();

        String stringQuery = "SELECT " + TABLE_LUOGHI + "." + COLONNA_LUOGHI_NOME + "," +
                TABLE_LUOGHI + "." + COLONNA_LUOGHI_DESCRIZIONE + "," +
                TABLE_LUOGHI + "." + COLONNA_LUOGHI_TIPOLOGIA + "," +
                TABLE_LUOGHI + "." + COLONNA_LUOGHI_ID + "," +
                TABLE_LUOGHI + "." + COLONNA_LUOGHI_EMAIL_CURATORE +
                " FROM (" + TABLE_CURATORI + " INNER JOIN " + TABLE_LUOGHI + " ON " + TABLE_CURATORI + "." + COLONNA_EMAIL + " = " + TABLE_LUOGHI + "." + COLONNA_LUOGHI_EMAIL_CURATORE + ") " +
                "WHERE " + COLONNA_LUOGHI_ID + " = " + luogoCorrente;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(stringQuery, null);

        if(cursor.getCount() == 1){
            if(cursor.moveToFirst()){
                id = cursor.getInt(cursor.getColumnIndexOrThrow(COLONNA_LUOGHI_ID));
                nome = cursor.getString(cursor.getColumnIndexOrThrow(COLONNA_LUOGHI_NOME));
                descrizione = cursor.getString(cursor.getColumnIndexOrThrow(COLONNA_LUOGHI_DESCRIZIONE));
                tipologia = Tipologia.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(COLONNA_LUOGHI_TIPOLOGIA)));
                emailCuratore = cursor.getString(cursor.getColumnIndexOrThrow(COLONNA_LUOGHI_EMAIL_CURATORE));

                luogo = new Luogo(nome, descrizione, tipologia, emailCuratore);
                luogo.setId(id);
            }
        }

        cursor.close();
        db.close();
        return luogo;
    }

    public int getIdLuogoCorrente(){
        int luogoCorrente =  0;

        String stringQuery = "SELECT * FROM " + TABLE_CURATORI + " WHERE " + COLONNA_EMAIL + " = ?";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(stringQuery, new String[] {emailCuratore});

        if(cursor.getCount() == 1){
            if(cursor.moveToFirst()){
                luogoCorrente = cursor.getInt(cursor.getColumnIndexOrThrow(COLONNA_CURATORE_LUOGO_CORRENTE));
            }
        }

        cursor.close();
        db.close();
        return luogoCorrente;
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

    public boolean updatePassword(String password){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COLONNA_CURATORE_PASSWORD, password);
        final int update = db.update(TABLE_CURATORI, contentValues, COLONNA_EMAIL + " = ?", new String[]{emailCuratore});

        if(update == -1){
            db.close();
            return false;
        }else {
            db.close();
            return true;
        }
    }


    public List<Luogo> getLuoghi(){
        List<Luogo> list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String stringQuery = "SELECT * FROM " + TABLE_LUOGHI + " WHERE " + COLONNA_LUOGHI_EMAIL_CURATORE + " = ?";
        Cursor cursor = db.rawQuery(stringQuery, new String[]{emailCuratore});

        if(cursor.moveToFirst()){

            do{
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLONNA_LUOGHI_ID));
                String nome = cursor.getString(cursor.getColumnIndexOrThrow(COLONNA_LUOGHI_NOME));
                String descrizione = cursor.getString(cursor.getColumnIndexOrThrow(COLONNA_LUOGHI_DESCRIZIONE));
                Tipologia tipologia = Tipologia.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(COLONNA_LUOGHI_TIPOLOGIA)));
                String emailCuratore = cursor.getString(cursor.getColumnIndexOrThrow(COLONNA_LUOGHI_EMAIL_CURATORE));

                Luogo luogo = new Luogo(nome, descrizione, tipologia, emailCuratore );
                luogo.setId(id);
                list.add(luogo);

            }while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return list;
    }

    public Luogo getLuogoById(int id){
        Luogo luogo = null;

        SQLiteDatabase db = this.getReadableDatabase();
        String stringQuery = "SELECT * FROM " + TABLE_LUOGHI + " WHERE " + COLONNA_LUOGHI_ID + " = " + id;
        Cursor cursor = db.rawQuery(stringQuery, null);

        if(cursor.getCount() == 1){
            if(cursor.moveToFirst()){
                String nome = cursor.getString(cursor.getColumnIndexOrThrow(COLONNA_LUOGHI_NOME));
                String descrizione = cursor.getString(cursor.getColumnIndexOrThrow(COLONNA_LUOGHI_DESCRIZIONE));
                Tipologia tipologia = Tipologia.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(COLONNA_LUOGHI_TIPOLOGIA)));
                String emailCuratore = cursor.getString(cursor.getColumnIndexOrThrow(COLONNA_LUOGHI_EMAIL_CURATORE));

                luogo = new Luogo(nome, descrizione, tipologia, emailCuratore );
                luogo.setId(id);
            }

        }

        cursor.close();
        db.close();
        return luogo;
    }

    public boolean setLuogoCorrente(int id){
        boolean risultato;

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLONNA_CURATORE_LUOGO_CORRENTE, id);

        int update = db.update(TABLE_CURATORI, contentValues, COLONNA_EMAIL + " = ?", new String[]{emailCuratore});

        if(update == -1){
            db.close();
            risultato = false;
        }else {
            db.close();
            risultato = true;
        }

        db.close();
        return risultato;
    }

    public boolean updateLuogo(int id, String nome, String descrizione, Tipologia tipologia){
        boolean risultato = false;

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLONNA_LUOGHI_NOME, nome);
        contentValues.put(COLONNA_LUOGHI_DESCRIZIONE, descrizione);
        contentValues.put(COLONNA_LUOGHI_TIPOLOGIA, tipologia.name());

        int update = db.update(TABLE_LUOGHI, contentValues, COLONNA_LUOGHI_ID + " = " + id, null);

        if(update == -1){
            risultato = false;
        }else {
            risultato = true;
        }

        db.close();
        return risultato;
    }

    public boolean deleteLuogo(int id){
        boolean risultato = false;

        SQLiteDatabase db = this.getWritableDatabase();

        //bisogna impostare a true il metodo per permettere a SQLite di tener conto delle foreign key
        db.setForeignKeyConstraintsEnabled(true);

        int delete = db.delete(TABLE_LUOGHI, COLONNA_LUOGHI_ID + " = " + id, null);

        if(delete == -1){
            risultato = false;
        }else {
            risultato = true;
        }

        db.close();
        return risultato;

    }

    public boolean deleteCuratore(){
        boolean risultato = false;

        SQLiteDatabase db = this.getWritableDatabase();

        //bisogna impostare a true il metodo per permettere a SQLite di tener conto delle foreign key
        db.setForeignKeyConstraintsEnabled(true);

        int delete = db.delete(TABLE_CURATORI, COLONNA_EMAIL + " = ?", new String[] {emailCuratore});

        if(delete == -1){
            risultato = false;
        }else {
            risultato = true;
        }

        db.close();
        return risultato;
    }


    public ArrayList<Zona> getZone(){
        ArrayList<Zona> info = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        //String stringQuery = "SELECT * FROM "+ TABLE_ZONE + " INNER JOIN " + TABLE_LUOGHI + " ON " + TABLE_ZONE + "." + COLONNA_LUOGO_RIFERIMENTO + "=" + TABLE_LUOGHI + "." + COLONNA_LUOGHI_ID;
        String stringQuery = "SELECT * FROM ZONE JOIN CURATORI ON ZONE.LUOGHI_RIF = CURATORI.CURATORE_LUOGO_CORRENTE AND CURATORI.EMAIL = ?";
        Cursor cursor = db.rawQuery(stringQuery, new String[]{emailCuratore});

        if (cursor.moveToFirst()) {

                do {
                    try {
                        int id = cursor.getColumnIndex(COLONNA_ZONE_ID);
                        int nome = cursor.getColumnIndex(COLONNA_ZONE_NOME);
                        int descrizione = cursor.getColumnIndex(COLONNA_ZONE_DESCRIZIONE);
                        int nOggetti = cursor.getColumnIndex(COLONNA_ZONE_NUMERO_OGGETTI);
                        int riferimentoLuogo = cursor.getColumnIndex(COLONNA_LUOGO_RIFERIMENTO);

                        Zona zona = new Zona(cursor.getInt(id), cursor.getString(nome), cursor.getString(descrizione), cursor.getInt(nOggetti), cursor.getInt(riferimentoLuogo));
                        info.add(zona);

                    } catch (Exception e) {
                        System.out.println("errore");
                    }

                } while (cursor.moveToNext());
        }

        /*try {
            int id = cursor.getColumnIndex(COLONNA_ZONE_ID);
            int nome = cursor.getColumnIndex(COLONNA_ZONE_NOME);
            int descrizione= cursor.getColumnIndex(COLONNA_ZONE_DESCRIZIONE);
            int nOggetti= cursor.getColumnIndex(COLONNA_ZONE_NUMERO_OGGETTI);
            int riferimentoLuogo = cursor.getColumnIndex(COLONNA_LUOGO_RIFERIMENTO);

            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                info.add(new Zona(cursor.getInt(id),cursor.getString(nome),cursor.getString(descrizione),cursor.getInt(nOggetti),cursor.getInt(riferimentoLuogo)));
                cursor.moveToNext();
            }
        }catch (Exception e){
            System.out.println("errore database");
        }*/

        cursor.close();
        db.close();

        return info;
    }

    public boolean aggiungiZona(Zona z){
        boolean risultato=false;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLONNA_ZONE_NOME,z.getNome());
        contentValues.put(COLONNA_ZONE_DESCRIZIONE,z.getDescrizione());
        contentValues.put(COLONNA_ZONE_NUMERO_OGGETTI,z.getNumeroOggetti());
        contentValues.put(COLONNA_LUOGO_RIFERIMENTO,z.getRiferimentoLuogo());
        long insert = db.insert(TABLE_ZONE, null, contentValues);

        if(insert == -1){
            risultato = false;
        }else {
            risultato = true;
        }


        db.close();

        return risultato;
    }

    public void rimuoviZona(Zona z){

        SQLiteDatabase db = this.getWritableDatabase();
        db.setForeignKeyConstraintsEnabled(true);

        String stringQuery = "DELETE  FROM ZONE WHERE  ZONE_NOME =?  AND ZONE_DESCRIZIONE = ?";

        db.execSQL(stringQuery,new String[] {z.getNome(),z.getDescrizione()});


        db.close();
    }

    public void modifica(Zona z1,Zona z2){

        SQLiteDatabase db = this.getWritableDatabase();
        String stringQuery = "UPDATE ZONE SET ZONE_NOME = ? , ZONE_DESCRIZIONE = ?, ZONE_NUMERO_OGGETTI = ?  WHERE ZONE_NOME = ?";

        db.execSQL(stringQuery,new String[] {z2.getNome(),z2.getDescrizione(),String.valueOf(z2.getNumeroOggetti()), z1.getNome()});

        db.close();
    }

    public Zona recuperoZonaModificata(int idz){
        Zona zona = null;
        SQLiteDatabase db = this.getReadableDatabase();
        String stringQuery = "SELECT * FROM ZONE WHERE ZONE_ID = "+ idz;
        Cursor cursor = db.rawQuery(stringQuery, null);

        try {
            int id = cursor.getColumnIndex(COLONNA_ZONE_ID);
            int nome = cursor.getColumnIndex(COLONNA_ZONE_NOME);
            int descrizione = cursor.getColumnIndex(COLONNA_ZONE_DESCRIZIONE);
            int nOggetti = cursor.getColumnIndex(COLONNA_ZONE_NUMERO_OGGETTI);
            int riferimentoLuogo = cursor.getColumnIndex(COLONNA_LUOGO_RIFERIMENTO);

            cursor.moveToFirst();
            zona = new Zona(cursor.getInt(id), cursor.getString(nome), cursor.getString(descrizione), cursor.getInt(nOggetti), cursor.getInt(riferimentoLuogo));
        }catch (Exception e){
            System.out.println("errore");
        }

        cursor.close();
        db.close();
        return  zona;
    }


    public List<Oggetto> getOggetti(){
        List<Oggetto> returnList = new ArrayList<>();
        int idLuogoCorrente = getLuogoCorrente().getId();
        SQLiteDatabase db = this.getReadableDatabase();

        String stringQuery = "SELECT * FROM "
                + "(" + "(" + "(" + TABLE_CURATORI + " INNER JOIN " + TABLE_LUOGHI + " ON " + TABLE_CURATORI + "." + COLONNA_EMAIL + " = " + TABLE_LUOGHI + "." + COLONNA_LUOGHI_EMAIL_CURATORE + ") "
                + " INNER JOIN " + TABLE_ZONE + " ON " + TABLE_ZONE + "." + COLONNA_LUOGO_RIFERIMENTO + " = " + TABLE_LUOGHI + "." + COLONNA_LUOGHI_ID + ") "
                + " INNER JOIN " + TABLE_OGGETTI + " ON " + TABLE_OGGETTI + "." + COLONNA_OGGETTO_ZONA_ID + " = " + TABLE_ZONE + "." + COLONNA_ZONE_ID + ") "
                + " WHERE " + COLONNA_EMAIL + " = ? and " + COLONNA_LUOGHI_ID + " = " + idLuogoCorrente;

        System.out.println("getOggetti: " + stringQuery);
        System.out.println("email curatore: " + emailCuratore);

        Cursor cursor = db.rawQuery(stringQuery, new String[] {emailCuratore});

        if(cursor.moveToFirst()){

            do{
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLONNA_OGGETTO_ID));
                String nome = cursor.getString(cursor.getColumnIndexOrThrow(COLONNA_OGGETTO_NOME));
                String descrizione = cursor.getString(cursor.getColumnIndexOrThrow(COLONNA_OGGETTO_DESCRIZIONE));
                TipologiaOggetto tipologia = TipologiaOggetto.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(COLONNA_OGGETTO_TIPOLOGIA)));
                String urlImg = cursor.getString(cursor.getColumnIndexOrThrow(COLONNA_OGGETTO_URL_IMMAGINE));
                String urlQrCode = cursor.getString(cursor.getColumnIndexOrThrow(COLONNA_OGGETTO_URL_QRCODE));
                int idZona = cursor.getInt(cursor.getColumnIndexOrThrow(COLONNA_OGGETTO_ZONA_ID));


                Oggetto oggetto = new Oggetto(id, nome, descrizione, urlImg, tipologia, idZona);
                returnList.add(oggetto);

            }while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return returnList;
    }

    public boolean addOggetto(Oggetto oggetto){
        boolean risultato = false;

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLONNA_OGGETTO_NOME, oggetto.getNome());
        contentValues.put(COLONNA_OGGETTO_DESCRIZIONE, oggetto.getDescrizione());
        contentValues.put(COLONNA_OGGETTO_TIPOLOGIA, oggetto.getTipologiaOggetto().name());
        contentValues.put(COLONNA_OGGETTO_URL_IMMAGINE, oggetto.getUrl());
        contentValues.put(COLONNA_OGGETTO_ZONA_ID, oggetto.getZonaAppartenenza());

        long insert = db.insert(TABLE_OGGETTI, null, contentValues);

        if(insert == -1){
            risultato = false;
        }else {
            risultato = true;
        }

        db.close();
        return risultato;
    }


    public Oggetto getOggettoById(int id){
        Oggetto oggetto = null;

        SQLiteDatabase db = this.getReadableDatabase();

        String stringQuery = "SELECT * FROM " + TABLE_OGGETTI + " WHERE " + COLONNA_OGGETTO_ID + " = " + id;
        Cursor cursor = db.rawQuery(stringQuery, null);

        if(cursor.getCount() == 1){
            if(cursor.moveToFirst()){
                int idOggetto = cursor.getInt(cursor.getColumnIndexOrThrow(COLONNA_OGGETTO_ID));
                String nome = cursor.getString(cursor.getColumnIndexOrThrow(COLONNA_OGGETTO_NOME));
                String descrizione = cursor.getString(cursor.getColumnIndexOrThrow(COLONNA_OGGETTO_DESCRIZIONE));
                TipologiaOggetto tipologia = TipologiaOggetto.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(COLONNA_OGGETTO_TIPOLOGIA)));
                String imgUri = cursor.getString(cursor.getColumnIndexOrThrow(COLONNA_OGGETTO_URL_IMMAGINE));
                int idZona = cursor.getInt(cursor.getColumnIndexOrThrow(COLONNA_OGGETTO_ZONA_ID));

                oggetto = new Oggetto(idOggetto, nome, descrizione, imgUri, tipologia, idZona);
            }

        }

        cursor.close();
        db.close();

        return oggetto;
    }


    public boolean deleteOggetto(int id){
        boolean risultato = false;

        SQLiteDatabase db = this.getWritableDatabase();

        int delete = db.delete(TABLE_OGGETTI, COLONNA_OGGETTO_ID + " = " + id, null);

        if(delete == -1){
            risultato = false;
        }else {
            risultato = true;
        }

        db.close();
        return risultato;
    }

    public boolean updateOggetto(int id, String nome, String descrizione, TipologiaOggetto tipologiaOggetto, int zona){
        boolean risultato = false;

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLONNA_OGGETTO_NOME, nome);
        contentValues.put(COLONNA_OGGETTO_DESCRIZIONE, descrizione);
        contentValues.put(COLONNA_OGGETTO_TIPOLOGIA, tipologiaOggetto.name());
        contentValues.put(COLONNA_OGGETTO_ZONA_ID, zona);

        int update = db.update(TABLE_OGGETTI, contentValues, COLONNA_OGGETTO_ID + " = " + id, null);

        if(update == -1){
            risultato = false;
        }else {
            risultato = true;
        }

        db.close();
        return risultato;
    }
}