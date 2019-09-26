package com.deep.gradhack;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

public class QRCodeScannerActivity extends AppCompatActivity implements View.OnClickListener {

    private IntentIntegrator qrScan;
    private Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_scanner);

        qrScan = new IntentIntegrator(QRCodeScannerActivity.this);
        qrScan.initiateScan();
        qrScan.setOrientationLocked(false);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //if qrcode has nothing in it
            if (result.getContents() == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
            } else {
                //if qr contains data
                Toast.makeText(this, result.getContents(), Toast.LENGTH_SHORT).show();
                String jsonData = result.getContents();

                Intent intent = new Intent(QRCodeScannerActivity.this, PaymentActivity.class);
                intent.putExtra("qrcodedata", jsonData);
                startActivity(intent);
                finish();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onClick(View view) {

    }
}



