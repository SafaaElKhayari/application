package com.example.androidMapApp;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class LocalDbManager extends SQLiteOpenHelper {

    private static final String DB_NAME = "mylocaldatabase";
    private static final int DB_VERSION = 1;

    public LocalDbManager(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //creation de la base de données ( tab(_id, name) )
        db.execSQL("CREATE TABLE locations (_id INTEGER PRIMARY KEY AUTOINCREMENT, sitename TEXT, description TEXT," +
                " latitude FLOAT, longitude FLOAT, date TEXT, number_votes INTEGER, sum_votes INTEGER )" );

        db.delete("locations",null,null);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    
    public void insertOneLocation(Localisation location) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues c = new ContentValues();

        c.put("username", location.sitename);
        c.put("description", location.description);
        c.put("date", location.date);
        c.put("latitude", location.latitude);
        c.put("longitude", location.longitude);
        c.put("number_votes", location.numberVotes);
        c.put("sum_votes", location.sumVotes);
        //inser des éléments à la base de données
        db.insert("locations", null, c);
        
        db.close();
    }



}