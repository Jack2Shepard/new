package com.jash.shepard.threadexample;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private String[] urls;
    private String urlToDownload;
    private ImageView mImageView;
    private Spinner mSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inits();
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                urlToDownload = urls[position];
                Thread thread = new Thread(new DownloadImage());
                thread.start();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void inits() {
        mImageView = findViewById(R.id.image);
        mSpinner = findViewById(R.id.spinner);
        urls = getResources().getStringArray(R.array.urls);
    }

    private Bitmap getBitmapFromURL(String src){
        try{
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream inputStream = connection.getInputStream();
            Bitmap bitmap  = BitmapFactory.decodeStream(inputStream);
            Log.d(TAG, "getBitmapFromURL: " + bitmap);
            return bitmap;
        }catch (IOException e){
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();
            return null;
        }
    }

    public class DownloadImage implements Runnable{

        @Override
        public void run() {
            mImageView.setImageBitmap(getBitmapFromURL(urlToDownload));
        }
    }

}
