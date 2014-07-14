package com.oscon.android.oss;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

public interface TwitterServiceInterface {
    public static final String API_URL = "https://api.twitter.com/1.1";
    public static final String AUTH_HEADER = "Bearer AAAAAAAAAAAAAAAAAAAAALKiYQAAAAAAY5Zy%2FKmPBYOh8by7IKAFMi5mAbI%3DWyeNjQmXcSmtuRYT15f7MmHK10WjHh1VVGU02IIMhy1bwtfglB";

    @GET("/search/tweets.json")
    void searchTweets(
            @Query("q") String query,
            Callback<SearchResults> response
    );
}
