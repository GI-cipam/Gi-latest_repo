package gov.cipam.gi.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import gov.cipam.gi.R;
import gov.cipam.gi.database.Database;
import gov.cipam.gi.model.Product;
import gov.cipam.gi.model.Seller;

/**
 * Created by karan on 11/20/2017.
 */

public class MapsFragment extends Fragment implements OnMapReadyCallback {

    MapView mapView;
    GoogleMap map;
    ArrayList<Seller> selectedSellerList;
    ArrayList<Seller> allSellerList=new ArrayList<>();

    Database databaseInstance;
    SQLiteDatabase database;
    public static boolean hasSpecializedList=false;

    public static MapsFragment newInstance(ArrayList<Seller> seletedSellerList) {

        Bundle args = new Bundle();
        if(seletedSellerList!=null){
            args.putSerializable("selectedSellerList",seletedSellerList);
        }
        else{
            args.putSerializable("selectedSellerList",null);
        }
        MapsFragment fragment = new MapsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        databaseInstance = new Database(getContext());
        database = databaseInstance.getReadableDatabase();
        SupportMapFragment mapFragment1=(SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment1.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
    map=googleMap;
        inflateSellerListFromDB();
        addMarkerForAllSeller();

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        }else{
            map.setMyLocationEnabled(true);// Write you code here if permission already given.
            selectedSellerList=(ArrayList<Seller>) getArguments().getSerializable("selectedSellerList");
            if(selectedSellerList!=null && selectedSellerList.size()>0) {
                Toast.makeText(getContext(),selectedSellerList.size()+"", Toast.LENGTH_SHORT).show();
                addMarkerForSelectedSeller();
            }
            else{
                Toast.makeText(getContext(), "No List provided", Toast.LENGTH_SHORT).show();

            }
        }
    }

    private void addMarkerForSelectedSeller() {
        for(int sellerIndex=0;sellerIndex<selectedSellerList.size();sellerIndex++){
            final Seller currentSeller=selectedSellerList.get(sellerIndex);
            final LatLng currentLatLng=new LatLng(currentSeller.getlat(),currentSeller.getlon());
            Product currentProduct=getProduct(currentSeller.getUid());

//            Target target=new Target() {
//                @Override
//                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
//                    Marker marker=map.addMarker(new MarkerOptions()
//                            .position(currentLatLng)
//                            .title(currentSeller.getName())
//                            .snippet(currentSeller.getaddress())
//                            .icon(BitmapDescriptorFactory.fromBitmap(bitmap)));
//                    marker.setTag(currentSeller);
//                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng,10));
//                }
//
//                @Override
//                public void onBitmapFailed(Drawable errorDrawable) {
//                    Marker marker=map.addMarker(new MarkerOptions()
//                            .position(currentLatLng)
//                            .title(currentSeller.getName())
//                            .snippet(currentSeller.getaddress())
//                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));
//                    marker.setTag(currentSeller);
//                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng,10));
//                }
//
//                @Override
//                public void onPrepareLoad(Drawable placeHolderDrawable) {
//
//                }
//            };
//            Picasso.with(getContext()).load(currentProduct.getDpurl()).into(target);

            Marker marker=map.addMarker(new MarkerOptions()
                    .position(currentLatLng)
                    .title(currentSeller.getName())
                    .snippet(currentSeller.getaddress())
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));
            marker.setTag(currentSeller);
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng,10));

        }
    }

    private Product getProduct(String uidToSearch) {
    Product product;
    String s[]={uidToSearch};
//       Cursor cursor=database.query(Database.GI_PRODUCT_TABLE,null,Database.GI_PRODUCT_UID +"=?",s,null,null,null);
        Cursor cursor=database.query(Database.GI_PRODUCT_TABLE,null,null,null,null,null,null);

        cursor.moveToNext();
            String name,detail,category,state,dpurl,uid,history,description;

            name=cursor.getString(cursor.getColumnIndex(Database.GI_PRODUCT_NAME));
            detail=cursor.getString(cursor.getColumnIndex(Database.GI_PRODUCT_DETAIL));
            category=cursor.getString(cursor.getColumnIndex(Database.GI_PRODUCT_CATEGORY));
            state=cursor.getString(cursor.getColumnIndex(Database.GI_PRODUCT_STATE));
            dpurl=cursor.getString(cursor.getColumnIndex(Database.GI_PRODUCT_DP_URL));
            uid=cursor.getString(cursor.getColumnIndex(Database.GI_PRODUCT_UID));

            history=cursor.getString(cursor.getColumnIndex(Database.GI_PRODUCT_HISTORY));
            description=cursor.getString(cursor.getColumnIndex(Database.GI_PRODUCT_DESCRIPTION));

            product = new Product(name,dpurl,detail,category,state,description,history,uid);
        cursor.close();
            return product;


    }

    private void addMarkerForAllSeller() {
        for(Seller currentSeller : allSellerList){
            LatLng currentLatLng=new LatLng(currentSeller.getlat(),currentSeller.getlon());
            Marker marker=map.addMarker(new MarkerOptions().position(currentLatLng).title(currentSeller.getName()).snippet(currentSeller.getaddress()));
            marker.setTag(currentSeller);
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng,6));
        }
    }

    private void inflateSellerListFromDB() {
        Cursor sellerCursor=database.query(Database.GI_SELLER_TABLE,null,null,null,null,null,null,null);
        while(sellerCursor.moveToNext()){
            String name,address,contact;
            Double lon,lat;

            name=sellerCursor.getString(sellerCursor.getColumnIndex(Database.GI_SELLER_NAME));
            address=sellerCursor.getString(sellerCursor.getColumnIndex(Database.GI_SELLER_ADDRESS));
            contact=sellerCursor.getString(sellerCursor.getColumnIndex(Database.GI_SELLER_CONTACT));
            lat=sellerCursor.getDouble(sellerCursor.getColumnIndex(Database.GI_SELLER_LAT));
            lon=sellerCursor.getDouble(sellerCursor.getColumnIndex(Database.GI_SELLER_LON));

            Seller oneSeller = new Seller(name, address, contact, lon, lat);

            allSellerList.add(oneSeller);
        }
        sellerCursor.close();
    }
}
