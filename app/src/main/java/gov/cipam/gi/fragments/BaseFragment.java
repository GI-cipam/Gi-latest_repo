package gov.cipam.gi.fragments;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import gov.cipam.gi.R;
import gov.cipam.gi.utils.Constants;

/**
 * Created by karan on 11/20/2017.
 */

public abstract class BaseFragment extends Fragment {

    String                                              textSize;
    boolean                                             downloadImages;
    Toolbar                                             mToolbar;
    SharedPreferences                                   sharedPreferences;
    SharedPreferences.OnSharedPreferenceChangeListener  prefListener;

   /* @Override
    public void onCreate(Bundle savedInstanceState) {

        setUpFont();

        super.onCreate(savedInstanceState);

        sharedPreferencesListener();
    }*/



    //get toolbar ID
    protected abstract int getToolbarID();

    protected void setUpToolbar(Activity activity) {
        mToolbar = getView().findViewById(getToolbarID());
        ((AppCompatActivity)activity).setSupportActionBar(mToolbar);

        loadPreferences();

        if (((AppCompatActivity)activity).getSupportActionBar() != null) {
            ((AppCompatActivity)activity).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    /*@Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(prefListener);
    }*/

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferencesListener();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setUpFont();
        return super.onCreateView(inflater, container, savedInstanceState);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpToolbar(getActivity());
    }

    protected void setUpFont() {

        loadPreferences();

        if (textSize.equals(getString(R.string.text_size_small))) {
            getContext().setTheme(R.style.TextSizeSmall);
        }
        else if (textSize.equals(getString(R.string.text_size_medium))) {
            getContext().setTheme(R.style.TextSizeMedium);
        }
        else if (textSize.equals(getString(R.string.text_size_large))) {
            getContext().setTheme(R.style.TextSizeLarge);
        }
    }
    protected void loadPreferences() {

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        textSize = sharedPreferences.getString(Constants.KEY_TEXT_SIZE, getString(R.string.text_size_small));
        downloadImages  = sharedPreferences.getBoolean(Constants.KEY_DOWNLOAD_IMAGES, true);
    }

    protected void sharedPreferencesListener() {

        prefListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            }
        };
    }
}
