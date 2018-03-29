package gov.cipam.gi.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import gov.cipam.gi.R;
import gov.cipam.gi.adapters.BioAdapter;
import gov.cipam.gi.model.Bio;

/**
 * Created by karan on 1/25/2018.
 */

public class BioScreenFragment extends Fragment implements BioAdapter.setOnBioClickListener {

    RecyclerView recyclerView;
    private ArrayList<Bio> mBioList = new ArrayList<>();

    public static BioScreenFragment newInstance() {

        Bundle args = new Bundle();

        BioScreenFragment fragment = new BioScreenFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bio, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.bioRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        setmBioList();
        recyclerView.setAdapter(new BioAdapter(this,mBioList));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().setTitle(getString(R.string.developer_screen));
    }

    @Override
    public void onItemClicked(View view, int position) {

    }

    private void setmBioList() {
        String[] dev_names = getContext().getResources().getStringArray(R.array.dev_names);
        String[] dev_email = getContext().getResources().getStringArray(R.array.dev_emails);
        int dev_dp[] = new int[]{R.drawable.ns1, R.drawable.karan_dp, R.drawable.deepak, R.drawable.kd, R.drawable.rachit_dp};

        for (int i = 0; i < dev_names.length; i++) {
            Bio bio = new Bio(dev_names[i],dev_email[i],dev_dp[i]);
            mBioList.add(bio);
        }
    }
}
