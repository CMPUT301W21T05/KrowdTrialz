package com.T05.krowdtrialz.ui.trial;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import com.T05.krowdtrialz.MainActivity;
import com.T05.krowdtrialz.R;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class GenerateTrialQRActivity extends AppCompatActivity {

    private Button generateButton;
    private ImageView qrImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_trial_q_r);

        Intent intent = getIntent();
        String data = intent.getStringExtra("Data");

        qrImage = findViewById(R.id.qr_code_imageView);
        QRGEncoder qrgEncoder = new QRGEncoder(data, null, QRGContents.Type.TEXT, 500);
        Bitmap bitmap = qrgEncoder.getBitmap();
        qrImage.setImageBitmap(bitmap);

    }
}