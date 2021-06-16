package com.mahfuznow.assignment.controller;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mahfuznow.assignment.model.Person;
import com.mahfuznow.assignment.utils.AppConstants;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    public DatabaseHandler(Context context) {
        super(context, AppConstants.DATABASE_NAME, null, AppConstants.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_LOCATION_TABLE = "CREATE TABLE " + AppConstants.TABLE_NAME + " (" +
                AppConstants.KEY_ID + " INTEGER PRIMARY KEY,"
                + AppConstants.KEY_Latitude + " TEXT,"
                + AppConstants.KEY_Longitude + " TEXT,"
                + AppConstants.KEY_Altitude + " TEXT,"
                + AppConstants.KEY_Address + " TEXT)";
        db.execSQL(CREATE_LOCATION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + AppConstants.TABLE_NAME);
        onCreate(db);
    }

    public void addPerson(Person person) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(AppConstants.KEY_Latitude, person.getLatitude());
        contentValues.put(AppConstants.KEY_Longitude, person.getLongitude());
        contentValues.put(AppConstants.KEY_Altitude, person.getAltitude());
        contentValues.put(AppConstants.KEY_Address, person.getAddress());

        database.insert(AppConstants.TABLE_NAME, null, contentValues);
        database.close();
    }

    public Person getPerson(int id) {
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.query(AppConstants.TABLE_NAME,
                new String[]{
                        AppConstants.KEY_ID,
                        AppConstants.KEY_Latitude,
                        AppConstants.KEY_Longitude,
                        AppConstants.KEY_Altitude,
                        AppConstants.KEY_Address
                },
                AppConstants.KEY_ID + "=?", new String[]{String.valueOf(id)},
                null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();
        Person person = new Person(
                Integer.parseInt(cursor.getString(0)),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4)
        );
        return person;

    }

    public List<Person> getPeople() {
        SQLiteDatabase database = this.getReadableDatabase();
        List<Person> personList = new ArrayList<>();

        String getAll = "SELECT * FROM " + AppConstants.TABLE_NAME;

        Cursor cursor = database.rawQuery(getAll, null);

        if (cursor.moveToFirst())

            do {
                Person person = new Person();
                person.setId(Integer.parseInt(cursor.getString(0)));
                person.setLatitude(cursor.getString(1));
                person.setLongitude(cursor.getString(2));
                person.setAltitude(cursor.getString(3));
                person.setAddress(cursor.getString(4));
                personList.add(person);

            } while (cursor.moveToNext());

        return personList;
    }


    public int getNumPerson() {
        String getAll = "SELECT * FROM " + AppConstants.TABLE_NAME;
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(getAll, null);
        // cursor.close();
        return cursor.getCount();
    }

}
