package com.oscon.android.oss.service;

import retrofit.http.GET;
import retrofit.http.Query;

public interface TwitterService {
    public static final String API_URL = "https://api.twitter.com/1.1/";

    @GET("/search/tweets.json")
    SearchResults searchTweets(@Query("q") String query);
}
