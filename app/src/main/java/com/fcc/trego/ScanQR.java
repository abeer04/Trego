package com.fcc.trego;

import android.content.Intent;
//import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

//import com.google.zxing.BarcodeFormat;
//import com.google.zxing.MultiFormatWriter;
//import com.google.zxing.WriterException;
//import com.google.zxing.common.BitMatrix;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.CaptureActivity;

import static android.hardware.Camera.CameraInfo.CAMERA_FACING_BACK;

public class ScanQR extends CaptureActivity
{
//    Button btnScan;
//    TextView tv_qr_readTxt;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState)
//    {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_scan_qr);
//
//        btnScan = findViewById(R.id.btnScan);
//        tv_qr_readTxt = findViewById(R.id.tv_qr_readTxt);
//
//        btnScan.setOnClickListener(view -> {
//            IntentIntegrator integrator = new IntentIntegrator(ScanQR.this);
//            integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
//            integrator.setPrompt("Scan");
//            integrator.setCameraId(CAMERA_FACING_BACK);
//            integrator.setBeepEnabled(false);
//            integrator.setBarcodeImageEnabled(false);
//            integrator.initiateScan();
//        });
//    }


}
