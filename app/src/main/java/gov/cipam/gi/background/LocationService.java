package gov.cipam.gi.background;

import android.Manifest;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import gov.cipam.gi.DirectionAPI.DirectionFinder;
import gov.cipam.gi.DirectionAPI.DirectionFinderListener;
import gov.cipam.gi.DirectionAPI.Route;
import gov.cipam.gi.activities.HomePageActivity;
import gov.cipam.gi.database.Database;
import gov.cipam.gi.model.Seller;
import gov.cipam.gi.utils.StartSnapHelper;

/**
 * Created by NITANT SOOD on 01-02-2018.
 */

public class LocationService extends IntentService implements DirectionFinderListener {
    LocationManager locationManager;
    LocationListener locationListener;
    LatLng currentLocation,savedLocation;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Double savedLat,savedLon;

    Database databaseInstance;
    SQLiteDatabase database;
    ArrayList<Seller> sellerList=new ArrayList<>();
    ArrayList<Seller> selectedWithinRangeSellers=new ArrayList<>();
    boolean isFirstLocation=true,locationTaken=false;

    public LocationService() {
        super("Location Service");
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        Toast.makeText(this, "Inside Service", Toast.LENGTH_SHORT).show();
        sharedPreferences=getSharedPreferences("abc",MODE_PRIVATE);
        editor = sharedPreferences.edit();

        databaseInstance = new Database(this);
        database = databaseInstance.getReadableDatabase();

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationListener=new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
//                Toast.makeText(LocationService.this, location.getLatitude()+","+location.getLongitude(), Toast.LENGTH_SHORT).show();
                if(!locationTaken){
                    locationTaken=true;
//                    currentLocation=new LatLng(location.getLatitude(),location.getLongitude());
                    currentLocation=new LatLng(28.539512,77.199085);
                    if(!sharedPreferences.contains("long")){
                        Toast.makeText(LocationService.this, "First time Location..", Toast.LENGTH_SHORT).show();
//                        isFirstLocation=false;
                        editor.putString("long",location.getLongitude()+"");
                        editor.putString("lat",location.getLatitude()+"");
                        editor.apply();
                        savedLocation=new LatLng(location.getLatitude(),location.getLongitude());
                    }else{
                        Toast.makeText(LocationService.this, "Not first time location", Toast.LENGTH_SHORT).show();
                        savedLat=Double.parseDouble(sharedPreferences.getString("lat","0.0"));
                        savedLon=Double.parseDouble(sharedPreferences.getString("long","0.0"));
//                        savedLocation=new LatLng(savedLat,savedLon);
                        savedLocation=new LatLng(28.561282,77.207263);
                        checkTravelDistance();
                        editor.putString("long",location.getLongitude()+"");
                        editor.putString("lat",location.getLatitude()+"");
                        editor.apply();
                    }
                }
//                stopSelf();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        if (Build.VERSION.SDK_INT < 23) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
        else
            {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
            }
    }

