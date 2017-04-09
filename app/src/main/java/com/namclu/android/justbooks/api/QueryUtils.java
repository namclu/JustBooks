package com.namclu.android.justbooks.api;

import android.text.TextUtils;
import android.util.Log;

import com.namclu.android.justbooks.api.models.Book;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by namlu on 05-Apr-17.
 *
 * Helper methods related to requesting and receiving Book info from Google Books API
 */

public final class QueryUtils {

    /* Constant fields */
    public static final String TAG = QueryUtils.class.getSimpleName();

     /*
     * No one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     * */
    private QueryUtils() {

    }

    /*
     * Query Google Books and return an {@link Book} object to represent a single book.
     * */
    public static List<Book> fetchBookData(String requestUrl) {

        URL url = createUrl(requestUrl);
        String jsonResponse = null;

        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(TAG, "Error obtaining JSON response.", e);
        }

        // Extract relevant fields from the JSON response and create a List of {@link Book}
        return extractBooksFromJson(jsonResponse);
    }

     /*
     * Returns new URL object from the given string URL.
     * */
    private static URL createUrl(String stringUrl) {

        URL url = null;

        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(TAG, "Error creating URL...");
        }
        return url;
    }

    /*
    * Make an HTTP request to the given URL and return a String as the response.
    * */
    private static String makeHttpRequest(URL url) throws IOException {

        String jsonResponse = null;

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputSteam = null;

        /*
        * Create connection to object, setup parameters and general request properties, and
        * make the actual connection to remote object
        * */
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");

            // time in milliseconds
            urlConnection.setReadTimeout(15000);
            urlConnection.connect();

            /*
            * If connection is successful (Response code = 200), read the input stream
             * and parse the response
            * */
            if (urlConnection.getResponseCode() == 200) {
                inputSteam = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputSteam);
            } else {
                Log.e(TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(TAG, "Problem retrieving JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputSteam != null) {
                inputSteam.close();
            }
        }
        return jsonResponse;
    }

    /*
    * Convert the binary data {@link InputStream} into a String which contains the
    * whole JSON response from the server.
    * */
    private static String readFromStream(InputStream inputStream) throws IOException {

        StringBuilder stringBuilder = new StringBuilder();

        if (inputStream != null) {
            // InputStreamReader reads bytes and decodes them into characters using a specified charset
            InputStreamReader inputStreamReader =
                    new InputStreamReader(inputStream, Charset.forName("UTF-8"));

            // Since InputStreamReader only reads a single character at a time, wrapping it in a
            // BufferedReader will buffer the input before converting into characters and returning
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();

            while (line != null) {
                stringBuilder.append(line);
                line = bufferedReader.readLine();
            }
        }
        return stringBuilder.toString();
    }

    /*
    * Return a List of {@link Book} object by parsing out information
    * about the book from the input bookJSON string.
    * */
    private static List<Book> extractBooksFromJson(String bookJson) {

        if (TextUtils.isEmpty(bookJson)) {
            return null;
        }

        List<Book> books = new ArrayList<>();

        try {
            JSONObject bookObject = new JSONObject(bookJson);
            JSONArray itemsArray = bookObject.getJSONArray("items");

            // If there are results in the JSONArray, then continue
            if (itemsArray.length() > 0) {

                // For each book in JSONArray, create a {@link Book} object and add it to
                // the book List.
                for (int i = 0; i < itemsArray.length(); i++) {

                    // Extract each "items" object, and get its "volumeInfo" object, which
                    // contains data needed for an {@link Book} object
                    JSONObject currentBookObject = itemsArray.getJSONObject(i);
                    JSONObject volumeInfoObject = currentBookObject.getJSONObject("volumeInfo");

                    // Extract the title and author from the JSON object
                    String title = volumeInfoObject.getString("title");
                    String author = "";
                    JSONArray authorsArray = volumeInfoObject.getJSONArray("authors");

                    if (authorsArray.length() > 0) {
                        author = authorsArray.getString(0);
                    }
                    String description = volumeInfoObject.getString("description");

                    // Add a {@link Book} object to the list books
                    books.add(new Book(title, author, description));
                }
            }
        } catch (JSONException e) {
            Log.e(TAG, "Problem parsing the book JSON results", e);
        }
        return books;
    }
}
