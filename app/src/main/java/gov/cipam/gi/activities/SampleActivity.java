package gov.cipam.gi.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import gov.cipam.gi.R;
import gov.cipam.gi.adapters.UniquenessPagerAdapter;

/**
 * Created by karan on 1/26/2018.
 */

public class SampleActivity extends BaseActivity implements ViewPager.OnPageChangeListener{

    int page_position = 0;
    ViewPager viewPager;
    LinearLayout linearLayout;
    ArrayList<String> mUniqueness;
    private TextView[] dots;
    UniquenessPagerAdapter uniquenessPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slider);
        setUpToolbar(this);

        init();
        addBottomDots(0);
        final Handler handler = new Handler();

        final Runnable update = new Runnable() {
            public void run() {
                if (page_position == mUniqueness.size()) {
                    page_position = 0;
                } else {
                    page_position = page_position + 1;
                }
                viewPager.setCurrentItem(page_position, true);
            }
        };

        new Timer().schedule(new TimerTask() {

            @Override
            public void run() {
                handler.post(update);
            }
        }, 100, 5000);
        
    }
    
    private void init(){
        viewPager=findViewById(R.id.vp_slider);
        linearLayout=findViewById(R.id.ll_dots);
        
        mUniqueness=new ArrayList<>();

        mUniqueness.add("Achha chalta hun duao mein yaad rakhna");
        mUniqueness.add("Achha chalta hun duao mein yaad rakhna");

        //uniquenessPagerAdapter=new UniquenessPagerAdapter(mUniqueness,this);
        viewPager.setAdapter(uniquenessPagerAdapter);
        viewPager.setOnPageChangeListener(this);
    }
    @Override
    protected int getToolbarID() {
        return 0;
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
    private void addBottomDots(int currentPage) {
        dots = new TextView[mUniqueness.size()];

        linearLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(Color.parseColor("#000000"));
            linearLayout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(Color.parseColor("#FFFFFF"));
    }
}
