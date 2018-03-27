package gov.cipam.gi.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.widget.BottomSheetBehavior;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ms.square.android.expandabletextview.ExpandableTextView;

import java.util.ArrayList;
import java.util.Locale;

import gov.cipam.gi.R;
import gov.cipam.gi.activities.MapsActivity;
import gov.cipam.gi.activities.WebViewActivity;
import gov.cipam.gi.adapters.GiUniquenessAdapter;
import gov.cipam.gi.adapters.SellerListAdapter;
import gov.cipam.gi.database.Database;
import gov.cipam.gi.model.Product;
import gov.cipam.gi.model.Seller;
import gov.cipam.gi.model.Uniqueness;
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
        , ViewPager.OnPageChangeListener
        , View.OnClickListener
        , View.OnLongClickListener
        , TextToSpeech.OnInitListener {

    int page_position = 0, MY_DATA_CHECK_CODE = 0, micHistoryFlag = 0, micDescriptionFlag = 0;
    float pitch, speed;
    String name, address, contact;
    boolean isImagePreLoaded = false;
    Double lon, lat;

    SharedPreferences sharedPreferences;
    ExpandableTextView etvHistory, etvDesc;
    LinearLayout dotsLinearLayout;
    RelativeLayout historyLinearLayout, descLinearLayout, bottomSheetTTS;
    TextView titleHistoryTv, titleDescTv, dots[];
    WrapContentHeightViewPager viewPager;
    RecyclerView rvSeller;
    ArrayList<Seller> sellerList;
    ArrayList<Uniqueness> uniquenessList;
    Database databaseInstance;
    SQLiteDatabase database;
    ImageView imageView;
    StartSnapHelper startSnapHelper;
    PaletteGenerate paletteGenerate;
    Product product;
    ImageButton micHistoryButton, micDescriptionButton, closeBottomSheetBtn;
    private TextToSpeech myTTS;

    BottomSheetBehavior sheetBehavior;
    public static Bitmap mBitmap;

    public ProductDetailFragment() {
    }

    public static ProductDetailFragment newInstance(Product product, Bitmap bitmap) {

        Bundle args = new Bundle();
        args.putSerializable("nalin", product);
        ProductDetailFragment fragment = new ProductDetailFragment();
        fragment.setArguments(args);
        mBitmap = bitmap;
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        sellerList = new ArrayList<>();
        uniquenessList = new ArrayList<>();
        databaseInstance = new Database(getContext());
        startSnapHelper = new StartSnapHelper();
        sharedPreferences = getContext().getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        database = databaseInstance.getReadableDatabase();
        paletteGenerate = new PaletteGenerate();
        populateSellerListFromDB();
        populateUniquenessList1fromDB();
        setHasOptionsMenu(true);

        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Intent checkTTSIntent = new Intent();
        checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkTTSIntent, MY_DATA_CHECK_CODE);
        return inflater.inflate(R.layout.fragment_product_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        rvSeller = view.findViewById(R.id.seller_recycler_view);
        imageView = view.findViewById(R.id.productDetailImage);
        viewPager = view.findViewById(R.id.vp_slider);
        dotsLinearLayout = view.findViewById(R.id.ll_dots);
        historyLinearLayout = view.findViewById(R.id.child_history_layout);
        descLinearLayout = view.findViewById(R.id.child_desc_layout);
        titleHistoryTv = historyLinearLayout.findViewById(R.id.headingText);
        titleHistoryTv.setText("History");
        etvHistory = historyLinearLayout.findViewById(R.id.expand_text_view);
        bottomSheetTTS = view.findViewById(R.id.bottom_sheet_tts);
        sheetBehavior = BottomSheetBehavior.from(bottomSheetTTS);
        closeBottomSheetBtn = view.findViewById(R.id.button_close_bottom_sheet);
        titleDescTv = descLinearLayout.findViewById(R.id.headingText);
        titleDescTv.setText("Description");
        etvDesc = descLinearLayout.findViewById(R.id.expand_text_view);
        micHistoryButton = historyLinearLayout.findViewById(R.id.tts_on_off);
        micHistoryButton.setOnClickListener(view1 ->

        {
            micHistoryTTS();

        });
        micDescriptionButton = descLinearLayout.findViewById(R.id.tts_on_off);
        setData();
        micDescriptionButton.setOnClickListener(view12 ->

        {
            micDescriptionTTS();
        });

        addBottomDots(0);

        rvSeller.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rvSeller.setAdapter(new SellerListAdapter(sellerList, this));
        viewPager.setAdapter(new GiUniquenessAdapter(uniquenessList, getActivity()));

        if (mBitmap != null) {
            CommonUtils.loadImageFromBitmap(imageView, mBitmap, getActivity());
        } else {
            CommonUtils.loadImageFromURL(imageView, product.getDpurl(), getActivity());
        }

        closeBottomSheetBtn.setOnClickListener(this);
        viewPager.setOnPageChangeListener(this);
        imageView.setOnLongClickListener(this);
//        if(sellerList.size()==0){
//            sellerCard.setVisibility(View.INVISIBLE);
//        }
//        if(uniquenessList.size()==0){
//            uniquenessCard.setVisibility(View.INVISIBLE);
//        }
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem settingsAction = menu.findItem(R.id.action_settings_product_list);
        settingsAction.setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                getActivity().getSupportFragmentManager()
                        .popBackStackImmediate();
                break;
            case R.id.action_location:
                startActivity(new Intent(getContext(), MapsActivity.class)
                        .putExtra("latitude", lat)
                        .putExtra("longitude", lon)
                        .putExtra("address", address));
                break;

            case R.id.action_url:
                String url = "https://google.com";
                Uri uri = Uri.parse(url);
                openCustomChromeTab(uri);
                break;
        }
        return true;
    }

    @Override
    public void onInit(int initStatus) {
        //check for successful instantiation
        pitch = sharedPreferences.getFloat("pitch", 5);
        speed = sharedPreferences.getFloat("speed", 5);
        if (initStatus == TextToSpeech.SUCCESS) {
            if (myTTS.isLanguageAvailable(Locale.US) == TextToSpeech.LANG_AVAILABLE) {
                myTTS.setSpeechRate(speed);
                myTTS.setPitch(pitch);
                myTTS.setLanguage(Locale.US);

            } else if (initStatus == TextToSpeech.ERROR) {
                Toast.makeText(getContext(), "Sorry! Text To Speech failed...", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void populateSellerListFromDB() {

        Bundle b = getArguments();
        product = (Product) b.get("nalin");
        String[] s = {product.getUid()};
        Cursor sellerCursor = database.query(Database.GI_SELLER_TABLE, null, Database.GI_SELLER_UID + "=?", s, null, null, null, null);
        while (sellerCursor.moveToNext()) {

            name = sellerCursor.getString(sellerCursor.getColumnIndex(Database.GI_SELLER_NAME));
            address = sellerCursor.getString(sellerCursor.getColumnIndex(Database.GI_SELLER_ADDRESS));
            contact = sellerCursor.getString(sellerCursor.getColumnIndex(Database.GI_SELLER_CONTACT));
            lat = sellerCursor.getDouble(sellerCursor.getColumnIndex(Database.GI_SELLER_LAT));
            lon = sellerCursor.getDouble(sellerCursor.getColumnIndex(Database.GI_SELLER_LON));

            Seller oneSeller = new Seller(name, address, contact, lon, lat);

            sellerList.add(oneSeller);
        }
        sellerCursor.close();
    }


    private void populateUniquenessList1fromDB() {
        String[] s = {product.getUid()};
        Cursor uniquenessCursor = database.query(Database.GI_UNIQUENESS_TABLE, null, Database.GI_UNIQUENESS_UID + "=?", s, null, null, null, null);
        while (uniquenessCursor.moveToNext()) {
            String oneUniqueInfo = uniquenessCursor.getString(uniquenessCursor.getColumnIndex(Database.GI_UNIQUENESS_VALUE));
            Uniqueness uniqueness = new Uniqueness(oneUniqueInfo);
            uniquenessList.add(uniqueness);
        }
        uniquenessCursor.close();
    }


    //speak the user text
    private void speakWords(String speech) {
        //speak straight away
        myTTS.speak(speech, TextToSpeech.QUEUE_FLUSH, null);
    }

    private void openCustomChromeTab(Uri uri) {
        CustomTabsIntent.Builder intentBuilder = new CustomTabsIntent.Builder();

        intentBuilder.setToolbarColor(getResources().getColor(R.color.colorPrimary));
        intentBuilder.setShowTitle(true);
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

    private void setData() {
        etvHistory.setText(product.getHistory());
        etvDesc.setText(product.getDescription());
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
    }

    /*private void setAutoScroll() {
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
    }*/

    private void addBottomDots(int currentPage) {
        dots = new TextView[uniquenessList.size()];

        dotsLinearLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(getActivity());
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(25);
            dots[i].setTextColor(Color.parseColor("#3897f0"));
            dotsLinearLayout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(Color.parseColor("#d3d3d3"));
    }

    @Override
    public void onDestroy() {
        myTTS.shutdown();
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        myTTS.shutdown();
        super.onDestroyView();
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
        switch (v.getId()) {
            case R.id.button_close_bottom_sheet:
                sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == MY_DATA_CHECK_CODE) {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                //the user has the necessary data - create the TTS
                myTTS = new TextToSpeech(getContext(), this);
            } else {
                //no data - install it now
                Intent installTTSIntent = new Intent();
                installTTSIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installTTSIntent);
            }
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (v == imageView) {
            if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            } else {
                sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        }
        return false;
    }


    private void micHistoryTTS() {
        if (micHistoryFlag == 0) {
            myTTS.stop();
            micHistoryButton.setImageResource(R.drawable.microphone_off_green);
            micHistoryFlag = 1;
            speakWords(product.getHistory());
            micDescriptionButton.setImageResource(R.drawable.ic_menu_microphone_green);
            micDescriptionFlag = 0;
        } else {
            micHistoryButton.setImageResource(R.drawable.ic_menu_microphone_green);
            micHistoryFlag = 0;
            myTTS.stop();
        }
    }

    private void micDescriptionTTS() {
        if (micDescriptionFlag == 0) {
            myTTS.stop();
            micDescriptionButton.setImageResource(R.drawable.microphone_off_green);
            speakWords(product.getDescription());
            micDescriptionFlag = 1;
            micHistoryButton.setImageResource(R.drawable.ic_menu_microphone_green);
            micHistoryFlag = 0;
        } else {
            micDescriptionButton.setImageResource(R.drawable.ic_menu_microphone_green);
            micDescriptionFlag = 0;

        }
    }
}
