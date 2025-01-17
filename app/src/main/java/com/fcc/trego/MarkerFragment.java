package com.fcc.trego;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.cunoraz.gifview.library.GifView;
import com.google.zxing.integration.android.IntentIntegrator;

import static android.hardware.Camera.CameraInfo.CAMERA_FACING_BACK;

public class MarkerFragment extends Fragment {
    View view;
    Button plantButton, waterButton;
    Button ok;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.marker_fragment, container, false);

        //watergif.setGifResource(R.drawable.water);
        // get the reference of Button
        plantButton = view.findViewById(R.id.btn_plant);

        // perform setOnClickListener on first Button
        plantButton.setOnClickListener(v -> {
            // display a message by using a Toast
            //Toast.makeText(getActivity(), "Plant Fragment", Toast.LENGTH_LONG).show();
            //Intent intent = new Intent(getActivity(), ScanQR.class);
            //startActivity(intent);
            IntentIntegrator integrator = new IntentIntegrator(getActivity());
            integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
            integrator.setPrompt("Scan");
            integrator.setCameraId(CAMERA_FACING_BACK);
            integrator.setOrientationLocked(true);
            integrator.setCaptureActivity(ScanQR.class);
            integrator.setBeepEnabled(false);
            integrator.setBarcodeImageEnabled(false);
            integrator.initiateScan();
        });

        // get the reference of Button
        waterButton = view.findViewById(R.id.btn_water);
        // perform setOnClickListener on first Button
        waterButton.setOnClickListener(v -> {

            waterit();
            // display a message by using a Toast\

        });

        return view;
    }


    public void waterit() {
        GifView water;

        water=view.findViewById(R.id.watergif);
        water.setGifResource(R.drawable.water);
        // TODO: Handle the error.
        water.setVisibility(View.VISIBLE);
        water.play();

        SuccessDialog alert = new SuccessDialog();
        alert.showDialog(getActivity(), "Exp +10\nGreen Credits +2");


        //water.setVisibility(View.INVISIBLE);
        Toast.makeText(getActivity(), "Water Fragment", Toast.LENGTH_LONG).show();

    }


}
