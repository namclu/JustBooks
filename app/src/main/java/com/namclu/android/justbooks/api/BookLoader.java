package com.namclu.android.justbooks.api;


import android.content.AsyncTaskLoader;
import android.content.Context;

import com.namclu.android.justbooks.api.models.Book;

import java.util.List;

/**
 * Created by namlu on 05-Apr-17.
 *
 * Loads a list of books by using an AsyncTask to perform the
 * network request to the given URL.
 */

public class BookLoader extends AsyncTaskLoader<List<Book>> {

    private final String mUrl;

    public BookLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    public List<Book> loadInBackground() {

        // Submit task only if URL entry is not null
        if (mUrl == null) {
            return null;
        }
        return QueryUtils.fetchBookData(mUrl);
    }
}
