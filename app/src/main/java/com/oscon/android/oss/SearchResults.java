package com.oscon.android.oss;

import java.util.List;

public class SearchResults {
    // Having an items data member that is a List of Tweets tells Retrofit that we are expecting
    // the JSON object to have a JSON array, named statuses, where each element in that array
    // should get mapped to Tweet objects.
    public List<Tweet> statuses;
}
