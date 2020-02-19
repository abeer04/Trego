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

import static android.hardware.Camera.CameraInfo.CAMERA_FACING_BACK;
import static android.hardware.Camera.CameraInfo.CAMERA_FACING_FRONT;

public class ScanQR extends AppCompatActivity
{
    Button btnScan;
    TextView tv_qr_readTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qr);

        btnScan = findViewById(R.id.btnScan);
        tv_qr_readTxt = findViewById(R.id.tv_qr_readTxt);

        btnScan.setOnClickListener(view -> {
            IntentIntegrator integrator = new IntentIntegrator(ScanQR.this);
            integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
            integrator.setPrompt("Scan");
            integrator.setCameraId(CAMERA_FACING_BACK);
            integrator.setBeepEnabled(false);
            integrator.setBarcodeImageEnabled(false);
            integrator.initiateScan();
        });
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

                tv_qr_readTxt.setText(result.getContents());
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();

                if (result.getContents().equals("https://www.intagleo.com/careers/"))
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
        else
        {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
