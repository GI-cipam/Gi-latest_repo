package gov.cipam.gi.activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.view.MenuItem;

import gov.cipam.gi.R;
import gov.cipam.gi.fragments.AboutFragment;
import gov.cipam.gi.fragments.AccountInfoFragment;
import gov.cipam.gi.fragments.BioScreenFragment;
import gov.cipam.gi.utils.Constants;

public class NavItemActivity extends BaseActivity{

    Fragment fragment;
    String navItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_item);
        setUpToolbar(this);

        navItem=getIntent().getStringExtra(Constants.NAV_CATEGORY);
        fragmentInflate();
    }

    public void fragmentInflate(){

        switch (navItem){
            case "AccountInfo":
                fragment= AccountInfoFragment.newInstance();
                navFragmentInflate(fragment);
                break;
            case "BioScreen":
                fragment= BioScreenFragment.newInstance();
                navFragmentInflate(fragment);
                break;
            case "AboutScreen":
                fragment= AboutFragment.newInstance();
                navFragmentInflate(fragment);
                break;
        }
    }

    private void navFragmentInflate(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.nav_activity_frame_layout, fragment);
        fragmentTransaction.commit();
    }

    @Override
    protected int getToolbarID() {
        return R.id.nav_activity_toolbar;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }
}
