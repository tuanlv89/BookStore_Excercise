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
import com.example.quanlysach.view.Login;


public class BookDatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "books_db";


    public BookDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        // create books table
        db.execSQL(Book.CREATE_TABLE);
        Log.d("AAA", Book.CREATE_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + Book.TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    public int addBook(Book book) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Book.COLUMN_ID_USER, book.getIdUser());
        values.put(Book.COLUMN_TITLE, book.getTitle());
        values.put(Book.COLUMN_AUTHOR, book.getAuthor());
        values.put(Book.COLUMN_CATEGORY, book.getCategory());

        // insert row
        long id = db.insert(Book.TABLE_NAME, null, values);
        Log.d("AAA", "Insert OK");
        // close db connection
        db.close();

        // return newly inserted row id
        return (int)id;
    }

    public Book getBook(int id) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Book.TABLE_NAME,
                new String[]{Book.COLUMN_ID, Book.COLUMN_ID_USER,Book.COLUMN_TITLE, Book.COLUMN_AUTHOR, Book.COLUMN_CATEGORY},
                Book.COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        Log.d("AAA", cursor.toString());
        if (cursor != null && cursor.moveToFirst()) {
            // prepare book object
            Book book = new Book(
                    cursor.getInt(cursor.getColumnIndex(Book.COLUMN_ID)),
                    cursor.getInt(cursor.getColumnIndex(Book.COLUMN_ID_USER)),
                    cursor.getString(cursor.getColumnIndex(Book.COLUMN_TITLE)),
                    cursor.getString(cursor.getColumnIndex(Book.COLUMN_AUTHOR)),
                    cursor.getString(cursor.getColumnIndex(Book.COLUMN_CATEGORY)));
            // close the db connection
            cursor.close();
            return book;

        }

        // close the db connection
        cursor.close();

        return null;
    }

    public List<Book> getAllBooks(int idUser) {
        List<Book> books = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + Book.TABLE_NAME + " WHERE " + Book.COLUMN_ID_USER + " = " + idUser + ";";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Book book = new Book();
                book.setId(cursor.getInt(cursor.getColumnIndex(Book.COLUMN_ID)));
                book.setId(cursor.getInt(cursor.getColumnIndex(Book.COLUMN_ID_USER)));
                book.setTitle(cursor.getString(cursor.getColumnIndex(Book.COLUMN_TITLE)));
                book.setAuthor(cursor.getString(cursor.getColumnIndex(Book.COLUMN_AUTHOR)));
                book.setCategory(cursor.getString(cursor.getColumnIndex(Book.COLUMN_CATEGORY)));
                books.add(book);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return books list
        return books;
    }

    public int getBooksCount() {
        String countQuery = "SELECT  * FROM " + Book.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();


        // return count
        return count;
    }

    public List<Book> getAllBooksByCondition(int idUser, String condition, String search) {
        List<Book> books = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + Book.TABLE_NAME + " WHERE " + Book.COLUMN_ID_USER + " = " + idUser + " AND " + condition + " LIKE '%"+ search +"%'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Book book = new Book();
                book.setId(cursor.getInt(cursor.getColumnIndex(Book.COLUMN_ID)));
                book.setId(cursor.getInt(cursor.getColumnIndex(Book.COLUMN_ID_USER)));
                book.setTitle(cursor.getString(cursor.getColumnIndex(Book.COLUMN_TITLE)));
                book.setAuthor(cursor.getString(cursor.getColumnIndex(Book.COLUMN_AUTHOR)));
                book.setCategory(cursor.getString(cursor.getColumnIndex(Book.COLUMN_CATEGORY)));
                books.add(book);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return books list
        return books;
    }

    public int updateBook(Book book) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Book.COLUMN_TITLE, book.getTitle());
        values.put(Book.COLUMN_AUTHOR, book.getAuthor());
        values.put(Book.COLUMN_CATEGORY, book.getCategory());

        // updating row
        return db.update(Book.TABLE_NAME, values, Book.COLUMN_ID + " = ?",
                new String[]{String.valueOf(book.getId())});
    }

    public void deleteBook(Book book) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Book.TABLE_NAME, Book.COLUMN_ID + " = ?",
                new String[]{String.valueOf(book.getId())});
        db.close();
    }
}
