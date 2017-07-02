package adapters;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.greiner_co.dailynews.R;

import java.util.ArrayList;
import java.util.List;

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
        holder.newsDate.setText(currentNews.getNewsDate());

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
        return mNewsArrayList.size();
    }

    public void swapData(List<News> newNewsList) {
        if (mNewsArrayList != null && !mNewsArrayList.isEmpty()) {
            mNewsArrayList.clear();
            mNewsArrayList.addAll(newNewsList);
        } else {
            mNewsArrayList = (ArrayList<News>) newNewsList;
        }
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView newsTitle;
        TextView newsSection;
        TextView newsAuthor;
        TextView newsDate;
        View layout;

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
