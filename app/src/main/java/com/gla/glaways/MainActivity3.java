package com.gla.glaways;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class MainActivity3 extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private LatLng destinationLatLng;
    private String destinationBlock;
    private TextView tvDistance, tvActiveBlock;
    private Polyline currentPolyline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode(androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitymain3);

        tvDistance = findViewById(R.id.tvDistance);
        tvActiveBlock = findViewById(R.id.tvActiveBlock);

        destinationBlock = getIntent().getStringExtra("BLOCK_NAME");
        double lat = getIntent().getDoubleExtra("LATITUDE", 0.0);
        double lng = getIntent().getDoubleExtra("LONGITUDE", 0.0);

        if (destinationBlock == null) destinationBlock = "GLA CAMPUS";
        tvActiveBlock.setText("NAVIGATING TO: " + destinationBlock.toUpperCase());

        if (lat != 0.0 && lng != 0.0) {
            destinationLatLng = new LatLng(lat, lng);
        } else {
            setDestinationCoordinates(destinationBlock);
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment);
        if (mapFragment != null) mapFragment.getMapAsync(this);

        // UI Buttons
        findViewById(R.id.btnExitNav).setOnClickListener(v -> finish());

        findViewById(R.id.btnZoomIn).setOnClickListener(v -> {
            if (mMap != null) mMap.animateCamera(CameraUpdateFactory.zoomIn());
        });

        findViewById(R.id.btnChangeMapType).setOnClickListener(v -> {
            if (mMap != null) {
                int currentType = mMap.getMapType();
                // ✅ Hybrid use kiya hai taaki satellite mein bhi blocks ke naam dikhein
                if (currentType == GoogleMap.MAP_TYPE_NORMAL) {
                    mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                    Toast.makeText(this, "Satellite View with Labels", Toast.LENGTH_SHORT).show();
                } else {
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    Toast.makeText(this, "Normal View", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setDestinationCoordinates(String block) {
        switch (block) {
            case "Block 1": destinationLatLng = new LatLng(27.605474, 77.592918); break;
            case "Block 2": destinationLatLng = new LatLng(27.606035, 77.592983); break;
            case "Block 3": destinationLatLng = new LatLng(27.606138, 77.593153); break;
            case "Block 4": destinationLatLng = new LatLng(27.606434, 77.595150); break;
            case "Block 5": destinationLatLng = new LatLng(27.605416, 77.595307); break;
            case "Block 6": destinationLatLng = new LatLng(27.604633, 77.595793); break;
            case "Block 7": destinationLatLng = new LatLng(27.606606, 77.595573); break;
            case "Block 8": destinationLatLng = new LatLng(27.606232, 77.595882); break;
            case "Block 9": destinationLatLng = new LatLng(27.603303, 77.595334); break;
            case "Block 10": destinationLatLng = new LatLng(27.603595, 77.595823); break;
            case "Block 11": destinationLatLng = new LatLng(27.603440, 77.595560); break;
            case "Block 12": destinationLatLng = new LatLng(27.601829, 77.597343); break;
            default: destinationLatLng = new LatLng(27.605474, 77.592918); break;
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        // Marker add karo
        mMap.addMarker(new MarkerOptions().position(destinationLatLng).title(destinationBlock));

        // Direct route draw aur camera set karo
        drawRoadRoute();
    }

    private void drawRoadRoute() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
                if (location != null) {
                    LatLng origin = new LatLng(location.getLatitude(), location.getLongitude());

                    // ✅ FIX: World Map problem solve karne ke liye dono points ko bounds mein dala
                    LatLngBounds bounds = new LatLngBounds.Builder()
                            .include(origin)
                            .include(destinationLatLng)
                            .build();

                    // Thoda padding (150) taaki markers screen ke corners mein na chhup jayein
                    mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 150));

                    new FetchRouteTask().execute(getDirectionsUrl(origin, destinationLatLng));
                } else {
                    // Agar location nahi mili toh sirf destination par zoom
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(destinationLatLng, 17f));
                }
            });
        }
    }

    private String getDirectionsUrl(LatLng origin, LatLng dest) {
        // Mode WALKING rakha hai taaki highway divider ke u-turn se bachein
        return "https://maps.googleapis.com/maps/api/directions/json?" +
                "origin=" + origin.latitude + "," + origin.longitude +
                "&destination=" + dest.latitude + "," + dest.longitude +
                "&mode=walking" +
                "&key=AIzaSyA7yqc1LISGZaZHnZHzXLFCaO6_5S5JnZc";
    }

    private class FetchRouteTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... url) {
            try {
                HttpURLConnection connection = (HttpURLConnection) new URL(url[0]).openConnection();
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) sb.append(line);
                return sb.toString();
            } catch (Exception e) { return null; }
        }

        @Override
        protected void onPostExecute(String result) {
            if (result == null) return;
            try {
                JSONObject jsonResponse = new JSONObject(result);
                if (jsonResponse.getString("status").equals("OK")) {
                    JSONObject route = jsonResponse.getJSONArray("routes").getJSONObject(0);
                    tvDistance.setText("Distance: " + route.getJSONArray("legs").getJSONObject(0).getJSONObject("distance").getString("text"));

                    List<LatLng> decodedPath = PolyUtil.decode(route.getJSONObject("overview_polyline").getString("points"));
                    if (currentPolyline != null) currentPolyline.remove();

                    currentPolyline = mMap.addPolyline(new PolylineOptions()
                            .addAll(decodedPath)
                            .width(14f)
                            .color(Color.parseColor("#1A237E"))
                            .geodesic(false));
                }
            } catch (Exception e) { Log.e("MapError", e.getMessage()); }
        }
    }
}