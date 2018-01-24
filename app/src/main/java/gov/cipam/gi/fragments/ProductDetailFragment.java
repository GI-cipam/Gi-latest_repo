package gov.cipam.gi.fragments;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.Window;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

import gov.cipam.gi.activities.MapsActivity;
import gov.cipam.gi.R;
import gov.cipam.gi.activities.WebViewActivity;
import gov.cipam.gi.adapters.GiUniquenessListAdapter;
import gov.cipam.gi.adapters.SellerListAdapter;
import gov.cipam.gi.database.Database;
import gov.cipam.gi.model.Product;
import gov.cipam.gi.model.Seller;
import gov.cipam.gi.model.Uniqueness;
import gov.cipam.gi.utils.ChromeTabActionBroadcastReceiver;
import gov.cipam.gi.utils.CommonUtils;
import gov.cipam.gi.utils.CustomTabActivityHelper;
import gov.cipam.gi.utils.PaletteGenerate;
import gov.cipam.gi.utils.StartSnapHelper;

import static gov.cipam.gi.utils.Constants.EXTRA_URL;

/**
 * Created by karan on 12/14/2017.
 */

public class ProductDetailFragment extends Fragment implements SellerListAdapter.setOnSellerClickListener
,GiUniquenessListAdapter.setOnItemClickListener{
//    Seller seller;
//    SellerFirebaseAdapter sellerFirebaseAdapter;
//    DatabaseReference mDatabaseReference;

    String name,address,contact;
    TextView txtState,txtCategory,txtHistory,txtDesc;
    Double lon,lat;
    RecyclerView rvSeller,rvUniqueness;
    ArrayList<Seller> sellerList;
    List<Uniqueness> uniquenessList;
    Database databaseInstance;
    SQLiteDatabase database;
    ImageView imageView;
    StartSnapHelper startSnapHelper;
    PaletteGenerate paletteGenerate;
    Product product;
    Toolbar toolbar;
    boolean isImagePreLoaded = false;
    public static Bitmap mBitmap;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_product_detail, container, false);
    }

    public ProductDetailFragment() {
    }


    public static ProductDetailFragment newInstance(Product product, Bitmap bitmap) {

        Bundle args = new Bundle();
        args.putSerializable("nalin", product);
        ProductDetailFragment fragment = new ProductDetailFragment();
        fragment.setArguments(args);
        mBitmap=bitmap;
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        sellerList = new ArrayList<>();
        uniquenessList=new ArrayList<>();
        databaseInstance = new Database(getContext());
        startSnapHelper=new StartSnapHelper();

        database = databaseInstance.getReadableDatabase();
        paletteGenerate=new PaletteGenerate();
        toolbar=((AppCompatActivity) getActivity()).findViewById(R.id.product_activity_toolbar);

        populateSellerListFromDB();
        setHasOptionsMenu(true);

        super.onCreate(savedInstanceState);
    }

    private void populateSellerListFromDB() {

//        String[] s={ProductDetailActivity.currentProduct.getUid()};

        Bundle b=getArguments();
        product=(Product)b.get("nalin");
        String[] s={product.getUid()};
        Cursor sellerCursor=database.query(Database.GI_SELLER_TABLE,null,Database.GI_SELLER_UID+"=?",s,null,null,null,null);
        while(sellerCursor.moveToNext()){

            name=sellerCursor.getString(sellerCursor.getColumnIndex(Database.GI_SELLER_NAME));
            address=sellerCursor.getString(sellerCursor.getColumnIndex(Database.GI_SELLER_ADDRESS));
            contact=sellerCursor.getString(sellerCursor.getColumnIndex(Database.GI_SELLER_CONTACT));
            lat=sellerCursor.getDouble(sellerCursor.getColumnIndex(Database.GI_SELLER_LAT));
            lon=sellerCursor.getDouble(sellerCursor.getColumnIndex(Database.GI_SELLER_LON));

            Seller oneSeller = new Seller(name, address, contact, lon, lat);

            sellerList.add(oneSeller);
        }
        sellerCursor.close();
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem settingsAction=menu.findItem(R.id.action_settings_product_list);
        settingsAction.setVisible(false);
        MenuItem refreshOption=menu.findItem(R.id.menu_refresh);
        refreshOption.setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();

        switch (id){
            case android.R.id.home:
                getActivity().getSupportFragmentManager()
                        .popBackStackImmediate();
                break;
            case R.id.action_location:
                startActivity(new Intent(getContext(),MapsActivity.class)
                        .putExtra("latitude",lat)
                        .putExtra("longitude",lon)
                        .putExtra("address",address));
                break;
            case R.id.action_url:

                String url="https://google.com";
                Uri uri=Uri.parse(url);
                openCustomChromeTab(uri);
                break;
        }
        return true;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvSeller= view.findViewById(R.id.seller_recycler_view);
        rvUniqueness=view.findViewById(R.id.recycler_unique);
        imageView=getActivity().findViewById(R.id.productDetailImage);
        txtState=view.findViewById(R.id.detail_stateName);
        txtCategory=view.findViewById(R.id.detail_categoryName);
        txtHistory = view.findViewById(R.id.productHistory);
        txtDesc=view.findViewById(R.id.productDesc);

        rvUniqueness.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        startSnapHelper.attachToRecyclerView(rvUniqueness);

        rvSeller.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));

        imageView = getActivity().findViewById(R.id.productDetailImage);
        imageView.setVisibility(View.VISIBLE);
        CommonUtils.loadImage(imageView,mBitmap,getActivity());
        rvUniqueness.setAdapter(new GiUniquenessListAdapter(getContext(),uniquenessList,this));
        rvSeller.setAdapter(new SellerListAdapter(getContext(), sellerList, this));
        setData();
    }

    private void openCustomChromeTab(Uri uri) {
        CustomTabsIntent.Builder intentBuilder = new CustomTabsIntent.Builder();

        // set toolbar colors
        intentBuilder.setToolbarColor(getResources().getColor(R.color.colorPrimary));
        intentBuilder.addMenuItem(getString(R.string.title_menu_1), createPendingIntent(ChromeTabActionBroadcastReceiver.ACTION_MENU_ITEM_1));
        intentBuilder.addMenuItem(getString(R.string.title_menu_2), createPendingIntent(ChromeTabActionBroadcastReceiver.ACTION_MENU_ITEM_2));

        // set action button
        intentBuilder.setActionButton(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher), "Action Button", createPendingIntent(ChromeTabActionBroadcastReceiver.ACTION_ACTION_BUTTON));

        // build custom tabs intent
        CustomTabsIntent customTabsIntent = intentBuilder.build();

        // call helper to open custom tab
        CustomTabActivityHelper.openCustomTab(getActivity(), customTabsIntent, uri, new CustomTabActivityHelper.CustomTabFallback() {
            @Override
            public void openUri(Activity activity, Uri uri) {
                // fall back, call open open webview
                openWebView(uri);
            }
        });
    }

    private void openWebView(Uri uri) {
        Intent webViewIntent = new Intent(getContext(), WebViewActivity.class);
        webViewIntent.putExtra(EXTRA_URL, uri.toString());
        startActivity(webViewIntent);
    }

    private PendingIntent createPendingIntent(int actionSource) {
        Intent actionIntent = new Intent(getContext(), ChromeTabActionBroadcastReceiver.class);
        actionIntent.putExtra(ChromeTabActionBroadcastReceiver.KEY_ACTION_SOURCE, actionSource);
        return PendingIntent.getBroadcast(getContext(), actionSource, actionIntent, 0);
    }

    private void setData(){
        txtDesc.setText(product.getDescription());
        txtHistory.setText(product.getDetail());
        txtCategory.setText(product.getCategory());
        txtState.setText(product.getState());

        Uniqueness uniqueness=new Uniqueness("Achha chalta hun duao mein yaad rakhna");
        uniquenessList.add(uniqueness);

        uniqueness=new Uniqueness("Achha chalta hun duao mein yaad rakhna");
        uniquenessList.add(uniqueness);
        uniqueness=new Uniqueness("Achha chalta hun duao mein yaad rakhna");
        uniquenessList.add(uniqueness);

        uniqueness=new Uniqueness("Achha chalta hun duao mein yaad rakhna");
        uniquenessList.add(uniqueness);

        uniqueness=new Uniqueness("Achha chalta hun duao mein yaad rakhna");
        uniquenessList.add(uniqueness);

    }
    @Override
    public void onSellerClicked(View v, int Position) {

    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onPause() {
        super.onPause();
        setRetainInstance(true);
    }

    @Override
    public void onItemClicked(View v, int Position) {

    }

}
