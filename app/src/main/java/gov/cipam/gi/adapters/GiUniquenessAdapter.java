package gov.cipam.gi.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import gov.cipam.gi.R;
import gov.cipam.gi.model.Uniqueness;

/**
 * Created by karan on 1/26/2018.
 */

public class GiUniquenessAdapter extends PagerAdapter {


    private ArrayList<Uniqueness> mUniqueness;
    private Activity activity;

    public GiUniquenessAdapter(ArrayList<Uniqueness> mUniqueness, Activity activity) {
        this.mUniqueness= mUniqueness;
        this.activity=activity;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        final LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.item_uniqueness, container, false);
        final TextView txtUni=view.findViewById(R.id.text_gi_uniqueness);
        final TextView txtPos=view.findViewById(R.id.text_uniqueness_position);

        txtUni.setText(mUniqueness.get(position).getInfo());
        txtPos.setText(String.valueOf(position+1));
        container.addView(view);

        return view;
    }

    @Override
    public int getCount() {
        return mUniqueness.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view= (View) object;
        container.removeView(view);
    }
}
