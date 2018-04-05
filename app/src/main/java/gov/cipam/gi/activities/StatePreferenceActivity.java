package gov.cipam.gi.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;

import gov.cipam.gi.R;
import gov.cipam.gi.adapters.StatePreferenceAdapter;
import gov.cipam.gi.database.Database;
import gov.cipam.gi.model.StatePreference;
import gov.cipam.gi.model.States;

import static gov.cipam.gi.fragments.HomePageFragment.mDisplayStateList;

public class StatePreferenceActivity extends BaseActivity implements
StatePreferenceAdapter.setOnPreferenceStateClickListener{
    private Database databaseInstance;
    private SQLiteDatabase database;
    private ArrayList<StatePreference> statePreferences = new ArrayList<>();
    private ArrayList<StatePreference> selectedStatePreferences = new ArrayList<>();
    RecyclerView rvStatePreference;
    StatePreferenceAdapter statePreferenceAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_state_preference);

        setUpToolbar(this);

        rvStatePreference=findViewById(R.id.recycler_preference_state);
        rvStatePreference.setLayoutManager(new GridLayoutManager(this,3));
        //rvStatePreference.addItemDecoration(new DividerItemDecoration(this,GridLayoutManager.VERTICAL));
        //rvStatePreference.addItemDecoration(new DividerItemDecoration(this,GridLayoutManager.HORIZONTAL));
        statePreferenceAdapter=new StatePreferenceAdapter(statePreferences,this);
                rvStatePreference.setAdapter(statePreferenceAdapter);

        databaseInstance = new Database(this);
        database=databaseInstance.getWritableDatabase();

        prepareList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_state_preference, menu);
        return true;
    }

    private void prepareList(){

        Cursor stateCursor=database.query(Database.GI_STATE_TABLE,null,null,null,null,null,null,null);
        while(stateCursor.moveToNext()){
            String name=stateCursor.getString(stateCursor.getColumnIndex(Database.GI_STATE_NAME));
            String dpurl=stateCursor.getString(stateCursor.getColumnIndex(Database.GI_STATE_DP_URL));

            States oneState=new States(name,dpurl);
            StatePreference statePreference=new StatePreference(oneState);
            statePreferences.add(statePreference);
        }
        statePreferenceAdapter.notifyDataSetChanged();
        stateCursor.close();
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
                int count=addToDatabase();
                if(count>=6) {
                    Toast.makeText(this, count+"", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, HomePageActivity.class));
                }
                else{
                    Toast.makeText(this, "Please select 6 states or more to continue", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        return true;
    }

    private int addToDatabase() {
        if(selectedStatePreferences.size()>=6) {
            database.delete(Database.GI_PREFERENCE_STATE_TABLE,null,null);
            for (int i = 0; i < selectedStatePreferences.size(); i++) {
                ContentValues contentValuesState = new ContentValues();

                States currentState = selectedStatePreferences.get(i).getState();
                contentValuesState.put(Database.GI_STATE_NAME, currentState.getName());
                contentValuesState.put(Database.GI_STATE_DP_URL, currentState.getDpurl());

                database.insert(Database.GI_PREFERENCE_STATE_TABLE, null, contentValuesState);
            }
        }

        return selectedStatePreferences.size();
    }

    @Override
    public void onPreferenceStateClicked(int position,View view) {
        StatePreference oneStatePreference=statePreferences.get(position);
        RelativeLayout relativeLayout=(RelativeLayout) view;
        if (selectedStatePreferences.contains(oneStatePreference)){
            selectedStatePreferences.remove(oneStatePreference);
            relativeLayout.setBackgroundColor(Color.TRANSPARENT);
        }
        else{
            selectedStatePreferences.add(oneStatePreference);
            relativeLayout.setBackgroundColor(ContextCompat.getColor(this,R.color.colorSelector));
        }
    }
}
