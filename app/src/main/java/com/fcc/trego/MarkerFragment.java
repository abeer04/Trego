package com.fcc.trego;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class MarkerFragment extends Fragment
{
    View view;
    Button plantButton, waterButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.marker_fragment, container, false);

        // get the reference of Button
        plantButton = view.findViewById(R.id.btn_plant);
        // perform setOnClickListener on first Button
        plantButton.setOnClickListener(v -> {
            // display a message by using a Toast
            //Toast.makeText(getActivity(), "Plant Fragment", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getActivity(), ScanQR.class);
            startActivity(intent);
        });

        // get the reference of Button
        waterButton = view.findViewById(R.id.btn_water);
        // perform setOnClickListener on first Button
        waterButton.setOnClickListener(v -> {
            // display a message by using a Toast
            Toast.makeText(getActivity(), "Water Fragment", Toast.LENGTH_LONG).show();
        });

        return view;
    }
}
