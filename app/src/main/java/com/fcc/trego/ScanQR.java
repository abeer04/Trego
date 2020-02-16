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

import static android.hardware.Camera.CameraInfo.CAMERA_FACING_FRONT;

public class ScanQR extends AppCompatActivity
{
    ImageView imageView;
    Button button;
    Button btnScan;
    EditText editText;
//    String EditTextValue ;
//    Thread thread ;
//    public final static int QRcodeWidth = 350 ;
//    Bitmap bitmap ;

    TextView tv_qr_readTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qr);

        imageView = findViewById(R.id.imageView);
        editText = findViewById(R.id.editText);
        button = findViewById(R.id.button);
        btnScan = findViewById(R.id.btnScan);
        tv_qr_readTxt = findViewById(R.id.tv_qr_readTxt);

//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//
//
//                if(!editText.getText().toString().isEmpty()){
//                    EditTextValue = editText.getText().toString();
//
//                    try {
//                        bitmap = TextToImageEncode(EditTextValue);
//
//                        imageView.setImageBitmap(bitmap);
//
//                    } catch (WriterException e) {
//                        e.printStackTrace();
//                    }
//                }
//                else{
//                    editText.requestFocus();
//                    Toast.makeText(ScanQR.this, "Please Enter Your Scanned Test" , Toast.LENGTH_LONG).show();
//                }
//
//            }
//        });


        btnScan.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                IntentIntegrator integrator = new IntentIntegrator(ScanQR.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                integrator.setPrompt("Scan");
                integrator.setCameraId(CAMERA_FACING_FRONT);
                integrator.setBeepEnabled(false);
                integrator.setBarcodeImageEnabled(false);
                integrator.initiateScan();
            }
        });
    }


//    Bitmap TextToImageEncode(String Value) throws WriterException {
//        BitMatrix bitMatrix;
//        try {
//            bitMatrix = new MultiFormatWriter().encode(
//                    Value,
//                    BarcodeFormat.DATA_MATRIX.QR_CODE,
//                    QRcodeWidth, QRcodeWidth, null
//            );
//
//        }
//        catch (IllegalArgumentException Illegalargumentexception)
//        {
//            return null;
//        }
//
//        int bitMatrixWidth = bitMatrix.getWidth();
//        int bitMatrixHeight = bitMatrix.getHeight();
//        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];
//
//        for (int y = 0; y < bitMatrixHeight; y++)
//        {
//            int offset = y * bitMatrixWidth;
//
//            for (int x = 0; x < bitMatrixWidth; x++)
//            {
//                pixels[offset + x] = bitMatrix.get(x, y) ?
//                        getResources().getColor(R.color.QRCodeBlackColor):getResources().getColor(R.color.QRCodeWhiteColor);
//            }
//        }
//        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);
//
//        bitmap.setPixels(pixels, 0, 350, 0, 0, bitMatrixWidth, bitMatrixHeight);
//        return bitmap;
//    }

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
            }
        }
        else
        {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
