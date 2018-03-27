package gov.cipam.gi.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import gov.cipam.gi.R;
import gov.cipam.gi.model.Categories;

/**
 * Created by karan on 1/26/2018.
 */

public class GiImageAdapter extends PagerAdapter implements View.OnClickListener {

    private ArrayList<Categories> mCategories;
    private ImageView imgvSliderImage;
    private Activity activity;
    private ProgressBar progressBar;
    int position;
    setOnGiClickListener mListener;

    public GiImageAdapter(ArrayList<Categories> mCategories, Activity activity, setOnGiClickListener mListener) {
        this.mCategories = mCategories;
        this.activity = activity;
        this.mListener = mListener;
    }

    public interface setOnGiClickListener {
        void onGiItemClicked(View view, int position);
    }

    @Override
    public int getCount() {
        return mCategories.size();
    }

    public Object instantiateItem(ViewGroup container, int position) {
        this.position = position;
        final LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        assert layoutInflater != null;
        View view = layoutInflater.inflate(R.layout.item_gi_image, container, false);

        final RelativeLayout relativeLayout = view.findViewById(R.id.giRelativeLayout);
        final TextView GiSliderTv = view.findViewById(R.id.text_gi);
        imgvSliderImage = view.findViewById(R.id.image_gi);
        progressBar = view.findViewById(R.id.progressBarGi);

        progressBar.setVisibility(View.VISIBLE);
        GiSliderTv.setText(mCategories.get(position).getName());
        Picasso.with(activity)
                .load(mCategories.get(position).getDpurl())
                .placeholder(R.drawable.btn_round_white)
                .into(imgvSliderImage, new Callback() {
                    @Override
                    public void onSuccess() {
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {
                        imgvSliderImage.setImageResource(R.drawable.ic_image_off);
                        progressBar.setVisibility(View.GONE);
                    }
                });
        container.addView(view);
        relativeLayout.setOnClickListener(this);
        return view;
    }


    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
    }

    @Override
    public void onClick(View view) {
        mListener.onGiItemClicked(view, position);
    }
}
