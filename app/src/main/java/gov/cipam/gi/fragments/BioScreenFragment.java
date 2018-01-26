package gov.cipam.gi.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import gov.cipam.gi.R;
import gov.cipam.gi.adapters.BioAdapter;
import gov.cipam.gi.model.Bio;

/**
 * Created by karan on 1/25/2018.
 */

public class BioScreenFragment extends Fragment implements BioAdapter.setOnBioClickListener{

    RecyclerView recyclerView;
    List<Bio> mBioList;

    public static BioScreenFragment newInstance() {

        Bundle args = new Bundle();

        BioScreenFragment fragment = new BioScreenFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bio_screen,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mBioList=new ArrayList<>();

        recyclerView=view.findViewById(R.id.bioRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(new BioAdapter(mBioList,this));
        setData();
    }

    private void setData(){

        Bio bio=new Bio("Random Name","email.e@gmail.com");
        mBioList.add(bio);

        bio=new Bio("Random Name","email.e@gmail.com");
        mBioList.add(bio);

        bio=new Bio("Random Name","email.e@gmail.com");
        mBioList.add(bio);

        bio=new Bio("Random Name","email.e@gmail.com");
        mBioList.add(bio);

        bio=new Bio("Random Name","email.e@gmail.com");
        mBioList.add(bio);
    }

    @Override
    public void onItemClicked(View view, int position) {

    }
}
