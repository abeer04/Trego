package com.fcc.trego;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import org.w3c.dom.Text;

public class MarkerInfoWindowAdapter implements GoogleMap.InfoWindowAdapter
{
    private Context context;

    public MarkerInfoWindowAdapter(Context context)
    {
        this.context = context.getApplicationContext();
    }

    @Override
    public View getInfoWindow(Marker marker)
    {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker)
    {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.map_marker_info_window, null);

        LatLng latLng = marker.getPosition();
        TextView tvLat = (TextView) view.findViewById(R.id.tv_lat);
        TextView tvLng = (TextView) view.findViewById(R.id.tv_lng);
        tvLat.setText("Latitude:" + latLng.latitude);
        tvLng.setText("Longitude:"+ latLng.longitude);

        return view;
    }
}
