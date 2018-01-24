package gov.cipam.gi.activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import gov.cipam.gi.R;
import gov.cipam.gi.adapters.BioAdapter;
import gov.cipam.gi.model.Bio;

public class BioScreenActivity extends BaseActivity implements BioAdapter.setOnBioClickListener{

    RecyclerView recyclerView;
    List<Bio> mBioList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bio_screen);

        setUpToolbar(this);

        mBioList=new ArrayList<>();

        recyclerView=findViewById(R.id.bioRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
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
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected int getToolbarID() {
        return R.id.bio_activity_toolbar;
    }

    @Override
    public void onItemClicked(View view, int position) {

    }
}
