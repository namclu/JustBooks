package com.namclu.android.justbooks.ui.activities;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.namclu.android.justbooks.R;
import com.namclu.android.justbooks.api.BookLoader;
import com.namclu.android.justbooks.api.models.Book;
import com.namclu.android.justbooks.ui.adapters.BookItemsAdapter;

import java.util.ArrayList;
import java.util.List;

public class BookResultsActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<Book>> {

    /* Constant fields */
    private static final String TAG = BookResultsActivity.class.getName();
    private static final String URL =
            "https://www.googleapis.com/books/v1/volumes?q=";

    /* Private fields */
    private List<Book> mBooks;
    private BookItemsAdapter mBookItemsAdapter;
    private RecyclerView mRecyclerView;
    private String mSearchString;
    private TextView mEmptyStateTextView;
    private ProgressBar mProgressBar;

    /*public static Intent getSearchIntent(Context context, String searchText) {
        Intent intent = new Intent(context, BookActivity.class);
        intent.putExtra("EXTRA_SEARCH_TEXT", searchText);

        return intent;
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        // Initialise List<Book>
        mBooks = new ArrayList<>();

        // Initialize fields
        mBookItemsAdapter = new BookItemsAdapter(mBooks);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_search_results);
        mEmptyStateTextView = (TextView) findViewById(R.id.text_empty_view);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar_spinner);

        // RecyclerView stuff
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mBookItemsAdapter);

        // Get the intent, verify the action and get the query
        mSearchString = getIntent().getStringExtra("EXTRA_SEARCH_TEXT");

        /*Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            doSearch(query);
        }*/

        // Check for network connectivity before attempting to load data
        try {
            ConnectivityManager connectivityManager =
                    (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

            if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
                getLoaderManager().initLoader(1, null, this).forceLoad();
            } else {
                mEmptyStateTextView.setText(getString(R.string.error_message_network));
                mProgressBar.setVisibility(View.GONE);
            }
        } catch (Exception e){
            Log.e(TAG, "Error w internet connection");
        }
    }

    /* Methods for LoaderManager.LoaderCallbacks */

    /*
    * called when the system needs a new loader to be created. Your code should create a Loader
    * object and return it to the system.
    * */
    @Override
    public Loader<List<Book>> onCreateLoader(int i, Bundle bundle) {
        StringBuilder sb = new StringBuilder(URL);
        sb.append(formatSearchText(mSearchString));

        return new BookLoader(this, sb.toString());
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
        } else if (books != null && books.isEmpty()) {
            mEmptyStateTextView.setText("No books found! \nPlease search again...");
        }

        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        // Loader reset, so we can clear out our existing data.
        mBookItemsAdapter.clear();
    }

    // Method to format searchString by replacing whitespaces with "+"
    private String formatSearchText(String inputString) {

        StringBuilder stringBuilder = new StringBuilder();
        String[] result = inputString.split("\\s");

        for (int i = 0; i < result.length; i++) {
            stringBuilder.append(result[i]).append("+");
        }
        // Delete the last "+"
        stringBuilder.deleteCharAt(stringBuilder.lastIndexOf("+"));

        return stringBuilder.toString();
    }
}
