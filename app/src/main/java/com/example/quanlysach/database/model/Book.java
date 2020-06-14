package com.example.quanlysach.database.model;

public class Book {
    public static final String TABLE_NAME = "books";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_ID_USER = "idUser";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_AUTHOR = "author";
    public static final String COLUMN_CATEGORY = "category";

    private int id;
    private int idUser;
    private String title;
    private String author;
    private String category;


    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "( "
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COLUMN_ID_USER + " INTEGER, "
                    + COLUMN_TITLE + " TEXT, "
                    + COLUMN_AUTHOR + " TEXT, "
                    + COLUMN_CATEGORY + " TEXT"
                    + ")";

    public Book() {
    }

    public Book(int id, int idUser, String title, String author, String category) {
        this.id = id;
        this.idUser = idUser;
        this.title = title;
        this.author = author;
        this.category = category;

    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", idUser=" + idUser +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", category='" + category + '\'' +
                '}';
    }
}
