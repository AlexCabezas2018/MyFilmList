package com.example.myfilmlist.integration.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.example.myfilmlist.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class ImageFromURL extends AsyncTask<String, Void, Bitmap> {

    private ImageView image;
    private Bitmap bitmap;

    public ImageFromURL(ImageView image) {
        this.image = image;
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
        String url = strings[0];
        bitmap = null;
        try{
            InputStream inputStream = new URL(url).openStream();
            bitmap = BitmapFactory.decodeStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        if (bitmap != null) {
            image.setImageBitmap(bitmap);
        } else {
            image.setImageResource(R.drawable.logo);
        }
    }
}
