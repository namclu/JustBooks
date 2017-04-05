package com.namclu.android.justbooks.ui.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.namclu.android.justbooks.R;
import com.namclu.android.justbooks.api.models.Book;
import com.namclu.android.justbooks.ui.adapters.BookItemsAdapter;

import java.util.ArrayList;
import java.util.List;

public class SearchResultsActivity extends AppCompatActivity {

    /* Constant fields */
    private static final String TAG = SearchResultsActivity.class.getName();

    /* Private fields */
    private List<Book> mBooks;
    private BookItemsAdapter mBookItemsAdapter;
    private RecyclerView mRecyclerView;

    public static Intent getSearchIntent(Context context, String searchText) {
        Intent intent = new Intent(context, BookActivity.class);
        intent.putExtra("EXTRA_SEARCH_TEXT", searchText);

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        // Create a fake list of Book objects
        mBooks = new ArrayList<>();
        mBooks.add(new Book("Moby Dick", "Herman Melville"));
        mBooks.add(new Book("A Tale of Two Cities", "Charles Dickens"));
        mBooks.add(new Book("Les Miserable", "Victor Hugo"));

        // Initialize fields
        mBookItemsAdapter = new BookItemsAdapter(mBooks);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_search_results);

        // RecyclerView stuff
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mBookItemsAdapter);

        // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            doSearch(query);
        }
    }

    // Method to search for books
    void doSearch(String query) {

    }
}
