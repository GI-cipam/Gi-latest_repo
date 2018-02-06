package gov.cipam.gi.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.ArrayList;

import gov.cipam.gi.R;
import gov.cipam.gi.adapters.StatePreferenceAdapter;
import gov.cipam.gi.model.StatePreference;

public class StatePreferenceActivity extends BaseActivity implements
StatePreferenceAdapter.setOnPreferenceStateClickListener{

    private ArrayList<StatePreference> statePreferences = new ArrayList<>();
    RecyclerView rvStatePreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_state_preference);

        setUpToolbar(this);

        rvStatePreference=findViewById(R.id.recycler_preference_state);
        rvStatePreference.setLayoutManager(new GridLayoutManager(this,3));
        rvStatePreference.addItemDecoration(new DividerItemDecoration(this,GridLayoutManager.VERTICAL));
        rvStatePreference.addItemDecoration(new DividerItemDecoration(this,GridLayoutManager.HORIZONTAL));
        rvStatePreference.setAdapter(new StatePreferenceAdapter(statePreferences,this));

        prepareList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_state_preference, menu);
        return true;
    }

    private void prepareList(){
        StatePreference statePreference=new StatePreference("One");
        statePreferences.add(statePreference);

        statePreference=new StatePreference("Two");
        statePreferences.add(statePreference);

        statePreference=new StatePreference("Three");
        statePreferences.add(statePreference);

        statePreference=new StatePreference("Two");
        statePreferences.add(statePreference);

        statePreference=new StatePreference("Three");
        statePreferences.add(statePreference);

        statePreference=new StatePreference("Two");
        statePreferences.add(statePreference);

        statePreference=new StatePreference("Three");
        statePreferences.add(statePreference);
    }

    @Override
    protected int getToolbarID() {
        return R.id.state_preference_toolbar;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;

            case R.id.action_done:
                startActivity(new Intent(this,HomePageActivity.class));
                break;
        }
        return true;
    }

    @Override
    public void onPreferenceStateClicked(Bundle bundle) {
    }
}
