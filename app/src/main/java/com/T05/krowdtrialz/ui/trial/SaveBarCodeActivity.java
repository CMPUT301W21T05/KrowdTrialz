package com.T05.krowdtrialz.ui.trial;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.T05.krowdtrialz.R;
import com.T05.krowdtrialz.util.Database;
import com.budiyev.android.codescanner.AutoFocusMode;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.budiyev.android.codescanner.ScanMode;
import com.google.zxing.Result;

public class SaveBarCodeActivity extends AppCompatActivity {
    private final String TAG = "ScanBarCode Activity";

    private final int CAMERA_REQUEST_CODE = 101;
    private CodeScanner codeScanner;
    private CodeScannerView scannerView;
    private Button okButton;
    private String data;
    private Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_bar_code);

        Intent intent = getIntent();
        data = intent.getStringExtra("Data");

        setupPermissions();
    }

    private void codeScanner() {
        scannerView = findViewById(R.id.scanner_codeScannerView);
        codeScanner = new CodeScanner(this, scannerView);
        // setup preferences
        codeScanner.setCamera(CodeScanner.CAMERA_BACK);
        codeScanner.setFormats(CodeScanner.ALL_FORMATS);
        codeScanner.setAutoFocusMode(AutoFocusMode.SAFE);
        codeScanner.setScanMode(ScanMode.SINGLE);
        codeScanner.setAutoFocusEnabled(true);
        codeScanner.setFlashEnabled(false);
        codeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull Result result) {
                // When Barcode or QrCode is detected give appropriate popup
                // resultArray has the following format {experimentID, type, Pass Count, Fail Count, Value, Longitude, latitude}
                data = String.format("%s/",result.getText()) + data;
                String[] resultArray = data.split("/");
                db = Database.getInstance();
                db.saveBarcode(resultArray);

                // Return to trial activity
                finish();
            }
        });

        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                codeScanner.startPreview();
            }
        });

        codeScanner.startPreview();
    }

    private void setupPermissions() {
        int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);

        if(permission != PackageManager.PERMISSION_GRANTED){
            makeRequest();
        }else{
            codeScanner();
        }
    }

    private void makeRequest() {
        String[] permissionArray = {Manifest.permission.CAMERA};
        ActivityCompat.requestPermissions(this, permissionArray, CAMERA_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        if (requestCode == CAMERA_REQUEST_CODE) {
            if(grantResults.length == 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED){
                // If user has not given permission to use camera
            }else{
                // User has given permission to use camera
                codeScanner();
            }
        }
    }
}