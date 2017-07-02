package loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import java.util.List;

import models.News;
import utilities.QueryUtils;

/**
 * Custom AsyncTaskLoader to load news from API in background
 * Created by Jens Greiner on 02.07.17.
 */

public class NewsLoader extends AsyncTaskLoader<List<News>> {
    private static final String LOG_TAG = NewsLoader.class.getName();

    private final String mUrl;

    public NewsLoader(Context context, String url) {
        super(context);
        this.mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        Log.d(LOG_TAG, "onStartLoading is called ...");

        forceLoad();
    }

    @Override
    public List<News> loadInBackground() {
        Log.d(LOG_TAG, "loadInBackground is called ...");

        if (TextUtils.isEmpty(mUrl)) {
            return null;
        }
        // Perform the HTTP request for news data and process the response.
        //noinspection UnnecessaryLocalVariable
        List<News> newsList = QueryUtils.fetchNewsData(mUrl);
        return newsList;
    }
}
