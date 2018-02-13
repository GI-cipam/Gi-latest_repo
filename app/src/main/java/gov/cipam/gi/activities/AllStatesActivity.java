package gov.cipam.gi.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import gov.cipam.gi.R;
import gov.cipam.gi.adapters.StatesAdapter;
import gov.cipam.gi.common.DatabaseFetch;
import gov.cipam.gi.database.Database;
import gov.cipam.gi.utils.Constants;

import static gov.cipam.gi.fragments.HomePageFragment.mDisplayStateList;

public class AllStatesActivity extends BaseActivity implements StatesAdapter.setOnStateClickedListener {
    RecyclerView rvAllStates;
    DatabaseFetch databaseFetch;
    DatabaseReference mDatabaseState;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_states);
        setUpToolbar(this);
        mAuth = FirebaseAuth.getInstance();
        mToolbar.setTitle(AllStatesActivity.class.getName());
        databaseFetch = new DatabaseFetch();
        databaseFetch.populateDisplayListFromDB(this);
        mDatabaseState=FirebaseDatabase.getInstance().getReference("States");

        rvAllStates=findViewById(R.id.allStatesRecycler);
        rvAllStates.setAdapter(new StatesAdapter(this,mDisplayStateList,this));
        rvAllStates.setLayoutManager(new GridLayoutManager(this,3));

    }

    @Override
    protected int getToolbarID() {
        return R.id.all_states_toolbar;
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
    public void onStateClickedListener(View view, int position) {
            startActivity(new Intent(this, ProductListActivity.class)
                    .putExtra(Constants.KEY_TYPE, Database.GI_STATE)
                    .putExtra(Constants.KEY_VALUE, mDisplayStateList.get(position).getName()));
    }
}
