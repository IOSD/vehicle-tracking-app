package com.rohan.vehicletrackingapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.github.anastr.speedviewlib.SpeedView;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Timer;
import java.util.TimerTask;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Location prevLocaton=null;
    locdata prelocdata = new locdata();
    LocationManager mLocationManager;
    TextView mTextView;
    Timer timer;
    SpeedView speedometer;
    String newString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        mTextView=findViewById(R.id.Speed);

        //String newString;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                newString= null;
            } else {
                newString= extras.getString("Speed");
            }
        } else {
            newString= (String) savedInstanceState.getSerializable("Speed");
        }

        Log.d("Vehicle",""+newString);
        mTextView.setText("Your Speed Limit is "+newString+"kmph");

         speedometer= findViewById(R.id.speedView);
        mLocationManager=(LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                if (ActivityCompat.checkSelfPermission(getApplicationContext()
                        , android.Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(),
                        android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    ActivityCompat.requestPermissions(MapsActivity.this, new String[]{android.Manifest.permission.
                                    ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                            101);
                    return;
                }
                mMap.setMyLocationEnabled(true);
                Log.d("vehicle","On Map ready");



                MyLocation.LocationResult lr=new MyLocation.LocationResult()
                {

                    @Override
                    public void gotLocation(final Location location) {

                        LatLng myLaLn = new LatLng(location.getLatitude(), location.getLongitude());

                       final CameraPosition camPos = new CameraPosition.Builder().target(myLaLn)
                                .zoom(17)
                                .bearing(0)
                                .tilt(30)
                                .build();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                CameraUpdate camUpd3 = CameraUpdateFactory.newCameraPosition(camPos);
                                mMap.animateCamera(camUpd3);
                            }
                        });

                                                runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                double Speed=getSpeed(location);
                                 int nSpeed=(
                                        int)Speed;
                                //mTextView.setText(""+nSpeed);

                                speedometer.setSpeedAt(nSpeed);
                                if(nSpeed > Integer.parseInt(newString)) {

                                    Toast.makeText(getApplicationContext(),"CAUTION YOU HAVE EXCEEDED YOUR SPEED LIMIT",Toast.LENGTH_SHORT).show();
                                    // Get instance of Vibrator from current Context
                                    Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

                                   // Vibrate for 400 milliseconds
                                    v.vibrate(1000);
                                }
                                Log.d("Vehicle App","OnLocation Changed");

                            }
                        });
                    }

                };
                MyLocation myLocation = new MyLocation();
                myLocation.getLocation(getApplicationContext(), lr);

    }

    /**
     * Gets distance in meters, coordinates in RADIAN
     */
    class locdata{
        long mtime;
        double latitude;
        double longitude;
    }
    private double getSpeed(Location location){
        double speed=0;

        Location currLoc = location;
        locdata data = new locdata();
        data.latitude = currLoc.getLatitude();
        data.longitude = currLoc.getLongitude();
        data.mtime = currLoc.getTime();



        Log.d("vehicle", "prelocdata.latitude" + prelocdata.latitude);
        Log.d("vehicle", "prelocdata.longitude" + prelocdata.longitude);
        Log.d("vehicle", "data.latitude" + data.latitude);
        Log.d("vehicle", "data.longitude" + data.longitude);
        Log.d("vehicle", "prelocdata.mtime" + prelocdata.mtime);
        Log.d("vehicle", "data.mtime" + data.mtime);
        if (prelocdata.mtime != 0){
            double distance = getDistance(prelocdata.latitude, prelocdata.longitude,
                    data.latitude, data.longitude);
            speed = (distance / (data.mtime - prelocdata.mtime)) * 1000;
            //mTextView.setText("" + speed);
            Log.d("vehicle", "SPEED==" + speed);
        }
        prelocdata.mtime=currLoc.getTime();
        prelocdata.latitude=currLoc.getLatitude();
        prelocdata.longitude=currLoc.getLongitude();
        Log.d("vehicle", "SPEED==" + speed);
         if(speed>=100) {

             return speed / 10;
         }
         else
         {
             return speed;
         }
    }

    private static double getDistance(double lat1, double lon1, double lat2, double lon2) {
        double R = 6371000; // for haversine use R = 6372.8 km instead of 6371 km
        double dLat = lat2 - lat1;
        double dLon = lon2 - lon1;
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(lat1) * Math.cos(lat2) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        //double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        return 2 * R * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        Log.d("vehicle","OnrequestPermissionsUP");
        if (grantResults.length > 0 && grantResults[0]==PackageManager.PERMISSION_GRANTED) {
                 Log.d("vehicle","OnrequestPermissions");
        }
        else{


            Log.d("Vehicle","Location Permission denied");
        }

    }
}
