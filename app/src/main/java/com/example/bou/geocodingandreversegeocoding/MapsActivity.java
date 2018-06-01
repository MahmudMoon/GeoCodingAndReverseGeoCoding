package com.example.bou.geocodingandreversegeocoding;

import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    EditText editText,editText1,editText2;
    ImageButton btn,button1;
    String location;
    double latitude,longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        latitude = 34;
        longitude = 151;
        editText = (EditText) findViewById(R.id.editText);
        btn = (ImageButton)findViewById(R.id.button);

        editText1 = (EditText) findViewById(R.id.editText1);
        editText2 = (EditText) findViewById(R.id.editText2);
        button1 = (ImageButton)findViewById(R.id.button1);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
          LatLng sydney = new LatLng(latitude, longitude);
          Log.d("Lat", String.valueOf(latitude));
          Log.d("Lon", String.valueOf(longitude));
            mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
             mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAG","Clicked");
                location = editText.getText().toString();
                editText.setText("");
                Geocoder geocoder = new Geocoder(getApplicationContext());
                try {
                    List<Address> fromLocationName = geocoder.getFromLocationName( location, 1);
                    Address res = fromLocationName.get(0);
                    latitude = res.getLatitude();
                    longitude = res.getLongitude();

                    LatLng sydney = new LatLng(latitude, longitude);
                    Log.d("Lat", String.valueOf(latitude));
                    Log.d("Lon", String.valueOf(longitude));
                    mMap.addMarker(new MarkerOptions().position(sydney).title(location));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,10));


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });


        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String lat_Str = editText1.getText().toString();
                String lon_Str = editText2.getText().toString();

                double lat = Double.parseDouble(lat_Str);
                double lon = Double.parseDouble(lon_Str);

                Geocoder geocoder = new Geocoder(getApplicationContext());
                LatLng latLng = new LatLng(lat,lon);
                try {
                    List<Address> fromLocation = geocoder.getFromLocation(lat, lon, 1);
                    Address add = fromLocation.get(0);
                    Log.d("Address",add.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static class Task extends AsyncTask<String,String,LatLng>
    {
        @Override
        protected LatLng doInBackground(String... strings) {
            Log.d("TAG","Starting Background");
            String endPoint = strings[0];
            try {
                URL url = new URL("https://maps.googleapis.com/maps/api/geocode/json?address=1600+Amphitheatre+Parkway,+Mountain+View,+CA&key=AIzaSyCuE3l9cRfx1cSRUBWyeDB9-4C7r-IMAO0");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.connect();
                InputStream inputStream = httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                StringBuffer stringBuffer = new StringBuffer();
                String line;
                while((line = bufferedReader.readLine())!=null){
                    stringBuffer.append(line);
                }
                String finalSting  = stringBuffer.toString();
                Log.d("TAG_value", String.valueOf(finalSting.length()));
                return null;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(LatLng latLng) {
            super.onPostExecute(latLng);
        }
    }
}
