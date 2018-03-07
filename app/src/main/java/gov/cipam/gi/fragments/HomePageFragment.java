package gov.cipam.gi.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.twitter.sdk.android.tweetui.TweetUi;

import java.util.ArrayList;

import gov.cipam.gi.R;
import gov.cipam.gi.activities.AllStatesActivity;
import gov.cipam.gi.activities.ProductListActivity;
import gov.cipam.gi.adapters.CategoryAdapter;
import gov.cipam.gi.adapters.GiSliderImageAdapter;
import gov.cipam.gi.adapters.StatesAdapter;
import gov.cipam.gi.common.DatabaseFetch;
import gov.cipam.gi.database.Database;
import gov.cipam.gi.model.Categories;
import gov.cipam.gi.model.States;
import gov.cipam.gi.utils.Constants;
import gov.cipam.gi.utils.StartSnapHelper;

/**
 * Created by karan on 11/20/2017.
 */

public class HomePageFragment extends Fragment implements CategoryAdapter.setOnCategoryClickListener,
        StatesAdapter.setOnStateClickedListener
        , GiSliderImageAdapter.setOnGiClickListener
        , ViewPager.OnPageChangeListener
        ,View.OnClickListener{

    TextView txtCategory,txtState,txtMore;
    private TextView[] dots;
    int page_position = 0;
    LinearLayout dotsLinearLayout;
    RecyclerView rvState, rvCategory;
    ViewPager giSliderViewPager;
    NestedScrollView scrollView;
    FirebaseAuth mAuth;
    DatabaseReference mDatabaseState, mDatabaseCategory;
    private DatabaseFetch databaseFetch;
    public static ArrayList<States> mDisplayStateList = new ArrayList<>();
    public static ArrayList<Categories> mDisplayCategoryList = new ArrayList<>();


    public static HomePageFragment newInstance() {

        Bundle args = new Bundle();

        HomePageFragment fragment = new HomePageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_homepage, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mAuth = FirebaseAuth.getInstance();

        databaseFetch = new DatabaseFetch();
        databaseFetch.populateDisplayListFromDB(getContext());

        mDatabaseState = FirebaseDatabase.getInstance().getReference("States");
        mDatabaseCategory = FirebaseDatabase.getInstance().getReference("Categories");

        new Thread(() -> TweetUi.getInstance()).start();

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        txtMore=view.findViewById(R.id.more_text);
        txtCategory=view.findViewById(R.id.category_text);
        txtState=view.findViewById(R.id.states_text);
        rvState = view.findViewById(R.id.recycler_states);
        rvCategory = view.findViewById(R.id.recycler_categories);
        giSliderViewPager = view.findViewById(R.id.gi_vp_slider);
        dotsLinearLayout = view.findViewById(R.id.gi_ll_dots);
        scrollView = view.findViewById(R.id.scroll_view_home);

        scrollView.setSmoothScrollingEnabled(true);
        scrollView.setNestedScrollingEnabled(true);

        /*Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/helvetica_bolder.otf");
        txtState.setTypeface(typeface);
        txtCategory.setTypeface(typeface);*/

        addBottomDots(0);
        //setAutoScroll();
        rvState.setAdapter(new StatesAdapter(this, mDisplayStateList, getContext()));
        rvCategory.setAdapter(new CategoryAdapter(mDisplayCategoryList, getContext(), this));
        giSliderViewPager.setAdapter(new GiSliderImageAdapter(mDisplayCategoryList, getActivity(), this));

        rvState.setLayoutManager(new GridLayoutManager(getContext(),2 , GridLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });

        rvCategory.setLayoutManager(new GridLayoutManager(getContext(), getActivity().getResources().getInteger(R.integer.portrait_grid_no), GridLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });

        giSliderViewPager.setOnPageChangeListener(this);
        txtMore.setOnClickListener(this);
    }

    /*private void setAutoScroll(){
        final Handler handler = new Handler();

        final Runnable update = new Runnable() {
            public void run() {
                if (page_position == mDisplayCategoryList.size()) {
                    page_position = 0;
                } else {
                    page_position = page_position + 1;
                }
                giSliderViewPager.setCurrentItem(page_position, true);
            }
        };

        new Timer().schedule(new TimerTask() {

            @Override
            public void run() {
                handler.post(update);
            }
        }, 100, 5000);
    }*/

    private void addBottomDots(int currentPage) {
        dots = new TextView[mDisplayCategoryList.size()];

        dotsLinearLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(getContext());
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(Color.parseColor("#aeaeae"));
            dotsLinearLayout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(Color.parseColor("#dbdbdb"));
    }

    @Override
    public void onPause() {
        super.onPause();
        setRetainInstance(true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        setRetainInstance(true);
    }

    @Override
    public void onCategoryClicked(View view, int position) {
        startActivity(new Intent(getContext(), ProductListActivity.class)
                .putExtra(Constants.KEY_TYPE, Database.GI_CATEGORY)
                .putExtra(Constants.KEY_VALUE, mDisplayCategoryList.get(position).getName()));
    }

    @Override
    public void onStateClickedListener(View view, int position) {
        startActivity(new Intent(getContext(), ProductListActivity.class)
                .putExtra(Constants.KEY_TYPE, Database.GI_STATE)
                .putExtra(Constants.KEY_VALUE, mDisplayStateList.get(position).getName()));
    }

    @Override
    public void onGiItemClicked(View view, int position) {
        startActivity(new Intent(getContext(), ProductListActivity.class)
                .putExtra(Constants.KEY_TYPE, Database.GI_CATEGORY)
                .putExtra(Constants.KEY_VALUE, mDisplayCategoryList.get(position).getName()));
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        addBottomDots(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.more_text:
                startActivity(new Intent(getContext(), AllStatesActivity.class));
        }
    }
}
