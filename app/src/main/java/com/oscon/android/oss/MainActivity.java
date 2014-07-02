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

import retrofit.RestAdapter;
import retrofit.RetrofitError;

public class MainActivity extends Activity {
    ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListView = (ListView) findViewById(R.id.list);
    }

    @Override
    protected void onResume() {
        super.onResume();
        new FetchTask().execute(1);
    }

    public class FetchTask extends AsyncTask<Integer, Integer, Integer> {
        @Override
        protected Integer doInBackground(Integer... integers) {
            final RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint(TwitterService.API_URL)
                    .build();
            final TwitterService service = restAdapter.create(TwitterService.class);
            try {
                final SearchResults results = service.searchTweets("OSCON");
                mListView.setAdapter(new TweetAdapter(MainActivity.this, results.statuses));
            } catch (RetrofitError e) {
                Log.e("androidoss", e.getUrl() + ": " + e.getMessage());
            }
            return null;
        }
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
