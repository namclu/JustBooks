package com.namclu.android.justbooks.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.namclu.android.justbooks.R;

public class BookActivity extends AppCompatActivity implements View.OnClickListener{

    private SearchView searchField;
    private Button searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        searchField = (SearchView) findViewById(R.id.search_main_search_field);
        searchButton = (Button) findViewById(R.id.button_main_search);

        // Set views
        searchField.setQueryHint(getResources().getString(R.string.search_hint));

        // Set onClickListener
        searchButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Toast.makeText(this, "Searching for " + searchField.getQuery(), Toast.LENGTH_SHORT).show();

        Intent intent  = new Intent(this, SearchResultsActivity.class);
        startActivity(intent);
    }
}
