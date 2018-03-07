package gov.cipam.gi.activities;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.MergeCursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.gms.appinvite.AppInviteInvitation;

import java.util.ArrayList;

import gov.cipam.gi.R;
import gov.cipam.gi.adapters.SearchCursorAdapter;
import gov.cipam.gi.background.LocationService;
import gov.cipam.gi.database.Database;
import gov.cipam.gi.common.HeaderViewPresenter;
import gov.cipam.gi.fragments.HomePageFragment;
import gov.cipam.gi.fragments.MapsFragment;
import gov.cipam.gi.fragments.SocialFeedFragment;
import gov.cipam.gi.model.Product;
import gov.cipam.gi.model.Seller;

import gov.cipam.gi.utils.Constants;

public class HomePageActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener,
        TabLayout.OnTabSelectedListener, SearchCursorAdapter.setOnSuggestionClickListener {

    private static final int REQUEST_INVITE = 23;
    public static final int MIN_NOTIFICATION_DISTANCE = 50;
    public static final int MIN_NOTIFICATION_SELLER_DISTANCE = 30;
    //private FirebaseAuth mAuth;
    private DrawerLayout drawer;

    SearchView searchView;
    FrameLayout frameLayout;
    String[] historyItem;
    String mQuery = "";
    BottomNavigationView navigation;
    String navItem;
    NavigationView navigationView;
    Cursor searchCursorHistory, searchCursorOther;
    SearchCursorAdapter searchCursorAdapter;
    MergeCursor searchMergeCursor, searchMergeCursorNew;
    Database databaseInstance;
    SQLiteDatabase database;
    Cursor searchCursorHistoryNew, searchCursorOtherNew;
    boolean isFABOpen = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        setUpToolbar(this);
        initializeTwitter();
        //showErrorSnackbar();
        databaseInstance = new Database(this);
        database = databaseInstance.getWritableDatabase();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        }

        Intent intent = getIntent();
        boolean isFromNotification = intent.getBooleanExtra("isFromNotification", false);
        if (isFromNotification) {
            ArrayList<Seller> myList = (ArrayList<Seller>) intent.getSerializableExtra("selectedSellerList");
//            Toast.makeText(this,myList.size()+" from Main", Toast.LENGTH_SHORT).show();
            fragmentInflate(MapsFragment.newInstance(myList));
        } else {
//            Toast.makeText(this, "Not from Notification", Toast.LENGTH_SHORT).show();
            if (savedInstanceState == null) {
                fragmentInflate(HomePageFragment.newInstance());
            }
        }

       // mAuth = FirebaseAuth.getInstance();
        populateInitialSearchCursor();
        drawer = findViewById(R.id.drawer_layout);

        //user = SharedPref.getSavedObjectFromPreference(HomePageActivity.this, Constants.KEY_USER_INFO, Constants.KEY_USER_DATA, Users.class);
        setNavigation();
    }


    @Override
    public void onBackPressed() {

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else
            startActivity(new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    private void setNavigation() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        HeaderViewPresenter headerviewPresenter = new HeaderViewPresenter(this, navigationView.getHeaderView(0), drawer);
        headerviewPresenter.initViews();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_home_page, menu);

        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        setSearchParameters();
/*
        if (mAuth.getCurrentUser().isAnonymous())
            menu.findItem(R.id.action_logout).setVisible(false);*/

        return true;
    }

    private void populateInitialSearchCursor() {
        historyItem = new String[]{Database.GI_HISTORY};
        searchCursorHistory = database.query(Database.GI_SEARCH_TABLE, null, Database.GI_SEARCH_TYPE + "=?", historyItem, null, null, Database.GI_SEARCH_ID + " DESC");
        searchCursorOther = database.query(Database.GI_SEARCH_TABLE, null, Database.GI_SEARCH_TYPE + "!=?", historyItem, null, null, Database.GI_SEARCH_NAME);
        searchMergeCursor = new MergeCursor(new Cursor[]{searchCursorHistory, searchCursorOther});
        searchCursorAdapter = new SearchCursorAdapter(this, searchMergeCursor, false, this, mQuery);

    }

    private MergeCursor populateDynamicSearchCursor(String newText) {
        historyItem = new String[]{Database.GI_HISTORY, newText + "%"};
        searchCursorHistoryNew = database.query(Database.GI_SEARCH_TABLE, null, Database.GI_SEARCH_TYPE + "=? AND " + Database.GI_SEARCH_NAME + " LIKE ?", historyItem, null, null, Database.GI_SEARCH_ID + " DESC");
        searchCursorOtherNew = database.query(Database.GI_SEARCH_TABLE, null, Database.GI_SEARCH_TYPE + "!=? AND " + Database.GI_SEARCH_NAME + " LIKE ?", historyItem, null, null, Database.GI_SEARCH_NAME);
        searchMergeCursorNew = new MergeCursor(new Cursor[]{searchCursorHistoryNew, searchCursorOtherNew});
//        searchCursorAdapter=new SearchCursorAdapter(this,searchMergeCursorNew,false,this,mQuery);
        return searchMergeCursorNew;
    }

    private void setSearchParameters() {
        searchView.isSubmitButtonEnabled();
        searchView.animate();
        searchView.setSuggestionsAdapter(searchCursorAdapter);
//        searchView.setSubmitButtonEnabled(true);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                boolean isAlreadyInHistory = false;
                while (searchCursorHistory.moveToNext()) {
                    if (searchCursorHistory.getString(searchCursorHistory.getColumnIndex(Database.GI_SEARCH_NAME)).equals(query)) {
                        isAlreadyInHistory = true;
                        break;
                    }
                }
                if (!isAlreadyInHistory) {
                    ContentValues contentValuesSearch = new ContentValues();

                    contentValuesSearch.put(Database.GI_SEARCH_NAME, query);
                    contentValuesSearch.put(Database.GI_SEARCH_TYPE, Database.GI_HISTORY);

                    database.insert(Database.GI_SEARCH_TABLE, null, contentValuesSearch);
//                populateInitialSearchCursor();
//                searchCursorAdapter.notifyDataSetChanged();
                }
                Bundle args = new Bundle();
                args.putString(Constants.KEY_QUERY, query);
                args.putString(Constants.KEY_TYPE, Database.GI_HISTORY);
                Intent intent = new Intent(HomePageActivity.this, SearchResultsActivity.class);
                intent.putExtras(args);
                startActivity(intent);
//                fragmentInflate(SearchResultPageFragment.newInstance(query,Database.GI_HISTORY));
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.equals("")) {

                } else {
                    mQuery = newText;
                    MergeCursor newMergeCursor = populateDynamicSearchCursor(newText);

                    searchCursorAdapter.updateQuery(newText);
                    searchCursorAdapter.swapCursor(newMergeCursor);
//                    searchCursorAdapter.notifyDataSetChanged();
                }
                return true;
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
/*            case R.id.action_logout:
                logoutAction();
                break;*/
            case R.id.action_search:
                break;
            case R.id.action_start_service:
                Intent intent = new Intent(this, LocationService.class);
                startService(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    selectedFragment = HomePageFragment.newInstance();
                    break;
                case R.id.navigation_map:
                    selectedFragment = MapsFragment.newInstance(null);
                    break;
                case R.id.navigation_social_feed:
                    selectedFragment = SocialFeedFragment.newInstance();
                    break;
            }
            fragmentInflate(selectedFragment);
            return true;
        }
    };

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle ic_tab_navigation view item clicks here.
        switch (item.getItemId()) {
/*
            case R.id.nav_account:
                navItem = "AccountInfo";
                startActivity(new Intent(this, NavItemActivity.class)
                        .putExtra(Constants.NAV_CATEGORY, navItem));
                break;*/

/*            case R.id.nav_sign_up:
                mAuth.signOut();
                startActivity(new Intent(this, LoginActivity.class));
                break;*/

            case R.id.nav_about_us:
                navItem = "AboutScreen";
                startActivity(new Intent(this, NavItemActivity.class)
                        .putExtra(Constants.NAV_CATEGORY, navItem));
                break;

            case R.id.nav_share:
                Intent intent = new AppInviteInvitation.IntentBuilder(getString(R.string.share_message))
                        .setMessage(getString(R.string.default_message))
                        .build();
                startActivityForResult(intent, REQUEST_INVITE);
                break;

            case R.id.nav_bio:
                navItem = "BioScreen";
                startActivity(new Intent(this, NavItemActivity.class)
                        .putExtra(Constants.NAV_CATEGORY, navItem));
                break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
/*        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            startActivity(new Intent(this, NewUserActivity.class));
        }*/
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

/*    public void logoutAction() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this).setMessage(R.string.logout);

        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                mAuth.signOut();
                startActivity(new Intent(HomePageActivity.this, LoginActivity.class));
                finish();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        }).show();
    }*/

    public void fragmentInflate(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.home_page_frame_layout, fragment);
        fragmentTransaction.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_INVITE) {
            if (requestCode == RESULT_OK) {
                String[] ids = AppInviteInvitation.getInvitationIds(requestCode, data);
                for (String id : ids) {

                }
            }
        } else {
            Toast.makeText(this, "sorry", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected int getToolbarID() {
        return R.id.toolbar;
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onSuggestionClickListener(View view, String type, String name) {
        switch (type) {

            case Database.GI_CATEGORY:
                startActivity(new Intent(this, ProductListActivity.class)
                        .putExtra(Constants.KEY_TYPE, type)
                        .putExtra(Constants.KEY_VALUE, name));
                break;

            case Database.GI_STATE:
                startActivity(new Intent(this, ProductListActivity.class)
                        .putExtra(Constants.KEY_TYPE, type)
                        .putExtra(Constants.KEY_VALUE, name));
                break;

            case Database.GI_HISTORY:
                Bundle args = new Bundle();
                args.putString(Constants.KEY_QUERY, name);
                args.putString(Constants.KEY_TYPE, Database.GI_HISTORY);
                startActivity(new Intent(HomePageActivity.this, SearchResultsActivity.class)
                        .putExtras(args));
                break;

            case Database.GI_PRODUCT:

                String[] s = {name};
                Cursor cursor = database.query(Database.GI_PRODUCT_TABLE, null, Database.GI_PRODUCT_NAME + "=?", s, null, null, null);

                cursor.moveToNext();
                String detail, category, state, dpurl, uid, history, description;

                name = cursor.getString(cursor.getColumnIndex(Database.GI_PRODUCT_NAME));
                detail = cursor.getString(cursor.getColumnIndex(Database.GI_PRODUCT_DETAIL));
                category = cursor.getString(cursor.getColumnIndex(Database.GI_PRODUCT_CATEGORY));
                state = cursor.getString(cursor.getColumnIndex(Database.GI_PRODUCT_STATE));
                dpurl = cursor.getString(cursor.getColumnIndex(Database.GI_PRODUCT_DP_URL));
                uid = cursor.getString(cursor.getColumnIndex(Database.GI_PRODUCT_UID));
                history = cursor.getString(cursor.getColumnIndex(Database.GI_PRODUCT_HISTORY));
                description = cursor.getString(cursor.getColumnIndex(Database.GI_PRODUCT_DESCRIPTION));

                Product oneGI = new Product(name, dpurl, detail, category, state, description, history, uid);

                Bundle bundle = new Bundle();
                bundle.putSerializable("ppp", oneGI);
                startActivity(new Intent(this, ProductListActivity.class)
                        .putExtras(bundle)
                        .putExtra(Constants.KEY_TYPE, type));

                cursor.close();
                break;
        }

    }

    @Override
    public void onClick(View view) {

    }
}