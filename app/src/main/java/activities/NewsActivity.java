package activities;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.greiner_co.dailynews.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import adapters.NewsAdapter;
import loaders.NewsLoader;
import models.News;

public class NewsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>> {
    private static final String LOG_TAG = NewsActivity.class.getName();
    private static final int NEWS_LOADER_ID = 1;
    private static final String NEWS_API_URL = "http://content.guardianapis.com/search";
    private static final String NEWS_API_KEY = "test";

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView.Adapter mAdapter;
    private TextView mEmptyTextView;
    private ProgressBar mLoadingSpinner;

    private ArrayList<News> mNewsArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.news_recycler_view);
        mEmptyTextView = (TextView) findViewById(R.id.empty_text_view);
        mLoadingSpinner = (ProgressBar) findViewById(R.id.loading_spinner);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        //noinspection Convert2Diamond
        mNewsArrayList = new ArrayList<News>();
        mAdapter = new NewsAdapter(mNewsArrayList);
        mRecyclerView.setAdapter(mAdapter);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (internetIsConnected()) {
                    getLoaderManager().restartLoader(0, null, NewsActivity.this);
                } else {
                    mSwipeRefreshLayout.setRefreshing(false);
                    if (mAdapter instanceof NewsAdapter) {
                        ((NewsAdapter) mAdapter).swapData(new ArrayList<News>());
                    }
                    mEmptyTextView.setText(R.string.no_internet_connection_text);
                    mEmptyTextView.setVisibility(View.VISIBLE);
                }
            }
        });

        if (internetIsConnected()) {

            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            Log.d(LOG_TAG, "initLoader is called ...");
            loaderManager.initLoader(NEWS_LOADER_ID, null, this);
        } else {
            // Hide the progressBar spinner after loading
            mLoadingSpinner.setVisibility(View.GONE);

            // Set empty state text to display
            mEmptyTextView.setText(getString(R.string.no_internet_connection_text));
        }
    }

    @Override
    public Loader<List<News>> onCreateLoader(int id, Bundle args) {
        Log.d(LOG_TAG, "onCreateLoader is called ...");

        Date today = new Date();
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(today);

        Uri baseUri = Uri.parse(NEWS_API_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter("show-fields", "byline");
        uriBuilder.appendQueryParameter("order-by", "newest");
        uriBuilder.appendQueryParameter("use-date", "published");
        uriBuilder.appendQueryParameter("from-date", date);
        uriBuilder.appendQueryParameter("api-key", NEWS_API_KEY);

        return new NewsLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> newsList) {
        Log.d(LOG_TAG, "onLoadFinished is called ...");

        // Hide the progressBar spinner after loading
        mLoadingSpinner.setVisibility(View.GONE);

        mSwipeRefreshLayout.setRefreshing(false);

        mEmptyTextView.setText(getString(R.string.empty_list_text));
        if (newsList != null && !newsList.isEmpty()) {
            mEmptyTextView.setVisibility(View.GONE);
        } else {
            mEmptyTextView.setVisibility(View.VISIBLE);
        }
        // Casting found on @link https://stackoverflow.com/a/34380750/1469260
        // swapData() is part of the custom instance of RecyclerView.Adapter and has to be casted
        if (mAdapter instanceof NewsAdapter) {
            ((NewsAdapter) mAdapter).swapData(newsList);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        Log.d(LOG_TAG, "onLoaderReset is called ...");

        mNewsArrayList.clear();
        if (mAdapter instanceof NewsAdapter) {
            ((NewsAdapter) mAdapter).swapData(mNewsArrayList);
        }
    }

    private boolean internetIsConnected() {
        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}
