package com.fcc.trego;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.Arrays;

public class maps extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnCameraIdleListener, View.OnClickListener, OnSuccessListener<Location> {

    private GoogleMap mMap;
    TextView address;
    private FusedLocationProviderClient fusedLocationClient;

    String formatted_address;
//    Button done;

    boolean mLocationPermissionGranted = false;
    String url;

    Location currentLoc;
//    LatLng fccMainGround;
    ArrayList<LatLng> positions = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        String apiKey = getString(R.string.google_maps_key);

        /**
         * Initialize Places. For simplicity, the API key is hard-coded. In a production
         * environment we recommend using a secure mechanism to manage API keys.
         */
        if (!Places.isInitialized()) {
            Places.initialize(this, apiKey);
        }

// Create a new Places client instance.
        //PlacesClient placesClient = Places.createClient(this);

        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.LAT_LNG,Place.Field.ID, Place.Field.NAME));

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                LatLng ll=place.getLatLng();
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(ll.latitude,ll.longitude),15));  //move camera to location

                mMap.animateCamera(CameraUpdateFactory.zoomIn());
                // Zoom out to zoom level 10, animating with a duration of 2 seconds.
                mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.

            }
        });



        if (checkMapServices())
        {
            getLocationPermission();

            if (mLocationPermissionGranted)
            {
                fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
                // Obtain the SupportMapFragment and get notified when the map is ready to be used.
                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.home_map);
                mapFragment.getMapAsync(this);
            }
            else
            {
                result_back("-1");
            }
        }
        else
        {
            result_back("-1");
        }

        //address = findViewById(R.id.show_address);
        //done = findViewById(R.id.done);
        //done.setOnClickListener(this);
    }

    public void result_back(String result)
    {
        Intent intent = new Intent();
        intent.putExtra("address", result);
        setResult(RESULT_OK, intent);
        this.finish();
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
    public void onMapReady(GoogleMap googleMap)
    {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            // TODO: Consider calling ActivityCompat#requestPermissions
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null)
                        {
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(),location.getLongitude())));
                            // Zoom in, animating the camera.
                            mMap.animateCamera(CameraUpdateFactory.zoomIn());
                            // Zoom out to zoom level 10, animating with a duration of 2 seconds.
                            mMap.animateCamera(CameraUpdateFactory.zoomTo(20), 2000, null);
                            //url="https://maps.googleapis.com/maps/api/geocode/json?latlng="+location.getLatitude()+","+location.getLongitude()+"&key=AIzaSyBAyEhcJM_a1riCY88giw-C5DhJRJiokmY";
                            //Request();
                        }
                        else
                        {
                            Toast.makeText(maps.this, "Cant't get your current Location, GPS not supported by emulator",
                                    Toast.LENGTH_LONG).show();
                        }
                    }

                });

        // Add a marker in FCC
        int width = 50, height = 50;
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.plant);
        Bitmap smBitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);
        BitmapDescriptor smMarkerIcon = BitmapDescriptorFactory.fromBitmap(smBitmap);

        positions.add(new LatLng(31.4469031, 74.2681719)); // UCP
        positions.add(new LatLng(31.521889, 74.3326335)); // FCC
        positions.add(new LatLng(31.5365019, 74.3383935)); // KC
        positions.add(new LatLng(31.5687544, 74.305066)); // NCA
        positions.add(new LatLng(31.4699827, 74.4088985)); // LUMS
        positions.add(new LatLng(31.570716, 74.319829)); // Abeer

        for(int i = 0 ; i < positions.size() ; i++)
        {
            mMap.addMarker(new MarkerOptions()
                    .position(positions.get(i))
                    .title("Plant " + i + "!")
                    .icon(smMarkerIcon));
        }

