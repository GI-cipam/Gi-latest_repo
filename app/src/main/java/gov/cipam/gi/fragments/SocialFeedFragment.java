package gov.cipam.gi.fragments;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ProgressBar;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.SearchTimeline;
import com.twitter.sdk.android.tweetui.TimelineResult;
import com.twitter.sdk.android.tweetui.TweetTimelineRecyclerViewAdapter;

import gov.cipam.gi.R;

/**
 * Created by karan on 12/21/2017.
 */

public class SocialFeedFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    ProgressBar progressBar;

    TweetTimelineRecyclerViewAdapter    adapter;
    RecyclerView                        recyclerView;
    SwipeRefreshLayout                  swipeRefreshLayout;
    private static final String         LOG_TAG ="Refresh" ;


    public static SocialFeedFragment newInstance() {

        Bundle args = new Bundle();

        SocialFeedFragment fragment = new SocialFeedFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_social_feed,container,false);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressBar = view.findViewById(R.id.progressBar);
        recyclerView = view.findViewById(R.id.recyclerViewSocialFeed);
        swipeRefreshLayout=view.findViewById(R.id.socialFeedSwipeRefreshLayout);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //setProgressDialogAnimation(progressBar,0);

        final SearchTimeline searchTimeline = new SearchTimeline.Builder()
                .query("#letstalkip")
                .maxItemsPerRequest(50)
                .build();

        adapter = new TweetTimelineRecyclerViewAdapter.Builder(getContext())
                .setTimeline(searchTimeline)
                .setViewStyle(R.style.tw__TweetLightWithActionsStyle)
                .build();

        recyclerView.setAdapter(adapter);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.md_red_500,R.color.md_blue_500,R.color.md_green_500);
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        adapter.refresh(new Callback<TimelineResult<Tweet>>() {
            @Override
            public void success(Result<TimelineResult<Tweet>> result) {
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void failure(TwitterException exception) {
                swipeRefreshLayout.setRefreshing(true);
            }

        });
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem item=menu.findItem(R.id.action_search);
        item.setVisible(false);
    }

}
