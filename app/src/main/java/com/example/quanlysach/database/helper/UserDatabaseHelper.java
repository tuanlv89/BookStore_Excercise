package com.example.quanlysach.database.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import com.example.quanlysach.database.model.Book;
import com.example.quanlysach.database.model.User;
import com.example.quanlysach.view.Login;


public class UserDatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "books_db";


    public UserDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        // create books table
        db.execSQL(User.CREATE_TABLE);
        db.execSQL(Book.CREATE_TABLE);
        Log.d("AAA", User.CREATE_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + User.TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    public int register(User user) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(User.COLUMN_USERNAME, user.getUsername());
        values.put(User.COLUMN_PASSWORD, user.getPassword());

        // insert row
        long id = db.insert(User.TABLE_NAME, null, values);
        Log.d("AAA", "Insert OK");
        // close db connection
        db.close();

        // return newly inserted row id
        return (int)id;
    }

    public User getUser(String username, String pass) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();
        Log.d("AAA", User.COLUMN_USERNAME + " = '"+ username+"' AND " + User.COLUMN_PASSWORD + " = '"+pass+"';");
        Cursor cursor = db.query(User.TABLE_NAME,
                new String[]{User.COLUMN_ID, User.COLUMN_USERNAME, User.COLUMN_PASSWORD},
                User.COLUMN_USERNAME + " = '"+ username+"' AND " + User.COLUMN_PASSWORD + " = '"+pass+"';",
                null, null, null, null, null);
        Log.d("AAA", cursor.toString());
        if (cursor != null && cursor.moveToFirst()) {
            User user = new User(
                    cursor.getInt(cursor.getColumnIndex(User.COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndex(User.COLUMN_USERNAME)),
                    cursor.getString(cursor.getColumnIndex(User.COLUMN_PASSWORD)));

            Log.d("AAA", "Loi ne" + user.toString());
            // close the db connection
            cursor.close();
            return user;
        }
        cursor.close();
        return null;
    }

    public List<User> getAllUser() {
        List<User> books = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT * FROM " + User.TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor!=null && cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setId(cursor.getInt(cursor.getColumnIndex(User.COLUMN_ID)));
                user.setUsername(cursor.getString(cursor.getColumnIndex(User.COLUMN_USERNAME)));
                user.setPassword(cursor.getString(cursor.getColumnIndex(User.COLUMN_PASSWORD)));
                books.add(user);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return books list
        return books;
    }

    
}
