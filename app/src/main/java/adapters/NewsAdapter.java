package adapters;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.greiner_co.dailynews.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import models.News;

/**
 * Custom Adapter for RecyclerView to provide news data to the list
 * Created by Jens Greiner on 02.07.17.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
    private static final String LOG_TAG = NewsAdapter.class.getName();

    private ArrayList<News> mNewsArrayList;

    public NewsAdapter(ArrayList<News> newsData) {
        this.mNewsArrayList = newsData;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.news_row_layout, parent, false);

        return new NewsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final News currentNews = mNewsArrayList.get(position);
        holder.newsTitle.setText(currentNews.getNewsTitle());
        holder.newsSection.setText(currentNews.getNewsSection());
        holder.newsAuthor.setText(currentNews.getNewsAuthor());
        holder.newsDate.setText(formatDate(currentNews.getNewsDate()));

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri newsUri = Uri.parse(currentNews.getmNewsUrl());

                // Create a new intent to view the earthquake URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, newsUri);

                // Send the intent to launch a new activity
                v.getContext().startActivity(websiteIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mNewsArrayList == null) {
            return 0;
        }
        return mNewsArrayList.size();
    }

    public void swapData(List<News> newNewsList) {
        if (mNewsArrayList != null && !mNewsArrayList.isEmpty()) {
            mNewsArrayList.clear();
            if (newNewsList == null) {
                //noinspection Convert2Diamond
                mNewsArrayList = new ArrayList<News>();
            } else {
                mNewsArrayList.addAll(newNewsList);
            }
        } else {
            mNewsArrayList = (ArrayList<News>) newNewsList;
        }
        notifyDataSetChanged();
    }

    /**
     * Formats the date format retrieved from JSON data and converts it into current timezone format
     *
     * @param jsonDateString JSON format of the date data
     * @return formatted date string converted to default locale
     */
    private String formatDate(String jsonDateString) {
        if (jsonDateString != null && !jsonDateString.isEmpty()) {
            String jsonDatePattern = "yyyy-MM-dd'T'HH:mm:ss'Z'";
            SimpleDateFormat jsonFormatter = new SimpleDateFormat(jsonDatePattern, Locale.getDefault());
            try {
                Date parsedJsonDate = jsonFormatter.parse(jsonDateString);
                String finalDatePattern = "yyyy-MM-dd HH:mm";
                SimpleDateFormat finalDateFormatter = new SimpleDateFormat(finalDatePattern, Locale.getDefault());
                return finalDateFormatter.format(parsedJsonDate);
            } catch (ParseException e) {
                Log.e(LOG_TAG, "Error parsing JSON date.", e);
            }
        }
        return "";
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView newsTitle;
        final TextView newsSection;
        final TextView newsAuthor;
        final TextView newsDate;
        final View layout;

        ViewHolder(View itemView) {
            super(itemView);
            layout = itemView;
            newsTitle = (TextView) itemView.findViewById(R.id.news_title);
            newsSection = (TextView) itemView.findViewById(R.id.news_section);
            newsAuthor = (TextView) itemView.findViewById(R.id.news_author);
            newsDate = (TextView) itemView.findViewById(R.id.news_date);
        }
    }
}
