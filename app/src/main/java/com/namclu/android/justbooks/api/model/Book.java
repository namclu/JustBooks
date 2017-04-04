package com.namclu.android.justbooks.api.model;

/**
 * Created by namlu on 04-Apr-17.
 */

public class Book {

    /*
     * @param mTitle    the title of the book
     * @param mAuthor   the author of the book
     *
     * */
    private String mTitle;
    private String mAuthor;

    /* Create new Book object with a title and author */
    public Book (String title, String author) {
        setTitle(title);
        setAuthor(author);
    }

    /* Getter and setter methods */
    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public void setAuthor(String author) {
        mAuthor = author;
    }
}
