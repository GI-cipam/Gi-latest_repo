package gov.cipam.gi.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import gov.cipam.gi.R;
import gov.cipam.gi.activities.ProductListActivity;
import gov.cipam.gi.adapters.MapsSellerAdapter;
import gov.cipam.gi.database.Database;
import gov.cipam.gi.model.Product;
import gov.cipam.gi.model.Seller;
import gov.cipam.gi.model.SellerAndView;
import gov.cipam.gi.utils.Constants;

/**
 * Created by karan on 11/20/2017.
 */

public class MapsFragment extends Fragment implements OnMapReadyCallback
        , GoogleMap.OnInfoWindowClickListener
        , MapsSellerAdapter.setOnMapSellerClickedListener
,View.OnClickListener{

    MapView mapView;
    FloatingActionButton fabBottomSheet;
    GoogleMap map;
    RecyclerView rvSellers;
    LinearLayout layoutBottomSheet;
    ArrayList<Seller> selectedSellerList;
    ArrayList<Seller> allSellerList = new ArrayList<>();
    BottomSheetBehavior sheetBehavior;
    Database databaseInstance;
    SQLiteDatabase database;
    public static boolean hasSpecializedList = false;

    public static MapsFragment newInstance(ArrayList<Seller> seletedSellerList) {

        Bundle args = new Bundle();
        if (seletedSellerList != null) {
            args.putSerializable("selectedSellerList", seletedSellerList);
        } else {
            args.putSerializable("selectedSellerList", null);
        }
        MapsFragment fragment = new MapsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps_alternate, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        databaseInstance = new Database(getContext());
        database = databaseInstance.getReadableDatabase();
        fabBottomSheet=view.findViewById(R.id.button_bottom_sheet);
        layoutBottomSheet = view.findViewById(R.id.bottom_sheet_layout);
        sheetBehavior = BottomSheetBehavior.from(layoutBottomSheet);
        rvSellers = view.findViewById(R.id.recycler_map_sellers);

        rvSellers.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        rvSellers.setAdapter(new MapsSellerAdapter(this));

        SupportMapFragment mapFragment1 = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment1.getMapAsync(this);

        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED: {
                        fabBottomSheet.setImageResource(R.drawable.ic_expand_more);
                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {
                        fabBottomSheet.setImageResource(R.drawable.ic_expand_less);
                    }
                    break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
        fabBottomSheet.setOnClickListener(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(final Marker marker) {
                TextView sellerNAme, sellerAddress, sellerContact, productName;
                ImageView imageView;
                View itemView = LayoutInflater.from(getContext()).inflate(R.layout.item_marker, null);
                productName = itemView.findViewById(R.id.mapInfoWindow_ProductName);
                sellerNAme = itemView.findViewById(R.id.mapInfoWindow_ProductSellerName);
                sellerAddress = itemView.findViewById(R.id.maoInfoWindow_ProductSellerAddress);
                sellerContact = itemView.findViewById(R.id.mapInfoWindow_ProductSellerContact);

                imageView = itemView.findViewById(R.id.mapInfoWindowImage);

                SellerAndView sellerAndViewIn = (SellerAndView) marker.getTag();
                Seller oneSeller = sellerAndViewIn.getSeller();
                Product product = getProduct(oneSeller.getUid());
//            Toast.makeText(getContext(),oneSeller.getUid(), Toast.LENGTH_SHORT).show();
                Picasso.with(getContext())
                        .load(product.getDpurl())
                        .resize(200, 300).centerCrop()
                        .into(imageView, new Callback() {
                            @Override
                            public void onSuccess() {
                                if (marker.isInfoWindowShown()) {
                                    marker.hideInfoWindow();
                                    marker.showInfoWindow();
                                }
                            }

                            @Override
                            public void onError() {

                            }
                        });
                productName.setText(product.getName());
                sellerNAme.setText(oneSeller.getName());
                sellerAddress.setText(oneSeller.getaddress());
                sellerContact.setText(oneSeller.getcontact());

                SellerAndView sellerAndView = new SellerAndView(oneSeller, itemView);
                marker.setTag(sellerAndView);
                return itemView;
            }
        });
        map.setOnInfoWindowClickListener(this);
        map.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                inflateSellerListFromDB();
                addMarkerForAllSeller();

                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

                } else {
                    map.setMyLocationEnabled(true);// Write you code here if permission already given.
                    selectedSellerList = (ArrayList<Seller>) getArguments().getSerializable("selectedSellerList");
                    if (selectedSellerList != null && selectedSellerList.size() > 0) {
//                        Toast.makeText(getContext(), selectedSellerList.size() + "", Toast.LENGTH_SHORT).show();
                        addMarkerForSelectedSeller();
                    } else {
//                        Toast.makeText(getContext(), "No List provided", Toast.LENGTH_SHORT).show();

                    }
                }
            }
        });
    }

    private void addMarkerForSelectedSeller() {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (int sellerIndex = 0; sellerIndex < selectedSellerList.size(); sellerIndex++) {
            final Seller currentSeller = selectedSellerList.get(sellerIndex);
            final LatLng currentLatLng = new LatLng(currentSeller.getlat(), currentSeller.getlon());
            Product currentProduct = getProduct(currentSeller.getUid());
            Marker marker = map.addMarker(new MarkerOptions()
                    .position(currentLatLng)
                    .title(currentSeller.getName())
                    .snippet(currentSeller.getaddress())
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));
            SellerAndView sellerAndView = new SellerAndView(currentSeller, null);
            marker.setTag(sellerAndView);
            builder.include(marker.getPosition());
        }
        LatLngBounds bounds = builder.build();
        int padding = 20; // offset from edges of the map in pixels
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        map.animateCamera(cu);
    }

    public Product getProduct(String uidToSearch) {
        Product product;
        String s[] = {uidToSearch};
        Cursor cursor = database.query(Database.GI_PRODUCT_TABLE, null, Database.GI_PRODUCT_UID + "=?", s, null, null, null);
//        Cursor cursor=database.query(Database.GI_PRODUCT_TABLE,null,null,null,null,null,null);

        cursor.moveToNext();
        String name, detail, category, state, dpurl, uid, history, description;

        name = cursor.getString(cursor.getColumnIndex(Database.GI_PRODUCT_NAME));
        detail = cursor.getString(cursor.getColumnIndex(Database.GI_PRODUCT_DETAIL));
        category = cursor.getString(cursor.getColumnIndex(Database.GI_PRODUCT_CATEGORY));
        state = cursor.getString(cursor.getColumnIndex(Database.GI_PRODUCT_STATE));
        dpurl = cursor.getString(cursor.getColumnIndex(Database.GI_PRODUCT_DP_URL));
        uid = cursor.getString(cursor.getColumnIndex(Database.GI_PRODUCT_UID));

        history = cursor.getString(cursor.getColumnIndex(Database.GI_PRODUCT_HISTORY));
        description = cursor.getString(cursor.getColumnIndex(Database.GI_PRODUCT_DESCRIPTION));

        product = new Product(name, dpurl, detail, category, state, description, history, uid);
        cursor.close();
        return product;


    }

    private void addMarkerForAllSeller() {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (Seller currentSeller : allSellerList) {
            LatLng currentLatLng = new LatLng(currentSeller.getlat(), currentSeller.getlon());
            Marker marker = map.addMarker(new MarkerOptions().position(currentLatLng).title(currentSeller.getName()).snippet(currentSeller.getaddress()));
            SellerAndView sellerAndView = new SellerAndView(currentSeller, null);
            marker.setTag(sellerAndView);
            builder.include(marker.getPosition());
        }
        LatLngBounds bounds = builder.build();
        int padding = 20; // offset from edges of the map in pixels
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        map.animateCamera(cu);
    }

    private void inflateSellerListFromDB() {
        Cursor sellerCursor = database.query(Database.GI_SELLER_TABLE, null, null, null, null, null, null, null);
        while (sellerCursor.moveToNext()) {
            String name, address, contact, uid;
            Double lon, lat;

            name = sellerCursor.getString(sellerCursor.getColumnIndex(Database.GI_SELLER_NAME));
            address = sellerCursor.getString(sellerCursor.getColumnIndex(Database.GI_SELLER_ADDRESS));
            contact = sellerCursor.getString(sellerCursor.getColumnIndex(Database.GI_SELLER_CONTACT));
            lat = sellerCursor.getDouble(sellerCursor.getColumnIndex(Database.GI_SELLER_LAT));
            lon = sellerCursor.getDouble(sellerCursor.getColumnIndex(Database.GI_SELLER_LON));
            uid = sellerCursor.getString(sellerCursor.getColumnIndex(Database.GI_SELLER_UID));

            Seller oneSeller = new Seller(name, address, contact, lon, lat, uid);

            allSellerList.add(oneSeller);
        }
        sellerCursor.close();
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        SellerAndView sellerAndView = (SellerAndView) marker.getTag();
        Seller oneSeller = sellerAndView.getSeller();
        View infoView = sellerAndView.getView();
        Product product = getProduct(oneSeller.getUid());

        ImageView imageView = infoView.findViewById(R.id.mapInfoWindowImage);

//        Bitmap bitmap=null;
//        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
//        if(drawable!=null) {
//            bitmap = drawable.getBitmap();
//        }
//
//        ProductDetailFragment productDetailFragment=ProductDetailFragment.newInstance(product,bitmap);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            productDetailFragment.setSharedElementEnterTransition(new DetailsTransition());
//            productDetailFragment.setEnterTransition(new Fade());
//            this.setSharedElementEnterTransition(new DetailsTransition());
//            this.setEnterTransition(new Fade());
//            this.setExitTransition(new Fade());
//        }
//
//        MapsFragment mapsFragment=MapsFragment.newInstance(null);
//        getActivity().getSupportFragmentManager()
//                .beginTransaction()
//                .addSharedElement(imageView, "commonImage")
//                .replace(R.id.product_list_frame_layout, productDetailFragment)
//                .addToBackStack(mapsFragment.getClass().getName())
//                .commit();


        Bundle bundle = new Bundle();
        bundle.putSerializable("ppp", product);
        startActivity(new Intent(getContext(), ProductListActivity.class)
                .putExtra(Constants.KEY_TYPE, Database.GI_PRODUCT)
                .putExtras(bundle));
    }

    @Override
    public void onMapSellerClicked(MapsSellerAdapter.MapsSellerViewHolder view, int position) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_bottom_sheet:
                if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    fabBottomSheet.setImageResource(R.drawable.ic_expand_less);
                } else {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    fabBottomSheet.setImageResource(R.drawable.ic_expand_more);
                }
                break;
        }
    }
}
