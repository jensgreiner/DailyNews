package models;

/**
 * POJO to store all the news retrieved from the API
 * Created by Jens Greiner on 02.07.17.
 */

public class News {
    private final String mNewsTitle;
    private final String mNewsSection;
    private final String mNewsAuthor;
    private final String mNewsDate;
    private final String mNewsUrl;

    public News(String title, String section, String author, String date, String url) {
        this.mNewsTitle = title;
        this.mNewsSection = section;
        this.mNewsAuthor = author;
        this.mNewsDate = date;
        this.mNewsUrl = url;
    }

    public String getNewsTitle() {
        return mNewsTitle;
    }

    public String getNewsSection() {
        return mNewsSection;
    }

    public String getNewsAuthor() {
        return mNewsAuthor;
    }

    public String getNewsDate() {
        return mNewsDate;
    }

    public String getmNewsUrl() {
        return mNewsUrl;
    }

    @Override
    public String toString() {
        return "News{" +
                "mNewsTitle='" + mNewsTitle + '\'' +
                ", mNewsSection='" + mNewsSection + '\'' +
                ", mNewsAuthor='" + mNewsAuthor + '\'' +
                ", mNewsDate='" + mNewsDate + '\'' +
                ", mNewsUrl='" + mNewsUrl + '\'' +
                '}';
    }
}
