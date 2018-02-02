package gov.cipam.gi.fragments;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import gov.cipam.gi.activities.MapsActivity;
import gov.cipam.gi.R;
import gov.cipam.gi.activities.WebViewActivity;
import gov.cipam.gi.adapters.GiUniquenessListAdapter;
import gov.cipam.gi.adapters.SellerListAdapter;
import gov.cipam.gi.adapters.UniquenessPagerAdapter;
import gov.cipam.gi.database.Database;
import gov.cipam.gi.model.Product;
import gov.cipam.gi.model.Seller;
import gov.cipam.gi.model.Uniqueness;
import gov.cipam.gi.utils.ChromeTabActionBroadcastReceiver;
import gov.cipam.gi.utils.CommonUtils;
import gov.cipam.gi.utils.CustomTabActivityHelper;
import gov.cipam.gi.utils.PaletteGenerate;
import gov.cipam.gi.utils.StartSnapHelper;
import gov.cipam.gi.utils.WrapContentHeightViewPager;

import static gov.cipam.gi.utils.Constants.EXTRA_URL;

/**
 * Created by karan on 12/14/2017.
 */

public class ProductDetailFragment extends Fragment implements SellerListAdapter.setOnSellerClickListener
,GiUniquenessListAdapter.setOnItemClickListener,ViewPager.OnPageChangeListener,View.OnClickListener{
//    Seller seller;
//    SellerFirebaseAdapter sellerFirebaseAdapter;
//    DatabaseReference mDatabaseReference;
    int page_position = 0;
    TextView txtvTitleHistory,txtvTitleDesc;
    LinearLayout historyLinearLayout,descLinearLayout;
    String name,address,contact;
    TextView txtState,txtCategory,txtHistory,txtDesc;
    Double lon,lat;
    LinearLayout dotsLinearLayout;
    WrapContentHeightViewPager viewPager;
    private TextView[] dots;
    RecyclerView rvSeller;
    ArrayList<Seller> sellerList;
    ArrayList<Uniqueness> uniquenessList;
    Database databaseInstance;
    SQLiteDatabase database;
    ImageView imageView;
    StartSnapHelper startSnapHelper;
    PaletteGenerate paletteGenerate;
    Product product;
    Toolbar toolbar;
    boolean isImagePreLoaded = false;
    public static Bitmap mBitmap;

    //TTS object
    private TextToSpeech myTTS;
    //status check code
    private int MY_DATA_CHECK_CODE = 0;
    //sample string
    String hiss;
    int fabCheck = 0;


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
        //toolbar=((AppCompatActivity) getActivity()).findViewById(R.id.product_activity_toolbar);
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
            case R.id.action_tts:
                /**Define TTS action here*/
                break;
        }
        return true;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvSeller = view.findViewById(R.id.seller_recycler_view);
        imageView = view.findViewById(R.id.productDetailImage);
        txtState = view.findViewById(R.id.detail_stateName);
        txtCategory = view.findViewById(R.id.detail_categoryName);
        viewPager = view.findViewById(R.id.vp_slider);
        dotsLinearLayout = view.findViewById(R.id.ll_dots);
        toolbar=getActivity().findViewById(R.id.product_activity_toolbar);

        historyLinearLayout = view.findViewById(R.id.childHistoryCard);
        descLinearLayout = view.findViewById(R.id.childDescCard);

        txtvTitleHistory = historyLinearLayout.findViewById(R.id.headingText);
        txtvTitleHistory.setText("History");
        txtHistory = historyLinearLayout.findViewById(R.id.descText);

        txtvTitleDesc = descLinearLayout.findViewById(R.id.headingText);
        txtvTitleDesc.setText("Description");
        txtDesc = descLinearLayout.findViewById(R.id.descText);

        setData();

        addBottomDots(0);
        //setAutoScroll();

        rvSeller.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        if(mBitmap!=null) {
            CommonUtils.loadImageFromBitmap(imageView, mBitmap, getActivity());
        }
        else{
            CommonUtils.loadImageFromURL(imageView,product.getDpurl(),getActivity());
        }
        rvSeller.setAdapter(new SellerListAdapter(getContext(), sellerList, this));
        viewPager.setAdapter(new UniquenessPagerAdapter(uniquenessList,getActivity()));
        viewPager.setOnPageChangeListener(this);
    }

    private void openCustomChromeTab(Uri uri) {
        CustomTabsIntent.Builder intentBuilder = new CustomTabsIntent.Builder();

        intentBuilder.setToolbarColor(getResources().getColor(R.color.colorPrimary));
        intentBuilder.setActionButton(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher), "Action Button", createPendingIntent(ChromeTabActionBroadcastReceiver.ACTION_ACTION_BUTTON));
        CustomTabsIntent customTabsIntent = intentBuilder.build();
        CustomTabActivityHelper.openCustomTab(getActivity(), customTabsIntent, uri, new CustomTabActivityHelper.CustomTabFallback() {
            @Override
            public void openUri(Activity activity, Uri uri) {
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

        //toolbar.setTitle(product.getName());
        //toolbar.setSubtitle(product.getState()+" " +product.getCategory());

        /*Uniqueness uniqueness=new Uniqueness("Achha chalta hun duao mein yaad rakhna");
        uniquenessList.add(uniqueness);

        uniqueness=new Uniqueness("Achha chalta hun duao mein yaad rakhna");
        uniquenessList.add(uniqueness);

        uniqueness=new Uniqueness("Achha chalta hun duao mein yaad rakhna");
        uniquenessList.add(uniqueness);

        uniqueness=new Uniqueness("Achha chalta hun duao mein yaad rakhna");
        uniquenessList.add(uniqueness);

        uniqueness=new Uniqueness("Achha chalta hun duao mein yaad rakhna");
        uniquenessList.add(uniqueness);*/

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

    private void setAutoScroll(){
        final Handler handler = new Handler();

        final Runnable update = new Runnable() {
            public void run() {
                if (page_position == uniquenessList.size()) {
                    page_position = 0;
                } else {
                    page_position = page_position + 1;
                }
                viewPager.setCurrentItem(page_position, true);
            }
        };

        new Timer().schedule(new TimerTask() {

            @Override
            public void run() {
                handler.post(update);
            }
        }, 100, 5000);
    }

    private void addBottomDots(int currentPage) {
        dots = new TextView[uniquenessList.size()];

        dotsLinearLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(getActivity());
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(Color.parseColor("#3897f0"));
            dotsLinearLayout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(Color.parseColor("#d3d3d3"));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    @Override
    public void onItemClicked(View v, int Position) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        addBottomDots(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().setTitle(product.getName());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

        }
    }
}