    private void checkTravelDistance() {
//        Toast.makeText(this,currentLocation.latitude+","+currentLocation.longitude+"..."+savedLocation.latitude+","+savedLocation.longitude, Toast.LENGTH_SHORT).show();
//        Toast.makeText(this, "Inside Distance Checker", Toast.LENGTH_SHORT).show();
        String origin=currentLocation.latitude+","+currentLocation.longitude;
        String destination=savedLocation.latitude+","+savedLocation.longitude;
        try {
            new DirectionFinder(this, origin, destination,true,-1).execute();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStart(@Nullable Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }

    @Override
    public void onDirectionFinderStart() {

    }

    @Override
    public void onDirectionFinderSuccess(List<Route> route, boolean isCheckingDistance, int index) {
        if(isCheckingDistance && route.size()>0){
            //When the distance is  to be checked
            int travelDistance=route.get(0).distance.value;
            if(travelDistance>=(/*HomePageActivity.MIN_NOTIFICATION_DISTANCE*1000*/0)){
                Toast.makeText(this, "Searching nearby Sellers", Toast.LENGTH_SHORT).show();
                searchNearbySellers();
            }
            else{
                // The  distance travelled is less than Min Notification Distance
                stopSelf();
            }
        }
        else{
            //When the nearby sellers are to be searched
            if(route.size()!=0) {
//                Toast.makeText(this, "Seller Distance found", Toast.LENGTH_SHORT).show();
                int distanceFromSellerToLocation = route.get(0).distance.value;
                if (distanceFromSellerToLocation <= HomePageActivity.MIN_NOTIFICATION_SELLER_DISTANCE * 1000) {
                    selectedWithinRangeSellers.add(sellerList.get(index));
                }
            }
            if(index==sellerList.size()-1){
                showNotification();
            }
        }
    }

    private void searchNearbySellers() {
        Cursor sellerCursor=database.query(Database.GI_SELLER_TABLE,null,null,null,null,null,null,null);
        while(sellerCursor.moveToNext()){
            String name,address,contact,uid;
            Double lon,lat;

            name=sellerCursor.getString(sellerCursor.getColumnIndex(Database.GI_SELLER_NAME));
            address=sellerCursor.getString(sellerCursor.getColumnIndex(Database.GI_SELLER_ADDRESS));
            contact=sellerCursor.getString(sellerCursor.getColumnIndex(Database.GI_SELLER_CONTACT));
            lat=sellerCursor.getDouble(sellerCursor.getColumnIndex(Database.GI_SELLER_LAT));
            lon=sellerCursor.getDouble(sellerCursor.getColumnIndex(Database.GI_SELLER_LON));
            uid=sellerCursor.getString(sellerCursor.getColumnIndex(Database.GI_SELLER_UID));

            Seller oneSeller = new Seller(name, address, contact, lon, lat,uid);

            sellerList.add(oneSeller);
        }
        sellerCursor.close();


        for(int sellerIndex=0;sellerIndex<sellerList.size();sellerIndex++){
            Seller oneSeller=sellerList.get(sellerIndex);
            String origin=currentLocation.latitude+","+currentLocation.longitude;
            String destination=oneSeller.getlat()+","+oneSeller.getlon();
            try {
                new DirectionFinder(this, origin, destination,false,sellerIndex).execute();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
//            if(sellerIndex==sellerList.size()-1){
//                showNotification();
//            }
        }


    }

    private void showNotification() {
        Toast.makeText(this, "Size of Selected Sellers :"+selectedWithinRangeSellers.size(), Toast.LENGTH_SHORT).show();
        if(selectedWithinRangeSellers.size()>0){
//            Toast.makeText(this, "Should show notification now !!", Toast.LENGTH_SHORT).show();
            long[] vibrate_patter={0,500,300,800};
            Toast.makeText(this, "Making Notification", Toast.LENGTH_SHORT).show();
            Uri uri= Uri.parse("uri://");
            Uri alarmsound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Notification.Builder mBuilder=new Notification.Builder(this)
                    .setSmallIcon(android.R.drawable.ic_menu_edit)
                    .setContentTitle("Some Nearby Indications")
                    .setContentText("Tap to Know More")
                    .setAutoCancel(true)
                    .setShowWhen(true)
                    .setVibrate(vibrate_patter)
                    .setSound(alarmsound)
                    .setLights(Color.MAGENTA,300,1000);

            Intent resultIntent=new Intent(this,HomePageActivity.class);
            resultIntent.putExtra("selectedSellerList",selectedWithinRangeSellers);
            resultIntent.putExtra("isFromNotification",true);
            PendingIntent resultPendingIntent=PendingIntent.getActivity(this,1,resultIntent,PendingIntent.FLAG_ONE_SHOT);
//            mBuilder.addAction(android.R.drawable.dialog_frame,"View",resultPendingIntent);
            mBuilder.setContentIntent(resultPendingIntent);
            NotificationManager mNotificationManager=(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(1,mBuilder.build());
        }
    }
}
