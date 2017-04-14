package com.namclu.android.justbooks.api.models;

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
    private String mDescription;

    /* Create new Book object with a title, author, description */
    public Book (String title, String author, String description) {
        setTitle(title);
        setAuthor(author);
        setDescription(description);
    }

    /* Getter and setter methods */
    public String getTitle() {
        return mTitle;
    }

    private void setTitle(String title) {
        mTitle = title;
    }

    public String getAuthor() {
        return mAuthor;
    }

    private void setAuthor(String author) {
        mAuthor = author;
    }

    public String getDescription() {
        return mDescription;
    }

    private void setDescription(String description) {
        mDescription = description;
    }
}
