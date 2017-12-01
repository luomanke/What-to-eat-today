package com.example.luoma.myapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

/**
 * Created by LuoMa on 10/30/2017.
 */

public class ResultActivity extends AppCompatActivity {

    private Bitmap getImageBitmap (String url){
        Bitmap bm = null;
        try {
            URL aURL = new URL(url);
            URLConnection conn = aURL.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();
        } catch (IOException e) {
            Log.e("Error getting bitmap", e.toString());
        }
        return bm;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        String selected = getIntent().getStringExtra("selected");

        ParseJson P = new ParseJson();
        P.setJsonData(selected);
        List res = P.getResults();

        TextView mRestName = (TextView) findViewById(R.id.restName);
        TextView mRandRc = (TextView) findViewById(R.id.ratingReviewCount);
        LinearLayout mLocation = (LinearLayout) findViewById(R.id.location);
        ImageView mImageView = (ImageView) findViewById(R.id.image);

        mRestName.setText(res.get(0).toString());
        mRandRc.setText(res.get(1).toString() + "/5 by " + res.get(2).toString() + " Reviews");

        for (int i = 4; i < res.size(); i++){
            TextView mTextView = new TextView(this);
            mTextView.setText(res.get(i).toString());
            mLocation.addView(mTextView);
        }
        mImageView.setImageBitmap(getImageBitmap(res.get(3).toString()));

    }

}