//        fccMainGround = new LatLng(31.570716, 74.319829);
//        Marker marker = mMap.addMarker(new MarkerOptions()
//                .position(fccMainGround)
//                .title("Plant 1!")
//                .icon(smMarkerIcon));
        // icon reference => <a href='https://pngtree.com/so/flower'>flower png from pngtree.com</a>

        mMap.setOnMarkerClickListener(marker1 -> {
            // Triggered when user click any marker on the map
            loadFragment(new MarkerFragment());
            return false;
        });

        mMap.setOnCameraIdleListener(this);
    }

    @Override
    public void onBackPressed()
    {
        if (getSupportFragmentManager().getBackStackEntryCount() == 1){
            finish();
        }
        else {
            super.onBackPressed();
        }
    }

    private void loadFragment(Fragment fragment)
    {
        // create a FragmentManager
        FragmentManager fm = getFragmentManager();
        // create a FragmentTransaction to begin the transaction and replace the Fragment
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.animator.enter, R.animator.exit, R.animator.pop_enter, R.animator.pop_exit);
        // replace the FrameLayout with new Fragment
        fragmentTransaction.replace(R.id.frameLayout, fragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .addToBackStack(null);

        fragmentTransaction.commit(); // save the changes
    }

    @Override
    public void onCameraIdle()
    {
        final LatLng mPosition;
        mPosition = mMap.getCameraPosition().target;
        url = "https://maps.googleapis.com/maps/api/geocode/json?latlng="+mPosition.latitude+","+mPosition.longitude+"&key=AIzaSyBAyEhcJM_a1riCY88giw-C5DhJRJiokmY";
        //Request();
    }

    @Override
    public void onClick(View v) {
        result_back(formatted_address);
    }

//    void Request()
//    {
//        requestQueue= Volley.newRequestQueue(this);
//        addressRes= new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                try
//                {
//                    Object responseA;
//                    responseA = response.getJSONArray("results").get(0);
//                    formatted_address = ((JSONObject) responseA).getString("formatted_address");
//                }
//                catch (JSONException e)
//                {
//                    e.printStackTrace();
//                }
//                address.setText(formatted_address);
//
//            }
//
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Request();
//                return;
//            }
//        });
//        requestQueue.add(addressRes);
//    }

    private boolean checkMapServices()
    {
        if(isServicesOK())
        {
            if(isMapsEnabled())
            {
                return true;
            }
            else
            {
                Toast.makeText(this, "Turn on GPS", Toast.LENGTH_LONG).show();
                return false;
            }
        }
        else
        {
            Toast.makeText(this, "Update/Install Google Play Services", Toast.LENGTH_LONG).show();
            return false;
        }
    }

    public boolean isMapsEnabled()
    {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE );

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER))
        {
            return false;
        }
        return true;
    }

    private void getLocationPermission()
    {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            mLocationPermissionGranted = true;
        }
        else
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                if(shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION))
                {
                    Toast.makeText(this, "Location Permission required to use this feature",
                            Toast.LENGTH_LONG).show();
                }

                ActivityCompat.requestPermissions(maps.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},003);
            }
        }
    }

    public boolean isServicesOK()
    {
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);

        if(available == ConnectionResult.SUCCESS)
        {
            // everything is fine and the user can make map requests
            return true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available))
        {
            // an error occurred but we can resolve it
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(maps.this, available,004 );
            dialog.show();
        }
        else
        {
            Toast.makeText(this, "Update/Install Google Play Services", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults)
    {
        mLocationPermissionGranted = false;

        switch (requestCode)
        {
            case 003:
            {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    mLocationPermissionGranted = true;
                }
                else
                {
                    Toast.makeText(this, "Location Permission required to use this feature", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture)
    {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null)
        {
            if (result.getContents() == null)
            {
                Log.e("Scan*******", "Cancelled scan");
            }
            else
            {
                Log.e("Scan", "Scanned");

                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();

                fusedLocationClient.getLastLocation()
                        .addOnSuccessListener(this);
            }
        }
        else
        {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onSuccess(Location location)
    {
        currentLoc = location;
        float[] results = new float[1];
        Location.distanceBetween(currentLoc.getLatitude(), currentLoc.getLongitude(),
                positions.get(5).latitude, positions.get(5).longitude, results);

        if (results[0] < 6)
        {
            SuccessDialog alert = new SuccessDialog();
            alert.showDialog(this, "Exp +100\nGreen Credits +20");
        }
        else
        {
            FailureDialog alert = new FailureDialog();
            alert.showDialog(this, "Wrong Location");
        }
    }
}
