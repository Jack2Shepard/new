package com.jash.shepard.threadexample;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.WeakHashMap;

public class MainActivity extends AppCompatActivity {
    private String[] urls;
    private String urlToDownload;
    private ImageView mImageView;
    private Spinner mSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        StrictMode();
        inits();
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                urlToDownload = urls[position];
                new DownloadImageTask(MainActivity.this).execute(urlToDownload);
                /*Thread thread = new Thread(new DownloadImage());
                thread.start();*/
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

    private Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream inputStream = connection.getInputStream();
            return BitmapFactory.decodeStream(inputStream);
        } catch (IOException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            return null;
        }
    }

/*    public class DownloadImage implements Runnable {

        @Override
        public void run() {
            final Bitmap bitmap = getBitmapFromURL(urlToDownload);
            mImageView.post(new Runnable() {
                @Override
                public void run() {
                    mImageView.setImageBitmap(bitmap);
                }
            });
        }
    }*/

    public static class DownloadImageTask extends AsyncTask <String,Void,Bitmap>{
        private WeakReference<MainActivity> mainActivity;

        DownloadImageTask( MainActivity activity){
            mainActivity = new WeakReference<>(activity);
        }

        @Override
        protected Bitmap doInBackground(String... urls) {
            MainActivity activity = mainActivity.get();
            if(activity == null || activity.isFinishing()){
                return null;
            }
            return activity.getBitmapFromURL(urls[0]);
        }
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            MainActivity activity = mainActivity.get();
            if(activity == null || activity.isFinishing()){
                return ;
            }
            activity.mImageView.setImageBitmap(bitmap);
        }
    }

   /* public void StrictMode(){
        StrictMode.ThreadPolicy policy = new StrictMode
                .ThreadPolicy
                .Builder()
                .permitAll()
                .build();
        StrictMode.setThreadPolicy(policy);
    }*/
}
