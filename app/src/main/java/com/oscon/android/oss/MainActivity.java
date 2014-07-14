package com.oscon.android.oss;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static retrofit.RestAdapter.LogLevel.FULL;
import static retrofit.RestAdapter.LogLevel.NONE;

public class MainActivity extends ListActivity {
    public static final String TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(BuildConfig.DEBUG ? FULL : NONE)
                .setEndpoint(TwitterServiceInterface.API_URL)
                .setRequestInterceptor(new RequestInterceptor() {
                    @Override
                    public void intercept(RequestFacade request) {
                        request.addHeader("Authorization", TwitterServiceInterface.AUTH_HEADER);
                    }
                })
                .build();

        final EditText searchQuery = (EditText) findViewById(R.id.search_query);

        findViewById(R.id.search_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final TwitterServiceInterface service = restAdapter.create(
                        TwitterServiceInterface.class);
                String query = searchQuery.getText().toString();
                if (!query.isEmpty()) {
                    service.searchTweets(query, new Callback<SearchResults>() {
                        @Override
                        public void success(SearchResults results, Response response) {
                            setListAdapter(new TweetAdapter(MainActivity.this,
                                    R.layout.list_item_row, results.statuses));
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            Log.e(TAG, error.getUrl() + ": " + error.getMessage());
                        }
                    });
                }
            }
        });
    }

    public static class TweetAdapter extends ArrayAdapter<Tweet> {
        private final Context mContext;
        private final int mResourceId;

        public TweetAdapter(Context context, int resource, List<Tweet> objects) {
            super(context, resource, objects);
            mContext = context;
            mResourceId = resource;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final View row = LayoutInflater.from(mContext).inflate(mResourceId, parent, false);
            final Tweet tweet = getItem(position);

            TextView label = (TextView) row.findViewById(R.id.label);
            label.setText(tweet.text);

            ImageView avatar = (ImageView) row.findViewById(R.id.avatar);
            Picasso.with(mContext)
                    .load(tweet.user.profileImageUrl)
                    .placeholder(R.drawable.placeholder)
                    .into(avatar);
            return row;
        }
    }
}
