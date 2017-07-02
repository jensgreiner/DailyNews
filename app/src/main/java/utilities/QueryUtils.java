package utilities;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

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

import models.News;

/**
 * Custom utility class to handle http requests
 * Created by Jens Greiner on 02.07.17.
 */

public class QueryUtils {
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();
    private static final int URL_CONNECTION_READ_TIMEOUT = 10000; // in milliseconds
    private static final int URL_CONNECTION_CONNECT_TIMEOUT = 15000; // in milliseconds

    private QueryUtils() {
        // intentionally left blank - no one should ever make an instance from this class
    }

    public static List<News> fetchNewsData(String requestUrl) {
        Log.d(LOG_TAG, "fetchEarthquakeData is called ...");

        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create an {@link Event} object
        //noinspection UnnecessaryLocalVariable
        List<News> newsList = extractNews(jsonResponse);

        // Return the list of {@link News}
        return newsList;
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        Log.d(LOG_TAG, url.toString());

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(URL_CONNECTION_READ_TIMEOUT);
            urlConnection.setConnectTimeout(URL_CONNECTION_CONNECT_TIMEOUT);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Handle redirect (response code 301)
            // @link https://discussions.udacity.com/t/earthquake-app-error-response-code-301/226098/8
            // See also @link http://www.mkyong.com/java/java-httpurlconnection-follow-redirect-example/
            int httpResponse = urlConnection.getResponseCode();
            if (httpResponse == 301) {
                // Get redirect url from "location" header field
                String newUrl = urlConnection.getHeaderField("Location");

                // Open the new connection again
                urlConnection = (HttpURLConnection) new URL(newUrl).openConnection();

                Log.d(LOG_TAG, "Redirect to URL : " + newUrl);
            }

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the news JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    @NonNull
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    @Nullable
    private static List<News> extractNews(String newsJSON) {

        // Create an empty ArrayList that we can start adding news to
        List<News> newsList = new ArrayList<>();

        // If the string newsJSON is empty or null return early
        if (TextUtils.isEmpty(newsJSON)) {
            Log.d(LOG_TAG, "List of news is empty");
            return null;
        }
        // Try to parse the newsJSON string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            JSONObject root = new JSONObject(newsJSON);
            JSONObject response = root.getJSONObject("response");

            if (response.has("results")) {
                JSONArray results = response.getJSONArray("results");

                for (int i = 0; i < results.length(); i++) {

                    // Get a single earthquake at position i within the list of earthquakes
                    JSONObject news = results.getJSONObject(i);

                    // For a given news item, extract the JSONObject associated with the
                    // key called "webTitle", which represents the readable title of the news item.
                    String title = news.getString("webTitle");

                    // Extract the value for the key called "sectionName"
                    String section = news.getString("sectionName");

                    // Extract the value for the key called "byline" (author)
                    JSONObject fields = news.getJSONObject("fields");
                    String author;
                    if (fields.has("byline")) {
                        author = fields.getString("byline");
                    } else {
                        author = "";
                    }

                    // Extract the value for the key called "date"
                    String date = news.getString("webPublicationDate");

                    // Extract the value for the key called "webUrl"
                    String url = news.getString("webUrl");

                    // Create a new {@link News} object with the title, section, author,
                    // date and url from the JSON response and add it to the list of news
                    newsList.add(new News(title, section, author, date, url));
                }
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the news JSON results", e);
        }

        return newsList;
    }
}
