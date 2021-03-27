package com.T05.krowdtrialz.ui.trial;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.T05.krowdtrialz.MainActivity;
import com.T05.krowdtrialz.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class GenerateTrialQRActivity extends AppCompatActivity {

    private Button downloadButton;
    private ImageView qrImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_trial_q_r);

        downloadButton = (Button) findViewById(R.id.qr_download_button);

        Intent intent = getIntent();
        String data = intent.getStringExtra("Data");

        qrImage = findViewById(R.id.qr_code_imageView);
        QRGEncoder qrgEncoder = new QRGEncoder(data, null, QRGContents.Type.TEXT, 500);
        Bitmap bitmap = qrgEncoder.getBitmap();
        qrImage.setImageBitmap(bitmap);

        ActivityCompat.requestPermissions(GenerateTrialQRActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        ActivityCompat.requestPermissions(GenerateTrialQRActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);


        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveToGallery(bitmap);
            }
        });

    }

    // saves bitmap image as png in root/Android/data/com.T05.krowdtrialz/files/QrCodes
    private void saveToGallery(Bitmap bitmap){
        FileOutputStream outputStream = null;
        File file = new File(getExternalFilesDir(null), "QrCodes");

        file.mkdir();
        String filename = String.format("%d.png",System.currentTimeMillis());
        File outFile = new File(file,filename);
        try{
            outputStream = new FileOutputStream(outFile);
        }catch (Exception e){
            e.printStackTrace();
        }
        bitmap.compress(Bitmap.CompressFormat.PNG,100,outputStream);
        try{
            outputStream.flush();
        }catch (Exception e){
            e.printStackTrace();
        }
        try{
            outputStream.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }


}