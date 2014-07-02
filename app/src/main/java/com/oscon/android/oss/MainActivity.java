package com.oscon.android.oss;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.oscon.android.oss.service.SearchResults;
import com.oscon.android.oss.service.Tweet;
import com.oscon.android.oss.service.TwitterService;

import java.util.List;

import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static retrofit.RestAdapter.LogLevel.FULL;
import static retrofit.RestAdapter.LogLevel.NONE;

public class MainActivity extends Activity {
    public static final String TAG = MainActivity.class.getName();
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListView = (ListView) findViewById(R.id.list);

        final RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(BuildConfig.DEBUG ? FULL : NONE)
                .setEndpoint(TwitterService.API_URL)
                .setRequestInterceptor(new RequestInterceptor() {
                    @Override
                    public void intercept(RequestFacade request) {
                        request.addHeader("Authorization", TwitterService.AUTH_HEADER);
                    }
                })
                .build();
        final TwitterService service = restAdapter.create(TwitterService.class);
        service.searchTweets("OSCON", new Callback<SearchResults>() {
            @Override
            public void success(SearchResults results, Response response) {
                mListView.setAdapter(new TweetAdapter(MainActivity.this, results.statuses));
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(TAG, error.getUrl() + ": " + error.getMessage());
            }
        });
    }

    public static class TweetAdapter extends BaseAdapter {
        private final LayoutInflater mInflater;
        private final List<Tweet> mTweets;

        public TweetAdapter(Context context, List<Tweet> tweets) {
            super();
            mInflater = LayoutInflater.from(context);
            mTweets = tweets;
        }

        @Override
        public int getCount() {
            return mTweets.size();
        }

        @Override
        public Object getItem(int i) {
            return mTweets.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            final View newView;
            final TextView tv;
            if (view == null) {
                newView = mInflater.inflate(R.layout.list_item_row, viewGroup, false);
                tv = (TextView) newView.findViewById(R.id.label);
                newView.setTag(tv);
            } else {
                newView = view;
                tv = (TextView) view.getTag();
            }

            tv.setText(mTweets.get(i).text);
            return newView;
        }
    }
}
