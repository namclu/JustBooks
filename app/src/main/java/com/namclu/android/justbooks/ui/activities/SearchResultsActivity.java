package com.namclu.android.justbooks.ui.activities;

import android.app.LoaderManager;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.namclu.android.justbooks.R;
import com.namclu.android.justbooks.api.BookLoader;
import com.namclu.android.justbooks.api.models.Book;
import com.namclu.android.justbooks.ui.adapters.BookItemsAdapter;

import java.util.ArrayList;
import java.util.List;

public class SearchResultsActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<Book>> {

    /* Constant fields */
    private static final String TAG = SearchResultsActivity.class.getName();
    private static final String URL =
            "https://www.googleapis.com/books/v1/volumes?q=android&maxResults=15";

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
        /*mBooks.add(new Book("Moby Dick", "Herman Melville"));
        mBooks.add(new Book("A Tale of Two Cities", "Charles Dickens"));
        mBooks.add(new Book("Les Miserable", "Victor Hugo"));*/

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

        // Try to load data
        try {
            // Get a reference to the ConnectivityManager to check state of network connectivity
            ConnectivityManager connectivityManager =
                    (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

            // Get details on the currently active default data network
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

            // If there is a network connection, fetch data
            if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
                // Initialize the loader. Pass in the int ID constant defined above and pass in null for
                // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
                // because this activity implements the LoaderCallbacks interface).
                getLoaderManager().initLoader(1, null, this).forceLoad();
            }
        } catch (Exception e){
            Log.e(TAG, "Error w internet connection");
        }
    }

    // Method to search for books
    void doSearch(String query) {

    }

    /* Methods for LoaderManager.LoaderCallbacks */

    /*
    * called when the system needs a new loader to be created. Your code should create a Loader
    * object and return it to the system.
    * */
    @Override
    public Loader<List<Book>> onCreateLoader(int i, Bundle bundle) {
        return new BookLoader(this, URL);
    }

    /*
    * called when a loader has finished loading data. Typically, your code should display
    * the data to the user.
    * */
    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> books) {

        mBookItemsAdapter.clear();

        // If there is a valid List of {@link Book}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (books != null && !books.isEmpty()) {
            mBooks.addAll(books);
            mBookItemsAdapter.notifyDataSetChanged();
            Toast.makeText(this, "Books added!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        // Loader reset, so we can clear out our existing data.
        mBookItemsAdapter.clear();
    }
}
