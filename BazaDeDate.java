package com.example.autoexpert;


import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.media.Image;
import android.widget.Toast;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class BazaDeDate extends SQLiteOpenHelper{


    private Context mcontext;
    private String dbName;
    private String dbPath;

    public BazaDeDate(Context context, String name, int version){
        super(context, name, null, version);
        this.mcontext = context;
        this.dbName = name;
        this.dbPath = "/data/data/" + mcontext.getPackageName() + "/databases/";

    }
    @Override
    public void onCreate(SQLiteDatabase db){

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

    }
    /*SQLiteDatabase checkDb = null;
        //CopyDatabase();
        String filePath = dbPath + dbName;
        checkDb = SQLiteDatabase.openDatabase(filePath, null, SQLiteDatabase.OPEN_READONLY);
        if(checkDb !=null){
            Toast.makeText(mcontext, "Database Exists", Toast.LENGTH_SHORT).show();
        }
        else{
            try{
                checkDb = SQLiteDatabase.openOrCreateDatabase(filePath, null);
                CopyDatabase();

            }
            catch(Exception e){
                e.printStackTrace();

            }

        }*/
    public void CheckDb() {
        SQLiteDatabase checkDb = null;
        String filePath = dbPath+dbName;
        try{
            checkDb = SQLiteDatabase.openDatabase(filePath, null, 0);

        }
        catch(Exception e){
            e.printStackTrace();
        }
        this.getReadableDatabase();
        CopyDatabase();
    }


    public void CopyDatabase(){
        this.getReadableDatabase();
        AssetManager assetManager = mcontext.getAssets();
        try {
            InputStream ios = assetManager.open("Chestionar_Auto_Official.db");
            OutputStream os = new FileOutputStream(dbPath + dbName);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = ios.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
            os.flush();
            ios.close();
            os.close();

            // Utilizați inputStream pentru a citi fișierul
            // Exemplu: InputStreamReader reader = new InputStreamReader(inputStream);
            //          BufferedReader bufferedReader = new BufferedReader(reader);
            //          String line = bufferedReader.readLine();
            //          ...
             // Închideți stream-ul după utilizare
        } catch (IOException e) {
            e.printStackTrace();
            // Tratați excepția în cazul în care fișierul nu poate fi deschis
        }
        Log.e("CopyDb","Database Copied");
    }
    public void OpenDatabase(){
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbPath + dbName, null, 0);
        Log.d("OpenDb","Database Opened");
    }
    public void getDatabaseInfo() {
        SQLiteDatabase db = this.getReadableDatabase();

        // Obține numele bazei de date
        String dbName = db.getPath();
        Log.e("DATABASE TAG! ", "Numele bazei de date: " + dbName);

        // Obține numele tuturor tabelelor
        Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                Log.d("DATABASE TAG! ", "Numele tabelului: " + cursor.getString(0));
                cursor.moveToNext();
            }
        }
        cursor.close();
    }
    public void afiseazaDate() {
        SQLiteDatabase db = this.getReadableDatabase();

        // Obține numele tuturor tabelelor
        Cursor cursorTables = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        if (cursorTables.moveToFirst()) {
            while (!cursorTables.isAfterLast()) {
                String tableName = cursorTables.getString(0);
                Log.d("DATABASE TAG! ", "Numele tabelului: " + tableName);

                // Obține toate datele din tabel
                Cursor cursorData = db.rawQuery("SELECT * FROM " + tableName, null);
                if (cursorData.moveToFirst()) {
                    while (!cursorData.isAfterLast()) {
                        StringBuilder rowData = new StringBuilder();
                        for (int i = 0; i < cursorData.getColumnCount(); i++) {
                            rowData.append(cursorData.getColumnName(i)).append(": ").append(cursorData.getString(i)).append(", ");
                        }
                        Log.d("DATABASE TAG! ", "Date: " + rowData.toString());
                        cursorData.moveToNext();
                    }
                }
                cursorData.close();
                cursorTables.moveToNext();
            }
        }
        cursorTables.close();
    }


    public void insertCont(String username, String parola, String email, String nr_de_telefon) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("username", username);
            contentValues.put("parola", parola);
            contentValues.put("email", email);
            contentValues.put("nr_de_telefon", nr_de_telefon);
            db.insert("cont", null, contentValues);
            //Toast.makeText(getContext(), "Date introduse cu succes!", Toast.LENGTH_SHORT).show();
        }catch(Exception e){
            e.printStackTrace();
        }

    }
    public String findUsername(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("cont",
                new String[]{"username", "parola"},
                "username = ?",
                new String[]{username},
                null, null, null);

        if (cursor.moveToFirst()) {
            int passwordColumnIndex = cursor.getColumnIndex("parola");
            if (passwordColumnIndex != -1) {
                String storedPassword = cursor.getString(passwordColumnIndex);
                cursor.close();
                if (storedPassword.equals(password)) {
                    return "GASIT";
                } else {
                    return "NOPASSWORD";
                }
            } else {
                cursor.close();
                throw new IllegalArgumentException("Coloana 'parola' nu există în tabelul 'cont'.");
            }
        } else {
            cursor.close();
            return "NEGASIT";
        }

    }

    public Cursor getHighScoresByUsername(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT c.username AS UTILIZATOR, " +
                "h.Timpul_Ramas, " +
                "h.IntrebarileRaspunse AS INTREBARILE_PARCURSE, " +
                "h.IntrebarileGresite AS GRESELI, " +
                "h.IntrebarileCorecte AS CORECTE, " +
                "h.Calificativ " +
                "FROM highscore h " +
                "JOIN cont c ON h.ID_Cont = c.ID " +
                "WHERE c.Username = ?";
        return db.rawQuery(query, new String[]{username});
    }


    // Metoda pentru actualizare
    public void updateCont(String username, String newParola, String newEmail, String newNrDeTelefon) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        if (newParola != null) {
            contentValues.put("parola", newParola);
        }
        if (newEmail != null) {
            contentValues.put("email", newEmail);
        }
        if (newNrDeTelefon != null) {
            contentValues.put("nr_de_telefon", newNrDeTelefon);
        }
        if (contentValues.size() > 0) {
            db.update("cont", contentValues, "username = ?", new String[]{username});
        }
    }

    public ArrayList<Intrebare> getAllIntrebare() {
        ArrayList<Intrebare> intrebareList = new ArrayList<>();

        // Obține o referință către baza de date
        SQLiteDatabase db = this.getReadableDatabase();

        // Interogare pentru a obține toate întrebările
        String query = "SELECT I.ID, I.Intrebare, I.Categoria, R.Raspuns, R.EsteCorect, I.Imagine " +
                "FROM INTREBARI I " +
                "JOIN RASPUNSURI R ON I.ID = R.Intrebare_ID " +
                "ORDER BY I.ID";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null) {
            try {
                int i = 0;
                ArrayList<Raspuns> raspunsuriCurente = new ArrayList<>();

                while (cursor.moveToNext()) {
                    int indexID = cursor.getColumnIndex("ID");
                    int indexIntrebare = cursor.getColumnIndex("Intrebare");
                    int indexRaspuns = cursor.getColumnIndex("Raspuns");
                    int indexCategoria = cursor.getColumnIndex("Categoria");
                    int indexEsteCorect = cursor.getColumnIndex("EsteCorect");
                    int indexImagine = cursor.getColumnIndex("Imagine");

                    if (indexID == -1 || indexIntrebare == -1 || indexRaspuns == -1 || indexCategoria == -1 || indexEsteCorect == -1) {
                        Log.e("DATABASE TAG!", "Coloana nu a fost găsită în tabelul bazei de date.");
                        continue;
                    }

                    int id = cursor.getInt(indexID);
                    String intrebareText = cursor.getString(indexIntrebare);
                    String raspunsText = cursor.getString(indexRaspuns);
                    boolean esteCorect = cursor.getInt(indexEsteCorect) == 1;
                    String categorie = cursor.getString(indexCategoria);

                    byte[] imageBytes = null;
                    Bitmap imagine = null;
                    if (indexImagine != -1 && !cursor.isNull(indexImagine)) {
                        imageBytes = cursor.getBlob(indexImagine);
                        if (imageBytes != null) {
                            imagine = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                        }
                    }

                    Raspuns raspuns = new Raspuns(raspunsText, esteCorect);
                    raspunsuriCurente.add(raspuns);
                    i++;

                    if (i % 3 == 0) {
                        Intrebare intrebare = new Intrebare(intrebareText, raspunsuriCurente.get(0), raspunsuriCurente.get(1), raspunsuriCurente.get(2), categorie, id, imagine);
                        intrebareList.add(intrebare);
                        raspunsuriCurente.clear();
                        i = 0;
                    }
                }
            } finally {
                cursor.close();
            }
        }

        Log.d("DATABASE TAG!", "Total questions retrieved: " + intrebareList.size());
        return intrebareList;
    }






    // Metoda pentru a sterge tabela
    public void dropTable(String tableName) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + tableName);
    }
    // Metode pentru tabela highscore
    public void insertHighscore(String username, String timpulRamas, int intrebarileRaspunse, int intrebarileGresite, int intrebarileCorecte, String calificativ) {
        int ID_Cont = GainUsername(username);
        if (ID_Cont != -1) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("ID_Cont", ID_Cont);
            contentValues.put("Timpul_Ramas", timpulRamas);
            contentValues.put("IntrebarileRaspunse", intrebarileRaspunse);
            contentValues.put("IntrebarileGresite", intrebarileGresite);
            contentValues.put("IntrebarileCorecte", intrebarileCorecte);
            contentValues.put("Calificativ", calificativ);

            // Executăm interogarea de inserare utilizând metoda insert()
            long result = db.insert("highscore", null, contentValues);

            // Verificăm rezultatul inserării
            if (result == -1) {
                // Inserarea a eșuat
                Log.e("MyApp", "Inserare highscore eșuată");
            } else {
                // Inserarea a avut succes
                Log.d("MyApp", "Highscore inserat cu succes");
            }
        } else {
            // Numele de utilizator invalid, inserarea nu poate fi efectuată
            Log.e("MyApp", "Nume de utilizator invalid");
        }
    }

    public int GainUsername(String Username){
        SQLiteDatabase db = this.getReadableDatabase();
        int ID = -1;

        // Definim interogarea SQL pentru a obține ID-ul bazat pe numele de utilizator
        String query = "SELECT ID FROM cont WHERE username = ?";

        // Executăm interogarea utilizând metoda rawQuery()
        Cursor cursor = db.rawQuery(query, new String[]{Username});

        // Verificăm dacă s-a găsit un rând în rezultatele interogării
        if (cursor.moveToFirst()) {
            // Extragem ID-ul din primul rând
            int IndexID = cursor.getColumnIndex("ID");
            if(IndexID!=-1){
                return IndexID;
            }
            else{
                return -1;
            }
        }
        else{
            cursor.close();

            return -1;
        }

        // Închidem cursorul pentru a elibera resursele

    }
    public SQLiteDatabase getDatabase() {
        // Deschide baza de date
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbPath + dbName, null, 0);
        return db;
    }
    public int getNumberOfRowsInTable(String tableName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + tableName, null);
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }

    public void afiseazaDimensiuneaTabeluluiIntrebari() {
        int numberOfRows = getNumberOfRowsInTable("intrebari");
        Log.d("DATABASE TAG!", "Dimensiunea tabelului INTREBARI: " + numberOfRows);
    }
    public void executaSiAfiseazaQuery() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT I.ID, I.Intrebare, I.Categoria, R.Raspuns, R.EsteCorect " +
                "FROM INTREBARI I " +
                "JOIN RASPUNSURI R ON I.ID = R.Intrebare_ID " +
                "ORDER BY I.ID";

        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    int indexID = cursor.getColumnIndex("ID");
                    int indexIntrebare = cursor.getColumnIndex("Intrebare");
                    int indexRaspuns = cursor.getColumnIndex("Raspuns");
                    int indexCategoria = cursor.getColumnIndex("Categoria");
                    int indexEsteCorect = cursor.getColumnIndex("EsteCorect");
                    if(indexID!=-1 && indexIntrebare!=-1 && indexRaspuns!=-1 && indexCategoria!=-1 && indexEsteCorect!=-1){
                        int id = cursor.getInt(indexID);
                        String intrebare = cursor.getString(indexIntrebare);
                        String categoria = cursor.getString(indexCategoria);
                        String raspuns = cursor.getString(indexRaspuns);
                        boolean esteCorect = cursor.getInt(indexEsteCorect) == 1;
                        Log.e("QueryResult", "ID: " + id + ", Intrebare: " + intrebare + ", Categoria: " + categoria + ", Raspuns: " + raspuns + ", EsteCorect: " + esteCorect);
                    }
                    else{
                        continue;
                    }




                } while (cursor.moveToNext());
            }
            cursor.close();
        }
    }

}
