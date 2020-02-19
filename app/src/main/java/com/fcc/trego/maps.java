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
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONException;
import org.json.JSONObject;

public class maps extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnCameraIdleListener, View.OnClickListener {

    private GoogleMap mMap;
    TextView address;
    private FusedLocationProviderClient fusedLocationClient;
    private RequestQueue requestQueue;
    JsonObjectRequest addressRes;
    String formatted_address;
    Button done;

    boolean mLocationPermissionGranted = false;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

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
                restult_back("-1");
            }
        }
        else
        {
            restult_back("-1");
        }

        address = findViewById(R.id.show_address);
        done = findViewById(R.id.done);
        done.setOnClickListener(this);
    }

    public void restult_back(String result)
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
                            mMap.setMinZoomPreference(15);
                            url="https://maps.googleapis.com/maps/api/geocode/json?latlng="+location.getLatitude()+","+location.getLongitude()+"&key=AIzaSyBAyEhcJM_a1riCY88giw-C5DhJRJiokmY";
                            Request();
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

        LatLng fccMainGround = new LatLng(31.522009, 74.3328702);
        Marker marker = mMap.addMarker(new MarkerOptions()
                .position(fccMainGround)
                .title("Plant 1!")
                .icon(smMarkerIcon));
        // icon reference => <a href='https://pngtree.com/so/flower'>flower png from pngtree.com</a>

        mMap.setOnMarkerClickListener(marker1 -> {
            // Triggered when user click any marker on the map
            loadFragment(new MarkerFragment());
            return false;
        });

        mMap.setOnCameraIdleListener(this);
    }

    private void loadFragment(Fragment fragment)
    {
        // create a FragmentManager
        FragmentManager fm = getFragmentManager();
        // create a FragmentTransaction to begin the transaction and replace the Fragment
        FragmentTransaction fragmentTransaction = fm.beginTransaction();

        // replace the FrameLayout with new Fragment
        fragmentTransaction.replace(R.id.frameLayout, fragment);

        fragmentTransaction.commit(); // save the changes
    }

    @Override
    public void onCameraIdle()
    {
        final LatLng mPosition;
        mPosition = mMap.getCameraPosition().target;
        url = "https://maps.googleapis.com/maps/api/geocode/json?latlng="+mPosition.latitude+","+mPosition.longitude+"&key=AIzaSyBAyEhcJM_a1riCY88giw-C5DhJRJiokmY";
        Request();
    }

    @Override
    public void onClick(View v) {
        restult_back(formatted_address);
    }

    @Override
    public void onBackPressed()
    {
        restult_back("-1");
        super.onBackPressed();
    }

    void Request()
    {
        requestQueue= Volley.newRequestQueue(this);
        addressRes= new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try
                {
                    Object responseA;
                    responseA = response.getJSONArray("results").get(0);
                    formatted_address = ((JSONObject) responseA).getString("formatted_address");
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
                address.setText(formatted_address);

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Request();
                return;
            }
        });
        requestQueue.add(addressRes);
    }

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
        final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

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
}
